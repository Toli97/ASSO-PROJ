# Blackboard Implementation

Project to implement the algorithm with Blackboard

## Instructions

You need to have `sbt` installed to run these steps

#### Building 
    
    sbt compile
    
#### Test

    sbt test

### Run

    sbt run

#### Package (Fat JAR)

    sbt assembly
    
#### Running (Fat JAR)

    java -jar target/scala-*/Blackboard*.jar


## Benchmarks

### Hours Coding
About 20h

### Number of lines of code
About 500

### Speed Benchmarks
#### Scenario 1 (100 runs)
- Run Average: 4,30 ms
- Run Median: 2,03 ms
- First Run: 106 ms
- Second Run: 20 ms

#### Scenario 2 (20 runs, fibonacci numbers at 1000B/s and prime numbers at 2000B/s)
- Run Average - 1172 ms
- Run Median: 1176 ms
- First Run: 1240 ms
- Second Run: 1137 ms

#### Scenario 2 (20 runs, fibonacci numbers at 1000B/s and prime numbers at 1000B/s)
- Run Average - 10754 ms
- Run Median: 10802 ms
- First Run: 10877 ms
- Second Run: 10826 ms

Clearly there is a blocking problem caused by slow inputs.

### Code Profiling

#### Scenario 1
![Scenario 1 profiling](https://i.imgur.com/1RB7u5e.png)

In scenario 1 72.8% of the CPU time is used executing the Knowledge Sources.

The knowledge sources that take the most CPU time are the Long Producers, which take 33.9% of it.

### Scenario 2
![Scenario 2 profiling](https://i.imgur.com/9JQLmJW.png)

In scenario 2 we simulate slow inputs. We can see that less that 50% of the CPU time is actually used executing the Knowledge Sources.

In the image above we can see that Long Producers take the biggest amount of CPU time, totaling 43.4%. This is caused by using blocking IO functions and the program being single threaded. 
## Architecture Discussion

 
According to Wikipedia, the Blackboard model is a behavioral design pattern which defines three major components:
- Blackboard: a structured global memory containing objects from the solution space
- Knowledge Sources: specialized modules with their own representation
- Controller: selects, configures and executes modules

The previous definition is very vague and does not give many clues about implementation.

![Blackboard structure](https://upload.wikimedia.org/wikipedia/commons/1/18/Blackboad_pattern_system_structure.png)

Given the problem described [here](https://github.com/Toliveira97/ASSO-PROJ#problem-description), we first decided to use the Blackboard architecture as follows:
- Blackboard: Create one queue for each type of Message and create methods to add and get Messages.
- Controller: Component that has all the Knowledge Sources and calls execute on them in a loop.
- Knowledge Sources: Number processing components that add and fetch Messages from the Blackboard.

### Thinking process

### Step 1:
- How to structure the data inside the Blackboard? Should we use queues? How to identify each Message type? Should Messages of the same type be stored in the same Queue? How to define a Message type?
- How to create the Blackboard queues in run-time? This may be needed since the structure of the problem should not be restricted to scenarios 1 and 2.
- When does the Controller exit the loop (stop)?
- The Controller usually orders and executes Knowledge Sources by priority. How to create a priority when the scenarios may vary? Do we set the priority on the creation of a Knowledge Source? Is a priority needed at all? If a priority is not needed, is the Controller doing anything useful? How do we know that all Knowledge Sources finished processing?
- What if more than one Knowledge Source should process the same Message. When does a Message get removed from the Blackboard queue?
- If the Messages should go through a well-defined process, how do we say that they have been processed and they are ready to be processed by the next filter? Should the Messages "path" be defined on Message instantiation? Should each Knowledge Source know what is the "next" state of each Message it processes? Or should each Knowledge Source have a reference to the next Knowledge Sources?
- Each Knowledge Source receives on creation the "state" to which it should transform the messages it receives, as an Enumeration. Having these "states" as enumeration does not scale very well with new types of Knowledge Sources and does not provide an easy API for the user.

![Blackboard-step1](https://i.imgur.com/w0l6LI9.png)

### Step 2:
- The Blackboard seems very similar to a Broker, but instead of proxying the Messages it only saves them. What if we convert this Blackboard into a Broker? The problem of having multiple Knowledge Sources fetching the same Message could be solved by using the Observer Pattern (notify each Knowledge source that is subscribed to that Message).
- If our Blackboard is only proxying messages, does it need to have internal queues? Or can it simply forward each message it receives to the subscribers?
- If we see our Blackboard as a Broker, then we are implementing a Publisher-Subscriber pattern, where the publishers and subscribers are our Knowledge Sources. We should then add a topic to each Message (which can be just a number) so that subscribers can tell the Blackboard about what Messages they want to be notified.
- When a Subscriber receives a Message, should it be processed immediately? If so, what is the purpose of the Controller? The Controller should be responsible for executing the Knowledge Sources. So, each Knowledge Source will need to have an internal queue to store the Messages it receives. This should have a worse memory performance, since Messages may be replicated across multiple Knowledge Sources.

### Step 3:
- When a Knowledge Source executes and has no stored Messages, how should it behave? Block and wait for a Message to be received? If it blocks then Knowledge Sources have to execute in separate threads. How long should it wait for a Message to be received?
- What if when there are no stored Messages, the Knowledge Source returns from the execute method? Maybe there is no need for parallel execution of the Knowledge Sources.
- How does the Controller know that a Knowledge Source has finished execution? What if each Knowledge Source has a public method that the Controller can use to know if it has finished? A Knowledge Source could internally change its state to Finished when it receives an End-Of-File Message. Upon receiving an End-Of-File, each Knowledge Source should forward that Message so that the following Knowledge Sources know that no more Messages will be coming through that "channel".
- The Controller execute method can be reduced to a simple for loop which iterates through all Knowledge Sources and removes them from its list when they are finished while there are Knowledge Sources. We could also use an Observer Pattern here so that each Knowledge Source would notify the Controller upon finishing execution.
- In a scenario where there's a join of two branches, for example in an add operation, receiving an EOF from one of the branches should halt the execution on the other branch. 
If we want the controller to handle this, then it needs to be aware of what chains exist. Another approach would be for each Knowledge Source to subscribe on the next Knowledge Source end of execution.
### FINAL SOLUTION


![Blackboard Final Solution](https://i.imgur.com/GgFEz5u.png)

#### Controller
- Has a method addKnowledgeSources that sets the blackboard on all Knowledge Sources.
- The method execute has a for loop that removes Knowledge Sources that have finished already and iterates on each Knowledge Source, calls the execute and waits for the execute to finish before processing the next Knowledge Source.

#### Message
- Can either be instantiated as a Value or as an EOF. Each message has a Topic which can be updated in run-time by the Knowledge Sources.

#### Blackboard

- Implements the Subject component of the Observer Pattern. In other words, has methods to add and to notify Observers.

#### Knowledge Sources

There are multiple types:
1. ConditionFilter - Filters messages using a condition function which is given on instantiation;
2. JoinConditionFilter - Filters messages from 2 topics. Finishes only when two EOFs are received;
3. OperationFilter - Receives messages from two different topics and applies an operation that returns a new value;
4. Output - Produces output to a text file; Stops on the first EOF received.
5. LongProducer - Reads long numbers from text file, either as fast as possible or with a given rate (bytes/sec) to simulate low input rate;

- Each Knowledge Source, on instantiation, receives the topic of the messages that it produces.
- Knowledge Sources can be chained only after receiving the blackboard. 
The chain method uses the blackboard to subscribe the correct topic.
- Knowledge Sources are observers and subjects of themselves. Each Knowledge Source subscribes to be notified of the end of execution of the chained Knowledge Sources.
This subscription is done when the chain method is called.

### Possible future work

1. Make Knowledge Sources asynchronous, so that scenarios (like 2) where there are slow Knowledge Sources can run faster. 
Furthermore, making Knowledge Sources asynchronous would enable a migration to a distributed architecture.
2. Create a priority mechanism on Knowledge Sources so that services that the user estimates to take longer time to execute can run more frequently.