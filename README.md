# ASSO Project

Final project for System Software Architecture

## Concept

Implementation and benchmarking of a problem in 2 software architectures: **Pipe and Filters (pull)** and **Blackboard**.


### Problem Description

We will use 4 inputs for numbers which will be sourced from files. The first input is filtered to obtain only the prime numbers, meaning the output of this filter can have less numbers than the input. Two other inputs are filtered so that only the numbers of one filter that are the multiple of the numbers of the other input get throught. The output of these 2 filters is subtracted by the last filter. The final 2 inputs are then added together. The processing ends when one source of numbers ends (end of file).

Slow streams are also explored such as what would happen getting the numbers from a slow TCP stream, which we simulate using a file and sleep.

![Description](https://i.imgur.com/frKF4XJ.png)

### Files used
- Numbers a: the first 93 numbers of the Fibonacci sequence;
- Numbers b: 361 numbers of the Collatz sequence of 999999999;
- Numbers c: the first 25997 Prime numbers
- Numbers d: 10⁵ random numbers

### Benchmarks Analysed

- Number of hours to implement;
- Number of lines of code;
- Speed benchmarks of each implementation;
- Memory used and peak memory used;
- Extensibility;
- Limitations.

### Built with

- [Scala](https://www.scala-lang.org/) - Scala combines object-oriented and functional programming in one concise, high-level language. Scala's static types help avoid bugs in complex applications, and its JVM and JavaScript runtimes let you build high-performance systems with easy access to huge ecosystems of libraries.
- [Sbt](https://www.scala-sbt.org/) - Scala's interactive build tool, to run Scala tasks.

## Benchmarks

### Speed Benchmarks

These benchmarks are based on 4 input files (which you can find in the Releases in github).

All the implementations assume the input files contain integer numbers with a maximum of 8-byte integers.
The total number of runs are chosen in the input.

There are 3 scenarios defined:

- Scenario 1: Input files with Fibonacci numbers as 1st input/numbers a, Collatz sequence as 2nd//numbers b, primes as 3rd/numbers c, and random numbers as 4th/numbers d.
- Scenario 2: Same as scenario 1 but with a slow file for the 1st input (1000 Bytes/sec) and 3rd input (2000 Bytes/sec)
- Scenario 3: Same as scenario 1 but with a slow file for the 1st input (1000 Bytes/sec) and 3rd input (1000 Bytes/sec)

### Diagrams

Blackboard - Scenario 1
![Scenario1-BB](https://i.imgur.com/T1yIqFa.gif)

Pipes and filters - Scenario 1
![Scenario1-PP](https://i.imgur.com/IflxqhN.gif)

Blackboard - Scenario 2
![Scenario2-BB](https://i.imgur.com/76lhgIK.gif)

Pipes and filters - Scenario 2
![Scenario2-PP](https://i.imgur.com/DFC7K74.gif)

Blackboard - Scenario 3
![Scenario3-BB](https://i.imgur.com/yLwTqcN.gif)

The first few executions are affected by the JVM JIT compiler and are therefore a lot slower than subsequent runs.

**Averages:**

|Implementation | Scenario 1 | Scenario 2 | Scenario 3 |
| ------------------------ |:--------------------:|:----------------------:|:----------------:|
| Blackboard               | 4,30 ms     | 1172 ms       | 10754 ms |
| Pipes-and-filters  | 942,87 ms | 2407,89 ms | Too long |
| Total number of runs  | 100 | 20 | 20 |

**Medians:**

|Implementation | Scenario 1 | Scenario 2 | Scenario 3 |
| ------------------------ |:--------------------:|:----------------------:|:----------------:|
| Blackboard               | 2,03 ms     | 1176 ms       | 10802 ms |
| Pipes-and-filters | 778,81 ms | 2252,51 ms | Too long |
| Total number of runs | 100 | 20 | 20 |

**Highs:**

|Implementation | Scenario 1 | Scenario 2 | Scenario 3 |
| ------------------------ |:--------------------:|:----------------------:|:----------------:|
| Blackboard               | 101,59 ms     | 1296,19 ms       | 10780,36 ms |
| Pipes-and-filters | 1583,46 ms | 2844,46 ms | Too long |
| Total number of runs | 100 | 20 | 20 |

**Lows:**

|Implementation | Scenario 1 | Scenario 2 | Scenario 3 |
| ------------------------ |:--------------------:|:----------------------:|:----------------:|
| Blackboard               | 1,61 ms     | 1118,45 ms       | 10238,03 ms |
| Pipes-and-filters | 766,28 ms | 2176,99 ms | Too long |
| Total number of runs | 100 | 20 | 20 |

From the data we can see the slow inputs cause a blocking problem.

### Developer benchmarks

These numbers are estimates, which mean to measure the developer cost of implementing each architecture.

|Implementation| Lines of code | Hours coding |
| ------------ |:-------------:|:------------:|
| Blackboard               | 600 | 25 |
| Pipes-and-filters | 400 | 25 | 

### Architecture Comparison

Both architectures seem to take about the same amount of time to implement but 
based on the speed benchmarks the pipes and filters implementation seems a lot slower, which might be explained by the message size used (8-byte integers) which favours blackboard because of the overhead of manipulating the integers in the pipes, while the blackboard can do multiple operations without much overhead. If message sizes were bigger the blackboard lead should decrease.

## Implementation
For more details about each implementation (including profiling) check out:

- Pipes and Filters : [here](pipes-and-filters/README.md)
- Blackboard: [here](blackboard/README.md)

### Members
Class **4MIEIC02**

- Renato Campos up201504942 - 40%
- Rostyslav Khoptiy up201506219 - 40%
- Tomás Oliveira up201504746 - 20%
