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
    