package mr

import (
	"log"
	"net/http"
	"strconv"
	"sync"
	"time"
)
import "net"
import "os"
import "net/rpc"

const (
	MAP    int = 0
	REDUCE int = 1
)

const (
	FREE      int = 0
	ASSIGNED  int = 1
	COMPLETED int = 2
)

type Task struct {
	Filename string
	Status   int
	Level    int
}
type Coordinator struct {
	mapTasks    map[int]Task
	reduceTasks map[int]Task
	l           sync.Mutex
	nMap        int
	nReduce     int
}

func checkProgress(c *Coordinator, id int, level int) {
	time.Sleep(10 * time.Second)
	c.l.Lock()
	defer c.l.Unlock()

	if level == MAP {
		if c.mapTasks[id].Status == ASSIGNED {
			task := c.mapTasks[id]
			task.Status = FREE
			c.mapTasks[id] = task
		}
	} else {
		if c.reduceTasks[id].Status == ASSIGNED {
			task := c.reduceTasks[id]
			task.Status = FREE
			c.reduceTasks[id] = task
		}
	}
}

func assignMapTask(c *Coordinator, reply *RPCReply) {
	i := -1
	for id, task := range c.mapTasks {
		if task.Status == FREE {
			i = id
			break
		}
	}
	if i == -1 {
		reply.Id = -1
		reply.Filename = ""
		reply.Level = -1
		reply.NRed = -1
		return
	}
	newTask := c.mapTasks[i]
	newTask.Status = ASSIGNED
	c.mapTasks[i] = newTask
	reply.Id = i
	reply.Level = MAP
	reply.Filename = newTask.Filename
	reply.NRed = c.nReduce
}

func assignReduceTask(c *Coordinator, reply *RPCReply) {
	i := -1
	for id, task := range c.reduceTasks {
		if task.Status == FREE {
			i = id
			break
		}
	}
	if i == -1 {
		reply.Id = -2
		reply.Filename = ""
		reply.Level = -2
		reply.NRed = -2
		return
	}
	newTask := c.reduceTasks[i]
	newTask.Status = ASSIGNED
	c.reduceTasks[i] = newTask
	reply.Id = i
	reply.Level = REDUCE
	reply.Filename = newTask.Filename
	reply.NRed = c.nReduce
}
func (c *Coordinator) AskForTask(args *RPCArgs, reply *RPCReply) error {
	c.l.Lock()
	defer c.l.Unlock()
	if args.Id != -1 {
		taskDone(c, args.Id, args.Level)
	}
	if c.nMap != 0 {
		assignMapTask(c, reply)
	} else {
		assignReduceTask(c, reply)
	}
	go checkProgress(c, reply.Id, reply.Level)
	return nil
}

func taskDone(c *Coordinator, id int, level int) error {
	var task Task
	if level == MAP {
		task = c.mapTasks[id]
	} else {
		task = c.reduceTasks[id]
	}
	if task.Status == COMPLETED {
		return nil
	}
	task.Status = COMPLETED
	if level == MAP {
		c.mapTasks[id] = task
		c.nMap--
	} else {
		c.reduceTasks[id] = task
		c.nReduce--
	}
	return nil
}

// an example RPC handler.
//
// the RPC argument and reply types are defined in rpc.go.
func (c *Coordinator) Example(args *ExampleArgs, reply *ExampleReply) error {
	reply.Y = args.X + 1
	return nil
}

// start a thread that listens for RPCs from worker.go
func (c *Coordinator) server() {
	rpc.Register(c)
	rpc.HandleHTTP()
	//l, e := net.Listen("tcp", ":1234")
	sockname := coordinatorSock()
	os.Remove(sockname)
	l, e := net.Listen("unix", sockname)
	if e != nil {
		log.Fatal("listen error:", e)
	}
	go http.Serve(l, nil)
}

// main/mrcoordinator.go calls Done() periodically to find out
// if the entire job has finished.
func (c *Coordinator) Done() bool {
	c.l.Lock()
	defer c.l.Unlock()
	return c.nReduce == 0
}

// create a Coordinator.
// main/mrcoordinator.go calls this function.
// nReduce is the number of reduce tasks to use.
func MakeCoordinator(files []string, nReduce int) *Coordinator {
	c := Coordinator{}
	c.mapTasks = make(map[int]Task)
	c.reduceTasks = make(map[int]Task)
	c.nReduce = nReduce
	c.nMap = len(files)
	for i, file := range files {
		c.mapTasks[i+1] = Task{file, FREE, MAP}
	}
	for i := 0; i < nReduce; i++ {
		c.reduceTasks[i] = Task{strconv.Itoa(c.nMap), FREE, REDUCE}
	}
	c.server()
	return &c
}
