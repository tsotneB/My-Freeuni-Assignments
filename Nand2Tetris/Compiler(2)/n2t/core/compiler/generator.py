from __future__ import annotations

from dataclasses import dataclass
from typing import Dict, List, Tuple


class Identifier:
    type: str
    kind: str
    index: int

    def __init__(self, type: str, kind: str, index: int) -> None:
        self.type = type
        self.kind = kind
        self.index = index


@dataclass
class CodeGenerator:
    tokens: List[str]
    label_count: int
    SUBROUTINE = "<subroutineDec>"
    CLASS_VARIABLE = "<classVarDec>"
    PARAMETERS = "<parameterList>"
    LOCAL_VARIABLES = "<varDec>"
    CONSTRUCTOR = "constructor"
    SYMBOL = "<symbol>"
    LOCAL = "local"
    STATEMENTS = "<statements>"
    LET = "<letStatement>"
    IF = "<ifStatement>"
    WHILE = "<whileStatement>"
    DO = "<doStatement>"
    RETURN = "<returnStatement>"
    UNARY_OPS = {"~": "not", "-": "neg"}
    OPERATIONS = {
        "+": "add",
        "-": "sub",
        "*": "call Math.multiply 2",
        "/": "call Math.divide 2",
        "=": "eq",
        "&gt;": "gt",
        "&lt;": "lt",
        "&amp;": "and",
        "|": "or",
    }
    METHOD = "method"
    EXP_LIST = "<expressionList>"
    STATIC = "static"

    @classmethod
    def create(cls, tokens: List[str]) -> CodeGenerator:
        return cls(tokens, label_count=0)

    def get_label(self) -> str:
        label = f"Label${self.label_count}"
        self.label_count = self.label_count + 1
        return label

    def trim_line(self, i: int) -> Tuple[str, str]:
        line = self.tokens[i].strip()
        f = line.index(">")
        s = line.index("<", f + 1)
        ft = f + 2
        st = s - 1
        inp = line[ft:st]
        tag = line[1:f]
        return tag, inp

    def generate(self) -> List[str]:
        _, className = self.trim_line(2)
        ind = 4  # ClassVarDec or SubroutineDec
        class_symbols: Dict[str, Identifier] = dict()
        class_symbols, ind, _ = self.construct_symbols(
            ind, class_symbols, 0, 0, self.CLASS_VARIABLE
        )  # ind points to subroutineDec
        return self.generate_subroutines(className, ind, class_symbols)

    def construct_symbols(
        self,
        i: int,
        symbols: Dict[str, Identifier],
        startindex: int,
        startforstatics: int,
        subpart_name: str,
    ) -> Tuple[Dict[str, Identifier], int, int]:
        subpart = self.tokens[i].strip()
        num_var = 0
        if not subpart == subpart_name:
            return symbols, i, 0
        index_of_variable = startindex
        index_of_static = startforstatics
        while self.tokens[i].strip() == subpart_name:
            i = i + 1
            (
                name,
                var,
                i,
                index_of_variable,
                index_of_static,
            ) = self.generate_single_type_variables(
                i, index_of_variable, index_of_static, symbols, subpart_name
            )
            i = i + 1
            num_var = index_of_variable - startindex + index_of_static - startforstatics
        return symbols, i, num_var

    def generate_single_type_variables(
        self,
        index_of_line: int,
        index_of_variable: int,
        index_of_static: int,
        symbols: Dict[str, Identifier],
        subpart_name: str,
    ) -> Tuple[str, Identifier, int, int, int]:
        _, kind = self.trim_line(index_of_line)
        index_of_line = index_of_line + 1
        if subpart_name == self.LOCAL_VARIABLES:
            kind = self.LOCAL
        _, type = self.trim_line(index_of_line)
        index_of_line = index_of_line + 1
        _, name = self.trim_line(index_of_line)
        variable = Identifier(type, kind, index_of_variable)
        if kind == self.STATIC:
            index_of_static = index_of_static + 1
        else:
            index_of_variable = index_of_variable + 1
        index_of_line = index_of_line + 1
        _, sym = self.trim_line(index_of_line)
        symbols[name] = variable
        while sym == ",":
            index_of_line = index_of_line + 1
            _, name = self.trim_line(index_of_line)
            num: int
            if kind == self.STATIC:
                num = index_of_static
            else:
                num = index_of_variable

            variable = Identifier(type, kind, num)
            symbols[name] = variable
            num = num + 1
            if type == self.STATIC:
                index_of_static = num
            else:
                index_of_variable = num

            index_of_line = index_of_line + 1
            _, sym = self.trim_line(index_of_line)
        index_of_line = index_of_line + 1
        return name, variable, index_of_line, index_of_variable, index_of_static

    def generate_subroutines(
        self, class_name: str, index: int, class_symbols: Dict[str, Identifier]
    ) -> List[str]:
        ans: List[str] = list()
        while self.tokens[index].strip() == self.SUBROUTINE:
            index = index + 1
            new_ans, index = self.generate_single_subroutine(
                class_name, index, class_symbols
            )
            ans = ans + new_ans
            index = index + 3
        return ans

    def generate_single_subroutine(
        self, class_name: str, index: int, class_symbols: Dict[str, Identifier]
    ) -> Tuple[List[str], int]:
        _, type = self.trim_line(index)  # constructor, method, function
        index = index + 1  # return type
        index = index + 1  # name
        _, name = self.trim_line(index)
        index = index + 2  # <parameterList>
        symbols, index, num_variables = self.construct_subroutine_parameters(
            class_name, index, type
        )
        index = index + 2  # index on varDec or statements
        symbols, index, variable_count = self.construct_symbols(
            index, symbols, 0, 0, self.LOCAL_VARIABLES
        )  # index on statements
        statements, index = self.generate_statements(
            class_name, index, class_symbols, symbols
        )
        constructor_statements: List[str] = list()
        method_statements: List[str] = list()
        if type == self.CONSTRUCTOR:
            constructor_statements.append(
                "push constant " + str(len(class_symbols.keys()))
            )
            constructor_statements.append("call Memory.alloc 1")
            constructor_statements.append("pop pointer 0")
        elif type == self.METHOD:
            method_statements.append("push argument 0")
            method_statements.append("pop pointer 0")
        statements = (
            ["function " + class_name + "." + name + " " + str(variable_count)]
            + constructor_statements
            + method_statements
            + statements
        )
        return statements, index

    def construct_subroutine_parameters(
        self, class_name: str, index: int, type: str
    ) -> Tuple[Dict[str, Identifier], int, int]:
        symbols: Dict[str, Identifier] = dict()
        index = index + 1
        params = self.tokens[index].strip()  # <keyword> or </parameterList>
        index_of_parameters = 0
        if type == self.METHOD:
            self_argument = Identifier(class_name, "argument", index_of_parameters)
            symbols["this"] = self_argument
            index_of_parameters = index_of_parameters + 1
        while not params == self.PARAMETERS[0] + "/" + self.PARAMETERS[1:]:
            if params.startswith("<symbol>"):
                index = index + 1
                params = self.tokens[index].strip()
                continue
            tag, type = self.trim_line(index)
            index = index + 1
            _, name = self.trim_line(index)
            param = Identifier(type, "argument", index_of_parameters)
            symbols[name] = param
            index_of_parameters = index_of_parameters + 1
            index = index + 1
            params = self.tokens[index].strip()
        return symbols, index + 2, index_of_parameters  # index on subroutineBody

    def generate_statements(
        self,
        class_name: str,
        index: int,
        class_symbols: Dict[str, Identifier],
        symbols: Dict[str, Identifier],
    ) -> Tuple[List[str], int]:
        # index on <statements>
        vm_lines: List[str] = list()
        index = index + 1  # index on statement type
        inp = self.tokens[index].strip()
        while inp != self.STATEMENTS[0] + "/" + self.STATEMENTS[1:]:
            if inp == self.LET:
                newLines, index = self.generate_let(
                    class_name, index, class_symbols, symbols
                )
                vm_lines = vm_lines + newLines
                inp = self.tokens[index].strip()
                continue
            if inp == self.DO:
                newLines, index = self.generate_do(
                    class_name, index, class_symbols, symbols
                )
                vm_lines = vm_lines + newLines
                inp = self.tokens[index].strip()
                continue
            if inp == self.WHILE:
                newLines, index = self.generate_while(
                    class_name, index, class_symbols, symbols
                )
                vm_lines = vm_lines + newLines
                inp = self.tokens[index].strip()
                continue
            if inp == self.IF:
                newLines, index = self.generate_if(
                    class_name, index, class_symbols, symbols
                )
                vm_lines = vm_lines + newLines
                inp = self.tokens[index].strip()
                continue
            if inp == self.RETURN:
                newLines, index = self.generate_return(
                    class_name, index, class_symbols, symbols
                )
                vm_lines = vm_lines + newLines
                inp = self.tokens[index].strip()
                continue
        return vm_lines, index + 1  # jump over </statements>

    def generate_let(
        self,
        class_name: str,
        index: int,
        class_symbols: Dict[str, Identifier],
        symbols: Dict[str, Identifier],
    ) -> Tuple[List[str], int]:
        # index points to <letStatement>
        index = index + 2  # on symbol
        segment, type_of_var, index_of_variable = self.look_variable_up(
            index, class_symbols, symbols
        )  # variable found
        index = index + 1  # =, (, ., [
        tag, inp = self.trim_line(index)
        if inp == "[":
            index = index + 1  # inside of brackets
            ans, index = self.generate_expression(
                class_name, index, class_symbols, symbols
            )
            ans = ["push " + segment + " " + str(index_of_variable)] + ans
            ans.append("add")
            index = index + 2
            new_ans, index = self.generate_expression(
                class_name, index, class_symbols, symbols
            )
            ans = ans + new_ans
            ans.append("pop temp 0")
            ans.append("pop pointer 1")
            ans.append("push temp 0")
            ans.append("pop that 0")
        else:
            index = index + 1  # second part
            ans, index = self.generate_expression(
                class_name, index, class_symbols, symbols
            )
            ans.append("pop " + segment + " " + str(index_of_variable))
        return ans, index + 2  # jumps over </letStatement>

    def generate_do(
        self,
        class_name: str,
        index: int,
        class_symbols: Dict[str, Identifier],
        symbols: Dict[str, Identifier],
    ) -> Tuple[List[str], int]:
        index = index + 1
        ans, index = self.generate_term(class_name, index, class_symbols, symbols)
        ans.append("pop temp 0")
        return ans, index + 1  # jumps over </doStatement>

    def generate_while(
        self,
        class_name: str,
        index: int,
        class_symbols: Dict[str, Identifier],
        symbols: Dict[str, Identifier],
    ) -> Tuple[List[str], int]:
        # index on <whileStatement>
        ans: List[str] = list()
        start_label = self.get_label()
        ans.append("label " + start_label)
        index = index + 3  # index on expression
        new_ans, index = self.generate_expression(
            class_name, index, class_symbols, symbols
        )
        ans = ans + new_ans
        ans.append("not")
        end_label = self.get_label()
        ans.append("if-goto " + end_label)
        index = index + 2  # index on statements
        new_ans, index = self.generate_statements(
            class_name, index, class_symbols, symbols
        )
        ans = ans + new_ans
        ans.append("goto " + start_label)
        ans.append("label " + end_label)
        return ans, index + 2

    def generate_return(
        self,
        class_name: str,
        index: int,
        class_symbols: Dict[str, Identifier],
        symbols: Dict[str, Identifier],
    ) -> Tuple[List[str], int]:
        index = index + 2
        ans: List[str] = list()
        if self.tokens[index].strip().startswith("<symbol>"):
            ans.append("push constant 0")
        else:
            ans, index = self.generate_expression(
                class_name, index, class_symbols, symbols
            )
        ans.append("return")
        return ans, index + 2  # jumps over </returnStatement>

    def generate_if(
        self,
        class_name: str,
        index: int,
        class_symbols: Dict[str, Identifier],
        symbols: Dict[str, Identifier],
    ) -> Tuple[List[str], int]:
        # index on <ifStatement>
        index = index + 3  # index on <expression>
        ans, index = self.generate_expression(class_name, index, class_symbols, symbols)
        ans.append("not")
        index = index + 2  # index on statements
        new_label = self.get_label()
        ans.append("if-goto " + new_label)
        new_ans, index = self.generate_statements(
            class_name, index, class_symbols, symbols
        )
        ans = ans + new_ans
        index = index + 1  # jump over }
        if self.tokens[index].strip() == "</ifStatement>":
            ans.append("label " + new_label)
            return ans, index + 1  # jump over </ifStatement>
        else:
            index = index + 2
            end_label = self.get_label()
            ans.append("goto " + end_label)
            ans.append("label " + new_label)
            new_ans, index = self.generate_statements(
                class_name, index, class_symbols, symbols
            )
            ans = ans + new_ans
            ans.append("label " + end_label)
            return ans, index + 2  # jump over </ifStatement>

    def look_variable_up(
        self,
        index: int,
        class_symbols: Dict[str, Identifier],
        symbols: Dict[str, Identifier],
    ) -> Tuple[str, str, int]:
        _, name = self.trim_line(index)
        variable: Identifier
        if name in class_symbols.keys():
            variable = class_symbols[name]
        elif name not in symbols.keys():
            return "NOT FOUND", "NOT FOUND", -1
        else:
            variable = symbols[name]
        segment: str
        if variable.kind == "field":
            segment = "this"
        else:
            segment = variable.kind
        type_of_var = variable.type
        return segment, type_of_var, variable.index

    # before: line on <expression>
    # after: line on after </expression>
    def generate_expression(
        self,
        class_name: str,
        index: int,
        class_symbols: Dict[str, Identifier],
        symbols: Dict[str, Identifier],
    ) -> Tuple[List[str], int]:
        # index stands on <expression>
        index = index + 1
        # index stands on <term>
        ans, index = self.generate_term(class_name, index, class_symbols, symbols)
        while not self.tokens[index].strip() == "</expression>":
            _, inp = self.trim_line(index)
            newans, index = self.generate_term(
                class_name, index + 1, class_symbols, symbols
            )
            ans = ans + newans

            ans.append(self.OPERATIONS[inp])
        index = index + 1  # jumps over </expression>
        return ans, index

    # before: line on <term>
    # after: line on after </term>
    def generate_term(
        self,
        class_name: str,
        index: int,
        class_symbols: Dict[str, Identifier],
        symbols: Dict[str, Identifier],
    ) -> Tuple[List[str], int]:
        index = index + 1
        # index on variable
        tag, inp = self.trim_line(index)
        if inp in self.UNARY_OPS.keys():
            ans, index = self.generate_term(
                class_name, index + 1, class_symbols, symbols
            )
            ans.append(self.UNARY_OPS[inp])
            return ans, index + 1
        if tag == "integerConstant":
            return ["push constant " + inp], index + 2  # jumps over </term>
        if tag == "stringConstant":
            return self.generate_string_constant(inp), index + 2  # jumps over </term>
        if inp == "this":
            return ["push pointer 0"], index + 2  # jumps over </term>
        if inp == "true":
            lsttrue: List[str] = ["push constant 1", "neg"]
            return lsttrue, index + 2
        if inp == "false":
            lstfalse: List[str] = ["push constant 0"]
            return lstfalse, index + 2
        if inp == "null":
            return ["push constant 0"], index + 2  # jumps over </term>
        return self.generate_identifier(class_name, index, class_symbols, symbols)

    def generate_string_constant(self, s: str) -> List[str]:
        length = len(s)
        ans = ["push constant " + str(length), "call String.new 1"]
        for char in s:
            code = ord(char)
            ans.append("push constant " + str(code))
            ans.append("call String.appendChar 2")
        return ans

    def generate_identifier(
        self,
        class_name: str,
        index: int,
        class_symbols: Dict[str, Identifier],
        symbols: Dict[str, Identifier],
    ) -> Tuple[List[str], int]:
        # index on variable

        _, var = self.trim_line(index)  # name
        segment, type_of_var, index_of_variable = self.look_variable_up(
            index, class_symbols, symbols
        )
        index = index + 1

        if self.tokens[index].strip() == "<expression>":
            ans, index = self.generate_expression(
                class_name, index, class_symbols, symbols
            )
            return ans, index + 2

        if not self.tokens[index].strip() == "</term>":
            _, next_symbol = self.trim_line(index)
        else:
            next_symbol = "</term>"
        index = index + 1
        if next_symbol not in ["[", ".", "("]:
            return ["push " + segment + " " + str(index_of_variable)], index
        if next_symbol == "[":
            ans, index = self.generate_array(
                class_name, segment, index_of_variable, index, class_symbols, symbols
            )
            return ans, index + 1
        if next_symbol == ".":
            return self.generate_object_method_call(
                class_name,
                var,
                type_of_var,
                segment,
                index_of_variable,
                index,
                class_symbols,
                symbols,
            )  # index over expressionList
        return self.generate_this_method_call(
            var, class_name, index, class_symbols, symbols
        )

    def generate_array(
        self,
        class_name: str,
        segment: str,
        index_of_variable: int,
        index: int,
        class_symbols: Dict[str, Identifier],
        symbols: Dict[str, Identifier],
    ) -> Tuple[List[str], int]:
        # index on after [
        ans, index = self.generate_expression(class_name, index, class_symbols, symbols)
        ans = ["push " + segment + " " + str(index_of_variable)] + ans
        ans.append("add")
        ans.append("pop pointer 1")
        ans.append("push that 0")
        return ans, index + 1  # jumps over ]

    def generate_object_method_call(
        self,
        class_name: str,
        var_name: str,
        type_of_var: str,
        segment: str,
        index_of_variable: int,
        index: int,
        class_symbols: Dict[str, Identifier],
        symbols: Dict[str, Identifier],
    ) -> Tuple[List[str], int]:
        # index on function
        _, func_name = self.trim_line(index)
        call_for: str
        if index_of_variable == -1:
            call_for = var_name
        else:
            call_for = type_of_var
        index = index + 2  # index on expressionlist
        param = self.tokens[index].strip()
        ans: List[str] = list()
        args_count = 0

        if segment in ["local", "argument", "this", "static"]:
            ans.append("push " + segment + " " + str(index_of_variable))
            args_count = args_count + 1  # first argument for method

        index = index + 1
        if self.tokens[index].strip() == "</expressionList>":
            ans.append("call " + call_for + "." + func_name + " " + str(args_count))
            return ans, index + 3
        while not param == self.EXP_LIST[0] + "/" + self.EXP_LIST[1:]:
            new_ans, index = self.generate_expression(
                class_name, index, class_symbols, symbols
            )
            ans = ans + new_ans
            args_count = args_count + 1
            if self.tokens[index].strip().startswith("<symbol>"):
                index = index + 1
            param = self.tokens[index].strip()
        # index on '</expressionList>

        ans.append("call " + call_for + "." + func_name + " " + str(args_count))
        return ans, index + 3  # jumps over </expressionList> and ) and </term>

    def generate_this_method_call(
        self,
        function: str,
        class_name: str,
        index: int,
        class_symbols: Dict[str, Identifier],
        symbols: Dict[str, Identifier],
    ) -> Tuple[List[str], int]:
        # index is on <expressionList>
        index = index + 1
        param = self.tokens[index].strip()
        ans: List[str] = list()
        args_count = 1
        ans.append("push pointer 0")
        while not param == self.EXP_LIST[0] + "/" + self.EXP_LIST[1:]:
            new_ans, index = self.generate_expression(
                class_name, index, class_symbols, symbols
            )
            ans = ans + new_ans
            args_count = args_count + 1
            if self.tokens[index].strip().startswith("<symbol>"):
                index = index + 1
            param = self.tokens[index].strip()
        call_for = class_name
        ans.append("call " + call_for + "." + function + " " + str(args_count))
        return ans, index + 3
