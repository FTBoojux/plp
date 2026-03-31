package org.example.store.engine;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
                storeEngine.set(TEST_KEY, TEST_VALUE, 1, TimeUnit.MINUTES);

                // 统一把固定时钟拨到过期时间后一秒，保证子线程调度时都已经处于过期状态
                Clock expiredClock = Clock.offset(fixedClock, Duration.ofMinutes(1).plusSeconds(1));
                storeEngine.setClock(expiredClock);
                // 进行一次get操作，触发过期删除
                storeEngine.get(TEST_KEY);
            };

            Runnable get = () -> {
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
    @Test
    public void set_initial_value_should_be_public() {
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
                storeEngine.get(TEST_KEY);
            };

            Runnable get = () -> {
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
    @Test
    public void compute_when_check_expired() {
        for (int i = 0; i < 1000; i++) {
            // 每次都创建新的存储引擎以防止遗留的测试数据产生污染
            ComputeSimpleStoreEngine storeEngine = new ComputeSimpleStoreEngine();
            // 使用固定时钟以便模拟过期场景
            Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
            storeEngine.setClock(fixedClock);

            storeEngine.set(TEST_KEY, TEST_VALUE, 1, TimeUnit.MINUTES);

            // 统一把固定时钟拨到过期时间后一秒，保证子线程调度时都已经处于过期状态
            Clock expiredClock = Clock.offset(fixedClock, Duration.ofMinutes(1).plusSeconds(1));
            storeEngine.setClock(expiredClock);
            Runnable delete = () -> {
                // 进行一次get操作，触发过期删除
                storeEngine.get(TEST_KEY);
            };

            Runnable get = () -> {
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
