import com.sun.source.tree.LiteralTree;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class PadariaForno<T> {
    private final Queue<T> queue;
    private final int capacity;
    private final Lock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    private static class Pao {
        String number;
        public Pao (String number) {
            this.number = number;
        }
    }
    public PadariaForno(int capacity) {
        this.queue = new LinkedList<>();
        this.capacity = capacity;
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.notFull= lock.newCondition();
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
        BlockingQueue<Pao> queue = new BlockingQueue<>(50);

        Runnable producer = () -> {
            try {
                for (int j = 0; j <=5 ; j++) {
                    for (int i = j; i <= 10*j; i++) {
                        Pao p = new Pao(String.valueOf(i));
                        queue.put(p);
                        System.out.println("Colocou o pão " + p.number);
                        Thread.sleep(100);
                    }
                    System.out.println("Colocou mais dez pães ");
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Thread.currentThread().interrupt();
        };

        Runnable consumer = () -> {
            try {
                for (int i = 0; i <= 10; i++) {
                    Pao p = queue.take();
                    System.out.println("Retirou o pão " + p.number);
                    Thread.sleep(1000);
                }
                System.out.println("Retirou mais dez pães ");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Thread.currentThread().interrupt();
        };

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();
    }
}
