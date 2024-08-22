import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<T> {
    private final Queue<T> queue;
    private final int capacity;
    private final Lock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    public BlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.queue = new LinkedList<>();
        this.capacity = capacity;
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
    }

    public void put(T item) throws InterruptedException{
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.add(item);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException{
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            T item = queue.remove();
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new BlockingQueue<>(5);

        Runnable producer = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    queue.put(i);
                    System.out.println("Produziu o número " + i);
                    Thread.sleep((int) (Math.random() * 100));
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Thread.currentThread().interrupt();
        };

        Runnable consumer = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    int item = queue.take();
                    System.out.println("Consumiu o número " + item);
                    Thread.sleep((int) (Math.random() * 100));
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Thread.currentThread().interrupt();
        };

        Thread producerThread1 = new Thread(producer);
        Thread producerThread2 = new Thread(producer);
        Thread consumerThread1 = new Thread(consumer);
        Thread consumerThread2 = new Thread(consumer);

        producerThread1.start();
        producerThread2.start();
        consumerThread1.start();
        consumerThread2.start();
    }
}
