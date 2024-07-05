#!/usr/bin/env python
import sys
import string
import random
import numpy

#### INSTRUCTIONS FOR USE:
# call program as follows: ./gibbs.py <Motif Length> <Data File>
# make sure the gibbs.py is marked as executable: chmod +x gibbs.py

alphabet = ['A', 'G', 'C', 'T']

#### GibbsSampler:
#### 	INPUTS:	S - list of sequences
####		L - length of motif
####	OUTPUT:	PWM - 4xL list with frequencies of each base at each position
####                  Order of bases should be consistent with alphabet variable

THRESHOLD = 0.0001
CONSECUTIVE_CONVERGENCE = 5

# For convergence, I chose these parameters:
# I allow previous and current estimations of PWM to differ by less than 0.0001, and
# to infer that we have converged, I decided that this requirement should be satisfied
# 5 times consecutively
# Because of that, it might need a little while to run on some tests


def GibbsSampler(S,L):
    PWM = []
    prev = []
    num_iters = 0
    for i in range(len(alphabet)):
        PWM.append([0.0]*L)
        prev.append([0.0] * L)  # to compare for convergence

    a = []
    for i in range(len(S)):
        a.append(random.randint(0, len(S[i]) - L))   # random initialization of starting points

    current = 0
    while True:
        num_iters += 1
        excl_seq_index = random.randint(0, len(S)-1)  # choose excluded sequence randomly
        freq = []
        for _ in range(4):
            freq.append([1] * L)    # 1 because of pseudocounts
        indices = {'A': 0, 'G': 1, 'C': 2, 'T': 3}
        for i in range(len(S)):
            if i == excl_seq_index:
                continue
            start = a[i]
            for pos in range(L):
                curr_pos = start + pos
                freq[indices[S[i][curr_pos]]][pos] += 1

        for i in range(4):
            for j in range(L):
                prev[i][j] = PWM[i][j]

        for k in range(L):
            sm = 0
            for j in range(4):
                sm += freq[j][k]
            for j in range(4):
                PWM[j][k] = float(freq[j][k]) / sm     # update PWM
        possible_start = len(S[excl_seq_index]) - L
        z = [1.0] * possible_start
        for i in range(len(z)):
            for pos in range(L):
                curr_pos = i+pos
                z[i] *= PWM[indices[S[excl_seq_index][curr_pos]]][pos]
        norm = sum(z)
        for i in range(len(z)):
            z[i] /= norm
        a[excl_seq_index] = numpy.random.choice(possible_start, size=1, p=z)[0]

        converges = True
        for i in range(4):
            for j in range(L):
                if abs(PWM[i][j] - prev[i][j]) > THRESHOLD:
                    converges = False
                    break
        if converges:
            current += 1
        else:
            current = 0
        if current > CONSECUTIVE_CONVERGENCE:
            break

    print "number of iterations - %d" % num_iters
    return PWM


def main():
    L = int(sys.argv[1])
    datafile = sys.argv[2]
    S = readdata(datafile)
	
    P = GibbsSampler(S,L)
	
    print "    ", 
    for i in range(L):
        print "%-5d " % (i+1),
    print ""
	
    for j in range(len(alphabet)):
        print " %s " % alphabet[j], 
        for i in range(L):
            print " %5.3f" % P[j][i],
        print ""
	
def readdata(file):
    data = [];
    for line in open(file,'r'):
        data.append(line[0:-1])
    return data

main()

