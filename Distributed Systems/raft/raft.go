package raft

//
// this is an outline of the API that raft must expose to
// the service (or tester). see comments below for
// each of these functions for more details.
//
// rf = Make(...)
//   create a new Raft server.
// rf.Start(command interface{}) (index, term, isleader)
//   start agreement on a new log entry
// rf.GetState() (term, isLeader)
//   ask a Raft for its current term, and whether it thinks it is leader
// ApplyMsg
//   each time a new entry is committed to the log, each Raft peer
//   should send an ApplyMsg to the service (or tester)
//   in the same server.
//

import (
	"6.824/labgob"
	"bytes"
	"math/rand"
	//	"bytes"
	"sync"
	"sync/atomic"
	"time"

	//	"6.824/labgob"
	"6.824/labrpc"
)

// as each Raft peer becomes aware that successive log entries are
// committed, the peer should send an ApplyMsg to the service (or
// tester) on the same server, via the applyCh passed to Make(). set
// CommandValid to true to indicate that the ApplyMsg contains a newly
// committed log entry.
//
// in part 2D you'll want to send other kinds of messages (e.g.,
// snapshots) on the applyCh, but set CommandValid to false for these
// other uses.
type ApplyMsg struct {
	CommandValid bool
	Command      interface{}
	CommandIndex int

	// For 2D:
	SnapshotValid bool
	Snapshot      []byte
	SnapshotTerm  int
	SnapshotIndex int
}

type Entry struct {
	Command interface{}
	Term    int
}

type Log struct {
	Entries []Entry
}

const FOLLOWER = 1
const CANDIDATE = 2
const LEADER = 3

// A Go object implementing a single Raft peer.
type Raft struct {
	mu        sync.Mutex          // Lock to protect shared access to this peer's state
	peers     []*labrpc.ClientEnd // RPC end points of all peers
	persister *Persister          // Object to hold this peer's persisted state
	me        int                 // this peer's index into peers[]
	dead      int32               // set by Kill()

	// Your data here (2A, 2B, 2C).
	// Look at the paper's Figure 2 for a description of what
	// state a Raft server must maintain.
	currentTerm       int
	votesGiven        map[int]int
	log               Log
	state             int
	heartbeat         chan bool
	votesReceived     int
	commitIndex       int
	lastApplied       int
	nextIndex         []int
	matchIndex        []int
	lastIncludedTerm  int
	lastIncludedIndex int
	applyCh           chan ApplyMsg
	agreementGoing    bool
}

// return currentTerm and whether this server
// believes it is the leader.
func (rf *Raft) GetState() (int, bool) {
	rf.mu.Lock()
	defer rf.mu.Unlock()
	return rf.currentTerm, rf.state == LEADER
}

// save Raft's persistent state to stable storage,
// where it can later be retrieved after a crash and restart.
// see paper's Figure 2 for a description of what should be persistent.
func (rf *Raft) persist() {
	// Your code here (2C).
	// Example:
	w := new(bytes.Buffer)
	e := labgob.NewEncoder(w)
	if e.Encode(rf.currentTerm) != nil ||
		e.Encode(rf.votesGiven) != nil || e.Encode(rf.log) != nil || e.Encode(rf.lastIncludedIndex) != nil ||
		e.Encode(rf.lastIncludedTerm) != nil {
		return // failed to save state
	}
	data := w.Bytes()
	rf.persister.SaveRaftState(data)
}

// restore previously persisted state.
func (rf *Raft) readPersist(data []byte) {
	if data == nil || len(data) < 1 { // bootstrap without any state?
		return
	}
	//Your code here (2C).
	//Example:
	r := bytes.NewBuffer(data)
	d := labgob.NewDecoder(r)
	if d.Decode(&rf.currentTerm) != nil ||
		d.Decode(&rf.votesGiven) != nil || d.Decode(&rf.log) != nil || d.Decode(&rf.lastIncludedIndex) != nil ||
		d.Decode(&rf.lastIncludedTerm) != nil {
		return // failed to load state
	}
}

