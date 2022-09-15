from __future__ import annotations

from dataclasses import dataclass
from typing import Iterable, Tuple


@dataclass
class Parser:
    def __init__(self, commands: Iterable[str]):
        self.commands = commands
        self.iterator = iter(self.commands)
        self.__currentCommand = ""

    def hasMoreCommands(self) -> bool:
        self.__currentCommand = next(self.iterator, "NO COMMAND")
        return self.__currentCommand != "NO COMMAND"

    def advance(self) -> None:
        self.__currentCommand = self.__currentCommand.strip()

    def isComment(self) -> bool:
        return self.__currentCommand.startswith("//")

    def isEmpty(self) -> bool:
        return self.__currentCommand == ""

    def trimLine(self) -> Tuple[str, str, str]:
        (command, _, intermed) = self.__currentCommand.partition(" ")
        (comtype, _, val) = intermed.partition(" ")
        return command, comtype, val
