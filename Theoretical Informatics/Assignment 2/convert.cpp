#include <bits/stdc++.h>
using namespace std;

void markTheStarters(map <pair <int,char> , pair <int, pair <char,char > > >& newTransitions) {
	newTransitions[make_pair(0,'1')] = make_pair(1,make_pair('X','R'));
	newTransitions[make_pair(0,'0')] = make_pair(1,make_pair('Y','R'));		
	newTransitions[make_pair(1,'1')] = make_pair(1,make_pair('1','R'));
	newTransitions[make_pair(1,'0')] = make_pair(1,make_pair('0','R'));
	newTransitions[make_pair(1,'_')] = make_pair(2,make_pair('$','R'));
	
	newTransitions[make_pair(2,'_')] = make_pair(3,make_pair('Z','R'));
	
	newTransitions[make_pair(3,'_')] = make_pair(4,make_pair('$','L'));
	
	newTransitions[make_pair(4,'Z')] = make_pair(5,make_pair('Z','L'));
	newTransitions[make_pair(5,'$')] = make_pair(5,make_pair('$','L'));
	newTransitions[make_pair(5,'1')] = make_pair(5,make_pair('1','L'));
	newTransitions[make_pair(5,'0')] = make_pair(5,make_pair('0','L'));
	newTransitions[make_pair(5,'_')] = make_pair(5,make_pair('_','L'));
}



