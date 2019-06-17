# Pipes and Filters Implementation

Project to implement the algorithm with pipes and filters using Scala.

## Instructions

You need to have `sbt` installed to run these steps

#### Building 
    
    sbt compile
    
#### Test

    sbt test

#### Package (Fat JAR)

    sbt assembly
    
#### Running (Fat JAR)

    java -jar target/scala-*/pipes-and-filters-assembly*.jar
    
## CLI 

### Pull

    pull <output file> <filtered by prime> <subtracted numbers> <numbers filtered by following multiples> <multiples>

Example: 

    java -jar target/scala-*/pipes-and-filters-assembly*.jar \
    pull files/out.txt files/collatz.in files/collatz.in files/collatz.in files/collatz.in
    
The same format is used for the `slowPull` and `slowPull2` commands.
    
If the `<multiples>` file is too large a stack overflow can happened (not a logic error) which can be fixed by increasing the stack size, for example:

    java -Xss515m -jar target/scala-*/pipes-and-filters-assembly*.jar bench 100 pull files/out.txt files/fib.in files/collatz.in files/primes.in files/rand.in
    
### Benchmarking

    bench <num runs> <command>
    
Example: 

    java -jar target/scala-*/pipes-and-filters-assembly*.jar \
    bench 10 pull files/out.txt files/collatz.in files/collatz.in files/collatz.in files/collatz.in


## Code Profiling

## Architecture Discussion

A very simple, yet powerful architecture, that is also very robust. It consists of any number of components (filters) that transform or filter data, before passing it on via connectors (pipes) to other components. The filters are all working at the same time. The architecture is often used as a simple sequence, but it may also be used for very complex structures.

The filter transforms or filters the data it receives via the pipes with which it is connected. A filter can have any number of input pipes and any number of output pipes.

The pipe is the connector that passes data from one filter to the next. It is a directional stream of data, that is usually implemented by a data buffer to store all data, until the next filter has time to process it.

![Pipes and Filters](https://www.oreilly.com/library/view/software-architecture-with/9781786468529/graphics/B05759_08_14.jpg)

### Thinking process

The idea is to have an output node which will pull in the messages from the input nodes. The messages can be one of 3 types:

- Value: Contains a number 
- NoValue: Represents no number (number filtered out in an operation), receiving this doesn't mean the input has no more numbers.
- Eof: End of file (represents no more numbers)

There are 3 types of nodes: 
- input nodes: which read the numbers from files and send them. The messages sent by this node can be either a Value or Eof
- output nodes, which reads the numbers from a node and writes them to a file.
- filter nodes: which can apply an operation to a node, can be a simple node or a fork node (joins 2 input streams), the messages produced can be any of the 3 types.

If any node receives an Eof message it will propagate it (if a join node receives it from any of the input nodes it will propagate it). 

Processing ends when the output node receives an Eof message.

Each node is connected by helper pipes which will filter the NoValue message out of the output (by pulling each time a NoValue is received).

The source nodes read the files and generate the numbers.

In this implementation pulling from a join node will cause it to create a new thread.

### Possible future work

- Use a different scala task scheduler to support single threaded asynchronous execution.
- Use bigger message sizes, maybe packing the numbers. 