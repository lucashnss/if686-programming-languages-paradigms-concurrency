import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class ThreadSafeArray {
    private final int[] array;
    private final ReentrantLock[] locks;

    public ThreadSafeArray(int size) {
        array = new int[size];
        locks = new ReentrantLock[size];
        for (int i = 0; i < size; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    public void write(int value, int index){
        locks[index].lock();
        try{
            array[index] = value;
        } finally {
            locks[index].unlock();
        }
    }

    public int read(int index){
        locks[index].lock();
        try {
            return array[index];
        } finally {
            locks[index].unlock();
        }
    }

    public void swap (int index1, int index2){
        if (index1 > index2){
            int temp = array[index1];
            array[index1] = array[index2];
            array[index2] = temp;
        }

        locks[index1].unlock();
        locks[index2].unlock();
        try {
            int temp = array[index1];
            array[index1] = array[index2];
            array[index2] = temp;
        } finally {
            locks[index1].unlock();
            locks[index2].unlock();
        }
    }

    public static void main(String[] args) {
        int size = 100000;
        int numThreads = 100;
        ThreadSafeArray array = new ThreadSafeArray(size);

        for (int i = 0; i < size; i++) {
            array.write(0, i);
        }

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    int index = (int) (Math.random() * size);
                    int value = (int) (Math.random() * 100000);
                    array.write(index, value);
                    array.read(index);

                    int index1 = (int) (Math.random() * size);
                    int index2 = (int) (Math.random() * size);
                    array.swap(index1, index2);
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < size; i++) {
            System.out.println("Index: " + i + ", Value: " + array.read(i));
        }

    }
}