func (rf *Raft) readPersistedSnapshot(data []byte) {
	if data == nil || len(data) < 1 { // bootstrap without any state?
		return
	}
	snapshot := ApplyMsg{SnapshotValid: true, Snapshot: data, SnapshotTerm: rf.lastIncludedTerm, SnapshotIndex: rf.lastIncludedIndex}
	rf.applyCh <- snapshot
}

// A service wants to switch to snapshot.  Only do so if Raft hasn't
// have more recent info since it communicate the snapshot on applyCh.
func (rf *Raft) CondInstallSnapshot(lastIncludedTerm int, lastIncludedIndex int, snapshot []byte) bool {

	// Your code here (2D).

	return true
}

// the service says it has created a snapshot that has
// all info up to and including index. this means the
// service no longer needs the log through (and including)
// that index. Raft should now trim its log as much as possible.
func (rf *Raft) Snapshot(index int, snapshot []byte) {
	rf.mu.Lock()
	defer rf.mu.Unlock()
	rf.lastIncludedTerm = rf.log.Entries[index-rf.lastIncludedIndex].Term
	rf.log.Entries = rf.log.Entries[index-rf.lastIncludedIndex+1:]
	empty := []Entry{{}}
	rf.log.Entries = append(empty, rf.log.Entries...)
	rf.lastIncludedIndex = index
	rf.persist()
	rf.persister.SaveStateAndSnapshot(rf.persister.ReadRaftState(), snapshot)
}

// example RequestVote RPC arguments structure.
// field names must start with capital letters!
type RequestVoteArgs struct {
	// Your data here (2A, 2B).
	Term         int
	CandidateId  int
	LastLogIndex int
	LastLogTerm  int
}

// example RequestVote RPC reply structure.
// field names must start with capital letters!
type RequestVoteReply struct {
	// Your data here (2A).
	Term        int
	VoteGranted bool
}

type AppendEntryArgs struct {
	NewEntry     []Entry
	Term         int
	LeaderId     int
	PrevLogIndex int
	PrevLogTerm  int
	LeaderCommit int
}

type AppendEntryReply struct {
	Term             int
	Success          bool
	ConflictingTerm  int
	ConflictingIndex int
}

type InstallSnapshotArgs struct {
	Term              int
	LeaderId          int
	LastIncludedTerm  int
	LastIncludedIndex int
	Data              []byte
}

type InstallSnapshotReply struct {
	Term int
}

func (rf *Raft) sendApply(newIndex int) {
	rf.mu.Lock()
	snapshot := rf.lastIncludedIndex
	rf.mu.Unlock()
	if rf.lastApplied < snapshot {
		rf.lastApplied = snapshot
	}
	for rf.lastApplied+1 <= newIndex {
		rf.mu.Lock()
		newMsg := ApplyMsg{CommandValid: true, Command: rf.log.Entries[rf.lastApplied-rf.lastIncludedIndex+1].Command, CommandIndex: rf.lastApplied + 1}
		rf.mu.Unlock()
		rf.applyCh <- newMsg
		rf.lastApplied++
	}
}

func (rf *Raft) getConflictIndex(index int) int {
	ans := index
	term := rf.log.Entries[index].Term
	for ans > 0 && rf.log.Entries[ans].Term == term {
		ans--
	}
	return ans + 1
}

