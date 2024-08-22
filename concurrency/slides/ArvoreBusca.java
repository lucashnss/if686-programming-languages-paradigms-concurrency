import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ArvoreBusca {
    private static class Node {
        int key;
        Node left, right;
        ReentrantLock lock = new ReentrantLock();

        Node(int item) {
            key = item;
            left = right = null;
        }
    }

    private Node root;

    public ArvoreBusca() {
        root = null;
    }

    public void add(int key) {
        if (root == null) {
            synchronized (this) {
                if (root == null) {
                    root = new Node(key);
                    return;
                }
            }
        }
        addRecursive(root, key);
    }

    private void addRecursive(Node current, int key) {
        current.lock.lock();
        try {
            if (key < current.key) {
                if (current.left == null) {
                    current.left = new Node(key);
                } else {
                    addRecursive(current.left, key);
                }
            } else if (key > current.key) {
                if (current.right == null) {
                    current.right = new Node(key);
                } else {
                    addRecursive(current.right, key);
                }
            }
        } finally {
            current.lock.unlock();
        }
    }

    public boolean contains(int key) {
        return containsRecursive(root, key);
    }

    private boolean containsRecursive(Node current, int key) {
        if (current == null) {
            return false;
        }

        current.lock.lock();
        try {
            if (key == current.key) {
                return true;
            } else if (key < current.key) {
                return containsRecursive(current.left, key);
            } else {
                return containsRecursive(current.right, key);
            }
        } finally {
            current.lock.unlock();
        }
    }

    public void remove(int key) {
        root = removeRecursive(root, key);
    }

    private Node removeRecursive(Node current, int key) {
        if (current == null) {
            return null;
        }

        current.lock.lock();
        try {
            if (key == current.key) {
                if (current.left == null && current.right == null) {
                    return null;
                } else if (current.left == null) {
                    return current.right;
                } else if (current.right == null) {
                    return current.left;
                } else {
                    int smallestValue = findSmallestValue(current.right);
                    current.key = smallestValue;
                    current.right = removeRecursive(current.right, smallestValue);
                    return current;
                }
            } else if (key < current.key) {
                current.left = removeRecursive(current.left, key);
                return current;
            } else {
                current.right = removeRecursive(current.right, key);
                return current;
            }
        } finally {
            current.lock.unlock();
        }
    }

    private int findSmallestValue(Node root) {
        root.lock.lock();
        try {
            return root.left == null ? root.key : findSmallestValue(root.left);
        } finally {
            root.lock.unlock();
        }
    }

    public static void main(String[] args) {
        int numOperacoes = 100000;
        int numThreads = 4;

        ArvoreBusca arvore = new ArvoreBusca();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        long inicio = System.nanoTime();

        for (int i = 0; i < numThreads; i++) {
            executor.execute(() -> {
                for (int j = 0; j < numOperacoes; j++) {
                    int valor = ThreadLocalRandom.current().nextInt(100000);
                    arvore.add(valor);
                    arvore.contains(valor);
                    arvore.remove(valor);
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long fim = System.nanoTime();
        long duracao = TimeUnit.NANOSECONDS.toMillis(fim - inicio);

        System.out.println("Tempo com travamento de nível de nó: " + duracao + " ms");
    }
}
