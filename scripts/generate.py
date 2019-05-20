### Generate an integer sequence
### CLI: python generate.py <sequence_name> <file_path> <num_numbers>

import sys
from math import sqrt
from random import randint 

INT_LIMIT = 9223372036854775805

# sequence_generator is a function that receives 
# the index in the sequence and returns the number
def run_sequence(sequence_generator):
    file_path = sys.argv[2]
    file = open(file_path, "w")
    num_numbers = int(sys.argv[3])

    for i in range(num_numbers):
        val = sequence_generator(i)
        if (val > INT_LIMIT):
            continue
        file.write("%d\n" % val)

    file.close()
    return


def fibbonaci(n):
    return ((1+sqrt(5))**n-(1-sqrt(5))**n)/(2**n*sqrt(5))

def random_seq(n):
    return randint(-INT_LIMIT, INT_LIMIT) ## max 8 byte int

sequence_name = sys.argv[1]
if sequence_name == "fib":
    run_sequence(fibbonaci)
elif sequence_name == "rand":
    run_sequence(random_seq)
else:
    raise "Invalid sequence name"

print("Done.")