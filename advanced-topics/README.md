# Kotlin Coroutines Advanced Topics
This module explores advanced concepts of Kotlin Coroutines. Here, we cover topics such as cancellation and timeouts, exception handling, Flow, channels, and more, providing both theoretical insights and practical examples.

## Topics Covered

- Cancellation and Timeouts
- Exception Handling
- Flow and Reactive Streams
- Channels
- Shared Mutable State and Concurrency
- Select Expression
- Coroutines and Java Frameworks

## Running the Examples

Each Kotlin file in the `src/main/kotlin` directory demonstrates a specific concept with real-world use case. To run an example, navigate to the file compile and run the code.

### Cancellation and Timeouts
Understanding how to properly cancel a coroutine and manage timeouts is crucial for writing resilient asynchronous code. 
This section explores the mechanisms Kotlin Coroutines offer to handle cancellation and enforce operation timeouts.

Kotlin Coroutines provides a framework for cancellation and timeouts, to ensure resources are used efficiently and your application remains responsive, even in the face of operations that take longer than expected or need to be cancelled.

#### Cancellation is cooperative 

Meaning a coroutine must be checking for cancellation and must be designed to be cancellable. The primary way to make a coroutine cancellable is by periodically invoking suspending functions that check for cancellation, such as `delay()` or explicitly checking the cancellation status.
```kotlin
fun main() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("job: I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancel() // cancels the job
    job.join() // waits for job's completion
    println("main: Now I can quit.")
}
```

#### Handling cancellation
When a coroutine is cancelled, it throws a `CancellationException`, which can be handled if necessary. 
However, in most cases, you don't need to catch `CancellationException`, as it's considered a normal reason for coroutine completion.

#### Timeouts
Another aspect of managing the execution time of coroutines are timeouts.
The `withTimeout` or `withTimeoutOrNull` function can be used to limit the execution time of a coroutine block.
```kotlin
fun main() = runBlocking {
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
}
```

If the timeout is exceeded, `withTimeout` throws a `TimeoutCancellationException`. 
To handle timeouts more gracefully, you can use `withTimeoutOrNull`, which returns `null` instead of throwing an exception when the timeout is exceeded.

```kotlin
fun main() = runBlocking {
    val result = withTimeoutOrNull(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
        "Done" // will not be executed
    }
    println("Result is $result")
}

```

- Use cancel when you need to explicitly cancel a coroutine.
- Use withTimeout or withTimeoutOrNull to prevent coroutines from running longer than necessary.
- Make your coroutines cancellable by checking for cancellation status or using suspending functions that are cancellable.

-------------------------------------------------------------

### Exception Handling
How Kotlin Coroutines deal with exceptions, including try-catch blocks, exception propagation, and the use of `CoroutineExceptionHandler`.

#### Fundamentals
Exceptions in coroutines work similarly to regular Kotlin/Java code but have some nuances due to the nature of asynchronous execution.

1- Propagation: By default, exceptions thrown are propagated to the coroutine's parent, potentially leading to the cancellation of the parent coroutine if not handled.
2- Scope: The way exceptions are handled can differ based on the coroutine scope and the specific builder used (`launch` vs. `async`).

#### Strategies
The most straightforward way to handle exceptions is by wrapping the suspending calls within a try-catch block.

```kotlin
fun main() = runBlocking {
    val job = launch {
        try {
            // Simulate a suspending function that can throw an exception
            throw IllegalArgumentException("Example exception")
        } catch (e: Exception) {
            println("Caught $e")
        }
    }
    job.join()
}
```

For uncaught exceptions, Kotlin provides `CoroutineExceptionHandler`—a coroutine context element for global exception handling. 
It's especially useful when you want to handle exceptions in a generic way for a particular coroutine scope.
```kotlin
fun main() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception with CoroutineExceptionHandler")
    }
    val job = launch(handler) {
        throw AssertionError("Example assertion error")
    }
    job.join()
}
```

#### Async and Wait
Exceptions in coroutines started with `async` are deferred until the resulting `Deferred` value is awaited. 
The `await` call will throw the exception, which can then be caught with a try-catch block.

