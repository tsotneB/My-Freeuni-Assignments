import sys

from oracle import Oracle_Connect, Oracle_Disconnect, Mac, Vrfy

def bytes_xor(prev_tag: bytearray, message_block: bytearray) -> bytearray:
    xored = (a ^ b for (a, b) in zip(prev_tag, message_block))
    ans = bytearray()
    ans.extend(xored)
    return ans

def bytes_to_str(block: bytearray) -> str:
    str_arr = [chr(c) for c in block]
    return "".join(str_arr)


def forge(message: str) -> bytearray:
    st = 32
    tag = Mac(message[:st],st)  # bytearray
    while st < len(message):
        b = bytearray()
        b.extend(map(ord, message[st: st+16]))
        new_block = bytes_xor(tag, b)
        new_block_str = bytes_to_str(new_block)
        new_message = new_block_str + message[st+16:st+32]
        tag = Mac(new_message, 32)
        st += 32
    return tag



f = open(sys.argv[1])
message = f.read()
Oracle_Connect()
tag = forge(message)
print(tag.hex())
print(Vrfy(message, len(message), tag))
Oracle_Disconnect()
