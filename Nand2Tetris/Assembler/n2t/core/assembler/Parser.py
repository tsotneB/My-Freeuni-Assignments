from __future__ import annotations

from dataclasses import dataclass
from typing import Iterable


@dataclass
class Parser:

    __currentCommand: str
    ACommand = "A-instruction"
    CCommand = "C-Instruction"
    LCommand = "L-Instruction"
    Comment = "Comment"
    Empty = "Empty Line"

    def __init__(self, assembly: Iterable[str]):
        self.assembly = assembly
        self.iterator = iter(self.assembly)
        self.__currentCommand = ""

    def hasMoreCommands(self) -> bool:
        self.__currentCommand = next(self.iterator, "NO COMMAND")
        return self.__currentCommand != "NO COMMAND"

    def advance(self) -> None:
        ind = self.__currentCommand.find("//")
        if ind != -1:
            self.__currentCommand = self.__currentCommand[0:ind]
        self.__currentCommand = self.__currentCommand.strip()

    def commandType(self) -> str:
        if self.__currentCommand.startswith("//"):
            return self.Comment
        if self.__currentCommand.startswith("("):
            return self.LCommand
        if self.__currentCommand.startswith("@"):
            return self.ACommand
        if self.__currentCommand == "":
            return self.Empty
        return self.CCommand

    def __trimString(self) -> tuple[str, str, str]:
        (dest, eqsign, eq) = self.__currentCommand.partition("=")
        if eq == "":
            forcomp = dest
            dest = ""
        else:
            forcomp = eq
        (comp, comma, jmp) = forcomp.partition(";")
        return dest, comp, jmp

    def symbol(self) -> str:
        return self.__currentCommand[1:]

    def label(self) -> str:
        st = 1
        nd = self.__currentCommand.find(")")
        return self.__currentCommand[st:nd]

    def dest(self) -> str:
        (dest, comp, jmp) = self.__trimString()
        return dest

    def comp(self) -> str:
        (dest, comp, jmp) = self.__trimString()
        return comp

    def jump(self) -> str:
        (dest, comp, jmp) = self.__trimString()
        return jmp
