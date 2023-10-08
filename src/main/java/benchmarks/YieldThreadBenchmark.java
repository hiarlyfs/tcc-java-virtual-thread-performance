package benchmarks;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;

@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1, warmups = 1)
@Measurement(iterations = 30)
public class YieldThreadBenchmark {

    private static final int NUMBER_OF_THREADS = 100000;
    private static final Runnable threadTask = () -> {
        try {
            Thread.sleep(100);
            Thread.yield();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    };

    @State(Scope.Thread)
    public static class ThreadsState {
        List<Thread> threads = new ArrayList<>();

        @Setup(Level.Invocation)
        public void createThreads() {
            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                Thread thread =  new Thread(threadTask);
                threads.add(thread);
            }
        }

        @TearDown(Level.Invocation)
        public void clearThreads() throws InterruptedException {
            for (Thread t : threads) {
                t.join();
            }
            threads.clear();
        }

    }

    @Benchmark
    public void thread(ThreadsState threadsState) throws InterruptedException {
        for (Thread t : threadsState.threads) {
            t.start();
        }
    }


    @State(Scope.Thread)
    public static class VirtualThreadsState {
        List<Thread.Builder.OfVirtual> virtualThreadsBuilder = new ArrayList<>();
        List<Thread> threads = new ArrayList<>(NUMBER_OF_THREADS);

        @Setup(Level.Invocation)
        public void createThreads() {
            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                virtualThreadsBuilder.add(Thread.ofVirtual());
            }
        }

        @TearDown(Level.Invocation)
        public void joinThreads() throws InterruptedException {
            for (Thread t : threads) {
                t.join();
            }

            threads.clear();
            virtualThreadsBuilder.clear();
        }
    }


    @Benchmark
    public void virtualThread(VirtualThreadsState threadsState) throws InterruptedException {
        for (Thread.Builder.OfVirtual t : threadsState.virtualThreadsBuilder) {
            threadsState.threads.add(t.start(threadTask));
        }
    }
}