func (rf *Raft) AppendEntry(args *AppendEntryArgs, reply *AppendEntryReply) {
	rf.mu.Lock()
	defer rf.mu.Unlock()
	defer rf.persist()
	reply.Term = rf.currentTerm
	if args.Term < rf.currentTerm {
		reply.Success = false
		return
	}
	if args.Term >= rf.currentTerm && rf.state != FOLLOWER { // We are no longer leader or candidate
		rf.state = FOLLOWER
	}
	go func() {
		rf.heartbeat <- true
	}()
	rf.currentTerm = args.Term
	if args.PrevLogTerm != 0 {
		indexToStart := args.PrevLogIndex - rf.lastIncludedIndex
		if indexToStart >= len(rf.log.Entries) {
			indexToStart = len(rf.log.Entries) - 1
		}
		if indexToStart < 0 {
			reply.Success = false
			return
		}
		conflictingTerm := rf.log.Entries[indexToStart].Term
		conflictingIndex := rf.getConflictIndex(indexToStart) + rf.lastIncludedIndex
		if args.PrevLogIndex-rf.lastIncludedIndex >= len(rf.log.Entries) {
			if !(len(rf.log.Entries) == 1 && rf.lastIncludedIndex != 0 && args.PrevLogIndex ==
				rf.lastIncludedIndex && args.PrevLogTerm == rf.lastIncludedTerm) {
				reply.Success = false
				reply.ConflictingTerm = conflictingTerm
				reply.ConflictingIndex = conflictingIndex
				return
			}
		}
		if args.PrevLogIndex-rf.lastIncludedIndex >= 1 && rf.log.Entries[args.PrevLogIndex-rf.lastIncludedIndex].Term != args.PrevLogTerm {
			reply.Success = false
			reply.ConflictingTerm = conflictingTerm
			reply.ConflictingIndex = conflictingIndex
			return
		}
		if len(rf.log.Entries) > args.PrevLogIndex-rf.lastIncludedIndex+1 && rf.log.Entries[args.PrevLogIndex-rf.lastIncludedIndex+1].Term != args.NewEntry[0].Term {
			rf.log.Entries = rf.log.Entries[:args.PrevLogIndex-rf.lastIncludedIndex+1]
		}
		if len(rf.log.Entries)+rf.lastIncludedIndex <= args.PrevLogIndex+len(args.NewEntry) {
			copyStart := args.PrevLogIndex + len(args.NewEntry) - len(rf.log.Entries) - rf.lastIncludedIndex
			rf.log.Entries = append(rf.log.Entries, args.NewEntry[len(args.NewEntry)-copyStart-1:]...)
		}
	}
	reply.Success = true
	if rf.log.Entries[len(rf.log.Entries)-1].Term != args.Term && args.PrevLogTerm == 0 { // it is heartbeat and log may not be consistent with leader yet
		return
	}
	if args.LeaderCommit > rf.commitIndex {
		newCommit := args.LeaderCommit
		if len(rf.log.Entries)-1+rf.lastIncludedIndex < newCommit {
			newCommit = len(rf.log.Entries) - 1 + rf.lastIncludedIndex
		}
		rf.commitIndex = newCommit
	}
}

// example RequestVote RPC handler.
func (rf *Raft) RequestVote(args *RequestVoteArgs, reply *RequestVoteReply) {
	rf.mu.Lock()
	defer rf.mu.Unlock()
	defer rf.persist()
	reply.Term = rf.currentTerm
	if args.Term < rf.currentTerm {
		reply.VoteGranted = false
		return
	}
	if args.Term > rf.currentTerm && rf.state != FOLLOWER {
		rf.state = FOLLOWER
	}
	rf.currentTerm = args.Term
	val, ok := rf.votesGiven[rf.currentTerm]
	if ok && val != args.CandidateId {
		reply.VoteGranted = false
		return
	}
	lastLogTerm := 0
	lastLogIndex := 0
	if len(rf.log.Entries) > 1 {
		lastLogTerm = rf.log.Entries[len(rf.log.Entries)-1].Term
		lastLogIndex = len(rf.log.Entries) - 1 + rf.lastIncludedIndex
	}
	if args.LastLogTerm < lastLogTerm {
		reply.VoteGranted = false
		return
	}
	if args.LastLogTerm == lastLogTerm && args.LastLogIndex < lastLogIndex {
		reply.VoteGranted = false
		return
	}
	go func() { rf.heartbeat <- true }() // might be important
	rf.votesGiven[rf.currentTerm] = args.CandidateId
	reply.VoteGranted = true
}

