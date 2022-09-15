#include <bits/stdc++.h>
using namespace std;

typedef struct{
    int n,a,t;
    set <int> acceptStates;
    map <int, set <pair <char,int> > > transitions;
} DFA;



void toString(DFA d) {
    cout<<d.n<<" "<<d.a<<" "<<d.t<<endl;
    for (set<int>::iterator it = d.acceptStates.begin();it != d.acceptStates.end();++it) {
        cout<<*it<<" ";
    }
    cout<<endl;
    for (int i=0;i<d.n;i++) {
        if (d.transitions.find(i) == d.transitions.end()) {
            cout<<0<<endl;
            continue;
        }
        set <pair <char,int> > p = d.transitions[i];
        cout<<p.size()<<" ";
        for (set < pair<char,int> >::iterator it1 = p.begin();it1 != p.end(); ++it1) {
            pair<char,int> p1 = *it1;
            cout<<p1.first<<" "<<p1.second<<" ";
        }
        cout<<endl;
    
    }
}


DFA basicDFA(char ch) {
    DFA ans;
    ans.n = 2;
    ans.a = 1;
    ans.t = 1;
    set<int> accept;
    ans.acceptStates = accept;
    ans.acceptStates.insert(1);
    map <int, set <pair <char,int> > > t;
    ans.transitions = t;
    set<pair <char,int> > dir;
    dir.insert(make_pair(ch,1));
    ans.transitions.insert(make_pair(0,dir));
    return ans;
}


DFA star(DFA dfa) {
    set<pair <char,int> > ep = dfa.transitions[0];
    bool zero=false;
    if (dfa.acceptStates.find(0) !=dfa.acceptStates.end()) {
            zero = true;
    }
    for (set<int>::iterator it = dfa.acceptStates.begin();it != dfa.acceptStates.end();++it) {

        if (dfa.transitions.find(*it) == dfa.transitions.end()) {
            set <pair <char,int> > newWays;
            dfa.transitions.insert(make_pair(*it,newWays));
        }
        if (*it == 0) continue;
        for (set<pair<char,int> >::iterator it1 = ep.begin();it1 != ep.end();++it1) {
            pair<char,int> p1 = *it1;
            if (dfa.transitions[*it].find(make_pair(p1.first,p1.second)) == dfa.transitions[*it].end()) dfa.t++;
            dfa.transitions[*it].insert(make_pair(p1.first,p1.second));
        }
    }
    if (!zero) dfa.acceptStates.insert(0);
    dfa.a = dfa.acceptStates.size();
    return dfa;
}

void copyFirst(map <int, set <pair <char,int> > >& from, map <int, set <pair <char,int> > >& to) {
    for (map <int, set <pair <char,int> > >::iterator it = from.begin(); it != from.end(); ++it) {
        pair <int, set <pair <char,int> > > p = *it;
        int f = p.first;
        set<pair <char,int> > dest;
        for (set <pair <char,int> >::iterator it1=p.second.begin();it1 != p.second.end();++it1) {
            pair <char,int> p = *it1;
            dest.insert(make_pair(p.first,p.second));
        }
        to.insert(make_pair(f,dest));
    }
}

void copySecond(map <int, set <pair <char,int> > >& from, map <int, set <pair <char,int> > >& to,set<int>& acc, int incr, int& initSize) {
    set<pair <char,int> > ep = from[0]; 
    for (set<int>::iterator it=acc.begin();it != acc.end();++it) {
        if (to.find(*it) == to.end()) {
            set <pair <char,int> > newWays;
            to.insert(make_pair(*it,newWays));
        }
        for (set<pair <char,int> >::iterator it1 = ep.begin();it1 != ep.end();++it1) {
            pair<char,int> p = *it1;
            to[*it].insert(make_pair(p.first,p.second+incr));
            initSize++;
        }
    }
    for (map <int, set <pair <char,int> > >::iterator it = from.begin(); it != from.end(); ++it) {
        pair <int, set <pair <char,int> > > p = *it;
        int f = p.first;
        if (f == 0) continue;
        f+=incr;
        set<pair <char,int> > dest;
        for (set<pair <char,int> >::iterator it1 = p.second.begin();it1 != p.second.end();++it1) {
            pair<char,int> p1 = *it1;
            dest.insert(make_pair(p1.first,p1.second+incr));
            initSize++;
        }
        to.insert(make_pair(f,dest));
    }
}

DFA concat(DFA dfa1, DFA dfa2) {
    DFA ans;
    ans.n = dfa1.n+dfa2.n-1;
    int incr = dfa1.n-1;
    map <int, set <pair <char,int> > > t;
    ans.transitions = t;
    copyFirst(dfa1.transitions,ans.transitions);
    int initSize = dfa1.t;
    copySecond(dfa2.transitions,ans.transitions,dfa1.acceptStates,incr,initSize);
    set <int> newAcc;
    if (dfa2.acceptStates.find(0) != dfa2.acceptStates.end()) {
            for (set<int>::iterator it=dfa1.acceptStates.begin(); it != dfa1.acceptStates.end();++it) 
                newAcc.insert(*it);
    } 
    for (set<int>::iterator it=dfa2.acceptStates.begin(); it != dfa2.acceptStates.end();++it) {
        newAcc.insert(*it + incr);
    }
    ans.acceptStates = newAcc;
    ans.a = ans.acceptStates.size();
    ans.t = initSize;
    return ans;
}

