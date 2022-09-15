from __future__ import annotations

from dataclasses import dataclass
from typing import Iterable, List

from n2t.core.compiler.parser import Parser
from n2t.core.compiler.tokenizer import Tokenizer


@dataclass
class JackCompiler:
    className: str

    KEYWORDS = [
        "class",
        "constructor",
        "function",
        "method",
        "fileld",
        "static",
        "var",
        "int",
        "char",
        "boolean",
        "void",
        "true",
        "false",
        "null",
        "this",
        "let",
        "do",
        "if",
        "else",
        "while",
        "return",
    ]

    SYMBOLS = [
        "{",
        "}",
        "(",
        ")",
        "[",
        "]",
        ".",
        ",",
        ";",
        "+",
        "+",
        "-",
        "*",
        "/",
        "&",
        "|",
        "<",
        ">",
        "=",
        "~",
    ]

    @classmethod
    def create(cls, className: str) -> JackCompiler:
        return cls(className)

    def compile(self, commands: Iterable[str]) -> List[str]:
        parser = Parser(commands)
        answer: List[str] = list()

        while parser.has_more_commands():
            parser.advance()
            if parser.is_comment() or parser.is_empty():
                continue
            linecoms = parser.trim_line()
            tokenizer = Tokenizer()
            answer = answer + tokenizer.tokenize_single_line(linecoms)
        answer.append("</tokens>")
        return answer