func (rf *Raft) InstallSnapshot(args *InstallSnapshotArgs, reply *InstallSnapshotReply) {
	rf.mu.Lock()
	defer rf.mu.Unlock()
	reply.Term = rf.currentTerm
	if args.Term >= rf.currentTerm && rf.state != FOLLOWER { // We are no longer leader or candidate
		rf.state = FOLLOWER
	}
	go func() {
		rf.heartbeat <- true
	}()
	if args.LastIncludedIndex <= rf.lastIncludedIndex {
		return
	}
	reply.Term = rf.currentTerm
	snapshot := ApplyMsg{SnapshotValid: true, Snapshot: args.Data, SnapshotTerm: args.LastIncludedTerm, SnapshotIndex: args.LastIncludedIndex}
	rf.applyCh <- snapshot
	prev := rf.lastIncludedIndex
	rf.lastIncludedIndex = args.LastIncludedIndex
	rf.lastIncludedIndex = args.LastIncludedIndex
	rf.lastIncludedTerm = args.LastIncludedTerm
	if len(rf.log.Entries)-1+prev > args.LastIncludedIndex {
		rf.log.Entries = rf.log.Entries[args.LastIncludedIndex-prev+1:]
		return
	}
	rf.log.Entries = []Entry{{}}
	rf.persist()
	rf.persister.SaveStateAndSnapshot(rf.persister.ReadRaftState(), args.Data)
}

func (rf *Raft) sendHeartBeats() {
	for {
		if rf.killed() {
			return
		}
		for ind, _ := range rf.peers {
			if ind == rf.me {
				continue
			}
			rf.mu.Lock()
			if rf.state != LEADER {
				rf.mu.Unlock()
				return
			}
			term := rf.currentTerm
			args := AppendEntryArgs{Term: term, LeaderId: rf.me, LeaderCommit: rf.commitIndex}
			rf.mu.Unlock()
			reply := AppendEntryReply{}
			go rf.sendAppendEntries(ind, &args, &reply)
		}
		time.Sleep(time.Millisecond * 100)
	}

}

// example code to send a RequestVote RPC to a server.
// server is the index of the target server in rf.peers[].
// expects RPC arguments in args.
// fills in *reply with RPC reply, so caller should
// pass &reply.
// the types of the args and reply passed to Call() must be
// the same as the types of the arguments declared in the
// handler function (including whether they are pointers).
//
// The labrpc package simulates a lossy network, in which servers
// may be unreachable, and in which requests and replies may be lost.
// Call() sends a request and waits for a reply. If a reply arrives
// within a timeout interval, Call() returns true; otherwise
// Call() returns false. Thus Call() may not return for a while.
// A false return can be caused by a dead server, a live server that
// can't be reached, a lost request, or a lost reply.
//
// Call() is guaranteed to return (perhaps after a delay) *except* if the
// handler function on the server side does not return.  Thus there
// is no need to implement your own timeouts around Call().
//
// look at the comments in ../labrpc/labrpc.go for more details.
//
// if you're having trouble getting RPC to work, check that you've
// capitalized all field names in structs passed over RPC, and
// that the caller passes the address of the reply struct with &, not
// the struct itself.
func (rf *Raft) sendRequestVote(server int, args *RequestVoteArgs, reply *RequestVoteReply) {
	ok := rf.peers[server].Call("Raft.RequestVote", args, reply)
	if !ok {
		return
	}
	rf.mu.Lock()
	defer rf.mu.Unlock()
	if reply.Term > rf.currentTerm {
		rf.state = FOLLOWER
		return
	}
	if reply.VoteGranted {
		rf.votesReceived++
		if rf.votesReceived > len(rf.peers)/2 && rf.state == CANDIDATE && args.Term == rf.currentTerm {
			rf.state = LEADER
			for i := range rf.nextIndex {
				rf.nextIndex[i] = len(rf.log.Entries) + rf.lastIncludedIndex // reinitializing nextIndex
				rf.matchIndex[i] = 0                                         // reinitializing matchIndex
			}
			go rf.sendHeartBeats()
		}
	}
}

