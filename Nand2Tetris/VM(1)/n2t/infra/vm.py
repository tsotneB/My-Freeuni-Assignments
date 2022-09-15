from __future__ import annotations

from dataclasses import dataclass
from pathlib import Path

from n2t.core.vm_translator.facade import VMTranslator
from n2t.infra.io import File


@dataclass
class VmProgram:  # TODO: your work for Projects 7 and 8 starts here
    filePath: Path
    className: str
    filePathStr: str

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> VmProgram:
        lastPart = file_or_directory_name.split("/")[-1]
        (className, _, _) = lastPart.partition(".")
        return cls(Path(file_or_directory_name), className, file_or_directory_name)

    def translate(self) -> None:
        commands = File(self.filePath).load()
        translator = VMTranslator(self.className)
        lines = translator.translate(commands)
        outputFilePath = self.filePathStr[:-2] + "asm"
        File(Path(outputFilePath)).save(lines)
