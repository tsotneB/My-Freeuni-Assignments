#include <bits/stdc++.h>
#include <unistd.h>
using namespace std;
string toUpperCase(string s);
void introduction();
void showInstructions();
void codeInput(vector <string>& lines);
void inputByHand(vector <string>& lines);
void inputByFile(vector <string>& lines);
void illegalLine(int line);
bool isNotValid(string address);
void processCode(vector <string>& lines,map<string,int>& registers,map<string,pair<int,int > >& functions);
long long stringToInt(string num);
bool notValid(string s);
bool stackPointerChange(int& SP,int PC, string line,map<string,int>& registers);
bool storeOperation(int SP, int PC, string line, map<string,int>& registers);
bool workWithRegisters(int SP,int PC,string line,map<string,int>& registers);
int determineMemberOfOperation(string str,int SP, int PC, map<string,int>& registers);
bool printfOperation(string line,int SP, int PC, map<string,int>& registers);
bool checkBranch(string line,int SP, int& PC,int size, map<string,int>& registers);
bool jumpInstruction(string line,int& PC,int size);
void callTheFunction(int currPC, map<string,int>& registers,int SP,vector<string>& lines);
bool anotherTry();


#define INF 2309943409 

int mem[100000];

string ERROR_MESSAGE = "Houston, we have a problem... \n";
int main() { 
    introduction();
    while (true) {
        vector <string> lines;
        map<string,int> registers;
        map<string,pair<int, int> > functions;
        codeInput(lines);
        processCode(lines,registers,functions);
        if (!anotherTry()) {
            break;
        }   else {
            cout<<"Cool, let's try again! \n";
        }
    }
    return 0;
}

string toUpperCase(string s) {
    string ans;
    if (s.substr(0,6)=="PRINTF") {
        int i=0;
        while (s[i] != '<') {
            if (s[i] != ' ') if (!isalpha(s[i])) ans+= s[i];  else ans+=toupper(s[i]);
            i++;    
        }
        ans+=s.substr(i);
        return ans;
    }
    for (int i=0;i<s.length();i++)
        if (s[i] != ' ') if (!isalpha(s[i])) ans+= s[i];  else ans+=toupper(s[i]);
    return ans;
}

void introduction() {
    cout<<"Welcome! \n";
    cout<<"This is assembly emulator speaking. I take you on a journey, during which I show you how \n";
    cout<<"code written in Assembly actually works. Together we'll take a look on computer memory, \n";
    cout<<"represent it as an array of four byte integers. We'll also have a map for registers \n";
    cout<<"- also four byte groups within the cpu used to load some information and gives us quick \n";
    cout<<"access to data. But enough with all the theory now, let's get practical. Now, I assume that \n";
    cout<<"the person using this emulator may be beginner. In that case, I advise you to take a little tutorial \n";
    cout<<"more like a list of few basic rules we'll have to follow. \nDo you want to get to know this rules? Y/N: ";
    string s;
    cin>>s;
    s=toUpperCase(s);
    while (s != "Y" && s!="N") {
        cout<<ERROR_MESSAGE<<"Please type Y or N: ";
        cin>>s;
        s=toUpperCase(s);
    }
    if (s == "Y") {
        showInstructions();
    }
}

void showInstructions(){
    cout<<"So, in Assembly, we have 3 kinds of operations:\n";
    cout<<"First one is load operation - when you load some data into registers from computer memory. \n";
    cout<<"For example: R1=M[100] copies four bytes from computer memory (100 is kind of an address) to first register.";
    cout<<endl;
    cout<<"Second operation is opposite of previous and it's called store operation - when you store \n";
    cout<<"data in your computer memory. You can either copy information from registers, or just type by hand.\n";
    cout<<"For example: 1) M[100]=R1 - copies data from first register to 100th element of memory array;\n";
    cout<<"             2) M[100]=5 writes 5 (integer) into 100th element of memory array. \n";
    cout<<"And the last one - ALU operation, which is basically all the arithmetical operations you may \n";
    cout<<"need to use.\n";
    cout<<"But what about that rules we've mentioned? It's actually just one rule! You can't combine \n";
    cout<<"any two operations into one. This emulator only supports code with one operation on one line!\n";
    cout<<"That means that ALU operations (only +,-,* and / are supported (and only one per line)) can only be performed on \ndata stored in registers!\n";    
    cout<<"Also, it only works with integers. Any other types of variables (such as short,char,etc) are strictly forbidden.\n";
    cout<<"In addition, note that uninitialized values of memory array (same goes with registers) will automatically be 0.\n";
    cout<<"That'd be all. Now let's get started!\n";
}