void copySecondUnion(map <int, set <pair <char,int> > >& from, map <int, set <pair <char,int> > >& to, int incr, int& size) {
    for (map<int, set <pair <char,int> > >::iterator it = from.begin(); it != from.end(); ++it) {
        pair <int, set <pair <char,int> > > p = *it;
        int f = p.first;
        if (f == 0) continue;
        f += incr;
        set<pair <char,int> > dest;
        for (set<pair<char,int> >::iterator it1 =p.second.begin();it1 != p.second.end();it1++) {
            pair<char,int> p1 = *it1;
            dest.insert(make_pair(p1.first,p1.second+incr));
            size++;
        }
        to.insert(make_pair(f,dest));
    }
}

void  copyAcceptStates(set <int> & acc1,set <int> & acc2,set <int> & acc,int incr) {
    for (set<int>::iterator it = acc1.begin();it!=acc1.end();++it) 
        acc.insert(*it);
    for (set<int>::iterator it = acc2.begin();it!=acc2.end();++it) 
        if (*it == 0) acc.insert(0); else acc.insert(*it+incr);
    
}

DFA dfaunion (DFA dfa1, DFA dfa2) {
    DFA ans;
    ans.n = dfa1.n+dfa2.n-1;
    int incr = dfa1.n-1;
    map <int, set <pair <char,int> > > t;
    ans.transitions = t;
    copyFirst(dfa1.transitions,ans.transitions);
    set<pair <char,int> > ep = dfa2.transitions[0];
    int size = dfa1.t;
    for (set<pair<char,int> >::iterator it=ep.begin();it != ep.end();++it) {
        pair<char,int> p = *it;
        ans.transitions[0].insert(make_pair(p.first,p.second+dfa1.n-1));
        size++;
    }
    copySecondUnion(dfa2.transitions,ans.transitions,dfa1.n-1,size);
    set <int> acc;
    ans.acceptStates = acc;
    copyAcceptStates(dfa1.acceptStates,dfa2.acceptStates,ans.acceptStates,dfa1.n-1);
    ans.a = ans.acceptStates.size();
    ans.t = size;
    return ans;
}

int findSecondParenthesis(string reg) {
    int diff = -1;
    for (int i=1;i<reg.size();i++) {
        if (reg[i] =='(') diff--;   else if (reg[i] == ')') diff++; 
        if (diff == 0) return i;
    }
    return -1;
}

string addConcats(string reg) {
	string ans;
	for (int i=0;i<reg.size()-1;i++) {
		if (reg[i] == '*' && ((reg[i+1] >= 'a' && reg[i+1] <='z') || (reg[i+1]>='A' && reg[i+1]<='Z') || (reg[i+1]>='0' && reg[i+1]<='9'))) {
			ans+='*';
			ans+='&';
			continue;
		}
		if ( ((reg[i] >= 'a' && reg[i] <='z') || (reg[i]>='A' && reg[i]<='Z') || (reg[i]>='0' && reg[i]<='9'))
		 && ((reg[i+1] >= 'a' && reg[i+1] <='z') || (reg[i+1]>='A' && reg[i+1]<='Z') || (reg[i+1]>='0' && reg[i+1]<='9'))) {
		 	ans+=reg[i];
			ans+='&';
			continue;
		 }
		if (reg[i] == ')' && (reg[i+1] == '(' || ((reg[i+1] >= 'a' && reg[i+1] <='z') || (reg[i+1]>='A' && reg[i+1]<='Z') || (reg[i+1]>='0' && reg[i+1]<='9')))) {
			ans+=')';
			ans+='&';
			continue;
		} 
		ans+=reg[i];
	} 
	ans+=reg[reg.size()-1];
	return ans;
}

string postFix(string reg) {
	reg = addConcats(reg);
    map<char,int> pre;
    pre['|'] = 2;
    pre['&'] = 3;
    pre['*'] = 4;
    pre['('] = 1;
    stack<char> op;
    string ans;
    int i=0;
    while (i<reg.size()) {
        if (reg[i] == '(') {
            op.push('(');
            i++;
            continue;
        }
        if (reg[i] == ')') {
            while(!op.empty() && op.top() != '(')
            {
                char temp = op.top();
                op.pop();
                ans += temp;
            }
            op.pop();
            i++;
            continue;
        }
        bool conc = false;
        if ((reg[i] >= 'a' && reg[i] <= 'z') || (reg[i] >= 'A' && reg[i] <= 'Z') || (reg[i] >= '0' && reg[i] <= '9')) {
            ans += reg[i];
            i++;
            continue;
        }
        char opr = reg[i];
        while(!op.empty() && pre[opr] <= pre[op.top()]) {
                char tmp = op.top();
                op.pop();
                ans += tmp;
            }
        op.push(opr); 
        i++;
    }    

    while (!op.empty()) {
        ans += op.top();
        op.pop();
    }

    return ans;
}


DFA build(string reg) {
    stack<DFA> res;
    for (int i=0;i<reg.size();i++) {
        if (reg[i] != '&' && reg[i]!='*' && reg[i]!='|') {
            string s = "";
            s+=reg[i];
            res.push(basicDFA(reg[i]));
            continue;
        }
        if (reg[i] == '*') {
            DFA d = res.top();
            res.pop();
            res.push(star(d));
            continue;
        }
        if (reg[i] == '&') {
            DFA d1 = res.top();
            res.pop();
            DFA d2 = res.top();
            res.pop();
            res.push(concat(d2,d1));
            continue;
        }
        if (reg[i] == '|') {
            DFA d1 = res.top();
            res.pop();
            DFA d2 = res.top();
            res.pop();
            res.push(dfaunion(d2,d1));
            continue;
        }
    }
    return res.top();
}

int main() {
   	string s;
    cin>>s;
    toString(build(postFix(s)));
    return 0;
}
