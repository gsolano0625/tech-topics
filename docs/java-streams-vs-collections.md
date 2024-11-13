## Java Streams API vs. Collections API: A Comprehensive Guide

In modern Java development, handling large datasets efficiently is crucial. Java introduced the **Streams API** in Java 8 as a way to process collections of data in a more declarative and functional style. Before Streams, the **Collections API** was the go-to approach for data processing, but it involves more manual work to iterate, filter, map, or reduce data. 

In this tutorial, we’ll explore both APIs with examples, focusing on how Streams help handle large datasets efficiently. We'll contrast this with the more traditional Collections-based approach.

#### Table of Contents
1. Introduction to Collections API
2. Introduction to Streams API
3. Key Differences Between Streams and Collections
4. Examples Using a Large Dataset
    - Example 1: Filtering data
    - Example 2: Sorting data
    - Example 3: Reducing data
    - Example 4: Parallel Processing with Streams
5. Performance Considerations
6. When to Use Streams vs. Collections

---

### 1. Introduction to Collections API
The Java **Collections API** (part of `java.util`) is a framework that provides an architecture to store and manipulate a group of objects. It includes data structures like `List`, `Set`, `Map`, and `Queue`.

With the Collections API, you typically iterate over data manually, using `for-loops` or `Iterator`.

Example: Filter a list of numbers that are even.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
List<Integer> evenNumbers = new ArrayList<>();

for (Integer num : numbers) {
    if (num % 2 == 0) {
        evenNumbers.add(num);
    }
}

System.out.println(evenNumbers); // Output: [2, 4, 6, 8, 10]
```

This approach is verbose, requiring manual iteration and state management.

---

### 2. Introduction to Streams API
The **Streams API** is part of `java.util.stream` and provides a functional approach to processing sequences of elements. It allows operations like filtering, mapping, and reducing data in a concise, declarative manner.

Example: Filter a list of numbers that are even using Streams.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

List<Integer> evenNumbers = numbers.stream()
                                   .filter(num -> num % 2 == 0)
                                   .collect(Collectors.toList());

System.out.println(evenNumbers); // Output: [2, 4, 6, 8, 10]
```

This approach is cleaner, focuses on *what* you want to do, and abstracts away the iteration logic.

---

### 3. Key Differences Between Streams and Collections

| **Aspect**            | **Collections API**                                        | **Streams API**                                        |
|-----------------------|------------------------------------------------------------|--------------------------------------------------------|
| **Iteration**         | External (manual `for-loop` or `Iterator`)                 | Internal (handled automatically by Streams)            |
| **Modifications**     | Supports adding/removing elements                          | Stream is immutable (cannot add/remove during stream)  |
| **Functional Style**  | Imperative programming                                      | Declarative, functional style (uses lambdas)           |
| **Evaluation**        | Eager (operations are performed as they appear)             | Lazy (intermediate operations are not executed until terminal operation) |
| **Parallelism**       | Requires explicit multi-threading                          | Easy parallelism with `parallelStream()`               |
| **Reusability**       | Collection can be iterated multiple times                  | Stream can only be used once                           |

The Collections API is more traditional and imperative, requiring manual iteration and state management.
Evaluation is eager, meaning operations are performed as they appear in the code.
Requires explicit multi-threading for parallel processing, and can be iterated multiple times.

The Streams API is designed to work with lambdas and functional programming concepts, 
making it more expressive and concise.
Evaluation is lazy, meaning intermediate operations are deferred until a terminal operation is called.
It also supports parallel processing out of the box, 
which can significantly improve performance for large datasets.
And streams are single-use, meaning they can't be reused once consumed.

---

### 4. Examples Using a Large Dataset

To illustrate the differences between Collections and Streams with large datasets, let’s assume we’re processing a list of 1 million integers, simulating data coming from a big data source.

```java
List<Integer> largeDataset = new Random().ints(1_000_000, 1, 100).boxed().collect(Collectors.toList());
```

#### Example 1: Filtering Data
##### Using Collections API

```java
List<Integer> filteredList = new ArrayList<>();
for (Integer num : largeDataset) {
    if (num > 50) {
        filteredList.add(num);
    }
}
```

##### Using Streams API

```java
List<Integer> filteredList = largeDataset.stream()
                                         .filter(num -> num > 50)
                                         .collect(Collectors.toList());
```

The Streams approach is more concise and doesn't require managing an external list.

#### Example 2: Sorting Data
##### Using Collections API

```java
List<Integer> sortedList = new ArrayList<>(largeDataset);
Collections.sort(sortedList);
```

##### Using Streams API

```java
List<Integer> sortedList = largeDataset.stream()
                                       .sorted()
                                       .collect(Collectors.toList());
```

#### Example 3: Reducing Data
##### Using Collections API

Summing up all values requires manual iteration.

```java
int sum = 0;
for (Integer num : largeDataset) {
    sum += num;
}
```

##### Using Streams API

Streams provide a cleaner way to handle reduction:

```java
int sum = largeDataset.stream().reduce(0, Integer::sum);
```

#### Example 4: Parallel Processing with Streams

Parallelism is a key advantage of the Streams API, especially when working with large datasets.

```java
List<Integer> parallelSortedList = largeDataset.parallelStream()
                                               .filter(num -> num > 50)
                                               .sorted()
                                               .collect(Collectors.toList());
```

Parallel streams divide the dataset into chunks and process them in parallel, significantly improving performance on multicore processors.

---

### 5. Performance Considerations

1. **Memory Consumption**: Streams don't store elements but operate on data "on-demand," so they are memory efficient. Collections hold all elements in memory.
2. **Lazy Evaluation**: Streams are lazily evaluated, meaning operations like `filter()` and `map()` are only executed when a terminal operation like `collect()` or `reduce()` is called.
3. **Parallelism**: The Collections API requires manual handling of multithreading and synchronization, whereas the Streams API simplifies parallel processing with `parallelStream()`.
4. **Reuse**: Collections can be reused multiple times, while streams are single-use and need to be re-created if further processing is required.

---

### 6. When to Use Streams vs. Collections

- **Streams API** is preferable when:
  - You're working with large datasets.
  - You want to take advantage of functional programming and a declarative style.
  - You need parallel processing for performance improvements.
  - You prefer lazy evaluation for performance efficiency.

- **Collections API** is preferable when:
  - You need to modify the underlying data structure.
  - You need to iterate over the dataset multiple times.
  - You want more control over the iteration process.

---

### Conclusion

The Java Streams API provides a powerful and efficient way to process large datasets, especially when combined with parallelism. While the Collections API is still useful for certain cases (such as modifying data or when iterative control is needed), Streams should be your go-to when working with functional-style operations and big data.

### Code
You can find the code examples used in this tutorial [here](../code/java/miscellaneous/src/DataProcessing.java).
