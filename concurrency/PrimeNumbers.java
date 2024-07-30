import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class PrimeCalculator implements Runnable{
    private final int start;
    private final int end;
    private final List<Integer> primes;

    public PrimeCalculator(int start, int end) {
        this.start = start;
        this.end = end;
        this.primes = new ArrayList<>();
    }

    @Override
    public void run() {
        for (int num = start; num <= end; num++) {
            if (isPrime(num)){
                primes.add(num);
            }
        }
    }

    public List<Integer> getPrimes() {
        return primes;
    }

    private boolean isPrime(int num) {
        if (num <= 1) return false;
        if (num == 2) return true;
        for (int i = 2; i < Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }
        return true;
    }
}

public class PrimeNumbers {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the maximum number (N): ");
        int maxNumber = scanner.nextInt();

        System.out.println("Enter the number of threads: ");
        int numThreads = scanner.nextInt();

        Thread[] threads = new Thread[numThreads];
        PrimeCalculator[] calculators = new PrimeCalculator[numThreads];

        int range = maxNumber / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int start = i * (range+1);
            int end = (i == numThreads-1) ? maxNumber : (i+1) * range;
            calculators[i] = new PrimeCalculator(start, end);
            threads[i] = new Thread(calculators[i]);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try{
                threads[i].join();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        List<Integer> allPrimes = new ArrayList<>();
        for (PrimeCalculator calculator: calculators){
            allPrimes.addAll(calculator.getPrimes());
        }

        for (Integer num: allPrimes){
            System.out.println(num);
        }
    }
}
