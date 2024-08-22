import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeVector {
    private final int[] vector;
    private final Lock[] locks;

    public ThreadSafeVector(int size) {
        vector = new int[size];
        locks = new ReentrantLock[size];
        for (int i = 0; i < size; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    public void write(int index, int value) {
        locks[index].lock();
        try {
            vector[index] = value;
        } finally {
            locks[index].unlock();
        }
    }

    public int read(int index) {
        locks[index].lock();
        try {
            return vector[index];
        } finally {
            locks[index].unlock();
        }
    }

    public void swap(int index1, int index2) {
        // Sempre adquira as travas na mesma ordem para evitar deadlock
        if (index1 > index2) {
            int temp = index1;
            index1 = index2;
            index2 = temp;
        }

        locks[index1].lock();
        locks[index2].lock();
        try {
            int temp = vector[index1];
            vector[index1] = vector[index2];
            vector[index2] = temp;
        } finally {
            locks[index2].unlock();
            locks[index1].unlock();
        }
    }

    public static void main(String[] args) {
        int size = 10;
        int numThreads = 4;
        ThreadSafeVector tsVector = new ThreadSafeVector(size);

        // Preenchendo o vetor inicial
        for (int i = 0; i < size; i++) {
            tsVector.write(i, i);
        }

        // Executando operações de leitura, escrita e troca
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    int index = (int) (Math.random() * size);
                    int value = (int) (Math.random() * 100);
                    tsVector.write(index, value);
                    tsVector.read(index);

                    int index1 = (int) (Math.random() * size);
                    int index2 = (int) (Math.random() * size);
                    tsVector.swap(index1, index2);
                }
            });
            threads[i].start();
        }

        // Aguardando todas as threads terminarem
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Imprimindo o vetor final
        for (int i = 0; i < size; i++) {
            System.out.println("Index " + i + ": " + tsVector.read(i));
        }
    }
}
