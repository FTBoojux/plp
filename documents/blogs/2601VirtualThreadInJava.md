# Java Backend Advanced: Experimentally Verifying the Concurrency Performance of Virtual Threads

## 1. Background

### 1.1 Traditional Thread Model in Java

The traditional Java thread model, also known as platform threads, is implemented by directly mapping Java threads to operating system kernel threads.
This implementation leads to the following shortcoming for traditional Java thread model:

- 1.Resource overhead
  - Traditional thread model has high memory overhead, with each thread requiring 1~2MB of memory
  - Limited scalability
  - High context switching overhead. Traditional thread scheduling involves switching between user mode and kernel mode
- 2.Low IO processing efficiency
  - When a traditional thread encounters an IO blocking scenario, the corresponding system thread is also paused, leading to performance waste

Before the introduction of Java's virtual threads, the Java community also saw some alternative solutions, such as:
- WebFlux
- RxJava
- Netty
- etc.

These solutions address high concurrency issues in Java through `Reactive Programming`, which offers high performance but increases code complexity.

## 2 Virtual Threads

To address the shortcomings of the traditional Java thread model, Java introduced `virtual thread`. 
Virtual thread was introduced as a preview feature in Java 19 and officially released in Java 21.

### 2.1 Virtual Threads vs Traditional Threads

Traditional threads are like carriages on a truck, where one truck can only carry one carriage (one system thread corresponds to one Java thread),
while virtual threads are like cargo boxes inside the carriage, 
where one carriage can hold many boxes (multiple virtual threads are mapped to the system thread through carrier threads).

- 1.Resource overhead
  - Virtual threads have minimal memory overhead and can automatically scale at runtime as needed.
  - Can easily create millions of them.
  - Virtual thread scheduling is managed by the JVM, without involving user mode and kernel model switching.
- 2.Higher IO processing efficiency
  - When a virtual thread enters an IO blocking state, it can release the occupied system thread for use by other virtual threads.

### 2.2 Suitable Scenarios for Virtual Threads

From the characteristic of "releasing the occupied system thread when in IO blocking state",
it can be seen that virtual threads are suitable for IO-intensive tasks that often require waiting for IO.
In such scenarios, the introduction of virtual theads can improve CPU resource utilization and increase system throughput.

Conversely, CPU-intensive tasks are not suitable for virtual threads.
In CPU-intensive task scenarios, virtual threads not only fail to leverage the advantage of
"releasing the occupied system thread when in IO blocking state",
but also incur additional operational costs that may lead to slight performance degradation.

In an article from Oracle Help Center about virtual threads, 
it is also mentioned:

```text
Virtual threads are not faster threads; they do not run code any faster than platform threads. They exist to provide scale (higher throughput), not speed (lower latency).
```

## 3 Experimental Verification of Virtual Thread Characteristics

### 3.1 Purpose of the Experiment

This experiment mainly aims to verify two points:

- Virtual threads can significantly improve system throughput in IO-intensive task scenarios.
- Virtual threads do not improve performance in CPU-intensive task scenarios, and may even slightly degrade performance.

### 3.2 Experiment Setup

The environment for this experiment is as follows:
- Hardware: 
  - a laptop(CPU Intel(R) Core(TM) Ultra 7 155H   1.40 GHz, Memory 32.0 GB (31.6 GB usable))
- Software:
  - IDEA(Version: 2025.3.1.1)
  - JDK(OpenJDK-23)
  - OS: Windows 11

### 3.3 Experiment Design

To validate the points in `3.1 Purpose of the Experiment`, we designed the following experiments:

### 3.3.1 Core Code

First, the overall process for validating both points is the same:

- 1.Create a thread pool (traditional thread pool / virtual thread pool)
- 2.Select the type of task to execute (IO-intensive / CPU-intensive)
- 3.Submit a large number of tasks to the thread pool for execution
- 4.Measure the total execution time of the tasks

The core code for the experiment is as follows:
```java
public class CompareIOThreadPools {
    static int taskCount = 10_000;
    public static void verify(String type, ExecutorService threadPool, Runnable task) {
      
      long startTime = System.currentTimeMillis();
      // submit a large number of tasks
      for (int i = 0; i < taskCount; i++) {
        threadPool.submit(task);
      }
      // wait for all tasks to complete
      threadPool.shutdown();
      try {
        if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
          threadPool.shutdownNow();
        }
      } catch (InterruptedException e) {
        threadPool.shutdownNow();
      }
      
      long endTime = System.currentTimeMillis();
      // calculate total and average time
      long total = endTime - startTime;
      float avgTime = total / (float) taskCount;
      System.out.println(type + "total: " + total + " ms, average: " + avgTime + " ms");
    }
}
```

We can design two types of tasks:

Stimulate IO-intensive tasks by making the thread sleep for a short period:
```java
class IOIntensiveTask implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
    }
}
```

Stimulate CPU-intensive tasks by performing a large number of calculations:
```java 
class CpuIntensiveTask implements Runnable {
  @Override
  public void run() {
    double pi = 0;
    for (int i = 0; i < 20_000_000; i++) {
      if (i % 2 == 0) {
        pi += 1.0 / (2 * i + 1);
      } else {
        pi -= 1.0 / (2 * i + 1);
      }
    }
  }
}
```

### 3.3.2 Experiment Execution

We can execute the experiments for both IO-intensive and CPU-intensive tasks as follows:
```java
public class CompareIOThreadPools {
  static int taskCount = 10_000;
  public static void main(String[] args) {
    System.out.println("IO密集型任务测试:");
    // 虚拟线程池
    try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
      verify("虚拟", virtualExecutor, new IOIntensiveTask());
    }
    // 传统线程池
    try (ExecutorService traditionalExecutor = Executors.newFixedThreadPool(200)) {
      verify("传统", traditionalExecutor, new IOIntensiveTask());
    }
    System.out.println("CPU密集型任务测试:");
    // 虚拟线程池
    try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
      verify("虚拟", virtualExecutor, new CpuIntensiveTask());
    }
    // 传统线程池
    try (ExecutorService traditionalExecutor = Executors.newFixedThreadPool(200)) {
      verify("传统", traditionalExecutor, new CpuIntensiveTask());
    }
  }
}
```

### 3.4 Experiment Results

After running the above code, we obtained the following results:

```
IO密集型任务测试:
虚拟线程池总耗时: 257 ms, 平均每个任务耗时: 0.0257 ms
传统线程池总耗时: 10374 ms, 平均每个任务耗时: 1.0374 ms
CPU密集型任务测试:
虚拟线程池总耗时: 23316 ms, 平均每个任务耗时: 2.3316 ms
传统线程池总耗时: 23715 ms, 平均每个任务耗时: 2.3715 ms
```

### 3.5 Analysis of Results

From the results, we can see that:
- 1.In the IO-intensive task scenario, the virtual thread pool significantly outperformed the traditional thread pool, with a total execution time of only 257 ms compared to 10374 ms for the traditional thread pool.
- 2.In the CPU-intensive task scenario, the performance of the virtual thread pool and traditional thread pool was similar, with the virtual thread pool being slightly faster (23316 ms vs 23715 ms), but the difference was not significant.

### 3.6 Conclusion
From this experiment, we can conclude that:
- 1.Virtual threads can significantly improve system throughput in IO-intensive task scenarios
- 2.Virtual threads do not run code faster than traditional threads, in CPU-intensive task scenarios, their performance is similar to traditional threads