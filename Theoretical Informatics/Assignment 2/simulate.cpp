#include <bits/stdc++.h>
using namespace std;

void simulate(vector <char>& tape, map <pair <int,char>, pair <pair <int,char>, int > >& transitions, int n) {
	int ind = 0;
	int currState = 0;
	while (true) {
		if (currState == n-1) {
			return;
		}
		if (ind < 0) {
			cout<<-1<<endl;
			return;
		}
		if (transitions.find(make_pair(currState,tape[ind])) == transitions.end()) {
			cout<<-1<<endl;
			return;
		}
		pair <pair <int,char>, int> currVal = transitions[make_pair(currState,tape[ind])];
		cout<<currVal.first.first<<endl;
		tape[ind] = currVal.first.second;
		ind += currVal.second;
		if (ind == tape.size()) {
			tape.push_back('_');
		}
		currState = currVal.first.first;
	}
}

int main() {
	vector <char> tape(10001);
	for (int i=0;i<tape.size();i++) tape[i] = '_';
	int n;
	cin>>n;
	map <pair <int,char>, pair <pair <int,char>, int > > transitions;
	for (int i=0;i<n-1;i++) {
		int c;
		cin>>c;
		for (int j=0;j<c;j++) {
			char letterRead,letterWrite,dir;
			int newState;
			cin>>letterRead>>newState>>letterWrite>>dir;
			int movedir;
			if (dir == 'L') {
				movedir = -1;
			} else {
				movedir = 1;
			}
			pair <int,char> newKey = make_pair(i,letterRead);
			pair <pair <int,char>, int> newValue = make_pair(make_pair(newState,letterWrite),movedir);
			transitions[newKey]=newValue;
		}
	}
	string inp;
	cin>>inp;
	for (int i=0;i<inp.size();i++) {
		tape[i]=inp[i];
	}
	simulate(tape, transitions, n);
	return 0;
}