void createShifters(int startind, map <pair <int,char> , pair <int, pair <char,char > > >& newTransitions, int finishind, bool foundAccept, int& maxInd) {
	newTransitions[make_pair(startind,'1')] = make_pair(startind+1,make_pair('$','R'));
	
	newTransitions[make_pair(startind,'0')] = make_pair(startind+2,make_pair('$','R'));
	
	newTransitions[make_pair(startind,'_')] = make_pair(startind+3,make_pair('$','R'));
	
	newTransitions[make_pair(startind,'$')] = make_pair(startind+4,make_pair('$','R')); 
	
	newTransitions[make_pair(startind,'X')] = make_pair(startind+6,make_pair('$','R'));
	
	newTransitions[make_pair(startind,'Y')] = make_pair(startind+7,make_pair('$','R'));
	
	
	newTransitions[make_pair(startind,'Z')] = make_pair(startind+8,make_pair('$','R'));
	
	
	newTransitions[make_pair(startind+4,'_')] = make_pair(startind+5,make_pair('$','L'));
	
	newTransitions[make_pair(startind+1,'1')] = make_pair(startind+1,make_pair('1','R'));
	
	newTransitions[make_pair(startind+1,'0')] = make_pair(startind+2,make_pair('1','R'));
	
	
	newTransitions[make_pair(startind+1,'_')] = make_pair(startind+3,make_pair('1','R'));
	
	
	newTransitions[make_pair(startind+1,'$')] = make_pair(startind+4,make_pair('1','R'));
	
	
	newTransitions[make_pair(startind+2,'1')] = make_pair(startind+1,make_pair('0','R'));

	newTransitions[make_pair(startind+2,'0')] = make_pair(startind+2,make_pair('0','R'));

	newTransitions[make_pair(startind+2,'_')] = make_pair(startind+3,make_pair('0','R'));

	newTransitions[make_pair(startind+2,'$')] = make_pair(startind+4,make_pair('0','R'));

	newTransitions[make_pair(startind+3,'1')] = make_pair(startind+1,make_pair('_','R'));

	newTransitions[make_pair(startind+3,'0')] = make_pair(startind+2,make_pair('_','R'));

	newTransitions[make_pair(startind+3,'_')] = make_pair(startind+3,make_pair('_','R'));

	newTransitions[make_pair(startind+3,'$')] = make_pair(startind+4,make_pair('_','R'));


	newTransitions[make_pair(startind+1,'X')] = make_pair(startind+6,make_pair('1','R'));
	
	newTransitions[make_pair(startind+1,'Y')] = make_pair(startind+7,make_pair('1','R'));
	
	newTransitions[make_pair(startind+1,'Z')] = make_pair(startind+8,make_pair('1','R'));
	
	newTransitions[make_pair(startind+2,'X')] = make_pair(startind+6,make_pair('0','R'));
	
	newTransitions[make_pair(startind+2,'Y')] = make_pair(startind+7,make_pair('0','R'));
	
	newTransitions[make_pair(startind+2,'Z')] = make_pair(startind+8,make_pair('0','R'));
	
	newTransitions[make_pair(startind+3,'X')] = make_pair(startind+6,make_pair('_','R'));
	
	newTransitions[make_pair(startind+3,'Y')] = make_pair(startind+7,make_pair('_','R'));
	
	newTransitions[make_pair(startind+3,'Z')] = make_pair(startind+8,make_pair('_','R'));
	
	newTransitions[make_pair(startind+6,'1')] = make_pair(startind+1,make_pair('X','R'));
	
	newTransitions[make_pair(startind+6,'0')] = make_pair(startind+2,make_pair('X','R'));
	
	newTransitions[make_pair(startind+6,'_')] = make_pair(startind+3,make_pair('X','R'));
	
	newTransitions[make_pair(startind+6,'$')] = make_pair(startind+4,make_pair('X','R'));
	
	newTransitions[make_pair(startind+7,'1')] = make_pair(startind+1,make_pair('Y','R'));
	
	newTransitions[make_pair(startind+7,'0')] = make_pair(startind+2,make_pair('Y','R'));
	
	newTransitions[make_pair(startind+7,'_')] = make_pair(startind+3,make_pair('Y','R'));
	
	newTransitions[make_pair(startind+7,'$')] = make_pair(startind+4,make_pair('Y','R'));
	
	newTransitions[make_pair(startind+8,'1')] = make_pair(startind+1,make_pair('Z','R'));
	
	newTransitions[make_pair(startind+8,'0')] = make_pair(startind+2,make_pair('Z','R'));
	
	
	newTransitions[make_pair(startind+8,'_')] = make_pair(startind+3,make_pair('Z','R'));
	
	newTransitions[make_pair(startind+8,'$')] = make_pair(startind+4,make_pair('Z','R'));
	
	newTransitions[make_pair(startind+5,'1')] = make_pair(startind+5,make_pair('1','L'));
	
	newTransitions[make_pair(startind+5,'0')] = make_pair(startind+5,make_pair('0','L'));
	
	newTransitions[make_pair(startind+5,'_')] = make_pair(startind+5,make_pair('_','L'));
	
	newTransitions[make_pair(startind+5,'X')] = make_pair(startind+5,make_pair('X','L'));
	
	newTransitions[make_pair(startind+5,'Y')] = make_pair(startind+5,make_pair('Y','L'));
	
	newTransitions[make_pair(startind+5,'Z')] = make_pair(startind+5,make_pair('Z','L'));
	
	newTransitions[make_pair(startind+5,'$')] = make_pair(startind+9,make_pair('$','L'));
	
	newTransitions[make_pair(startind+9,'_')] = make_pair(startind+10,make_pair('Z','R'));
	
	if (foundAccept) {
		finishind = -1;		
	}
	
	if (startind+10 > maxInd) maxInd = startind + 10;
	
	newTransitions[make_pair(startind+10,'1')] = make_pair(finishind,make_pair('1','L'));
	newTransitions[make_pair(startind+10,'0')] = make_pair(finishind,make_pair('0','L'));
	newTransitions[make_pair(startind+10,'_')] = make_pair(finishind,make_pair('_','L'));
	newTransitions[make_pair(startind+10,'$')] = make_pair(finishind,make_pair('$','L'));
}

	
void modifyMachine(map <pair <int, pair <char, char> >, pair <int, pair < pair <char,char>, pair <char,char> > > >& transitions,
					map <int,int>& states,map <pair <int,char> , pair <int, pair <char,char > > >& newTransitions, int addedStates, int& maxInd, int n) {
	map <pair <int, pair <char, char> >, int>  alreadyConverted;
	for (map <pair <int, pair <char, char> >, pair <int, pair < pair <char,char>, pair <char,char> > > >::iterator it = 
			transitions.begin(); it != transitions.end(); ++it) {
		pair <int, pair <char, char> > k = it->first;
		pair <int, pair < pair <char,char>, pair <char,char> > > v = it->second;
		if (alreadyConverted.find(k) != alreadyConverted.end()) {
			continue;
		}
		int oldIndexFirst = k.first;
		int oldIndexSecond = v.first;
		int newIndexFirst;
		int newIndexSecond;
		int addedOnThisTurn = 0;
		if (states.find(oldIndexFirst) == states.end()) {
			newIndexFirst = addedStates;
			states[oldIndexFirst] = newIndexFirst;
			addedStates++;
		}	else {
			newIndexFirst = states[oldIndexFirst];
		}
		if (newIndexFirst > maxInd) maxInd = newIndexFirst;
		char sym[4] = {'1','0','_','$'};
		char def[3] = {'X','Y','Z'};
		
		
		if (newTransitions.find(make_pair(newIndexFirst,'X')) == newTransitions.end()) {
			newTransitions[make_pair(newIndexFirst,'X')] = make_pair(addedStates,make_pair('X','R'));
			addedStates++;
		}	
		if (newTransitions.find(make_pair(newIndexFirst,'Y')) == newTransitions.end()) {
			newTransitions[make_pair(newIndexFirst,'Y')] = make_pair(addedStates,make_pair('Y','R'));
			addedStates++;
		}
	
		if (newTransitions.find(make_pair(newIndexFirst,'Z')) == newTransitions.end()) {
			newTransitions[make_pair(newIndexFirst,'Z')] = make_pair(addedStates,make_pair('Z','R'));
			addedStates++;
		}
	
		for (int i=0;i<3;i++) 
			for (int j=0;j<4;j++) {
				int curind = newTransitions[make_pair(newIndexFirst,def[i])].first;
				if (newTransitions.find(make_pair(curind,sym[j])) != newTransitions.end()) continue;
				newTransitions[make_pair(curind,sym[j])] = make_pair(curind,make_pair(sym[j],'R'));
		}

		for (int i=0;i<3;i++) 
			for (int j=0;j<3;j++) {
				if (transitions.find(make_pair(oldIndexFirst,make_pair(sym[i],sym[j]))) != transitions.end()) {
					if (transitions[make_pair(oldIndexFirst,make_pair(sym[i],sym[j]))].first == oldIndexSecond) {
						int firstInd = newTransitions[make_pair(newIndexFirst,def[i])].first;
						int secondInd;
						if (newTransitions.find(make_pair(firstInd,def[j])) == newTransitions.end()) {
							char dir;
							if (oldIndexSecond == n-1) dir = 'R'; else dir = v.second.second.second;
							newTransitions[make_pair(firstInd,def[j])] = make_pair(addedStates,
																	make_pair(v.second.first.second,dir));
							secondInd = addedStates;
							addedStates++; 
						}	else {
							secondInd = newTransitions[make_pair(firstInd,def[j])].first;
						}
						
						int lastind = addedStates;
						if (newTransitions.find(make_pair(secondInd,'$')) == newTransitions.end()) {
							newTransitions[make_pair(secondInd,'$')] = make_pair(lastind,
																	make_pair('_','R'));
							addedStates++;	
							newTransitions[make_pair(lastind,'_')] = make_pair(secondInd,
																	make_pair('$','L'));
						}
						int newInd;
						if (newTransitions.find(make_pair(secondInd,sym[0])) == newTransitions.end()) {
							for (int c = 0; c < 3; c++) {
								newTransitions[make_pair(secondInd,sym[c])] = make_pair(addedStates,
																		make_pair(def[c],'L'));
							}
							newInd = addedStates;
							addedStates++;
						}	else {
							newInd = newTransitions[make_pair(secondInd,sym[0])].first;
						}
						char newSym[4] = {'1','0','_','$'};
						if (newTransitions.find(make_pair(newInd,newSym[0])) == newTransitions.end()) {
	 						for (int c = 0; c < 4; c++) {
								newTransitions[make_pair(newInd,newSym[c])] = make_pair(newInd,
																		make_pair(newSym[c],'L'));	
							}
						}
					
						bool foundAccept = false;
						if (oldIndexSecond == n-1) {
								foundAccept = true;	 
						}
						bool stateAdded = false;
						if (states.find(oldIndexSecond) == states.end()) {
							newIndexSecond = addedStates+13;
							states[oldIndexSecond] = newIndexSecond;
							stateAdded = true;
						}	else {
							newIndexSecond = states[oldIndexSecond];
						}		
						char dir2;
						if (oldIndexSecond == n-1) {
							dir2 = 'R';
						}	else {
							dir2 = v.second.second.first;
						}
						newTransitions[make_pair(newInd,def[i])] = make_pair(addedStates+12,
																	make_pair(v.second.first.first,dir2));
						
						for (int c = 0; c < 3; c++ ) {
							newTransitions[make_pair(addedStates+12,sym[c])] = make_pair(addedStates, 
																make_pair(def[c],'R'));
							if (!foundAccept) {
								newTransitions[make_pair(addedStates,sym[c])] = make_pair(newIndexSecond,
																	make_pair(sym[c],'L'));
							} else {
								newTransitions[make_pair(addedStates,sym[c])] = make_pair(-1,
																	make_pair(sym[c],'L'));
							}
						}			
						
						if (newIndexSecond+1 > maxInd) maxInd = newIndexSecond+1;	

						if (!foundAccept) {
							newTransitions[make_pair(addedStates,'$')] = make_pair(newIndexSecond,
																make_pair('$','L'));
						}
						else {
							newTransitions[make_pair(addedStates,'$')] = make_pair(-1,make_pair('$','L'));										
						}
						addedOnThisTurn++;
						newTransitions[make_pair(addedStates + 12,'$')] = make_pair (addedStates+1,
																	make_pair('_','R'));
						
						createShifters(addedStates+1, newTransitions, newIndexSecond, foundAccept, maxInd);
						addedStates += 13; 
						if (stateAdded) addedStates++;
						if (foundAccept) addedStates--;
						alreadyConverted[make_pair(oldIndexFirst,make_pair(sym[i],sym[j]))] = 1;
					}
				}
			}
		}	
}