func (rf *Raft) sendAppendEntries(server int, args *AppendEntryArgs, reply *AppendEntryReply) {
	rf.mu.Lock()
	ind := rf.nextIndex[server]
	rf.mu.Unlock()
	if args.PrevLogTerm != 0 && ind != args.PrevLogIndex+1 { // no need to send
		return
	}
	ok := rf.peers[server].Call("Raft.AppendEntry", args, reply)
	if !ok {
		return
	}
	rf.mu.Lock()
	defer rf.mu.Unlock()
	defer rf.persist()
	if reply.Term > rf.currentTerm {
		rf.state = FOLLOWER
		rf.currentTerm = reply.Term
		return
	}
	if args.PrevLogTerm == 0 || rf.state != LEADER {
		return // means it was just heartbeat
	}
	if reply.Success {
		currIndex := args.PrevLogIndex + len(args.NewEntry)
		if rf.nextIndex[server] == currIndex+1 {
			return
		}
		rf.matchIndex[server] = currIndex
		rf.nextIndex[server] = currIndex + 1
	} else {
		if rf.nextIndex[server] != args.PrevLogIndex+1 {
			return
		}
		rf.nextIndex[server] = reply.ConflictingIndex
		for rf.log.Entries[rf.nextIndex[server]-rf.lastIncludedIndex].Term == reply.ConflictingTerm && rf.nextIndex[server] < args.PrevLogIndex {
			rf.nextIndex[server]++
		}
	}
}

func (rf *Raft) sendInstallSnapshot(server int, args *InstallSnapshotArgs, reply *InstallSnapshotReply) {
	ok := rf.peers[server].Call("Raft.InstallSnapshot", args, reply)
	if !ok {
		return
	}
	rf.mu.Lock()
	defer rf.mu.Unlock()
	if reply.Term > rf.currentTerm {
		rf.state = FOLLOWER
		return
	}
	rf.nextIndex[server] = args.LastIncludedIndex + 1
	rf.matchIndex[server] = args.LastIncludedIndex
}

func (rf *Raft) startAgreement() {
	for rf.killed() == false {
		rf.mu.Lock()
		canStop := true
		isLeader := rf.state == LEADER
		if !isLeader {
			rf.agreementGoing = false
			rf.mu.Unlock()
			break
		}
		rf.mu.Unlock()
		for ind, _ := range rf.peers {
			rf.mu.Lock()
			if rf.nextIndex[ind] == len(rf.log.Entries)+rf.lastIncludedIndex || ind == rf.me {
				rf.mu.Unlock()
				continue
			}
			if rf.state != LEADER {
				rf.agreementGoing = false
				rf.mu.Unlock()
				return
			}
			if rf.nextIndex[ind] <= rf.lastIncludedIndex {
				snapshotArgs := InstallSnapshotArgs{rf.currentTerm, rf.me, rf.lastIncludedTerm, rf.lastIncludedIndex, rf.persister.ReadSnapshot()}
				snapshotReply := InstallSnapshotReply{}
				rf.mu.Unlock()
				go rf.sendInstallSnapshot(ind, &snapshotArgs, &snapshotReply)
				continue
			}
			canStop = false
			prevLogTerm := -1
			prevLogIndex := -1
			if len(rf.log.Entries) > 1 && rf.nextIndex[ind]-rf.lastIncludedIndex > 1 {
				prevLogIndex = rf.nextIndex[ind] - 1
				prevLogTerm = rf.log.Entries[rf.nextIndex[ind]-1-rf.lastIncludedIndex].Term // es mosapiqrebelia !!!
			} else {
				prevLogTerm = rf.lastIncludedTerm
				prevLogIndex = rf.lastIncludedIndex
			}
			var entries []Entry
			entries = append(entries, rf.log.Entries[rf.nextIndex[ind]-rf.lastIncludedIndex:]...)
			args := AppendEntryArgs{NewEntry: entries, Term: rf.currentTerm, LeaderId: rf.me,
				PrevLogIndex: prevLogIndex, PrevLogTerm: prevLogTerm, LeaderCommit: rf.commitIndex}
			rf.mu.Unlock()
			reply := AppendEntryReply{}
			go rf.sendAppendEntries(ind, &args, &reply)
		}
		rf.mu.Lock()
		if canStop {
			extraCheck := false
			for _, nind := range rf.nextIndex {
				if len(rf.log.Entries)-1+rf.lastIncludedIndex >= nind {
					extraCheck = true
					break
				}
			}
			if !extraCheck {
				rf.agreementGoing = false
				rf.mu.Unlock()
				break
			}
		}
		rf.mu.Unlock()
		time.Sleep(10 * time.Millisecond)
	}
}