void codeInput(vector <string>& lines){
    cout<<"P.S. Please note that your code will be converted to upper case version no matter what. \n";
    cout<<"Also, space bars will be excluded.\n";
    cout<<"So, For example, R1 and r1 will be same registers.\n";
    cout<<"Type Y if you want to type code by hand, N if you want to use txt files.\n";
    cout<<"Do you want to type code by hand? Y/N: ";
    string s;
    cin>>s;
    s=toUpperCase(s);
    while (s != "Y" && s!="N") {
        cout<<ERROR_MESSAGE<<"Please type Y or N: ";
        cin>>s;
        s=toUpperCase(s);
    }
    if (s=="Y") {
        inputByHand(lines);
        return;
    }   
    inputByFile(lines);
}

void illegalLine (int PC) {
    cout<<"The emulator finished working prematurely. Code wasn't right."<<endl;
    cout<<"The instructions on line No "<<PC/4+1<<" couldn't be done.\n"<<endl;
}

void inputByHand(vector <string>& lines){
    cout<<"Gotta be honest, You've chosen the hard way. Please note that entry string on the line \n"; 
    cout<<"will be perceived as the end of it!\n";
    cout<<"Start typing!\n";
    string line;
    getline(cin,line);
    while (true) {
        string line;
        getline(cin,line);
        if (line == "") break;
        lines.push_back(toUpperCase(line));
    }
}

void inputByFile(vector <string>& lines){
    cout<<"You've chosen to work with files. \n";
    cout<<"Please enter file path: ";
    string address;
    cin>>address;
    while (isNotValid(address)) {
        cout<<"Invalid input. No file can be found with that path. Try Again!\n";
        string curr;
        cin>>curr;
        address = curr;
    }
    ifstream file(address);
    string line;
    while (getline(file,line)) 
        lines.push_back(toUpperCase(line));
}

bool isNotValid(string address) {
    ifstream file(address);
    return !file;
}

long long stringToInt(string num) {
    if (num=="") {
        return 0;
    }   
    return 10*stringToInt(num.substr(0,num.size()-1))+(num[num.size()-1]-'0');
}

bool stackPointerChange(int& SP, int PC, string line, map<string,int>& registers) {
    if (line[3] != 'S') {
        cout<<"Current PC - "<<PC<<".\nThis line performs ALU operation. User wants to change stack pointer.\n";
        int newSp = determineMemberOfOperation(line.substr(3),SP,PC,registers);
        if (newSp < 0 || newSp >=100000) {
            cout<<endl<<ERROR_MESSAGE;
            cout<<"The index of element can't be negative or greater than 99999.\n";
            return false;
        }
        SP=newSp;
        cout<<"Operation done. Stack Pointer has been changed successfully.\n";
        cout<<"It now points to element No "<<SP<<" of the array.\n"<<endl;
        return true;
    }
    cout<<"Current PC - "<<PC<<".\nThis line performs ALU operation. User wants to change stack pointer by ";
    cout<<line.substr(6,line.size()-6)<<"."<<endl;
    long long diff = stringToInt(line.substr(6,line.size()-6));
    if (diff % 4 != 0)  {
        cout<<endl<<ERROR_MESSAGE;
        cout<<"The size of one element in the array is 4 bytes. So the value you want to use as a \n";
        cout<<"decrement or increment must be multiple of 4.\n"<<endl;
        return false;
    }
    if (line[5]=='-')   {
        if (diff/4 > SP) {
            cout<<endl<<ERROR_MESSAGE;
            cout<<"Please note that stack pointer points to element No "<<SP<<endl;
            cout<<"Therefore, index of the element it's pointing can't be decreased by such a big value.\n"<<endl;
            return false;
        }
        SP-=diff/4;
        cout<<"Operation done. Stack Pointer has been changed successfully.\n";
        cout<<"It now points to element No "<<SP<<" of the array.\n"<<endl;
        return true;
    }
    if (SP+diff/4>=100000) {
        cout<<endl<<ERROR_MESSAGE;
        cout<<"Please note that we represent computer memory as an array of 4-byte integers with the size of 100000.\n";
        cout<<"Stack pointer now points to element No "<<SP<<"."<<endl;
        cout<<"Therefore, index of the element it's pointing can not be increased by such a big value.\n"<<endl;
        return false;
    }
    SP=SP+diff/4;
    cout<<"Operation done. Stack Pointer has been changed successfully.\n";
    cout<<"It now points to element No "<<SP<<" of the array.\n"<<endl;
    return true;    
}

