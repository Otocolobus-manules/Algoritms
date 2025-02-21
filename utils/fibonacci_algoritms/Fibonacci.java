package utils.fibonacci_algoritms;

public class Fibonacci {

    public static class Result {
        public int n;
        public long result;
        public long iterations;
        public double timeMs;

        public Result(int n, long result, long iterations, double timeMs) {
            this.n = n;
            this.result = result;
            this.iterations = iterations;
            this.timeMs = timeMs;
        }
    }

    public static class Counter {
        private long count = 0;

        public void increment() {
            count++;
        }

        public long getCount() {
            return count;
        }
    }

    public static Result fibonacciRec(int n) {
        long startTime = System.nanoTime();

        Counter counter = new Counter();
        long result = fibonacciRecHelper(n, counter);

        long endTime = System.nanoTime();
        double timeMs = (endTime - startTime) / 1_000_000.0;

        return new Result(n, result, counter.getCount(), timeMs);
    }

    private static long fibonacciRecHelper(int n, Counter counter) {
        counter.increment();
        if (n == 0) return 0;
        if (n == 1 || n == 2) return 1;
        return fibonacciRecHelper(n - 1, counter) + fibonacciRecHelper(n - 2, counter);
    }

    public static Result fibonacciIter(int n) {
        long startTime = System.nanoTime();

        Counter counter = new Counter();
        if (n == 0) return new Result(n, 0, 1, 0);
        if (n == 1) return new Result(n, 1, 1, 0);

        long a = 0L, b = 1L;
        for (long i = 2L; i <= n; i++) {
            counter.increment();
            long temp = a + b;
            a = b;
            b = temp;
        }
        
        long endTime = System.nanoTime();
        double timeMs = (endTime - startTime) / 1_000_000.0;
        return new Result(n, b, counter.getCount(), timeMs);
    }

}