// the service using Raft (e.g. a k/v server) wants to start
// agreement on the next command to be appended to Raft's log. if this
// server isn't the leader, returns false. otherwise start the
// agreement and return immediately. there is no guarantee that this
// command will ever be committed to the Raft log, since the leader
// may fail or lose an election. even if the Raft instance has been killed,
// this function should return gracefully.
//
// the first return value is the index that the command will appear at
// if it's ever committed. the second return value is the current
// term. the third return value is true if this server believes it is
// the leader.
func (rf *Raft) Start(command interface{}) (int, int, bool) {
	rf.mu.Lock()
	defer rf.mu.Unlock()
	index := -1
	term := rf.currentTerm
	isLeader := true
	if rf.lastIncludedTerm == 0 {
		rf.lastIncludedTerm = -1
	}
	if rf.state != LEADER {
		isLeader = false
		return index, term, isLeader
	}
	index = len(rf.log.Entries) + rf.lastIncludedIndex
	newEntry := Entry{Term: rf.currentTerm, Command: command}
	rf.log.Entries = append(rf.log.Entries, newEntry)
	rf.persist()
	if !rf.agreementGoing {
		rf.agreementGoing = true
		go rf.startAgreement()
	}
	return index, term, isLeader
}

// the tester doesn't halt goroutines created by Raft after each test,
// but it does call the Kill() method. your code can use killed() to
// check whether Kill() has been called. the use of atomic avoids the
// need for a lock.
//
// the issue is that long-running goroutines use memory and may chew
// up CPU time, perhaps causing later tests to fail and generating
// confusing debug output. any goroutine with a long-running loop
// should call killed() to check whether it should stop.
func (rf *Raft) Kill() {
	atomic.StoreInt32(&rf.dead, 1)
	// Your code here, if desired.
}

func (rf *Raft) killed() bool {
	z := atomic.LoadInt32(&rf.dead)
	return z == 1
}

func (rf *Raft) updateCommitIndex() {
	posInd := len(rf.log.Entries) - 1 + rf.lastIncludedIndex
	found := false
	for ; posInd > rf.commitIndex; posInd-- {
		if rf.log.Entries[posInd-rf.lastIncludedIndex].Term < rf.currentTerm {
			break
		}
		cnt := 1
		for ind, _ := range rf.matchIndex {
			if ind != rf.me && rf.matchIndex[ind] >= posInd {
				cnt++
			}
		}
		if cnt > len(rf.peers)/2 {
			found = true
			break
		}
	}
	if !found {
		return
	}
	rf.commitIndex = posInd
}
func (rf *Raft) checkIfCommitted() {
	for rf.killed() == false {
		rf.mu.Lock()
		if rf.state == LEADER {
			rf.updateCommitIndex()
		}
		commitIndex := rf.commitIndex
		rf.mu.Unlock()
		rf.sendApply(commitIndex)
		time.Sleep(time.Millisecond * 10)
	}
}

