@1
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@1           // that = argument[1]
D=A
@3
D=A+D
@13
M=D
@SP
AM=M-1
D=M
@13
A=M
M=D
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@0              // first element in the series = 0
D=A
@THAT
D=M+D
@13
M=D
@SP
AM=M-1
D=M
@13
A=M
M=D
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
@1              // second element in the series = 1
D=A
@THAT
D=M+D
@13
M=D
@SP
AM=M-1
D=M
@13
A=M
M=D
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@2
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
A=M-1
D=M
A=A-1
D=M-D
M=D
@SP
M=M-1
@0          // num_of_elements -= 2 (first 2 elements are set)
D=A
@ARG
D=M+D
@13
M=D
@SP
AM=M-1
D=M
@13
A=M
M=D
(MAIN_LOOP_START)
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@COMPUTE_ELEMENT
D;JNE
@END_PROGRAM
0;JMP
(COMPUTE_ELEMENT)
@0
D=A
@THAT
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@THAT
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
A=M-1
D=M
A=A-1
D=D+M
M=D
@SP
M=M-1
@2              // that[2] = that[0] + that[1]
D=A
@THAT
D=M+D
@13
M=D
@SP
AM=M-1
D=M
@13
A=M
M=D
@1
D=A
@3
A=A+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
A=M-1
D=M
A=A-1
D=D+M
M=D
@SP
M=M-1
@1           // that += 1
D=A
@3
D=A+D
@13
M=D
@SP
AM=M-1
D=M
@13
A=M
M=D
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
A=M-1
D=M
A=A-1
D=M-D
M=D
@SP
M=M-1
@0          // num_of_elements--
D=A
@ARG
D=M+D
@13
M=D
@SP
AM=M-1
D=M
@13
A=M
M=D
@MAIN_LOOP_START
0;JMP
(END_PROGRAM)