bool storeOperation(int SP, int PC, string line, map<string,int>& registers) {
    int ind = 2;
    while (line[ind] != ']') {
        ind++;
    }
    int length=ind-2;
    string memPnt = line.substr(2,length); 
    int numToStore;
    if (line[ind+2]=='R') {
        string reg=line.substr(ind+2);
        numToStore=registers[reg];
    }   else if (line[ind+2]=='S') {
        numToStore=SP;
    }   else if (line[ind+2]=='P') {
        numToStore=PC;
    }   else numToStore = stringToInt(line.substr(ind+2,line.size()-ind-2));
    cout<<"Current PC - "<<PC<<".\nThis line performs store operation. User wants to save some data";
    cout<<" into computer memory."<<endl;
    if (memPnt=="SP") {
        mem[SP]=numToStore;
        cout<<"Operation done. "<<numToStore<<" has been successfully stored in the memory array -"; 
        cout<<" element with index No "<<SP<<".\n"<<endl;
        return true;
    }
    int indexOfMem;
    if (memPnt[0]=='R') {
        indexOfMem=registers[memPnt];
    }   else {
        indexOfMem=stringToInt(memPnt);
    }
    if (indexOfMem<0 || indexOfMem >= 100000) {
        cout<<endl<<ERROR_MESSAGE;
        cout<<"Illegal index. Please note that we represent computer memory as an array of 4-byte ";
        cout<<"integers with the size of 100000. So the index can't be negative or more than 100000.\n"<<endl;
        return false;
    }
    mem[indexOfMem]=numToStore;
    cout<<"Operation done. "<<numToStore<<" has been successfully stored in the memory array -"; 
    cout<<" element with index No "<<indexOfMem<<".\n"<<endl;
    return true;
} 


int determineMemberOfOperation(string str,int SP, int PC, map<string,int>& registers) {
    if (str=="PC") {
        return PC;
    }
    if (str=="SP") {
        return SP;
    }
    if (str[0]=='R') {
        return registers[str];
    }
    return stringToInt(str);
}

