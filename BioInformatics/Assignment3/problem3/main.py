# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
import random

alphabet = ['A', 'G', 'C', 'U']

indices = {'A': 0, 'G': 1, 'C': 2, 'U': 3}

s = [
    [0,0,0,1],
    [0,0,1,1],
    [0,1,0,0],
    [1,1,0,0]
]


def nussinov(seq):
    dp = []
    for i in range(len(seq)):
        dp.append([0] * len(seq))

    for i in range(1, len(seq)):
        for j in range(len(seq)-i):
            k = i+j
            extend_both = dp[j+1][k-1] + s[indices[seq[j]]][indices[seq[k]]]
            max_inside = 0
            for l in range(j, k):
                max_inside = max(max_inside, dp[j][l] + dp[l+1][k])
            dp[j][k] = max(extend_both, max_inside)
    return dp[0][len(seq)-1]


def main():
    overall = 0
    for i in range(1000):
        seq = "".join(random.choice(alphabet) for _ in range(100))
        score = nussinov(seq)
        overall += score
    print(float(overall) / 1000)

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    main()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
