#include <bits/stdc++.h>
using namespace std;
int main (){
	string s;
	cin>>s;
	string ans;
	for (int i=0;i<s.size();i++) {
		ans += "N";
	}
	int n,a,t;
	cin>>n>>a>>t;
	set<int> acceptStates;
	for (int i=0;i<a;i++){
		int ind;
		cin>>ind;
		acceptStates.insert(ind);
	}
	map<pair<int,char>,vector<int> >  transitions;
	for (int i=0;i<n;i++) {
		int num;
		cin>>num;
		for (int j=0;j<num;j++) {
			char ch;
			cin>>ch;
			int to;
			cin>>to;
			if (transitions.find(make_pair(i,ch)) == transitions.end()) {
				vector <int> nodes;
				nodes.push_back(to);
				transitions.insert(make_pair(make_pair(i,ch),nodes));	
			}	else {
				transitions[make_pair(i,ch)].push_back(to);
			}
		}
	}
	set<int> currentStates;
	int currInd = 0;
	currentStates.insert(0);
	while (currInd<s.size()) {
		set<int> newState;
		for (set<int>::iterator it=currentStates.begin(); it!=currentStates.end(); ++it) {
			int node = *it;
			if (transitions.find(make_pair(node,s[currInd])) == transitions.end()) continue;
			vector <int> nodes = transitions[make_pair(node,s[currInd])]; 
			for (int i=0;i<nodes.size();i++) {
				if (acceptStates.find(nodes[i]) != acceptStates.end()) {
					ans[currInd]='Y';
				} 
				newState.insert(nodes[i]);
			}
 		}
 		currentStates = newState;
 		currInd++;
	}
	cout<<ans<<endl; 
	return 0;
}
