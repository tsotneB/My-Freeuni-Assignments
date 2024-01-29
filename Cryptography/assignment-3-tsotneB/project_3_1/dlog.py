import math


# Find x such that g^x = h (mod p)
# 0 <= x <= max_x
def discrete_log(p, g, h, max_x):
	b = math.isqrt(max_x)
	hash_table = {}
	pwg = pow(g,-1,p)
	pwhg = h % p #(h % p) * pow(g,-1,p)
	for x1 in range(b):
		hash_table[pwhg] = x1 #+1
		if len(hash_table.keys()) == p:
			break
		pwhg *= pwg
		pwhg %= p
		# print(x1)
	pwb = 1 # pow(g,b) % p
	st = pow(g,b,p) #% p
	for x0 in range(b):
		#rem = pwb % p
		if pwb in hash_table:
			return x0 * b + hash_table[pwb]
		pwb *= st
		pwb %= p
		#print(x0)

def main():
	p = int(input().strip())
	g = int(input().strip())
	h = int(input().strip())
	max_x = 1 << 40 # 2^40
	dlog = discrete_log(p, g, h, max_x)
	print(dlog)

if __name__ == '__main__':
	main()