func (rf *Raft) startElections() {
	rf.mu.Lock()
	if rf.killed() {
		return
	}
	defer rf.mu.Unlock()
	rf.state = CANDIDATE                  // become candidate
	rf.currentTerm++                      // Increase Term
	rf.votesGiven[rf.currentTerm] = rf.me // Vote for self
	rf.persist()
	go func() { rf.heartbeat <- true }() // reset timeout
	rf.votesReceived = 1
	for ind, _ := range rf.peers {
		if rf.state != CANDIDATE {
			return
		}
		if rf.me == ind {
			continue
		}
		lastLogTerm := 0
		lastLogIndex := 0
		if len(rf.log.Entries) > 1 {
			lastLogTerm = rf.log.Entries[len(rf.log.Entries)-1].Term
			lastLogIndex = len(rf.log.Entries) - 1 + rf.lastIncludedIndex
		}
		args := RequestVoteArgs{Term: rf.currentTerm, CandidateId: rf.me, LastLogIndex: lastLogIndex, LastLogTerm: lastLogTerm}
		reply := RequestVoteReply{}
		go rf.sendRequestVote(ind, &args, &reply)
	}
}

// The ticker go routine starts a new election if this peer hasn't received
// heartsbeats recently.
func (rf *Raft) ticker() {
	for rf.killed() == false {
		// Your code here to check if a leader election should
		// be started and to randomize sleeping time using
		// time.Sleep().
		rf.mu.Lock()
		state := rf.state
		rf.mu.Unlock()
		if state == LEADER {
			continue
		}
		rand.Seed(time.Now().UnixNano())
		electionTimeOut := rand.Int63n(300) + 500
		d := time.Duration(electionTimeOut)
		select {
		case <-rf.heartbeat:
		case <-time.After(d * time.Millisecond):
			rf.mu.Lock()
			if rf.state != LEADER { // While we were waiting in follower state, we might've already won the elections
				rf.mu.Unlock()
				rf.startElections()
			} else {
				rf.mu.Unlock()
			}
		}
	}
}

// the service or tester wants to create a Raft server. the ports
// of all the Raft servers (including this one) are in peers[]. this
// server's port is peers[me]. all the servers' peers[] arrays
// have the same order. persister is a place for this server to
// save its persistent state, and also initially holds the most
// recent saved state, if any. applyCh is a channel on which the
// tester or service expects Raft to send ApplyMsg messages.
// Make() must return quickly, so it should start goroutines
// for any long-running work.
func Make(peers []*labrpc.ClientEnd, me int,
	persister *Persister, applyCh chan ApplyMsg) *Raft {
	rf := &Raft{}
	rf.peers = peers
	rf.persister = persister
	rf.me = me

	// Your initialization code here (2A, 2B, 2C).
	rf.state = FOLLOWER
	rf.votesGiven = make(map[int]int)
	rf.heartbeat = make(chan bool)
	rf.currentTerm = 0
	rf.log = Log{}
	rf.log.Entries = make([]Entry, 1)
	rf.log.Entries[0] = Entry{}
	rf.votesReceived = 0
	rf.commitIndex = 0
	rf.lastApplied = 0
	rf.nextIndex = make([]int, len(rf.peers))
	rf.matchIndex = make([]int, len(rf.peers))
	rf.applyCh = applyCh
	rf.agreementGoing = false
	rf.lastIncludedTerm = 0
	rf.lastIncludedIndex = 0
	rf.readPersist(persister.ReadRaftState())
	if rf.lastIncludedTerm == 0 {
		rf.lastIncludedTerm = -1
	}
	go rf.readPersistedSnapshot(persister.ReadSnapshot())
	// start ticker goroutine to start elections
	go rf.ticker()
	go rf.checkIfCommitted()

	return rf
}
