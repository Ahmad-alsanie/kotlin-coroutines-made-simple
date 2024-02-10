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
Cold streams and Kotlin Flow. How Flow complements coroutines by providing a robust stream processing API, and we will see how it can interoperate with reactive streams libraries like RxJava.

---------------------------------------------------------------

### Channels
Channels allow for communication between coroutines in a way that's safe and concurrent. 
This section covers the types of channels available, their use cases, and how to implement producer-consumer patterns.

---------------------------------------------------------------

### Shared Mutable State and Concurrency
Managing shared mutable state in a concurrent environment is challenging. 
Here we will explore the strategies and best practices for using shared mutable state safely within coroutines.

---------------------------------------------------------------

### Select Expression
The select expression allows coroutines to await multiple suspending functions and select the first one that becomes available. 
Here we'll discuss to use select for complex coordination tasks.

---------------------------------------------------------------
## Additional Resources
[Official Kotlin Docs](https://kotlinlang.org/docs/coroutines-overview.html)
[Kotlin Flows](https://kotlinlang.org/docs/flow.html)
[Channels](https://kotlinlang.org/docs/channels.html)