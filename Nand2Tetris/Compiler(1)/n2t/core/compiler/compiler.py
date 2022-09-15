from __future__ import annotations

from dataclasses import dataclass
from typing import List, Tuple


@dataclass
class CompilationEngine:
    tokens: List[str]
    NON_TERMINALS = [
        "keyword",
        "symbol",
        "integerConstant",
        "stringConstant",
        "identifier",
    ]
    CLASS_VAR_DEC = ["field", "static"]
    SUB_ROUTINE_DEC = ["constructor", "method", "function"]
    OP = ["+", "-", "*", "/", "&amp;", "|", "&lt;", "&gt;", "="]
    TAB = "  "

    @classmethod
    def create(cls, tokens: List[str]) -> CompilationEngine:
        return cls(tokens)

    def trim_line(self, i: int) -> Tuple[str, str]:
        line = self.tokens[i]
        f = line.index(">")
        s = line.index("<", f + 1)
        ft = f + 2
        st = s - 1
        inp = line[ft:st]
        tag = line[1:f]
        return tag, inp

    def compile(self) -> List[str]:
        lines: List[str] = list()
        lines.append("<class>")
        lines = lines + self.compile_class()
        lines.append("</class>")
        return lines

    def compile_class(self) -> List[str]:
        numofindents = 1
        lines: List[str] = list()
        lines.append(numofindents * self.TAB + self.tokens[0])
        lines.append(numofindents * self.TAB + self.tokens[1])
        lines.append(numofindents * self.TAB + self.tokens[2])
        i = 3
        tag, inp = self.trim_line(i)
        if inp in self.CLASS_VAR_DEC:
            newlines, j = self.compile_class_var_dec(i, numofindents)
            lines = lines + newlines
            i = j
            tag, inp = self.trim_line(i)
        while inp in self.SUB_ROUTINE_DEC:
            lines.append(numofindents * self.TAB + "<subroutineDec>")
            newlines, j = self.compile_subroutine_dec(i, numofindents)
            lines = lines + newlines
            lines.append(numofindents * self.TAB + "</subroutineDec>")
            i = j
            i = i + 1
            tag, inp = self.trim_line(i)
        lines.append(numofindents * self.TAB + self.tokens[i])
        return lines

    def compile_class_var_dec(self, i: int, numofindents: int) -> Tuple[List[str], int]:
        lines: List[str] = list()
        tag, inp = self.trim_line(i)
        while inp in self.CLASS_VAR_DEC:
            lines.append(numofindents * self.TAB + "<classVarDec>")
            while inp != ";":
                lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
                i = i + 1
                tag, inp = self.trim_line(i)
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            tag, inp = self.trim_line(i)
            lines.append(numofindents * self.TAB + "</classVarDec>")
        return lines, i

    def compile_subroutine_dec(
        self, i: int, numofindents: int
    ) -> Tuple[List[str], int]:
        lines: List[str] = list()
        tag, inp = self.trim_line(i)
        while inp in self.SUB_ROUTINE_DEC:
            while inp != "(":
                lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
                i = i + 1
                tag, inp = self.trim_line(i)
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            tag, inp = self.trim_line(i)
            lines.append(self.TAB * (numofindents + 1) + "<parameterList>")
            newlines, j = self.compile_parameter_list(i, numofindents + 1)
            lines = lines + newlines
            lines.append(self.TAB * (numofindents + 1) + "</parameterList>")
            i = j
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            lines.append(self.TAB * (numofindents + 1) + "<subroutineBody>")
            newlines, j = self.compile_subroutine_body(i, numofindents + 1)
            i = j
            lines = lines + newlines
            lines.append(self.TAB * (numofindents + 1) + "</subroutineBody>")
            tag, inp = self.trim_line(i)
        return lines, i

    def compile_parameter_list(
        self, i: int, numofindents: int
    ) -> Tuple[List[str], int]:
        lines: List[str] = list()
        tag, inp = self.trim_line(i)
        while inp != ")":
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            tag, inp = self.trim_line(i)
        return lines, i

    def compile_subroutine_body(
        self, i: int, numofindents: int
    ) -> Tuple[List[str], int]:
        lines: List[str] = list()
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        tag, inp = self.trim_line(i)
        if inp == "var":
            newlines, j = self.compile_var_dec(i, numofindents + 1)
            lines = lines + newlines
            i = j
            tag, inp = self.trim_line(i)
        lines.append(self.TAB * (numofindents + 1) + "<statements>")
        newlines, j = self.compile_statements(i, numofindents + 1)
        lines = lines + newlines
        i = j
        lines.append(self.TAB * (numofindents + 1) + "</statements>")
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        return lines, i

    def compile_var_dec(self, i: int, numofindents: int) -> Tuple[List[str], int]:
        lines: List[str] = list()
        tag, inp = self.trim_line(i)
        while inp == "var":
            lines.append(self.TAB * numofindents + "<varDec>")
            while inp != ";":
                lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
                i = i + 1
                tag, inp = self.trim_line(i)
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            tag, inp = self.trim_line(i)
            lines.append(self.TAB * numofindents + "</varDec>")
        return lines, i

    def compile_statements(self, i: int, numofindents: int) -> Tuple[List[str], int]:
        lines: List[str] = list()
        tag, inp = self.trim_line(i)
        while inp != "}":
            if inp == "let":
                lines.append(self.TAB * (numofindents + 1) + "<letStatement>")
                newlines, j = self.compile_let_statement(i, numofindents + 1)
                lines = lines + newlines
                lines.append(self.TAB * (numofindents + 1) + "</letStatement>")
                i = j
                tag, inp = self.trim_line(i)
                continue
            if inp == "do":
                lines.append(self.TAB * (numofindents + 1) + "<doStatement>")
                newlines, j = self.compile_do_statement(i, numofindents + 1)
                lines = lines + newlines
                lines.append(self.TAB * (numofindents + 1) + "</doStatement>")
                i = j
                tag, inp = self.trim_line(i)
                continue
            if inp == "return":
                lines.append(self.TAB * (numofindents + 1) + "<returnStatement>")
                newlines, j = self.compile_return_statement(i, numofindents + 1)
                lines = lines + newlines
                lines.append(self.TAB * (numofindents + 1) + "</returnStatement>")
                i = j
                tag, inp = self.trim_line(i)
                continue
            if inp == "while":
                lines.append(self.TAB * (numofindents + 1) + "<whileStatement>")
                newlines, j = self.compile_while_statement(i, numofindents + 1)
                lines = lines + newlines
                lines.append(self.TAB * (numofindents + 1) + "</whileStatement>")
                i = j
                tag, inp = self.trim_line(i)
                continue
            if inp == "if":
                lines.append(self.TAB * (numofindents + 1) + "<ifStatement>")
                newlines, j = self.compile_if_statement(i, numofindents + 1)
                lines = lines + newlines
                lines.append(self.TAB * (numofindents + 1) + "</ifStatement>")
                i = j
                tag, inp = self.trim_line(i)
                continue

        return lines, i

    def compile_let_statement(self, i: int, numofindents: int) -> Tuple[List[str], int]:
        lines: List[str] = list()
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1

        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])

        i = i + 1
        tag, inp = self.trim_line(i)

        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        if inp == "[":
            lines.append(self.TAB * (numofindents + 1) + "<expression>")
            newlines, j = self.compile_expression(i + 1, numofindents + 1)
            lines = lines + newlines
            i = j
            lines.append(self.TAB * (numofindents + 1) + "</expression>")
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        lines.append(self.TAB * (numofindents + 1) + "<expression>")
        newlines, j = self.compile_expression(i + 1, numofindents + 1)
        lines = lines + newlines
        i = j
        lines.append(self.TAB * (numofindents + 1) + "</expression>")
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        return lines, i

    def compile_do_statement(self, i: int, numofindents: int) -> Tuple[List[str], int]:
        lines: List[str] = list()
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        newlines, j = self.compile_subroutine_call(i, numofindents)
        i = j
        lines = lines + newlines
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        return lines, i

    def compile_return_statement(
        self, i: int, numofindents: int
    ) -> Tuple[List[str], int]:
        lines: List[str] = list()
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        tag, inp = self.trim_line(i)
        if inp != ";":
            lines.append(self.TAB * (numofindents + 1) + "<expression>")
            newlines, j = self.compile_expression(i, numofindents + 1)
            lines = lines + newlines
            i = j
            lines.append(self.TAB * (numofindents + 1) + "</expression>")
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        return lines, i

    def compile_while_statement(
        self, i: int, numofindents: int
    ) -> Tuple[List[str], int]:
        lines: List[str] = list()
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        lines.append(self.TAB * (numofindents + 1) + "<expression>")
        newlines, j = self.compile_expression(i, numofindents + 1)
        lines = lines + newlines
        i = j
        lines.append(self.TAB * (numofindents + 1) + "</expression>")
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        lines.append(self.TAB * (numofindents + 1) + "<statements>")
        newlines, j = self.compile_statements(i, numofindents + 1)
        lines = lines + newlines
        i = j
        lines.append(self.TAB * (numofindents + 1) + "</statements>")
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        return lines, i

    def compile_if_statement(self, i: int, numofindents: int) -> Tuple[List[str], int]:
        lines, j = self.compile_while_statement(i, numofindents)
        i = j
        tag, inp = self.trim_line(i)
        if inp == "else":
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            lines.append(self.TAB * (numofindents + 1) + "<statements>")
            newlines, j = self.compile_statements(i, numofindents + 1)
            lines = lines + newlines
            i = j
            lines.append(self.TAB * (numofindents + 1) + "</statements>")
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
        return lines, i

    def compile_expression(self, i: int, numofindents: int) -> Tuple[List[str], int]:
        lines: List[str] = list()
        lines.append(self.TAB * (numofindents + 1) + "<term>")
        newlines, j = self.compile_term(i, numofindents + 1)
        lines = lines + newlines
        lines.append(self.TAB * (numofindents + 1) + "</term>")
        i = j
        tag, inp = self.trim_line(i)
        while inp in self.OP:
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            lines.append(self.TAB * (numofindents + 1) + "<term>")
            newlines, j = self.compile_term(i, numofindents + 1)
            lines = lines + newlines
            lines.append(self.TAB * (numofindents + 1) + "</term>")
            i = j
            tag, inp = self.trim_line(i)
        return lines, i

    def compile_term(self, i: int, numofindents: int) -> Tuple[List[str], int]:
        lines: List[str] = list()
        tag, inp = self.trim_line(i)
        if tag in ["integerConstant", "stringConstant"] or inp in [
            "true",
            "null",
            "false",
            "this",
        ]:
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            return lines, i + 1
        if inp == "-" or inp == "~":
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            lines.append(self.TAB * (numofindents + 1) + "<term>")
            newlines, j = self.compile_term(i, numofindents + 1)
            lines = lines + newlines
            lines.append(self.TAB * (numofindents + 1) + "</term>")
            i = j
            return lines, i
        if inp == "(":
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            lines.append(self.TAB * (numofindents + 1) + "<expression>")
            newlines, j = self.compile_expression(i, numofindents + 1)
            lines = lines + newlines
            i = j
            lines.append(self.TAB * (numofindents + 1) + "</expression>")
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            return lines, i
        tagn, inpn = self.trim_line(i + 1)
        if inpn == "(" or inpn == ".":
            newlines, j = self.compile_subroutine_call(i, numofindents)
            lines = lines + newlines
            i = j
            return lines, i
        elif inpn == "[":
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i + 1])
            i = i + 2
            lines.append(self.TAB * (numofindents + 1) + "<expression>")
            newlines, j = self.compile_expression(i, numofindents + 1)
            lines = lines + newlines
            i = j
            lines.append(self.TAB * (numofindents + 1) + "</expression>")
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            return lines, i
        else:
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            return lines, i

    def compile_subroutine_call(
        self, i: int, numofindents: int
    ) -> Tuple[List[str], int]:
        lines: List[str] = list()
        tag, inp = self.trim_line(i)
        tagn, inpn = self.trim_line(i + 1)
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        if inpn == ".":
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            i = i + 1
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        lines.append(self.TAB * (numofindents + 1) + "<expressionList>")
        tagtemp, inptemp = self.trim_line(i)
        if inptemp != ")":
            newlines, j = self.compile_expression_list(i, numofindents + 1)
            lines = lines + newlines
            i = j
        lines.append(self.TAB * (numofindents + 1) + "</expressionList>")
        lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
        i = i + 1
        return lines, i

    def compile_expression_list(
        self, i: int, numofindents: int
    ) -> Tuple[List[str], int]:
        lines = [self.TAB * (numofindents + 1) + "<expression>"]
        newlines, j = self.compile_expression(i, numofindents + 1)
        i = j
        lines = lines + newlines
        lines.append(self.TAB * (numofindents + 1) + "</expression>")
        tag, inp = self.trim_line(i)
        while inp == ",":
            lines.append(self.TAB * (numofindents + 1) + self.tokens[i])
            lines.append(self.TAB * (numofindents + 1) + "<expression>")
            newlines, j = self.compile_expression(i + 1, numofindents + 1)
            i = j
            lines = lines + newlines
            lines.append(self.TAB * (numofindents + 1) + "</expression>")
            tag, inp = self.trim_line(i)
        return lines, i