bool workWithRegisters(int SP,int PC,string line,map<string,int>& registers) {
    int ind=2;
    while (line[ind] != '=') {
        ind++;
    }
    int indArithOp=-1;
    for (int i=ind+1;i<line.size();i++) {
        if (line[i]=='+' || line[i]=='-'
                || line[i]=='*' || line[i]=='/') {
                    indArithOp=i;
                    break;
                }
    }
    string reg = line.substr(0,ind);
    int regInd = stringToInt(reg.substr(1,reg.size()-1));
    if (indArithOp == -1) {     // There are no ALU operations
        cout<<"Current PC - "<<PC<<".\nThis line performs load operation. User wants to load some data";
        cout<<" into registers either from computer memory, other register or input."<<endl;
        if (line[ind+1]=='S') {
            //Looks like user wants to copy stack pointer value to register
            registers[reg]=SP;
            cout<<"Operation done. Value of stack pointer has been successfully copied to "<<reg<<".\n"<<endl;
            return true;
        }
        if (line[ind+1]=='P') {
            //looks like user wants to copy program counter value to register
            registers[reg]=PC;
            cout<<"Operaion done. Value of Program Counter has been successfully copied to "<<reg<<".\n"<<endl;
            return true;
        }
        if (line[ind+1]=='M') {
            //User wants to load data from computer memory to register
            //This M could be followed by number (typed by hand) or register (SP or regular ones)
            if (line[ind+3]=='S') {
                cout<<"Operation done. Data from memory location "<<SP<<" has been successfully loaded into ";
                cout<<reg<<".\n"<<endl;
                registers[reg]=mem[SP];
                return true;
            }
            if (line[ind+3] == 'R') {
                string regBuff = line.substr(ind+3,line.size()-ind-4);
                int regBuffInd = stringToInt(regBuff.substr(1,regBuff.size()-1));
                cout<<"Operation done. Data from memory location "<<registers[regBuff]<<" has been successfully ";
                cout<<"loaded into "<<reg<<".\n"<<endl;
                registers[reg]=mem[registers[regBuff]];
                return true;
            }
            string loc =  line.substr(ind+3,line.size()-ind-4);
            int memLoc = stringToInt(loc);
            if (memLoc<0 || memLoc >= 100000) {
                cout<<endl<<ERROR_MESSAGE;
                cout<<"Illegal index. Please note that we represent computer memory as an array of 4-byte ";
                cout<<"integers with the size of 100000. So the index can't be negative or more than 100000.\n"<<endl;
                return false;
            }
            registers[reg]=mem[memLoc];
            cout<<"Operation done. Data from memory location "<<memLoc<<" has been Successfully ";
            cout<<"loaded into "<<reg<<".\n"<<endl;
            return true;
        }
        if (line[ind+1]=='R') {
            string regBuff=line.substr(ind+1);
            registers[reg]=registers[regBuff];
            cout<<"Operation done. User wanted to copy data from "<<regBuff<<" to "<<reg<<".\n"<<endl;
            return true;
        }
        cout<<"Looks like user just wants to load integer "<<line.substr(ind+1)<<" into register No "<<regInd<<".\n";
        int numToLoad = stringToInt(line.substr(ind+1));
        registers[reg]=numToLoad;
        cout<<"Operation done. The number "<<numToLoad<<" has been successfully loaded to "<<reg<<" register.\n"<<endl;
        return true;
    }

    //This part of the code works for ALU operations

    cout<<"Current PC - "<<PC<<".\nThis line performs ALU operation."<<endl;    

    string first=line.substr(ind+1,indArithOp-ind-1);
    string second=line.substr(indArithOp+1);
    int firstOp=determineMemberOfOperation(first,SP,PC,registers);
    int secondOp=determineMemberOfOperation(second,SP,PC,registers);
    if (first == "SP") secondOp/=4; 
    if (second == "SP") firstOp/=4;
    switch (line[indArithOp])
    {
        case '+': 
            registers[reg]=firstOp+secondOp;
            break;
        case '-': 
            registers[reg]=firstOp-secondOp;
            break;
        case '*': 
            registers[reg]=firstOp*secondOp;
            break;
        case '/': 
            if (secondOp == 0) {
                cout<<endl<<ERROR_MESSAGE;
                cout<<"Division by zero. Strictly Forbidden!.\n"<<endl;
                return false;
            }
            registers[reg]=firstOp/secondOp;
            break;
    }
    cout<<"As a result of ALU operations performed by CPU, "<<registers[reg]<<" was loaded into "<<reg<<".\n"<<endl;
    return true;
}

