from __future__ import annotations

from dataclasses import dataclass
from pathlib import Path

from n2t.core.hack_simulator import HackSimulator
from n2t.infra.io import File


@dataclass
class HackProgram:
    filePath: str
    cycles: int

    @classmethod
    def load_from(cls, file_or_directory_name: str, num_cycles: int) -> HackProgram:
        return cls(file_or_directory_name, num_cycles)

    def simulate(self) -> None:
        instructions = File(Path(self.filePath)).load()
        simulator = HackSimulator()
        output = simulator.simulate(instructions, self.cycles)
        outputFilePath = self.filePath[:-4] + "out"
        File(Path(outputFilePath)).save(output)
