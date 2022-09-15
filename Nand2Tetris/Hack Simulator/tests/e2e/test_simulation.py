from pathlib import Path

import pytest

from n2t.infra.io import File
from n2t.runner.cli import run_simulator

_TEST_PROGRAMS = [
    "add",
    "FibonacciElement",
    "NestedCall",
    "pong",
    "StaticsTest",
    "Mytests",
]


def files_match(file1: str, file2: str) -> bool:
    first = list(File(Path(file1)).load())
    second = list(File(Path(file2)).load())
    if len(first) != len(second):
        return False
    for i in range(len(first)):
        if first[i] != second[i]:
            return False
    return True


@pytest.mark.parametrize("program", _TEST_PROGRAMS)
def test_should_disassemble(program: str, hack_directory: Path) -> None:
    hack_file = str(hack_directory.joinpath(f"{program}.hack"))

    run_simulator(hack_file, 1000000)

    assert files_match(
        str(hack_directory.joinpath(f"{program}.out")),
        str(hack_directory.joinpath(f"{program}.cmp")),
    )
