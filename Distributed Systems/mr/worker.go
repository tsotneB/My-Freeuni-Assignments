package mr

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"os"
	"strconv"
)
import "log"
import "net/rpc"
import "hash/fnv"

// Map functions return a slice of KeyValue.
type KeyValue struct {
	Key   string
	Value string
}

// use ihash(key) % NReduce to choose the reduce
// task number for each KeyValue emitted by Map.
func ihash(key string) int {
	h := fnv.New32a()
	h.Write([]byte(key))
	return int(h.Sum32() & 0x7fffffff)
}

func readFile(filename string) []byte {
	file, err := os.Open(filename)
	if err != nil {
		log.Fatalf("cannot open %v", filename)
	}
	content, err := ioutil.ReadAll(file)
	if err != nil {
		log.Fatalf("cannot read %v", filename)
	}
	file.Close()
	return content
}

func createFiles(nred int, id int) ([]*json.Encoder, []*os.File) {
	x := strconv.Itoa(id)
	ansEnc := make([]*json.Encoder, nred)
	ansF := make([]*os.File, nred)
	for i := 0; i < nred; i++ {
		name := "mr-" + x + "-" + strconv.Itoa(i)
		file, _ := os.Create(name)
		ansEnc[i] = json.NewEncoder(file)
		ansF[i] = file
	}
	return ansEnc, ansF
}

func closeFiles(files []*os.File) {
	for _, file := range files {
		file.Close()
	}
}
func writeInFile(id int, kva []KeyValue, nred int) error {
	encoders, files := createFiles(nred, id)
	defer closeFiles(files)
	for _, kv := range kva {
		hashed := ihash(kv.Key) % nred
		encoders[hashed].Encode(&kv)
	}
	return nil
}

func processMap(mapf func(string, string) []KeyValue, reply RPCReply) error {
	fileContent := readFile(reply.Filename)
	kva := mapf(reply.Filename, string(fileContent))
	written := writeInFile(reply.Id, kva, reply.NRed)
	return written
}

func processReduce(reducef func(string, []string) string, reply RPCReply) error {
	reduceInput := make(map[string][]string)
	nMap, _ := strconv.Atoi(reply.Filename)
	for i := 1; i <= nMap; i++ {
		filename := "mr-" + strconv.Itoa(i) + "-" + strconv.Itoa(reply.Id)
		file, err := os.Open(filename)
		if err != nil {
			//	fmt.Printf("%v\n", err)
			return err
		}
		dec := json.NewDecoder(file)

		for {
			var kv KeyValue
			if err := dec.Decode(&kv); err != nil {
				break
			}
			reduceInput[kv.Key] = append(reduceInput[kv.Key], kv.Value)
		}
		file.Close()
	}
	outFile, nwerror := os.Create("mr-out-" + strconv.Itoa(reply.Id))
	if nwerror != nil {
		return nwerror
	}
	for key, values := range reduceInput {
		output := reducef(key, values)
		fmt.Fprintf(outFile, "%v %v\n", key, output)
	}
	outFile.Close()
	return nil
}

// main/mrworker.go calls this function.
func Worker(mapf func(string, string) []KeyValue,
	reducef func(string, []string) string) {
	args := RPCArgs{-1, -1}
	for {
		reply := RPCReply{}
		taskCall := call("Coordinator.AskForTask", &args, &reply)
		if !taskCall {
			continue
		}
		if reply.Id == -1 {
			continue
		}
		if reply.Id == -2 {
			continue
		}
		var err error
		if reply.Level == MAP {
			err = processMap(mapf, reply)
		} else {
			err = processReduce(reducef, reply)
		}
		if err != nil {
			continue
		}
		args = RPCArgs{reply.Id, reply.Level}
	}
}

// example function to show how to make an RPC call to the coordinator.
//
// the RPC argument and reply types are defined in rpc.go.
func CallExample() {

	// declare an argument structure.
	args := ExampleArgs{}

	// fill in the argument(s).
	args.X = 99

	// declare a reply structure.
	reply := ExampleReply{}

	// send the RPC request, wait for the reply.
	// the "Coordinator.Example" tells the
	// receiving server that we'd like to call
	// the Example() method of struct Coordinator.
	ok := call("Coordinator.Example", &args, &reply)
	if ok {
		// reply.Y should be 100.
		fmt.Printf("reply.Y %v\n", reply.Y)
	} else {
		fmt.Printf("call failed!\n")
	}
}

// send an RPC request to the coordinator, wait for the response.
// usually returns true.
// returns false if something goes wrong.
func call(rpcname string, args interface{}, reply interface{}) bool {
	// c, err := rpc.DialHTTP("tcp", "127.0.0.1"+":1234")
	sockname := coordinatorSock()
	c, err := rpc.DialHTTP("unix", sockname)
	if err != nil {
		//log.Fatal("dialing:", err)
		os.Exit(1)
	}
	defer c.Close()

	err = c.Call(rpcname, args, reply)
	if err == nil {
		return true
	}

	//fmt.Println(err)
	return false
}