```kotlin
fun main() = runBlocking {
    val deferred = async {
        // Simulate an error condition in an async block
        throw ArithmeticException("Example arithmetic exception")
    }
    try {
        deferred.await()
    } catch (e: ArithmeticException) {
        println("Caught $e")
    }
}
```

- Use specific exception handlers where necessary to handle exceptions at a finer granularity.
- Be cautious with global exception handlers, as they might swallow exceptions you'd want to handle explicitly.

--------------------------------------------------------------

### Flow and Reactive Streams
How Flow complements coroutines by providing a robust stream processing API, and we will see how it can interoperate with reactive streams libraries like RxJava.

#### Fundamentals
Flow offers cold streams, meaning the code inside a flow builder does not run until the flow is collected. 
This is particularly useful for representing an asynchronous stream of data that can be computed on demand. 
Reactive Streams, on the other hand, is a standard for asynchronous stream processing with non-blocking back pressure. 
Kotlin Flow provides interoperability with Reactive Streams, allowing seamless integration with other reactive libraries like RxJava.

#### Flow as a concept
Flow is a type in Kotlin that represents a cold asynchronous stream of data. It is conceptually similar to sequences but for asynchronous operations.

```kotlin
fun simpleFlow(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100) // Pretend we are doing something asynchronous here
        emit(i) // Send a value downstream
    }
}

fun main() = runBlocking<Unit> {
    launch {
        for (k in 1..3) {
            println("I'm not blocked $k")
            delay(100)
        }
    }
    simpleFlow().collect { value ->
        println(value)
    }
}
```

#### Backpressure and Flows
Backpressure is a situation where a data source produces data faster than it can be consumed. 
Flow handles such situations implicitly by suspending the emission of values until the consumer is ready to process them.

#### Stream Functions
A flow supports a set of various functions that can be used to transform, combine, and consume the data streams.

```kotlin
fun main() = runBlocking<Unit> {
    (1..5).asFlow()
        .filter { it % 2 == 0 }
        .map { it * it }
        .collect { println(it) }
}

```

Kotlin supports interoperability and conversion between flows and reactive streams:

```kotlin

fun main() = runBlocking<Unit> {
    val flow = (1..5).asFlow()
    val publisher: Publisher<Int> = flow.asPublisher(coroutineContext)
    publisher.subscribe("sub")
}

```

Usage:
- Used for cold streams that are computed on demand.
- Used to perform transformations and combinations of data streams.

---------------------------------------------------------------

### Channels
Channels allow for communication between coroutines in a way that's safe and concurrent. 
This section covers the types of channels available, their use cases, and how to implement producer-consumer patterns.


#### Operations
- Created using the `Channel()` factory function. You can specify the capacity of the channel (buffer size). If not specified, the channel will be created with a default capacity.
- We will use `send(value)` to send data to a channel and `receive()` to receive data from it. Both of these operations are suspending functions and can be called from coroutine contexts.
- Senders can close a channel to indicate that no more elements are going to be sent. Receivers can check for channel closure using `isClosedForReceive`.

#### Types
- Rendezvous Channel: default without a buffer. The sender suspends until the receiver is ready to receive the element.
- Buffered Channel: has a buffer of a fixed size. The sender is suspended only when the buffer is full.
- Unlimited Channel: backed by a linked list of elements, allowing practically unlimited sends.
- Conflated Channel: Only keeps the latest element sent. If a sender sends an element before the previous element has been received, the previous element will be overwritten.

```kotlin
fun main() = runBlocking {
    val channel = Channel<Int>()
    
    launch {
        for (x in 1..5) channel.send(x)
        channel.close()
    }
    
    // Consumes the channel until it's closed
    for (y in channel) println(y)
}
```

#### Advanced Operations
Producer consumer example
```kotlin
fun CoroutineScope.produceNumbers() = produce<Int> {
    var x = 1 // start from 1
    while (true) {
        send(x++) // produce next number
        delay(100) // wait 0.1s
    }
}

fun main() = runBlocking {
    val numbers = produceNumbers() // starts the producer coroutine
    repeat(5) { // take first five numbers
        println(numbers.receive())
    }
    println("Done!")
    coroutineContext.cancelChildren() // cancel producer coroutine
}
```

