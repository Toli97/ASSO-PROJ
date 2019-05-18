# ASSO Project

Final project for System Software Arquitecture

### Members
Class **4MIEIC02**

- Renato Campos up201504942
- Rostyslav Khoptiy up201506219
- Tomás Oliveira up201504746 


## Concept

Implementation and benchmarking of a problem in 3 software architectures: **Pipe and Filters (pull)**, **Pipes and Filters (push)**, **Blackboard**.


### Problem Description

We will use 4 inputs for numbers which will be sourced from a certain file. The first input is filtered to obtain only the prime numbers, meaning the output of this filter can have less numbers than the input. Two other inputs are filtered so that only the numbers of one filter that are the multiple of the numbers of the other input get throught. The output of these 2 filters is subtracted by the last filter. The final 2 inputs are then added together. The processing ends when one source of numbers ends (end of file).

Slow streams are also explored such as what would happen getting the numbers from a slow TCP stream, which we will simulate using a file and sleep.

Scenario 1 (unlimited file speed):

![Scenario 1](https://i.imgur.com/3IsUexE.png)

Scenario 2 (file reading speed limited):

![Scenario 2](https://i.imgur.com/BAmL4m8.png)

### Benchmarks Analysed

- Number of hours to implement;
- Number of lines of code;
- Speed benchmarks of each implementation;
- Memory used and peak memory used;
- Extensibility;
- Limitations.

## Implementation

For pipes and filters check out [here](pipes-and-filters/README.md)
