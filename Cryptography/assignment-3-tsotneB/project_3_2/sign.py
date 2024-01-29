import math

from oracle import *
from helper import *

def get_signature(m, n):
  m1 = 1
  # print(Sign(1))
  s1 = Sign(m1)
  # print(Verify(m1,s1))
  c = 2
  for i in range(2, math.isqrt(m)+1):
    if m % i == 0:
      c = i
      break
  m2 = (m//c) % n
  #print(m2)
  s2 = Sign(m2)
  s3 = Sign(c)
  s = (pow(s1,-1,n) * s2 * s3) % n
  #print(Verify(m,s))
  return s


def main():
  with open('project_3_2/input.txt', 'r') as f:
    n = int(f.readline().strip())
    msg = f.readline().strip()

  Oracle_Connect()    

  m = ascii_to_int(msg)
  sigma = get_signature(m, n) 

  print(sigma)

  Oracle_Disconnect()

if __name__ == '__main__':
  main()
