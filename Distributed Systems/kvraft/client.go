package kvraft

import (
	"6.824/labrpc"
)
import "crypto/rand"
import "math/big"

type Clerk struct {
	servers  []*labrpc.ClientEnd
	leaderId int
}

func nrand() int64 {
	max := big.NewInt(int64(1) << 62)
	bigx, _ := rand.Int(rand.Reader, max)
	x := bigx.Int64()
	return x
}

func MakeClerk(servers []*labrpc.ClientEnd) *Clerk {
	ck := new(Clerk)
	ck.servers = servers
	// You'll have to add code here.
	return ck
}

// fetch the current value for a key.
// returns "" if the key does not exist.
// keeps trying forever in the face of all other errors.
//
// you can send an RPC with code like this:
// ok := ck.servers[i].Call("KVServer.Get", &args, &reply)
//
// the types of args and reply (including whether they are pointers)
// must match the declared types of the RPC handler function's
// arguments. and reply must be passed as a pointer.
func (ck *Clerk) Get(key string) string {
	args := GetArgs{Key: key, CommandId: nrand()}
	//fmt.Printf("Get Operation with Key %s and ID %v\n", key, args.CommandId)
	reply := GetReply{}
	for {
		ok := ck.servers[ck.leaderId].Call("KVServer.Get", &args, &reply)
		if ok && reply.Err != ErrWrongLeader {
			break
		}
		ck.leaderId++
		ck.leaderId %= len(ck.servers)
	}
	//fmt.Printf("Got Value %v for Key %s; %v\n", reply.Value, key, args.CommandId)
	return reply.Value
}

// shared by Put and Append.
//
// you can send an RPC with code like this:
// ok := ck.servers[i].Call("KVServer.PutAppend", &args, &reply)
//
// the types of args and reply (including whether they are pointers)
// must match the declared types of the RPC handler function's
// arguments. and reply must be passed as a pointer.
func (ck *Clerk) PutAppend(key string, value string, op string) {
	args := PutAppendArgs{Key: key, Value: value, Op: op, CommandId: nrand()}
	//fmt.Printf("%s Operation with Key %s, Value %s and ID %v\n", op, key, value, args.CommandId)
	reply := PutAppendReply{}
	for {
		ok := ck.servers[ck.leaderId].Call("KVServer.PutAppend", &args, &reply)
		if ok && reply.Err != ErrWrongLeader {
			break
		}
		ck.leaderId++
		ck.leaderId %= len(ck.servers)
		//fmt.Printf("Received error from leader %d, so new leader is %d\n", (ck.leaderId+len(ck.servers)-1)%len(ck.servers), ck.leaderId)
	}
	//fmt.Printf("%s Operation %v Finished\n", op, args.CommandId)
	return
}

func (ck *Clerk) Put(key string, value string) {
	ck.PutAppend(key, value, "Put")
}
func (ck *Clerk) Append(key string, value string) {
	ck.PutAppend(key, value, "Append")
}
