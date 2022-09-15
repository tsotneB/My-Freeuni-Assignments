from __future__ import annotations

from ctypes import c_short
from dataclasses import dataclass
from typing import Any, Iterable, List


@dataclass
class HackSimulator:
    ram = [0] * 32768
    written = [False] * 32768
    address_register: int = 0
    data_register: int = 0

    @classmethod
    def create(cls) -> HackSimulator:
        return cls()

    def simulate(self, instructions: Iterable[str], cycles: int) -> List[str]:
        written = [False] * 32768
        instr = list(iter(instructions))
        ind = 0
        while cycles > 0:
            cycles = cycles - 1
            if ind >= len(instr):
                continue
            ind = self.simulate_single_line(instr[ind], ind)
        return self.describe_ram_state()

    def simulate_single_line(self, instruction: str, ind: int) -> Any:
        if instruction.startswith("0"):
            self.simulate_a_instruction(instruction[1:])
            return ind + 1
        return self.simulate_c_instruction(instruction[3:], ind)

    def simulate_a_instruction(self, value: str) -> None:
        self.address_register = int(value, 2)
        return

    def simulate_c_instruction(self, instruction: str, ind: int) -> Any:
        comp: int = c_short(self.get_comp(instruction[:7])).value
        dest = instruction[7:10]
        jump = instruction[10:]
        if dest != "000":
            self.save_value(dest, comp)
        if jump == "000":
            return ind + 1
        return self.simulate_jump_instruction(jump, comp, ind)

    def get_comp(self, computation: str) -> Any:
        a = computation[0]
        computation = computation[1:]
        if computation == "101010":
            return 0
        if computation == "111111":
            return 1
        if computation == "111010":
            return -1
        if computation == "001100":
            return self.data_register
        if computation == "110000":
            if a == "0":
                return self.address_register
            return self.ram[self.address_register]
        if computation == "001101":
            return ~self.data_register
        if computation == "110001":
            if a == "0":
                return ~self.address_register
            return ~(self.ram[self.address_register])
        if computation == "001111":
            return -self.data_register
        if computation == "110011":
            if a == "0":
                return -self.address_register
            return -self.ram[self.address_register]
        if computation == "011111":
            return self.data_register + 1
        if computation == "110111":
            if a == "0":
                return self.address_register + 1
            return self.ram[self.address_register] + 1
        if computation == "001110":
            return self.data_register - 1
        if computation == "110010":
            if a == "0":
                return self.address_register - 1
            return self.ram[self.address_register] - 1
        if computation == "000010":
            if a == "0":
                return self.data_register + self.address_register
            return self.data_register + self.ram[self.address_register]
        if computation == "010011":
            if a == "0":
                return self.data_register - self.address_register
            return self.data_register - self.ram[self.address_register]
        if computation == "000111":
            if a == "0":
                return self.address_register - self.data_register
            return self.ram[self.address_register] - self.data_register
        if computation == "000000":
            if a == "0":
                return self.address_register & self.data_register
            return self.ram[self.address_register] & self.data_register
        if a == "0":
            return self.address_register | self.data_register
        return self.ram[self.address_register] | self.data_register

    def save_value(self, destination: str, value: Any) -> None:
        if destination == "010":
            self.data_register = value
            return
        if destination == "100":
            self.address_register = value
            return
        if destination == "110":
            self.address_register = value
            self.data_register = value
            return
        if destination == "001":
            self.ram[self.address_register] = value
            self.written[self.address_register] = True
            return
        if destination == "011":
            self.ram[self.address_register] = value
            self.written[self.address_register] = True
            self.data_register = value
            return
        if destination == "101":
            self.ram[self.address_register] = value
            self.written[self.address_register] = True
            self.address_register = value
            return
        self.data_register = value
        self.ram[self.address_register] = value
        self.written[self.address_register] = True
        self.address_register = value
        return

    def simulate_jump_instruction(
        self, jump_type: str, comp: Any, current_index: int
    ) -> Any:
        ans: Any = self.address_register
        if jump_type == "001":
            if comp > 0:
                return self.address_register
            return current_index + 1

        if jump_type == "010":
            if comp == 0:
                return self.address_register
            return current_index + 1

        if jump_type == "011":
            if comp >= 0:
                return self.address_register
            return current_index + 1

        if jump_type == "100":
            if comp < 0:
                return self.address_register
            return current_index + 1
        if jump_type == "101":
            if comp != 0:
                return self.address_register
            return current_index + 1
        if jump_type == "110":
            if comp <= 0:
                return self.address_register
            return current_index + 1
        return ans

    def describe_ram_state(self) -> List[str]:
        output: List[str] = list()
        for i in range(32768):
            line: str
            if self.written[i]:
                line = f"{self.ram[i]}"
            else:
                line = ""
            output.append(line)
        return output
