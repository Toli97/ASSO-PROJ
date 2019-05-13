import sys

def parseCli():
    filePath = sys.argv[1]
    file = open(filePath, "w")
    return file