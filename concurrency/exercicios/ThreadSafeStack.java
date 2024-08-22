import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeStack<T> {
    private final Stack<T> stack = new Stack<>();
    private final ReentrantLock lock = new ReentrantLock();

    // Adiciona um elemento à pilha (operação push)
    public void push(T value) {
        lock.lock();
        try {
            stack.push(value);
        } finally {
            lock.unlock();
        }
    }

    // Remove e retorna o elemento do topo da pilha (operação pop)
    public T pop() {
        lock.lock();
        try {
            if (stack.isEmpty()) {
                return null;
            }
            return stack.pop();
        } finally {
            lock.unlock();
        }
    }

    // Retorna o elemento do topo da pilha sem removê-lo (operação peek)
    public T peek() {
        lock.lock();
        try {
            if (stack.isEmpty()) {
                return null;
            }
            return stack.peek();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSafeStack<Integer> safeStack = new ThreadSafeStack<>();
        Stack<Integer> unsafeStack = new Stack<>();
        int numThreads = 50;

        // Implementação com pilha segura para threads
        long startTime = System.currentTimeMillis();

        Thread[] producers = new Thread[numThreads];
        Thread[] consumers = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            producers[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    safeStack.push((int) (Math.random() * 100));
                }
            });

            consumers[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    Integer value = safeStack.pop();
                    if (value != null) {
                        System.out.println("Consumed: " + value);
                    }
                }
            });

            producers[i].start();
            consumers[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            producers[i].join();
            consumers[i].join();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Execution time with thread-safe stack: " + (endTime - startTime) + " ms");

        // Implementação com pilha não segura para threads
        startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            producers[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    unsafeStack.push((int) (Math.random() * 100));
                }
            });

            consumers[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    if (!unsafeStack.isEmpty()) {
                        Integer value = unsafeStack.pop();
                        System.out.println("Consumed: " + value);
                    }
                }
            });

            producers[i].start();
            consumers[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            producers[i].join();
            consumers[i].join();
        }

        endTime = System.currentTimeMillis();
        System.out.println("Execution time with non-thread-safe stack: " + (endTime - startTime) + " ms");
    }
}
