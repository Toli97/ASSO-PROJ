# Pipes and Filters Implementation

Project to implement the alogirthm with pipes and filters

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
    pull files/out.txt files/primes.txt files/sub.txt files/filtered.txt files/filter.txt
    
### Benchmarking

    bench <num runs> <command>
    
Example: 

    java -jar target/scala-*/pipes-and-filters-assembly*.jar \
    bench 10 pull files/out.txt files/primes.txt files/sub.txt files/filtered.txt files/filter.txt
    
### Hours Coding

### Number of lines of code

### Speed Benchmarks
#### Scenario 1 (100 runs)
- Run Average: 116 ms
- Run Median: 112 ms
- First Run: 161 ms
- Second Run: 114 ms 

### Code Profiling

## Architecture Discussion

A very simple, yet powerful architecture, that is also very robust. It consists of any number of components (filters) that transform or filter data, before passing it on via connectors (pipes) to other components. The filters are all working at the same time. The architecture is often used as a simple sequence, but it may also be used for very complex structures.

The filter transforms or filters the data it receives via the pipes with which it is connected. A filter can have any number of input pipes and any number of output pipes.

The pipe is the connector that passes data from one filter to the next. It is a directional stream of data, that is usually implemented by a data buffer to store all data, until the next filter has time to process it.

![Pipes and Filters](https://www.oreilly.com/library/view/software-architecture-with/9781786468529/graphics/B05759_08_14.jpg)

### Thinking process

    
