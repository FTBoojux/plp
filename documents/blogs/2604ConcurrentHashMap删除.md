# 测试并发 Bug 时踩到的“坑中坑”：如何保证测试代码本身没有并发问题

> **💡 写在前面**
> 
> 本文的源码与踩坑过程源自我正在手写的一个简易 Key-Value 存储系统（类似于 Java 版的简易 Redis）。如果你对这个造轮子项目感兴趣，欢迎来点个 Star 支持一下：
> **[👉 点击访问项目的 GitHub 仓库](https://github.com/FTBoojux/plp)** （也欢迎提 PR 共同建设哦！）
> 本文中的源代码，可以在 labs -> bedis 模块的 src/test/java/org/example/store/engine 路径下找到，你也可以拉取到本地直接运行，这样会更加直观

## 一、背景
我正在开发一个基于Java的简易key-value存储系统（类似于Redis），目前单机版底层使用ConcurrentHashMap来存储key-value键值对。

### 需求说明
为了支持给key设置过期时间，在起步阶段，我们先采用惰性删除机制：在进行 `get` 查询时，检查数据的过期时间。如果已经过期，就进行删除，并返回空。

## 二、案发现场
在前一节的需求下，我们很容易写出这样的方法：
```java
package org.example.store.engine;

public class UnsafeSimpleStoreEngine implements StoreEngine{
    private Clock clock = Clock.systemDefaultZone();
    ConcurrentHashMap<String, StorageEntry> concurrentHashMap = new ConcurrentHashMap<>();
    // 此处省略其他方法
    @Override
    public Optional<byte[]> get(String key) {
        StorageEntry valueEntry = concurrentHashMap.get(key);
        if (Objects.isNull(valueEntry)) {
            return Optional.empty();
        } else if (expired(valueEntry)) {
            concurrentHashMap.remove(key);
            return Optional.empty();
        } else {
            return Optional.of(valueEntry.value());
        }
    }

    private boolean expired(StorageEntry valueEntry) {
        if(valueEntry == null) {
            return true;
        }
        return valueEntry.hasExpire() && valueEntry.expireTime() < clock.millis();
    }

    @Override
    public boolean delete(String key) {
        return concurrentHashMap.remove(key) != null;
    }
}
```

这里的get方法逻辑也很简单，当查找到的键值对不为空时，检查他的过期时间，如果已经过期了，就把他删除掉，然后返回空，否则就把值返回。

背过Java面试题的我们都知道，JUC包中提供的 `ConcurrentHashMap` 是线程安全的实现，但是在这里我们的调用逻辑真的正确吗？

## 三、测试用例

我们写出下面这个测试用例，来模拟并发删除和更新的案例：

```java
package org.example.store.engine;

import org.junit.jupiter.api.Test;

public class UnsafeConcurrencyStoreEngineTest {
    private static final String TEST_KEY = "key";
    private static final byte[] TEST_VALUE = "value".getBytes();
    private static final byte[] ANOTHER_VALUE = "another".getBytes();

    @Test
    public void del_and_get_should_be_safe() {
        for (int i = 0; i < 1000; i++) {
            // 每次都创建新的存储引擎以防止遗留的测试数据产生污染
            UnsafeSimpleStoreEngine storeEngine = new UnsafeSimpleStoreEngine();
            // 使用固定时钟以便模拟过期场景
            Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
            storeEngine.setClock(fixedClock);


            Runnable delete = () -> {
                // 设置一个1分钟后过期的key
                storeEngine.set(TEST_KEY, TEST_VALUE, 1, TimeUnit.MINUTES);

                // 统一把固定时钟拨到过期时间后一秒，保证子线程调度时都已经处于过期状态
                Clock expiredClock = Clock.offset(fixedClock, Duration.ofMinutes(1).plusSeconds(1));
                storeEngine.setClock(expiredClock);
                // 进行一次get操作，触发过期删除
                storeEngine.get(TEST_KEY);
            };

            Runnable get = () -> {
                // 设置一个永不过期的key
                storeEngine.set(TEST_KEY, ANOTHER_VALUE);
                Optional<byte[]> value = storeEngine.get(TEST_KEY);
                if (value.isEmpty()) {
                    throw new RuntimeException("value 不应为空！");
                }
                if (!Arrays.equals(ANOTHER_VALUE, value.get())) {
                    throw new RuntimeException("value 结果不正确！实际获得的value：" + new String(value.get()));
                }
            };

            try {
                // 使用 CompletableFuture 来获取线程执行过程中的异常
                CompletableFuture<Void> deleteFuture = CompletableFuture.runAsync(delete);
                CompletableFuture<Void> getFuture = CompletableFuture.runAsync(get);
                // 等待两个线程都执行完毕，如果有异常会在这里抛出 CompletionException
                CompletableFuture.allOf(deleteFuture, getFuture).join();
            } catch (Exception e) {
                System.out.println("轮次：" + i + " 发生异常！" + e.getMessage());
            }
        }
    }
}
```

这个测试用例的内容非常好理解：我们先启动一个线程delete，他会设置一个过期时间为1分钟的key，然后将时钟拨到1分1秒之后，这样就刚好来到了key过期的时间点。然后我们执行一次查询，触发过期删除逻辑。
在另一个get线程中，我们更新同一个key，给他设置一个永不过期的值。在执行更新过后，不论我们什么时候查询这个key，都应该能得到一个返回值。

## 四、抽丝剥茧：暗藏的深坑

我们执行前一节的测试用例

```text
轮次：25 发生异常！java.lang.RuntimeException: value 不应为空！
轮次：41 发生异常！java.lang.RuntimeException: value 结果不正确！实际获得的value：value
轮次：82 发生异常！java.lang.RuntimeException: value 结果不正确！实际获得的value：value
轮次：98 发生异常！java.lang.RuntimeException: value 结果不正确！实际获得的value：value
轮次：235 发生异常！java.lang.RuntimeException: value 结果不正确！实际获得的value：value
轮次：259 发生异常！java.lang.RuntimeException: value 不应为空！
轮次：260 发生异常！java.lang.RuntimeException: value 不应为空！
轮次：274 发生异常！java.lang.RuntimeException: value 结果不正确！实际获得的value：value
轮次：283 发生异常！java.lang.RuntimeException: value 结果不正确！实际获得的value：value
```

仔细观察日志，你会发现抛出的异常居然有两种完全不同的类型！
1. **`value 结果不正确！实际获得的value：value`**
2. **`value 不应为空！`**

这其实是一个非常经典的“坑中坑”：**不仅我们的业务代码有并发问题，连我们的测试代码本身也有并发Bug！**

### 1. 为什么会“结果不正确”？（测试代码的Bug）

我们来看看第一种异常出现的情况：明明在 `get` 线程设置了新的值并且永不过期，结果读出来的却是旧的 "value"。
重新审视我们的测试用例，我们把**准备初始数据**的代码 `storeEngine.set(...)` 放到了 `delete` 的并发线程中执行。

这就导致了一个极其尴尬的时序交错（Race Condition）：
- **时序 A**：`get` 线程先抢到了CPU，执行了 `set(TEST_KEY, ANOTHER_VALUE)`（此时库里是 "another"）。
- **时序 B**：`delete` 线程后来居上，执行了 `set(TEST_KEY, TEST_VALUE, ...)`（此时库里的值被覆盖回了老值 "value"）。
- **时序 C**：`get` 线程继续向下执行 `get()`，获取出来一对比，发现值是 "value"，直接抛出了异常！

这种异常是测试用例不严谨导致的**伪异常**，它掩盖了我们要排查的真实底层逻辑Bug。

**破局方法**：将初始数据的 `set` 操作以及时钟偏移等“舞台准备”工作，移动到多线程启动之前（只在主线程中单线程独立执行）。我们修改测试用例：

```java
// ...之前的代码...
// 【关键修改】预置初始数据和时钟，必须在主线程单线程执行，避免测试代码本身的污染
storeEngine.set(TEST_KEY, TEST_VALUE, 1, TimeUnit.MINUTES);

Clock expiredClock = Clock.offset(fixedClock, Duration.ofMinutes(1).plusSeconds(1));
storeEngine.setClock(expiredClock);

Runnable delete = () -> {
    // 此时它只需要执行一次 get 操作，纯粹用于触发惰性删除
    storeEngine.get(TEST_KEY);
};
// ...之后的代码...
```

### 2. 为什么会“值为空”？（真实的业务Bug）

当我们把测试用例上述的Bug修复后，再次运行1000次大循环，发现日志清爽了许多，但依然在冷酷地报错：
```text
轮次：21 发生异常！java.lang.RuntimeException: value 不应为空！
轮次：249 发生异常！java.lang.RuntimeException: value 不应为空！
...
```

现在排除掉测试数据的干扰，这才是我们要定位的真正“幽灵Bug”。

回到本文开头的代码，`ConcurrentHashMap` 固然是线程安全的，但这仅仅说明它**单个方法的调用**是原子的（操作它的内部结构不会报错或者死锁）。而我们这种**“先 `get` 出来看看，满足条件再通过 `remove` 删掉” (Check-Then-Act)** 的复合逻辑，根本没有任何锁的保障！

我们再来倒带重放一下这个“必定会空”的时序：
1. **前置状态**：主线程已经设置了即将过期的Key。
2. **读操作触发删除（Thread-Delete）**：执行 `engine.get(key)`，代码读取判断出已过期，正准备调用 `concurrentHashMap.remove(key)`。然而在此刻，线程被挂起。
3. **写操作续命（Thread-Get）**：执行了 `engine.set(key, NEW_VALUE)`，给这个 Key 放进去了全新的、永不过期的值。
4. **致命一刀（Thread-Delete 恢复运行）**：`Delete` 线程从刚刚的挂起点复苏，无视了刚刚发生的一切，闭着眼睛执行了刚才未完成的 `concurrentHashMap.remove(key)`。直接把 `Get` 线程刚塞进去的新值给一刀蒸发了！
5. **获取失败**：`Get` 线程满心欢喜去读自己刚设置的值，却只收到了冰冷的 `null`，含恨抛出了 `value 不应为空！`。

## 五、如何破局？

### 加锁:最简单粗暴的解决方案

最简单粗暴的方法，就是我们在每个线程内进行对存储引擎进行操作时，手动把concurrentHashMap加锁，保障在操作期间不被其他线程干扰。我们写出如下测试用例：
```java
    @Test
    public void synchronized_del_and_get() {
        for (int i = 0; i < 1000; i++) {
            // 每次都创建新的存储引擎以防止遗留的测试数据产生污染
            UnsafeSimpleStoreEngine storeEngine = new UnsafeSimpleStoreEngine();
            // 使用固定时钟以便模拟过期场景
            Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
            storeEngine.setClock(fixedClock);

            storeEngine.set(TEST_KEY, TEST_VALUE, 1, TimeUnit.MINUTES);

            // 统一把固定时钟拨到过期时间后一秒，保证子线程调度时都已经处于过期状态
            Clock expiredClock = Clock.offset(fixedClock, Duration.ofMinutes(1).plusSeconds(1));
            storeEngine.setClock(expiredClock);
            Runnable delete = () -> {
                // 进行一次get操作，触发过期删除
                synchronized(storeEngine) {
                    storeEngine.get(TEST_KEY);
                }
            };

            Runnable get = () -> {
                synchronized(storeEngine){

                    storeEngine.set(TEST_KEY, ANOTHER_VALUE);
                    Optional<byte[]> value = storeEngine.get(TEST_KEY);
                    if (value.isEmpty()) {
                        throw new RuntimeException("value 不应为空！");
                    }
                    if (!Arrays.equals(ANOTHER_VALUE, value.get())) {
                        throw new RuntimeException("value 结果不正确！实际获得的value：" + new String(value.get()));
                    }
                }
            };

            try {
                // 使用 CompletableFuture 来获取线程执行过程中的异常
                CompletableFuture<Void> deleteFuture = CompletableFuture.runAsync(delete);
                CompletableFuture<Void> getFuture = CompletableFuture.runAsync(get);
                // 等待两个线程都执行完毕，如果有异常会在这里抛出 CompletionException
                CompletableFuture.allOf(deleteFuture, getFuture).join();
            } catch (Exception e) {
                System.out.println("轮次：" + i + " 发生异常！" + e.getMessage());
            }
        }
    }
```

我们在每个线程内都对concurrentHashMap进行加锁，重新运行测试用例，现在 `value 不应为空！` 的错误不再出现。

### 缩小锁粒度：使用 compute 方法

面对这种 Check-Then-Act （先检查后执行） 的经典并发问题，简单的 `synchronized` 可以解决但性能太差。我们可以使用 `ConcurrentHashMap` 提供的 `compute()` 方法来执行更新操作。

```java
public V compute(
        K key,
        java.util.function.BiFunction<? super K, ? super V, ? extends V> remappingFunction
)
```

它的作用是：**以原子方式计算给定键的值**。也就是说：在 `remappingFunction` 执行期间，其他线程无法对这个键做任何的 `put` 或者 `remove` 操作。
简单来说，就是在该期间，对这个键值对进行加锁。 这样我们就把锁的粒度从原来的整个ConcurrentHashMap，缩小到了单一键值对。

```java
    @Override
public Optional<byte[]> get(String key) {
    StorageEntry entry = concurrentHashMap.compute(key, (k, oldEntry) -> {
        if (oldEntry == null) {
            return null;
        }
        if (expired(oldEntry)) {
            return null; // 返回null的话，在操作结束后一起删除这个entry
        }
        return oldEntry; // 返回不是null的值，表示维持已有的值
    });

    return entry == null
            ? Optional.empty()
            : Optional.of(entry.value());
}
```

### 借助 API 乐观更新：使用 CAS（Compare And Swap）思想

我们也可以运用CAS(Compare And Swap)的思想。虽然在我使用的 JDK 实现中，`ConcurrentHashMap` 底层针对具体的节点依旧会使用 `synchronized` 进行加锁，但从 API 调用者的角度，我们可以体会到乐观锁的思路。`ConcurrentHashMap`也提供了下面的方法来供我们使用:

```java
public boolean remove(
    Object key,
    Object expectedValue
)
```

这个方法也很好理解，只有当 `key` 对应的 `value` 与我们给出的值一致时，我们才真正进行删除操作

```java
    @Override
    public Optional<byte[]> get(String key) {
        StorageEntry valueEntry = concurrentHashMap.get(key);
        if (Objects.isNull(valueEntry)) {
            return Optional.empty();
        } else if (expired(valueEntry)) {
            concurrentHashMap.remove(key, valueEntry);
            return Optional.empty();
        } else {
            return Optional.of(valueEntry.value());
        }
    }
```

## 六、尾记

通过这次的并发Bug排查，感慨良多：
- 一方面是对Java并发编程的理解加深，像这种**检查-再操作**的复合逻辑，真是并发编程中的一大“雷区”。
- 另一方面则是对测试用例编写的反思，像这次的“伪异常”，真是让人防不胜防。

希望我的这篇博文，能给你带来一些启发和帮助。
