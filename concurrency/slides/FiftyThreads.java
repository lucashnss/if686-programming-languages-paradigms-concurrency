public class FiftyThreads {
    public static void main(String[] args) {
        int numThreads =  50;
        long max_Number = 2_000_000_000L;
        long range  = max_Number / numThreads;

        Thread[] threads = new Thread[numThreads];
        PrintRange[] printRanges = new PrintRange[numThreads];
        for (int i = 0; i < numThreads; i++) {
            long start = i * range + 1;
            long end = (i == numThreads - 1) ? max_Number : (i + 1) * range;
            printRanges[i] = new PrintRange(start, end);
            threads[i] = new Thread(printRanges[i]);
            threads[i].start();

        }

        int firstFinishedThread = -1;
        for (int i = 0; i < numThreads; i++) {
            try{
                threads[i].join();
                if (firstFinishedThread == -1) {
                    firstFinishedThread = i;
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        System.out.println("Thread " + firstFinishedThread + " finished.");
    }

    static class PrintRange implements Runnable {
        private long start, end;

        public PrintRange(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            for (long i = start; i < end; i++) {
                // System.out.println(i); hiding the numbers printed
            }
            System.out.println("Thread for range " + start + " to " + end + " finished.");
        }
    }
}
