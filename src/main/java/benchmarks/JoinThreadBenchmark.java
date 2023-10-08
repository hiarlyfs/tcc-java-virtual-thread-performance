package benchmarks;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;

@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1, warmups = 1)
@Measurement(iterations = 30)
public class JoinThreadBenchmark {

    private static final int NUMBER_OF_THREADS = 10000;

    @State(Scope.Thread)
    public static class ThreadsState {
        List<Thread> threads = new ArrayList<>();

        @Setup(Level.Invocation)
        public void createThreads() {
            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                threads.add(new Thread(() -> {}));
            }
        }

        @TearDown(Level.Invocation)
        public void clearThreads() throws InterruptedException {
            threads.clear();
        }

    }

    @Benchmark
    public void thread(ThreadsState threadsState) throws InterruptedException {
        for (Thread t : threadsState.threads) {
            t.start();
        }

        for (Thread t : threadsState.threads) {
            t.join();
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
            threads.clear();
            virtualThreadsBuilder.clear();
        }
    }


    @Benchmark
    public void virtualThread(VirtualThreadsState threadsState) throws InterruptedException {
        for (Thread.Builder.OfVirtual t : threadsState.virtualThreadsBuilder) {
            threadsState.threads.add(t.start(() -> {}));
        }

        for (Thread t : threadsState.threads) {
            t.join();
        }
    }

}
