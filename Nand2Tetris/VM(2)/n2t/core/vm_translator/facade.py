from __future__ import annotations

import itertools
from dataclasses import dataclass
from typing import Iterable, List

from n2t.core.vm_translator.parser import Parser
from n2t.core.vm_translator.translator import SingleCommandTranslator


@dataclass
class VMTranslator:
    className: str

    @classmethod
    def create(cls, className: str) -> VMTranslator:
        return cls(className)

    def translate(self, commands: Iterable[str], starterCommand: str) -> List[str]:
        if starterCommand != "":
            commands = itertools.chain([starterCommand], iter(commands))
        parser = Parser(commands)
        answer: List[str] = list()
        i = 0
        while parser.hasMoreCommands():
            parser.advance()
            if parser.isComment() or parser.isEmpty():
                continue
            (command, comtype, val) = parser.trimLine()
            i = i + 1
            translator = SingleCommandTranslator(self.className)
            answer = answer + translator.translatecommand(command, comtype, val, i)
        return answer
