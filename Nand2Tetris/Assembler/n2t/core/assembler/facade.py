from __future__ import annotations

from dataclasses import dataclass
from typing import Iterable

from n2t.core.assembler.Parser import Parser
from n2t.core.assembler.SymbolsHandling import SymbolTable
from n2t.core.assembler.Translator import Translator


@dataclass
class Assembler:
    @classmethod
    def create(cls) -> Assembler:
        return cls()

    def assemble(self, assembly: Iterable[str]) -> Iterable[str]:
        symbols = self.firstIteration(assembly)
        ans = self.secondIteration(assembly, symbols)
        return ans

    def isNumber(self, sym: str) -> bool:
        for char in sym:
            if "9" < char or char < "0":
                return False
        return True

    def firstIteration(self, assembly: Iterable[str]) -> SymbolTable:
        parser = Parser(assembly)
        symbols = SymbolTable()
        line = 0
        while parser.hasMoreCommands():
            parser.advance()
            cmdType = parser.commandType()
            if cmdType == parser.Comment:
                continue
            if cmdType == parser.Empty:
                continue
            if cmdType == parser.CCommand:
                line = line + 1
                continue
            if cmdType == parser.ACommand:
                line = line + 1
                continue

            symbols.addEntry(parser.label(), line)
        return symbols

    def secondIteration(
        self, assembly: Iterable[str], symbols: SymbolTable
    ) -> Iterable[str]:
        parser = Parser(assembly)
        translator = Translator()
        lst = list()
        variables = 15
        while parser.hasMoreCommands():
            parser.advance()
            (assembled, cnt) = self.assembleCommand(
                parser, translator, variables, symbols
            )
            variables = variables + cnt
            if assembled == "":
                continue
            lst.append(assembled)
        return lst

    def assembleCommand(
        self,
        parser: Parser,
        translator: Translator,
        variables: int,
        symbols: SymbolTable,
    ) -> tuple[str, int]:
        cmdType = parser.commandType()
        hackCommand = ""
        cnt = 0
        if (
            cmdType == parser.Empty
            or cmdType == parser.Comment
            or cmdType == parser.LCommand
        ):
            return hackCommand, cnt
        if cmdType == parser.ACommand:
            symnum = parser.symbol()
            if self.isNumber(symnum):
                num = int(symnum, base=10)
            else:
                if not symbols.contains(symnum):
                    cnt = cnt + 1
                    symbols.addEntry(symnum, variables + cnt)
                num = symbols.getAddress(symnum)
            hackCommand = format(num, "016b")
        else:
            destination = parser.dest()
            computation = parser.comp()
            jump = parser.jump()
            hackCommand = (
                "111"
                + translator.comp(computation)
                + translator.dest(destination)
                + translator.jump(jump)
            )
        return hackCommand, cnt