int main() {
//	freopen("input.txt","r",stdin);
//	freopen("output.txt","w",stdout);
	int n;
	cin>>n;
	map <pair <int, pair <char, char> >, pair <int, pair < pair <char,char>, pair <char, char> > > > transitions;
	for (int i=0;i<n-1;i++) {
		int c;
		cin>>c;
		for (int j=0;j<c;j++) {
			char fl,sl,nfl,nsl,fd,sd;
			int newState;
			cin>>fl>>sl>>newState>>nfl>>nsl>>fd>>sd;
			transitions[make_pair(i,make_pair(fl,sl))] = make_pair(newState,make_pair(make_pair(nfl,nsl),make_pair(fd,sd)));
		}
	}

	map <pair <int,char> , pair <int, pair <char,char > > > newTransitions;	
	markTheStarters(newTransitions);
	int addedStates = 6;
	map <int,int> states;
	states[0] = 5;
	int maxInd = 5;
	modifyMachine (transitions, states, newTransitions,addedStates, maxInd, n);
	char syms[7] = {'1','0','_','$','X','Y','Z'};
	int mx = 0;
	for (map <pair <int,char> , pair <int, pair <char,char > > >::iterator it = newTransitions.begin(); it != newTransitions.end(); ++it) {
		if (it->first.first > mx) {
			mx = it->first.first;
		}
		if (it->second.first > mx) {
			mx = it->second.first;
		}
	}
	maxInd = mx;
	cout<<maxInd+2<<endl;
	for (int i=0;i<maxInd+1;i++) {
		set <char> moves;
		for (int j=0;j<7;j++) 
			if (newTransitions.find(make_pair(i,syms[j])) != newTransitions.end()) {
				moves.insert(syms[j]);
			}
		cout<<moves.size()<<" ";
		
		for (set<char>::iterator its = moves.begin(); its != moves.end(); ++its) {
			
			char l = *its;
			cout<<l<<" ";
			if (newTransitions[make_pair(i,l)].first == -1) {
				cout<<maxInd+1<<" ";
			}
			else {
				cout<<newTransitions[make_pair(i,l)].first<<" ";
			}
			cout<<newTransitions[make_pair(i,l)].second.first<<" "<<
										newTransitions[make_pair(i,l)].second.second<<" ";
		} 
		
		cout<<endl;
	}
	return 0;
}