bool printfOperation(string line,int SP, int PC, map<string,int>& registers) {
    string askedInfo = line.substr(7,line.size()-8);
    cout<<"Current PC - "<<PC<<".\n";
    if (askedInfo[0]=='S') {
        cout<<"User wants to see where the stack pointer stands.\n";
        cout<<"Operation done. Stack pointer currently pointers to the element with index "<<SP<<" of the memory.\n"<<endl;
        return true;
    }
    if (askedInfo[0]=='P') {
        cout<<"User wants to know what is the current Program Counter.\n";
        cout<<"Operation done. Current Program Counter is "<<PC<<".\n"<<endl;
        return true;
    }
    if (askedInfo=="RV") {
        cout<<"User wants to see what's currently written in RV register.\n";
        cout<<"A little manual: RV is special register used by functions to load their return values.";
        cout<<"So, basically, as a result of this operation, \nreturn value of the last called function  will be printed.";
        cout<<" 0, if no function has been called yet.\n";
        cout<<"Operation done. Current data loaded in RV register is "<<registers[askedInfo]<<".\n"<<endl;
        return true;
    }
    if (askedInfo[0]=='R') {
        cout<<"Looks like user wants to see what's loaded in register "<<askedInfo<<"\n";
        cout<<"Operation done. Data loaded in "<<askedInfo<<" is: "<<registers[askedInfo]<<".\n"<<endl;
        return true;
    }
    if (askedInfo[0]=='M')  {
        cout<<"Looks like user wants to see some data into computer memory."<<endl;
        if (askedInfo[2]=='S') {
            cout<<"Operation done. Data loaded in memory location "<<SP<<" is "<<mem[SP]<<".\n"<<endl;
            return true;
        }
        if (askedInfo[2] == 'R') {
            string reg = askedInfo.substr(2,askedInfo.size()-3);
            cout<<"Operation done. Data loaded in memory location "<<registers[reg]<<" is "<<mem[registers[reg]]<<".\n"<<endl;
            return true;
        }
        int memLoc = stringToInt(askedInfo.substr(2,askedInfo.size()-3));
        if (memLoc<0 || memLoc >= 100000) {
            cout<<endl<<ERROR_MESSAGE;
            cout<<"Illegal index. Please note that we represent computer memory as an array of 4-byte ";
            cout<<"integers with the size of 100000. So the index can't be negative or more than 100000.\n"<<endl;
            return false;
        }
        cout<<"Operation done. Data loaded in memory location "<<memLoc<<" is "<<mem[memLoc]<<".\n"<<endl;
        return true;
    }
    cout<<askedInfo<<"\n"<<endl;
    return true;
}