Keep in mind that `flow` can be more efficient for these use cases due to its support for functional transformations and its integration with the rest of the Kotlin coroutines ecosystem.

- Use channels for hot streams where the data is produced independently of the consumption, and you need to handle backpressure or when you're implementing producer-consumer patterns.
- Consider using Flow for cold streams where the data is produced only in response to a consumer request, especially when dealing with a fixed set of data or when applying transformations.
- Properly manage channel lifecycle, especially closing it when no longer needed to prevent memory leaks and ensure coroutine cancellation is handled correctly.


---------------------------------------------------------------

### Shared Mutable State and Concurrency
Managing shared mutable state in a concurrent environment is challenging. 
Here we will explore the strategies and best practices for using shared mutable state safely within coroutines.

#### Challenges with Shared Mutable State
- Race Conditions: Occur when two or more threads access shared state concurrently and at least one of them modifies the state.
- Visibility Issues: Changes made by one thread might not be immediately visible to other due to caching and optimization done by modern CPUs.

#### Strategies for Safe Concurrent Modifications
- Thread-Confined State: Limiting state modification to a single thread to ensure consistency.
- Immutable Shared State: Using immutable data structures that can safely be shared without the need for synchronization.
- Thread-Safe Data Structures: Utilizing data structures that guarantee safe concurrent access and modifications, such as `kotlinx.coroutines.Channel`.
- Actors: An actor is an encapsulation of state and behavior that interacts with other actors through message passing, ensuring that state mutations are confined to a specific coroutine context.

#### Using Actors
Actor pattern prevents concurrent access to the state and makes it easier to reason about changes to the state

```kotlin
sealed class CounterMsg
object IncCounter : CounterMsg() // Increment message
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // Request current count

//create a counter actor
fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter = 0 // Actor state
    for (msg in channel) { // Iterate over incoming messages
        when (msg) {
            is IncCounter -> counter++ 
            is GetCounter -> msg.response.complete(counter)
        }
    }
}

fun main() = runBlocking {
    val counter = counterActor() // create the actor

    // Increment the counter 100 times in parallel
    val jobs = List(100) {
        launch {
            counter.send(IncCounter)
        }
    }
    jobs.forEach { it.join() } // Wait for all increments to complete

    // retrieve value
    val response = CompletableDeferred<Int>()
    counter.send(GetCounter(response))
    println("Counter = ${response.await()}")

    counter.close() // close the actor
}
```

- try to reduce the amount of shared mutable state to simplify concurrency management.
- when shared mutable state is necessary, prefer using data structures designed for concurrent access.
- utilize structures like actors or Mutex for critical sections to manage state changes.
- encapsulate operations on shared state within specific scopes to ensure lifecycle and cancellation are handled properly.


---------------------------------------------------------------

### Select Expression
The select expression allows coroutines to await multiple suspending functions and select the first one that becomes available. 
Here we'll discuss how to use select for complex coordination tasks.

- Non-Blocking: `select` is non-blocking, it suspends the coroutine until one of its branches is ready to execute.
- Flexible: `select` can be used with channels, deferred values, and other suspending functions.

In the following example we will see how select expression is used with channels to receive from multiple channels and process the first received element

```kotlin
suspend fun selectExample(channelA: ReceiveChannel<Int>, channelB: ReceiveChannel<Int>) {
    select<Unit> { 
        channelA.onReceive { value ->
            println("received $value from channelA")
        }
        channelB.onReceive { value ->
            println("received $value from channelB")
        }
    }
}
```

In the next example we will be using select with deferred values to await the first completed operation then terminate early.

```kotlin
suspend fun asyncSelectExample(deferredA: Deferred<Int>, deferredB: Deferred<Int>) {
    select<Unit> {
        deferredA.onAwait { value ->
            println("deferredA completed with value $value")
        }
        deferredB.onAwait { value ->
            println("deferredB completed with value $value")
        }
    }
}
```


---------------------------------------------------------------
## Additional Resources
[Official Kotlin Docs](https://kotlinlang.org/docs/coroutines-overview.html)
[Kotlin Flows](https://kotlinlang.org/docs/flow.html)
[Channels](https://kotlinlang.org/docs/channels.html)