package benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1, warmups = 1)
@Measurement(iterations = 30)
public class CreateThreadBenchmark {

    private static final int NUMBER_OF_THREADS = 100000;

    @Benchmark
    public void thread(Blackhole blackhole) {
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            blackhole.consume(new Thread(() -> {}));
        }
    }

    @Benchmark
    public void virtualThread(Blackhole blackhole) {
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {;
            blackhole.consume(Thread.ofVirtual());
        }
    }
}
