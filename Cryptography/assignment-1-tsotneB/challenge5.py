key = input().strip()
ciphertext = input().strip()

key_length = len(key)
ciphertext_length = len(ciphertext)

elongated_keys = [key] * int((ciphertext_length/key_length))
elongated_keys.append(key[:ciphertext_length % key_length])

dec = [ord(cip) ^ ord(ke) for cip, ke in zip(ciphertext, "".join(elongated_keys))]
dec_to_hex = ["0" + hex(d)[2:] if len(hex(d)[2:]) < 2 else hex(d)[2:] for d in dec]

print("".join(dec_to_hex))
