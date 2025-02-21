package utils.sorting_algoritms;

public class Sorting {

    public static class Result {
        public int[] result;
        public long iterations;
        public double timeMs;

        public Result(int[] result, long iterations, double timeMs) {
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

    public static Result GnomMethod(int[] arr){
        long StartTime = System.nanoTime();
        Counter counter = new Counter();

        int cursor = 0;
        int lenght = arr.length;

        while (cursor < lenght) {
            counter.increment();

            if (cursor == 0 || arr[cursor] >= arr[cursor - 1]){
                cursor++;
            }
            else{
                int buf = arr[cursor];
                arr[cursor] = arr[cursor - 1];
                arr[cursor - 1] = buf;
                cursor--;
            }
        }

        long EndTime = System.nanoTime();
        double timeMs = (EndTime - StartTime) / 1_000_000.0;
        return new Result(arr, counter.getCount(), timeMs);
    }

    public static Result BubbleMethod(int[] arr){
        long StartTime = System.nanoTime();
        Counter counter = new Counter();

        int end_coursor = arr.length - 1;

        while ( end_coursor > 0){
            counter.increment();

            int start_coursor = 0;
            while (start_coursor < end_coursor){
                counter.increment();

                if (arr[start_coursor] > arr[start_coursor + 1]){
                    int buf = arr[start_coursor];
                    arr[start_coursor] = arr[start_coursor + 1];
                    arr[start_coursor + 1] = buf;
                }

                start_coursor++;
            }
            end_coursor--;
        }


        long EndTime = System.nanoTime();
        double timeMs = (EndTime - StartTime) / 1_000_000.0;
        return new Result(arr, counter.getCount(), timeMs);
    }

    public static Result AddedMethod(int[] arr){
        long StartTime = System.nanoTime();
        Counter counter = new Counter();

        int coursor = 1;
        int lenght = arr.length;

        while (coursor < lenght){
            counter.increment();

            int current_coursor = coursor;
            int buf = arr[coursor];

            while (current_coursor > 0 && buf < arr[current_coursor - 1]){
                counter.increment();

                arr[current_coursor] = arr[current_coursor - 1];
                current_coursor--;
            }
            arr[current_coursor] = buf;

            coursor++;
        }

        long EndTime = System.nanoTime();
        double timeMs = (EndTime - StartTime) / 1_000_000.0;
        return new Result(arr, counter.getCount(), timeMs);
    }
}
