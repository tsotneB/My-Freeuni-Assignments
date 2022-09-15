import re
from dataclasses import dataclass
from typing import Iterable, List


@dataclass
class Parser:
    def __init__(self, commands: Iterable[str]):
        self.commands = commands
        self.iterator = iter(self.commands)
        self.__current_command = ""

    def has_more_commands(self) -> bool:
        self.__current_command = next(self.iterator, "NO COMMAND")
        return self.__current_command != "NO COMMAND"

    def advance(self) -> None:
        curr = self.__current_command.split("//", 1)[0]
        self.__current_command = curr.strip()

    def is_comment(self) -> bool:
        return (
            self.__current_command.startswith("//")
            or self.__current_command.startswith("*")
            or self.__current_command.startswith("/**")
            or self.__current_command.startswith("*/")
        )

    def is_empty(self) -> bool:
        return self.__current_command == ""

    def trim_line(self) -> List[str]:
        tempans = re.split(r"(\W)", self.__current_command)
        ans: List[str] = list()
        i = 0
        while i < len(tempans):
            if tempans[i] != '"':
                ans.append(tempans[i])
                i = i + 1
            else:
                ans.append(tempans[i])
                j = i + 1
                word = ""
                while tempans[j] != '"':
                    word = word + tempans[j]
                    j = j + 1
                ans.append(word)
                ans.append(tempans[j])
                i = j + 1
        return ans