bool checkBranch(string line, int SP, int& PC,int size, map<string,int>& registers) {
    int firstComma = -1;
    cout<<"Current PC - "<<PC<<".\nThis line is for branch instructions. PLease note that the operands in the test ";
    cout<<"of a branch statement must be  \nin registers or constant values.\n";
    for (int i=3;i<line.size();i++) {
        if (line[i]==',') {
            firstComma = i;
            break;
        }
    }
    int secondComma = -1;
    for (int i=firstComma+1;i<line.size();i++) {
        if (line[i]==',') {
            secondComma = i;
            break;
        }
    }
    int possiblePC;
    if (line[secondComma+1]=='P') {
        int delta=stringToInt(line.substr(secondComma+4));
        if (delta % 4 != 0)  {
            cout<<endl<<ERROR_MESSAGE;
            cout<<"The increment of Program Counter must be multiple of 4.\n"<<endl;
            return false;
        }
        if (line[secondComma+3]=='+')   {
            if ((PC+delta)/4>=size) {
                cout<<endl<<ERROR_MESSAGE;
                cout<<"Can't change program counter by such a big value. Count lines of your code once again!\n"<<endl;
                return false;
            }
            possiblePC=PC+delta;
        }
        if (line[secondComma+3]=='-') {
            if (PC-delta+4<0) {
                cout<<endl<<ERROR_MESSAGE;
                cout<<"Can't change program counter by such a big value. Count lines of your code once again!\n"<<endl;
                return false;
            }
            possiblePC=PC-delta;
        }
    }
    else {
        possiblePC=stringToInt(line.substr(secondComma+1));
        if (possiblePC % 4 != 0)  {
            cout<<endl<<ERROR_MESSAGE;
            cout<<"The value of Program Counter must always be multiple of 4.\n"<<endl;
            return false;
        }
        if (possiblePC/4>=size) {
            cout<<endl<<ERROR_MESSAGE;
            cout<<"Program Counter can't be equal to such a big value. Count lines of your code once again!\n"<<endl;
            return false;
        }
    }
    int firstOperand=determineMemberOfOperation(line.substr(3,firstComma-3),SP,PC,registers);
    int secondOperand=determineMemberOfOperation(line.substr(firstComma+1,secondComma-firstComma-1),SP,PC,registers);
    string op=line.substr(1,2);
    if (op == "LT") {
        if (firstOperand<secondOperand) {
            cout<<"Branch condition is true. Program Counter will be changed to "<<possiblePC<<".\n"<<endl;
            PC=possiblePC;
        }   else {
            cout<<"Branch condition is false. Emulator will just continue executing instructions from the next line.\n"<<endl;
            PC+=4;
        }
        return true;    
    }
    if (op == "LE") {
        if (firstOperand<=secondOperand) {
            cout<<"Branch condition is true. Program Counter will be changed to "<<possiblePC<<".\n"<<endl;
            PC=possiblePC;
        }   else {
            cout<<"Branch condition is false. Emulator will just continue executing instructions from the next line.\n"<<endl;
            PC+=4;
        }
        return true;    
    }
    if (op == "GT") {
        if (firstOperand>secondOperand) {
            cout<<"Branch condition is true. Program Counter will be changed to "<<possiblePC<<".\n"<<endl;
            PC=possiblePC;
        }   else {
            cout<<"Branch condition is false. Emulator will just continue executing instructions from the next line.\n"<<endl;
            PC+=4;
        }
        return true;    
    }
    if (op == "GE") {
        if (firstOperand>=secondOperand) {
            cout<<"Branch condition is true. Program Counter will be changed to "<<possiblePC<<".\n"<<endl;
            PC=possiblePC;
        }   else {
            cout<<"Branch condition is false. Emulator will just continue executing instructions from the next line.\n"<<endl;
            PC+=4;
        }
        return true;    
    }
    if (op == "EQ") {
        if (firstOperand==secondOperand) {
            cout<<"Branch condition is true. Program Counter will be changed to "<<possiblePC<<".\n"<<endl;
            PC=possiblePC;
        }   else {
            cout<<"Branch condition is false. Emulator will just continue executing instructions from the next line.\n"<<endl;
            PC+=4;
        }
        return true;    
    }
    if (op == "NE") {
        if (firstOperand!=secondOperand) {
            cout<<"Branch condition is true. Program Counter will be changed to "<<possiblePC<<".\n"<<endl;
            PC+=possiblePC;
        }   else {
            cout<<"Branch condition is false. Emulator will just continue executing instructions from the next line.\n"<<endl;
            PC+=4;
        }
        return true;    
    }
    return false;
}

bool jumpInstruction(string line,int& PC,int size){
    cout<<"Current PC - "<<PC<<".\nThis line is for jump operation.\n";
    if (line[3] =='P') {
        int delta=stringToInt(line.substr(6));
        if (delta % 4 != 0)  {
            cout<<endl<<ERROR_MESSAGE;
            cout<<"The increment of Program Counter must be multiple of 4.\n"<<endl;
            return false;
        }
        if (line[5]=='-')  {
            if (PC-delta+4<0) {
                cout<<endl<<ERROR_MESSAGE;
                cout<<"The increment of Program Counter must be multiple of 4.\n"<<endl;
                return false;
            }
            PC-=delta-4;
            cout<<"Jump operation successfully executed. Program Counter will be changed to "<<PC<<".\n"<<endl;
            return true;
        }
        if ((PC+delta)/4>=size) {
            cout<<endl<<ERROR_MESSAGE;
            cout<<"Can't change program counter by such a big value. Count lines of your code once again!\n"<<endl;
            return false;
        }
        PC+=delta;
        cout<<"Jump operation successfully executed. Program Counter will be changed to "<<PC<<".\n"<<endl;
        return true;
    }
    int newPC = stringToInt(line.substr(3));
    if (newPC % 4 != 0)  {
        cout<<endl<<ERROR_MESSAGE;
        cout<<"The value of Program Counter must always be multiple of 4.\n"<<endl;
        return false;
    }
    if (newPC/4>=size){
        cout<<endl<<ERROR_MESSAGE;
        cout<<"Program Counter can't be equal to such a big value. Count lines of your code once again!\n"<<endl;
        return false;
    } 
    PC=newPC; 
    cout<<"Jump operation successfully executed. Program Counter will be changed to "<<PC<<".\n"<<endl; 
    return true; 
}

