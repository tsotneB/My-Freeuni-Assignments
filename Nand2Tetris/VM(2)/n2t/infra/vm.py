from __future__ import annotations

import os
from dataclasses import dataclass
from pathlib import Path
from typing import List

from n2t.core.vm_translator import VMTranslator
from n2t.infra.io import File


@dataclass
class VmProgram:  # TODO: your work for Projects 7 and 8 starts here
    filePath: List[Path]
    classNames: List[str]
    filePathStr: str

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> VmProgram:
        paths = []
        classNames = []
        for (root, dirs, files) in os.walk(file_or_directory_name):
            for file in files:
                filepath = os.path.join(root, file)
                if filepath.endswith("vm"):
                    paths.append(Path(filepath))
                    (className, _, _) = file.partition(".")
                    classNames.append(className)
        lastPart = file_or_directory_name.split("/")[-1]
        (className, _, _) = lastPart.partition(".")
        return cls(paths, classNames, file_or_directory_name)

    def translate(self) -> None:
        lines: List[str] = []
        num = len(self.filePath)
        if num > 1:
            lines = lines + ["@256", "D=A", "@SP", "M=D"]
        ind = 0
        for path in self.filePath:
            commands = File(path).load()
            translator = VMTranslator(self.classNames[ind])
            starterCommand = ""
            if num > 1:
                starterCommand = "call Sys.init 0"
            lines = lines + translator.translate(commands, starterCommand)
            ind = ind + 1
        outputFilePath = (
            self.filePathStr[:-1]
            + "\\"
            + self.filePathStr[:-1].split("\\")[-1]
            + ".asm"
        )
        File(Path(outputFilePath)).save(lines)
