def fixed_xor(first: str, second: str) -> str:
    return '{:x}'.format(int(first, 16) ^ int(second, 16))

str_hex1 = input().strip()
str_hex2 = input().strip()
print(fixed_xor(str_hex1, str_hex2))
