import base64
from typing import List


def compute_hamming_distance(first: str, second: str) -> int:
    dis = 0
    for f,s in zip(first, second):
        m = ord(f) ^ ord(s)
        dis += bin(m).count("1")
    return dis

keysize = 2
ans = keysize
best = 1000000000
ciphertext = base64.b64decode(input().strip()).decode()
# print(ciphertext)
while keysize < 41:
    normalized_distance = 0
    st = 0
    for i in range(4):
        first_block = ciphertext[st : st + keysize]
        second_block = ciphertext[st + keysize:st + 2 * keysize]
        st += keysize
        normalized_distance += compute_hamming_distance(first_block,second_block) / 4
    normalized_distance /= keysize
    # print(f"{keysize} : {normalized_distance}",normalized_distance)
    if normalized_distance < best:
        ans = keysize
        best = normalized_distance
    keysize = keysize + 1

# print(ans)

blocks = []
i = 0
while i < len(ciphertext):
    blocks.append(ciphertext[i : min(i+ans, len(ciphertext))])
    i += ans

# print(blocks)

transposed_blocks = []
for j in range(ans):
    symbols = []
    for block in blocks:
        if len(block) > j:
            symbols.append(block[j])
    transposed_blocks.append("".join(symbols))

# print(transposed_blocks)

frequencies = {'a': 0.0651738, 'b':0.0124248, 'c':0.0217339, 'd':0.0349835, 'e':0.1041442, 'f':0.0197881,
                'g':0.0158610, 'h':0.0492888, 'i':0.0558094, 'j':0.0009033, 'k':0.0050529, 'l':0.0331490,
                'm':0.0202124, 'n':0.0564513, 'o':0.0596302, 'p':0.0137645, 'q':0.0008606, 'r':0.0497563,
                's':0.0515760, 't':0.0729357, 'u':0.0225134, 'v':0.0082903, 'w':0.0171272, 'x':0.0013692,
                'y':0.0145984, 'z':0.0007836, ' ':0.1918182}

def decrypt(key: int, cipher: str) -> List[str]:
    dec = []
    for ch in cipher:
        dec.append(chr(ord(ch) ^ key))
    return dec

def single_character_xor(cipher: str) -> int:
    curr = 0
    key = 0
    for i in range(256):
        decrypted = decrypt(i, cipher)
        res = 0
        for symbol in decrypted:
            if symbol not in frequencies.keys():
                continue
            res += frequencies[symbol]
        if res > curr:
            curr = res
            key = i
    return key

single_character_keys = [chr(single_character_xor(tb)) for tb in transposed_blocks]

# for tb in transposed_blocks:
#     print(tb)

key = "".join(single_character_keys)

# print(key)

key_length = len(key)
ciphertext_length = len(ciphertext)

elongated_keys = [key] * int((ciphertext_length/key_length))
elongated_keys.append(key[:ciphertext_length % key_length])

# print("".join(elongated_keys))

dec = [chr(ord(cip) ^ ord(ke)) for cip, ke in zip(ciphertext, "".join(elongated_keys))]
# dec_to_hex = ["0" + hex(d)[2:] if len(hex(d)[2:]) < 2 else hex(d)[2:] for d in dec]

print("".join(dec))