bool notValid(string s) {
    bool first=false, second=false;
    for (int i=0;i<s.size();i++) 
        if (s[i]=='M') first=true; else if (s[i]=='+' || s[i]=='-' || s[i]=='/' || s[i]=='*') second=true; 
    return first && second;
}

void processCode(vector <string>& lines,map<string,int>& registers,map<string,pair<int,int> >& functions) {
    int RV=INF,SP=1000,PC=0;
    stack<string> funcs;
    bool retVal = false;
    int st = 0;
    cout<<"\nIn the beginning, stack pointer points to element No "<<SP<<" of the memory.\n\n";
    while (PC/4 < lines.size()) {
        string line = lines[PC/4];
        if (line[0]!='J' && line[0]!='P')
        if (notValid(line)) {
            illegalLine(PC);
            cout<<"More than one operation per line is strictly forbidden!\n"<<endl;
            return;
        }   
        if (line.substr(0,2) == "SP") {
            if (stackPointerChange(SP,PC,line,registers)) {;
                PC+=4;
                continue;
            }   else {
                illegalLine(PC);
                return;
            }
        }
        if (line[0] == 'M') {
            if (storeOperation(SP, PC, line, registers)) {
                PC+=4;
                continue;
            }   else {
                illegalLine(PC);
                return;
            }
        }
        if (line=="RET") {
            cout<<"Executing of function "<<funcs.top()<<" has finished.\n";
            if (retVal) {
                cout<<"This function returned "<<registers["RV"]<<".\n"<<endl;
            }   else {
                cout<<"Apparently, this was void function. It didn't return anything.\n"<<endl;
            }
            PC=mem[SP];
            SP=SP+1;
            funcs.pop();
            continue;
        }
        if (line[0] == 'R') {
            if (line[1]=='V') retVal=true;
            if (workWithRegisters(SP,PC,line,registers)) {
                PC+=4;
                continue;
            }   else {
                illegalLine(PC);
                return;
            }
        }
        if (line.substr(0,6) == "PRINTF") {
           if (printfOperation(line,SP,PC,registers)) {
               PC+=4;
               continue;
            }   else {
                illegalLine(PC);
                return;
            }
        }
        if (line[0]=='B') {
            if (checkBranch(line,SP,PC,lines.size(),registers)) {
                continue;
            }   else {
                illegalLine(PC);
                return;
            }
        }
        if (line[0]=='J') {
            if (jumpInstruction(line,PC,lines.size())) {
                continue;
            }   else {
                illegalLine(PC);
                return;
            }
        }
        if (line[0]=='D') {
            string functionDef=line.substr(7,line.size()-8);
            PC+=4;
            functions[functionDef].first=PC;
            functions[functionDef].second=-1;
            while (lines[PC/4]!="END_DEF") PC+=4;
            PC+=4;
            cout<<"Emulator memorized the address on which definition of the function "<<functionDef<<" starts.\n";
            cout<<"When user calls this function, Program Counter will become "<<functions[functionDef].first<<" and the execution";
            cout<<" of this particular function will begin.\n"<<endl;
        }
        if (line[0]=='C') {
            string functionCall=line.substr(5,line.size()-6);
            if (functions.find(functionCall)==functions.end()) {
                cout<<"User hasn't defined called function.\n";
                illegalLine(PC);
                return;
            }
            cout<<"User called function "<<functionCall<<".\n";
            cout<<"Program Counter has become "<<functions[functionCall].first<<" and so execution of this function begins.\n"<<endl;
            SP=SP-1;
            mem[SP]=PC+4;
            functions[functionCall].second=PC+4;
            PC=functions[functionCall].first;
            funcs.push(functionCall);
            continue;
        }
    }
}

bool anotherTry(){
    cout<<"Emulator finished processing the code. Want to try one more time? Y/N: ";
    string s;
    cin>>s;
    s=toUpperCase(s);
    while (s != "Y" && s!="N") {
        cout<<ERROR_MESSAGE<<"Please type Y or N: ";
        cin>>s;
        s=toUpperCase(s);
    }
    return (s=="Y");
}