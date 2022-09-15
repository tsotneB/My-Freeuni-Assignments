from __future__ import annotations

from dataclasses import dataclass
from typing import List


@dataclass
class SingleCommandTranslator:
    PUSH_IN_STACK = [
        "@SP",
        "A=M",
        "M=D",
        "@SP",
        "M=M+1",
    ]  # BASED ON THE PREMISE THAT WE HAVE OUR VARIABLE STORED IN D
    PUSH_CONSTANT = ["D=A", "@SP", "A=M", "M=D", "@SP", "M=M+1"]
    PUSH_LOCAL = ["D=A", "@LCL", "A=M+D", "D=M"]
    PUSH_ARGUMENT = ["D=A", "@ARG", "A=M+D", "D=M"]
    PUSH_THIS = ["D=A", "@THIS", "A=M+D", "D=M"]
    PUSH_THAT = ["D=A", "@THAT", "A=M+D", "D=M"]
    PUSH_TEMP = ["D=A", "@5", "A=A+D", "D=M"]
    PUSH_POINTER = ["D=A", "@3", "A=A+D", "D=M"]
    POP_FROM_STACK = ["@SP", "AM=M-1", "D=M", "@13", "A=M", "M=D"]
    POP_LOCAL = ["D=A", "@LCL", "D=M+D", "@13", "M=D"]
    POP_ARGUMENT = ["D=A", "@ARG", "D=M+D", "@13", "M=D"]
    POP_THIS = ["D=A", "@THIS", "D=M+D", "@13", "M=D"]
    POP_THAT = ["D=A", "@THAT", "D=M+D", "@13", "M=D"]
    POP_TEMP = ["D=A", "@5", "D=A+D", "@13", "M=D"]
    POP_POINTER = ["D=A", "@3", "D=A+D", "@13", "M=D"]
    ONE_ARG_FUNCS = ["@SP", "AM=M-1", "D=M", "@13", "M=D"]
    TWO_ARG_FUNCS = ONE_ARG_FUNCS + ["@SP", "AM=M-1", "D=M", "@14", "M=D"]
    COMP_FUNCTION_TEMPLATE = [
        "@SP",
        "A=M-1",
        "D=M",
        "A=A-1",
        "D=M-D",
        "MUST BE EDITED",
        "MUST BE EDITED",
        "@SP",
        "A=M-1",
        "A=A-1",
        "M=0",
        "MUST BE EDITED",
        "0;JMP",
        "MUST BE EDITED",
        "@SP",
        "A=M-1",
        "A=A-1",
        "M=-1",
        "MUST BE EDITED",
        "@SP",
        "M=M-1",
    ]

    OPERATIONS_MAP = {
        "add": ["@SP", "A=M-1", "D=M", "A=A-1", "D=D+M", "M=D", "@SP", "M=M-1"],
        "sub": ["@SP", "A=M-1", "D=M", "A=A-1", "D=M-D", "M=D", "@SP", "M=M-1"],
        "neg": ["@SP", "A=M-1", "M=-M"],
        "not": ["@SP", "A=M-1", "M=!M"],
        "or": ["@SP", "A=M-1", "D=M", "A=A-1", "M=D|M", "@SP", "M=M-1"],
        "and": ["@SP", "A=M-1", "D=M", "A=A-1", "M=D&M", "@SP", "M=M-1"],
        "eq": COMP_FUNCTION_TEMPLATE,
        "lt": COMP_FUNCTION_TEMPLATE,
        "gt": COMP_FUNCTION_TEMPLATE,
    }

    SAVE_FRAME = ["D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"]
    CALL_FUNC_PREP = (
        PUSH_CONSTANT
        + ["@LCL"]
        + SAVE_FRAME
        + ["@ARG"]
        + SAVE_FRAME
        + ["@THIS"]
        + SAVE_FRAME
        + ["@THAT"]
        + SAVE_FRAME
        + ["@SP", "D=M", "@5", "D=D-A"]
    )

    FUNC_RET = (
        ["@LCL", "D=M", "@R13", "M=D", "@5", "A=D-A", "D=M", "@R14", "M=D"]
        + POP_FROM_STACK[:3]
        + [
            "@ARG",
            "A=M",
            "M=D",
            "@ARG",
            "D=M",
            "@SP",
            "M=D+1",
            "@R13",
            "AM=M-1",
            "D=M",
            "@THAT",
            "M=D",
            "@R13",
            "AM=M-1",
            "D=M",
            "@THIS",
            "M=D",
            "@R13",
            "AM=M-1",
            "D=M",
            "@ARG",
            "M=D",
            "@R13",
            "AM=M-1",
            "D=M",
            "@LCL",
            "M=D",
            "@R14",
            "A=M",
            # "A=D",
            "0;JMP",
        ]
    )

    def __init__(self, classForStatics: str):
        self.classForStatics = classForStatics

    def translatecommand(
        self, command: str, segment: str, value: str, ind: int
    ) -> List[str]:
        if command == "push":
            return self.translate_push(segment, value)
        if command == "pop":
            return self.translate_pop(segment, value)
        if command == "label":
            return self.translate_label(segment)
        if command == "if-goto" or command == "goto":
            return self.translate_goto(command, segment)
        if command == "call":
            return self.translate_call(segment, value, ind)
        if command == "function":
            return self.translate_function(segment, value, ind)
        if command == "return":
            return self.translate_return()
        return self.translate_operation(command, ind)

    def translate_push(self, segment: str, value: str) -> List[str]:
        if segment == "constant":
            return self.translate_push_constant(value)
        if segment == "local":
            return self.translate_push_local(value)
        if segment == "argument":
            return self.translate_push_argument(value)
        if segment == "this":
            return self.translate_push_this(value)
        if segment == "that":
            return self.translate_push_that(value)
        if segment == "static":
            return self.translate_push_static(value)
        if segment == "temp":
            return self.translate_push_temp(value)
        if segment == "pointer":
            return self.translate_push_pointer(value)
        return []

    def translate_push_constant(self, value: str) -> List[str]:
        return [f"@{value}"] + ["D=A"] + self.PUSH_IN_STACK

    def translate_push_local(self, value: str) -> List[str]:
        return [f"@{value}"] + self.PUSH_LOCAL + self.PUSH_IN_STACK

    def translate_push_argument(self, value: str) -> List[str]:
        return [f"@{value}"] + self.PUSH_ARGUMENT + self.PUSH_IN_STACK

    def translate_push_this(self, value: str) -> List[str]:
        return [f"@{value}"] + self.PUSH_THIS + self.PUSH_IN_STACK

    def translate_push_that(self, value: str) -> List[str]:
        return [f"@{value}"] + self.PUSH_THAT + self.PUSH_IN_STACK

    def translate_push_static(self, value: str) -> List[str]:
        return [f"@{self.classForStatics}.{value}"] + ["D=M"] + self.PUSH_IN_STACK

    def translate_push_temp(self, value: str) -> List[str]:
        return [f"@{value}"] + self.PUSH_TEMP + self.PUSH_IN_STACK

    def translate_push_pointer(self, value: str) -> List[str]:
        return [f"@{value}"] + self.PUSH_POINTER + self.PUSH_IN_STACK

    def translate_pop(self, segment: str, value: str) -> List[str]:
        if segment == "constant":
            return self.translate_pop_constant(value)
        if segment == "local":
            return self.translate_pop_local(value)
        if segment == "argument":
            return self.translate_pop_argument(value)
        if segment == "this":
            return self.translate_pop_this(value)
        if segment == "that":
            return self.translate_pop_that(value)
        if segment == "static":
            return self.translate_pop_static(value)
        if segment == "temp":
            return self.translate_pop_temp(value)
        if segment == "pointer":
            return self.translate_pop_pointer(value)
        return []

    def translate_pop_constant(self, value: str) -> List[str]:
        return []

    def translate_pop_local(self, value: str) -> List[str]:
        return [f"@{value}"] + self.POP_LOCAL + self.POP_FROM_STACK

    def translate_pop_argument(self, value: str) -> List[str]:
        return [f"@{value}"] + self.POP_ARGUMENT + self.POP_FROM_STACK

    def translate_pop_this(self, value: str) -> List[str]:
        return [f"@{value}"] + self.POP_THIS + self.POP_FROM_STACK

    def translate_pop_that(self, value: str) -> List[str]:
        return [f"@{value}"] + self.POP_THAT + self.POP_FROM_STACK

    def translate_pop_static(self, value: str) -> List[str]:
        return self.POP_FROM_STACK[:3] + [f"@{self.classForStatics}.{value}"] + ["M=D"]

    def translate_pop_temp(self, value: str) -> List[str]:
        return [f"@{value}"] + self.POP_TEMP + self.POP_FROM_STACK

    def translate_pop_pointer(self, value: str) -> List[str]:
        return [f"@{value}"] + self.POP_POINTER + self.POP_FROM_STACK

    def translate_operation(self, op: str, ind: int) -> List[str]:
        starter = self.OPERATIONS_MAP[op]
        if op == "eq":
            starter[5] = f"@TRUE_RESULT{ind}"
            starter[6] = "D;JEQ"
            starter[11] = f"@CONTINUE{ind}"
            starter[13] = f"(TRUE_RESULT{ind})"
            starter[18] = f"(CONTINUE{ind})"
            return starter

        elif op == "gt":
            starter[5] = f"@TRUE_RESULT{ind}"
            starter[6] = "D;JGT"
            starter[11] = f"@CONTINUE{ind}"
            starter[13] = f"(TRUE_RESULT{ind})"
            starter[18] = f"(CONTINUE{ind})"

        elif op == "lt":
            starter[5] = f"@TRUE_RESULT{ind}"
            starter[6] = "D;JLT"
            starter[11] = f"@CONTINUE{ind}"
            starter[13] = f"(TRUE_RESULT{ind})"
            starter[18] = f"(CONTINUE{ind})"

        return starter

    def translate_label(self, segment: str) -> List[str]:
        return [f"({segment})"]

    def translate_goto(self, command: str, segment: str) -> List[str]:
        if command == "goto":
            return [f"@{segment}", "0;JMP"]
        return self.POP_FROM_STACK[:3] + [f"@{segment}", "D;JNE"]

    def translate_call(self, segment: str, value: str, ind: int) -> List[str]:
        retAddr = [f"@{segment}$ret.{ind}"]
        argPointer = [f"@{value}", "D=D-A", "@ARG", "M=D", "@SP", "D=M", "@LCL", "M=D"]
        argPointer = (
            argPointer
            + self.translate_goto("goto", segment)
            + [f"({segment}$ret.{ind})"]
        )
        return retAddr + self.CALL_FUNC_PREP + argPointer

    def translate_function(self, segment: str, value: str, ind: int) -> List[str]:
        func = [f"({segment})"]
        val, _, _ = value.partition(" ")
        valInt = int(val)
        initLocals = ["@0"] + self.PUSH_CONSTANT
        initLocals = initLocals * valInt
        return func + initLocals

    def translate_return(self) -> List[str]:
        return self.FUNC_RET
