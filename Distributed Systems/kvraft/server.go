package kvraft

import (
	"6.824/labgob"
	"6.824/labrpc"
	"6.824/raft"
	"log"
	"sync"
	"sync/atomic"
	"time"
)

const Debug = false

func DPrintf(format string, a ...interface{}) (n int, err error) {
	if Debug {
		log.Printf(format, a...)
	}
	return
}

type Op struct {
	// Your definitions here.
	// Field names must start with capital letters,
	// otherwise RPC will break.
	OperationType string
	Key           string
	Value         string
	CommandId     int64
}

type KVServer struct {
	mu      sync.Mutex
	me      int
	rf      *raft.Raft
	applyCh chan raft.ApplyMsg
	dead    int32 // set by Kill()

	maxraftstate int // snapshot if log grows this big

	// Your definitions here.
	database map[string]string
	outcomes map[int64]Err
}

func (kv *KVServer) Get(args *GetArgs, reply *GetReply) {
	_, isLeader := kv.rf.GetState()
	if !isLeader {
		reply.Err = ErrWrongLeader
		return
	}
	kv.rf.Start(Op{OperationType: "Get", Key: args.Key, CommandId: args.CommandId})
	//fmt.Printf("Received Get Operation with Key %s and ID %v by %d\n", args.Key, args.CommandId, kv.me)
	ok := false
	val := Err(ErrNoKey)
	for !ok {
		time.Sleep(time.Millisecond * 10)
		kv.mu.Lock()
		val, ok = kv.outcomes[args.CommandId]
		kv.mu.Unlock()
		_, stillLeader := kv.rf.GetState()
		if !stillLeader {
			reply.Err = ErrWrongLeader
			return
		}
	}
	reply.Err = val
	if val == ErrNoKey {
		reply.Value = ""
		return
	}
	kv.mu.Lock()
	reply.Value = kv.database[args.Key]
	kv.mu.Unlock()
	//fmt.Printf("Finished get operation %v by %d\n", args.CommandId, kv.me)
}

func (kv *KVServer) PutAppend(args *PutAppendArgs, reply *PutAppendReply) {
	_, isLeader := kv.rf.GetState()
	if !isLeader {
		reply.Err = ErrWrongLeader
		return
	}
	//fmt.Printf("Received %s Operation with Key %s, Value %s and ID %v by %d\n", args.Op, args.Key, args.Value, args.CommandId, kv.me)
	kv.mu.Lock()
	val, ok := kv.outcomes[args.CommandId]
	if ok {
		reply.Err = val
		kv.mu.Unlock()
		return
	}
	kv.mu.Unlock()
	kv.rf.Start(Op{OperationType: args.Op, Key: args.Key, Value: args.Value, CommandId: args.CommandId})
	for !ok {
		time.Sleep(time.Millisecond * 10)
		kv.mu.Lock()
		val, ok = kv.outcomes[args.CommandId]
		kv.mu.Unlock()
		_, stillLeader := kv.rf.GetState()
		if !stillLeader {
			reply.Err = ErrWrongLeader
			return
		}
	}
	reply.Err = val
	//fmt.Printf("Finished Append Operation %v by %d\n", args.CommandId, kv.me)
}

// the tester calls Kill() when a KVServer instance won't
// be needed again. for your convenience, we supply
// code to set rf.dead (without needing a lock),
// and a killed() method to test rf.dead in
// long-running loops. you can also add your own
// code to Kill(). you're not required to do anything
// about this, but it may be convenient (for example)
// to suppress debug output from a Kill()ed instance.
func (kv *KVServer) Kill() {
	atomic.StoreInt32(&kv.dead, 1)
	kv.rf.Kill()
	// Your code here, if desired.
}

func (kv *KVServer) killed() bool {
	z := atomic.LoadInt32(&kv.dead)
	return z == 1
}

func (kv *KVServer) readApplyChannel() {
	for kv.killed() == false {
		applied := <-kv.applyCh
		op := applied.Command.(Op)
		kv.mu.Lock()
		//fmt.Printf("Reading Operation with CommandID %d by peer No %d\n", op.CommandId, kv.me)
		_, found := kv.outcomes[op.CommandId]
		if found {
			kv.mu.Unlock()
			continue
		}
		switch op.OperationType {
		case "Get":
			key := op.Key
			_, ok := kv.database[key]
			if !ok {
				kv.outcomes[op.CommandId] = ErrNoKey
			} else {
				kv.outcomes[op.CommandId] = OK
			}
		case "Put":
			kv.outcomes[op.CommandId] = OK
			kv.database[op.Key] = op.Value
		case "Append":
			val, ok := kv.database[op.Key]
			if ok {
				kv.database[op.Key] = val + op.Value
			} else {
				kv.database[op.Key] = op.Value
			}
			kv.outcomes[op.CommandId] = OK
		}
		//fmt.Printf("Operation with CommandID %d; Type %v; Key %v; Value %v; committed by kv %d's raft peer\nDatabase looks like this: %v\n", op.CommandId, op.OperationType, op.Key, op.Value, kv.me, kv.database)
		kv.mu.Unlock()
	}
}

// servers[] contains the ports of the set of
// servers that will cooperate via Raft to
// form the fault-tolerant key/value service.
// me is the index of the current server in servers[].
// the k/v server should store snapshots through the underlying Raft
// implementation, which should call persister.SaveStateAndSnapshot() to
// atomically save the Raft state along with the snapshot.
// the k/v server should snapshot when Raft's saved state exceeds maxraftstate bytes,
// in order to allow Raft to garbage-collect its log. if maxraftstate is -1,
// you don't need to snapshot.
// StartKVServer() must return quickly, so it should start goroutines
// for any long-running work.
func StartKVServer(servers []*labrpc.ClientEnd, me int, persister *raft.Persister, maxraftstate int) *KVServer {
	// call labgob.Register on structures you want
	// Go's RPC library to marshall/unmarshall.
	labgob.Register(Op{})

	kv := new(KVServer)
	kv.me = me
	kv.maxraftstate = maxraftstate

	// You may need initialization code here.

	kv.applyCh = make(chan raft.ApplyMsg)
	kv.rf = raft.Make(servers, me, persister, kv.applyCh)

	// You may need initialization code here.
	kv.database = make(map[string]string)
	kv.outcomes = make(map[int64]Err)
	go kv.readApplyChannel()
	return kv
}
