# Core Concepts of Kotlin Coroutines

This module introduces the fundamental concepts of Kotlin coroutines. You'll learn about coroutine builders, suspending functions, coroutine scope, context, and dispatchers.

## Topics Covered

- **Coroutine Basics:** what coroutines are and how they're structured.
- **Coroutine Builders:** the `launch` and `async` builders.
- **Suspending Functions:** how suspending functions allow for non-blocking code.
- **Coroutine Scope:** how scopes manage coroutine lifecycles.
- **Context and Dispatchers:** how to control coroutine execution with contexts and dispatchers.

## Running the Examples

Each Kotlin file in the `src/main/kotlin` directory demonstrates a specific concept with real-world use case. To run an example, navigate to the file compile and run the code.

## Introduction to Coroutines
Coroutines are a Kotlin feature that converts async callbacks for long-running tasks, such as database or network operations, into sequential code. 
Think of coroutines as light-weight threads that you can launch thousands of, without the overhead associated with traditional threads.

### Coroutine Builders
Coroutines are launched using builders. The two primary builders are:

1- launch: Launches a new coroutine without blocking the current thread and returns a reference to the coroutine as a Job. 
It is typically used for fire-and-forget operations.

```kotlin
GlobalScope.launch {
    delay(1000L) // Non-blocking delay
    println("World!")
}
```

2- async: Starts a new coroutine that can compute a result value. 
It returns a Deferred value that is a non-blocking future—you call `.await()` on it to get the result.

```kotlin
val deferred: Deferred<String> = GlobalScope.async {
    delay(1000L) // Non-blocking delay
    "Hello"
}
println(deferred.await())
```

### Suspending Functions
A suspending function is a function that can be paused and resumed at a later time. 
They can execute a long-running operation and wait for it to complete without blocking. 
Suspending functions can only be called from within a coroutine or another suspending function.

```kotlin
suspend fun performRequest(url: String): String {
    delay(1000) // Simulate long-running operation
    return "Response"
}

```

### Coroutine Scope
Coroutine scope defines the scope in which coroutines run. 
It is used to keep track of coroutines and cancel them if needed. 
Every coroutine builder is an extension on `CoroutineScope` and inherits its context.

- `runBlocking`: Creates a new coroutine that blocks the current thread until its completion. Primarily used for bridging between blocking and non-blocking worlds and for testing.

- `GlobalScope`: A global coroutine scope not tied to any job.

### Context and Dispatchers
Is a set of elements, with `CoroutineDispatcher` being one of them. 
Dispatchers determine what thread or threads the corresponding coroutine uses for its execution.

- `Dispatchers.Main`: Runs on the main thread. Primarily used for UI interactions in Android.
- `Dispatchers.IO`: Optimized for offloading blocking IO tasks to a shared pool of threads.
- `Dispatchers.Default`: Optimized for CPU-intensive tasks.

```kotlin
launch(Dispatchers.IO) {
    // IO-bound code
}
```

### Real World Use Case
Navigate to `MainApplication.kt` to see an example that simulates performing asynchronous operations like network calls by using coroutines while avoiding the callback hell associated with traditional asynchronous programming in Java.

## Additional Resources

For more detailed information on each topic, refer to the [official Kotlin coroutines documentation](https://kotlinlang.org/docs/reference/coroutines-overview.html).
