#!/usr/bin/env python
import sys
if __name__=='__main__':
    reduce_count={}  
    line=sys.stdin.readline()
    while line!= "":
        if line in reduce_count:
            reduce_count[line]+=1
        else:
            reduce_count[line]=1
        line=sys.stdin.readline()
    for item in reduce_count:
        if reduce_count[item] > 2:
            orient=item.split()
	    print orient[0], orient[1], orient[2]
	    print orient[1], orient[0], orient[2]
	    print orient[2], orient[0], orient[1]
	
