from dataclasses import dataclass
from typing import Iterable, List


@dataclass
class Tokenizer:
    KEYWORDS = [
        "class",
        "constructor",
        "function",
        "method",
        "field",
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
    SYMBOLS_MAP = {"<": "&lt;", ">": "&gt;", "&": "&amp;"}

    def tokenize_single_line(self, line: Iterable[str]) -> List[str]:
        singleline: List[str] = list()
        startedword = False
        currword = ""
        for word in line:
            category = "identifier"
            if word == " " or word == "":
                continue
            if word == '"':
                startedword = not startedword
                if not startedword:
                    category = "stringConstant"
                    singleline.append(
                        " ".join(self.translate_single_word(category, currword))
                    )
                    currword = ""
                continue
            if startedword:
                if currword == "":
                    currword = word
                elif word not in self.SYMBOLS:
                    currword = currword + f" {word}"
                else:
                    currword = currword + f"{word}"
                continue
            if word in self.KEYWORDS:
                category = "keyword"
            elif word in self.SYMBOLS:
                category = "symbol"
                if word in self.SYMBOLS_MAP.keys():
                    word = self.SYMBOLS_MAP[word]
            elif word.isnumeric():
                category = "integerConstant"
            singleline.append(" ".join(self.translate_single_word(category, word)))
        return singleline

    def translate_single_word(self, category: str, word: str) -> List[str]:
        ans: List[str] = [f"<{category}>", f"{word}", f"</{category}>"]
        return ans
