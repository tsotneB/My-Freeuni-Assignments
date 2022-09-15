from __future__ import annotations

from dataclasses import dataclass
from pathlib import Path

from n2t.core.compiler.compiler import CompilationEngine
from n2t.core.compiler.facade import JackCompiler
from n2t.infra.io import File


@dataclass
class JackProgram:
    filePath: str

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> JackProgram:
        return cls(file_or_directory_name)

    def compile(self) -> None:
        p = Path(self.filePath)
        if p.is_dir():
            for x in list(p.glob("**/*.jack")):
                self.compile_single_file(x.as_posix())
        else:
            self.compile_single_file(self.filePath)

    def compile_single_file(self, path: str) -> None:
        compiler = JackCompiler(path)
        lines = ["<tokens>"]
        commands = File(Path(path)).load()
        tokens = compiler.compile(commands)
        lines = lines + tokens
        lines.append("<tokens>")
        outputFilePath = path[:-5] + "T.xml"
        File(Path(outputFilePath)).save(lines)
        engine = CompilationEngine(tokens)
        linesfinal = engine.compile()
        outputFilePath = path[:-5] + ".xml"
        File(Path(outputFilePath)).save(linesfinal)
