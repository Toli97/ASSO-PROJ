# Blackboard Implementation

Project to implement the algorithm with Blackboard

![Blackboard](https://i.imgur.com/7PTReP2.png)

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

### Hours to Implement - About 20h

### Number of lines of code - About 530

### Speed Benchmarks

### Code Profiling

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

### Step 2:
- The Blackboard seems very similar to a Broker, but instead of proxying the Messages it only saves them. What if we convert this Blackboard into a Broker? The problem of having multiple Knowledge Sources fetching the same Message could be solved by using the Observer Pattern (notify each Knowledge source that is subscribed to that Message).
- If our Blackboard is only proxying messages, does it need to have internal queues? Or can it simply forward each message it receives to the subscribers?
- If we see our Blackboard as a Broker, then we are implementing a Publisher-Subscriber pattern, where the publishers and subscribers are our Knowledge Sources. We should then add a topic to each Message (which can be just a number) so that subscribers can tell the Blackboard about what Messages they want to be notified.
- When a Subscriber receives a Message, should it be processed immediately? If so, what is the purpose of the Controller? The Controller should be responsible for executing the Knowledge Sources. So, each Knowledge Source will need to have an internal queue to store the Messages it receives. This should have a worse memory performance, since Messages may be replicated across multiple Knowledge Sources.

### Step 3:
- When a Knowledge Source executes and has no stored Messages, how should it behave? Block and wait for a Message to be received? If it blocks then Knowledge Sources have to execute in separate threads. How long should it wait for a Message to be received?
- What if when there are no stored Messages, the Knowledge Source returns from the execute method? Maybe there is no need for parallel execution of the Knowledge Sources.
- How does the Controller know that a Knowledge Source has finished execution? What if each Knowledge Source has a public method that the Controller can use to know if it has finished? A Knowledge Source could internally change its state to Finished when it receives an End-Of-File Message. Upon receiving an End-Of-File, each Knowledge Source should forward that Message so that the following Knowledge Sources know that no more Messages will be coming through that "channel".
- The Controller can be reduced to a simple for loop which iterates through all Knowledge Sources and removes them from its list when they are finished. We could also use an Observer Pattern here so that each Knowledge Source would notify the Controller upon finishing execution.

### FINAL SOLUTION

#### Controller
- Has a method addKnowledgeSources that sets the blackboard on all knowledge sources.
- The method execute has a for loop that removes knowledge sources that have finished already and iterates on each Knowledge Source, calls the execute and waits for the execute to finish before processing the next Knowledge Source.

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
- Knowledge Sources can be chained. The chain method subscribes the knowledge sources to the correct topic.