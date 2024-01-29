import sys
from oracle import *


def decrypt(ciphertext: str) -> str:
    Oracle_Connect()
    ctext = [(int(ciphertext[i:i + 2], 16)) for i in range(0, len(ciphertext), 2)]
    block = len(ctext) // 16 - 1
    answer = [0] * (len(ctext) - 16)
    # print(len(answer))
    # print(len(ctext))
    while block >= 1:
        cipher_block = ctext[block * 16: (block + 1) * 16]
        iv_block = ctext[(block-1) * 16 : block * 16]
        # print(iv_block)
        init_iv = iv_block.copy()
        i = 0
        while i < 16:
            iv_block[i] = 0
            new_ctxt = iv_block.copy()
            new_ctxt.extend(cipher_block)
            rc = Oracle_Send(new_ctxt, 2)
            if rc == 0:
                break
            i += 1
        starting_index = i
        starting_pad_value = 16-i
        iv_block[i] = init_iv[i]
        # print(i)
        # print(len(iv_block))
        # print(iv_block)
        # print(starting_pad_value)
        if i == 0:
            starting_index = 16
            starting_pad_value = 0
        for j in range(starting_index, 16):
            pad = starting_pad_value + 1
            iv_block[j] ^= starting_pad_value ^ pad
        starting_pad_value += 1
        starting_index -= 1
        # print(iv_block)
        # print(init_iv)
        while starting_index >= 0:
            for c in range(256):
                iv_block[starting_index] = c
                new_ctxt = iv_block.copy()
                new_ctxt.extend(cipher_block)
                rc = Oracle_Send(new_ctxt, 2)
                if rc == 1:
                    break
            encoded_data = iv_block[starting_index] ^  init_iv[starting_index]
            encoded_data ^= starting_pad_value
            answer[(block-1) * 16 + starting_index] = encoded_data
            for j in range(starting_index, 16):
                pad = starting_pad_value + 1
                iv_block[j] ^= starting_pad_value ^ pad
            starting_pad_value += 1
            starting_index -= 1
            # print(encoded_data)
            # print("one byte found")
        block -= 1
    answer_to_ascii = [chr(c) for c in answer]
    Oracle_Disconnect()
    return "".join(answer_to_ascii)

f = open(sys.argv[1])
cipher = f.read()
print(decrypt(cipher))
