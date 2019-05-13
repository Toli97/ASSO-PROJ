### Generate an integer sequence
### CLI: python merge.py <input_path_1> <input_path_2> <output_path>

import sys
from random import shuffle

file_1 = open(sys.argv[1], 'r')
file_2 = open(sys.argv[2], 'r')
file_out = open(sys.argv[3], 'w')

lines1 = file_1.readlines()
lines2 = file_2.readlines()

lines = lines1 + lines2
shuffle(lines)

for l in lines:
    file_out.write(l)
