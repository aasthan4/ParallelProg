#!/usr/bin/env python
import sys
if __name__ == '__main__':
    line = sys.stdin.readline()
    while line != "":
        data=line.split()
        for i in range(1, len(data)):
	    for j in range(i+1, len(data)):
	        a=[int(data[0]), int(data[i]), int(data[j])]
	        a.sort()
	        print a[0],' ', a[1],' ', a[2]
        line = sys.stdin.readline()
