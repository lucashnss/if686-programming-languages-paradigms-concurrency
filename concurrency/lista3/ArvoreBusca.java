import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class ArvoreBusca {
    private Node root;
    private final ReentrantLock lock = new ReentrantLock();

    private static class Node {
        int value;
        Node left, right;
        ReentrantLock nodeLock = new ReentrantLock();

        Node(int value) {
            this.value = value;
        }
    }

    public void add(int value) {
        lock.lock();
        try {
            if (root == null) {
                root = new Node(value);
            } else {
                addRecursive(root, value);
            }
        } finally {
            lock.unlock();
        }
    }

    private void addRecursive(Node current, int value) {
        current.nodeLock.lock();
        try {
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new Node(value);
                } else {
                    addRecursive(current.left, value);
                }
            } else if (value > current.value) {
                if (current.right == null) {
                    current.right = new Node(value);
                } else {
                    addRecursive(current.right, value);
                }
            }
        } finally {
            current.nodeLock.unlock();
        }
    }

    public int countNodes() {
        return countNodesRecursive(root);
    }

    private int countNodesRecursive(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodesRecursive(node.left) + countNodesRecursive(node.right);
    }

    public static void main(String[] args) {
        ArvoreBusca arvore = new ArvoreBusca();

        // Medindo o tempo para execução concorrente
        long startTime = System.currentTimeMillis();
        Thread[] threads = new Thread[50];
        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 2000; j++) {
                    arvore.add(random.nextInt(10000));
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        // b) -> 118ms no tempo concorrente
        System.out.println("Tempo concorrente: " + (endTime - startTime) + " ms");

        // Contagem dos nós
        System.out.println("Total de nós: " + arvore.countNodes());

        // Medindo o tempo para execução sequencial
        ArvoreBusca arvoreSequencial = new ArvoreBusca();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 50 * 2000; i++) {
            arvoreSequencial.add(random.nextInt(10000));
        }
        endTime = System.currentTimeMillis();
        // b) -> 43ms no tempo sequencial
        System.out.println("Tempo sequencial: " + (endTime - startTime) + " ms");

        // o aumento no tempo concorrente pode ter ocorrido pelo overhead que ocorre na troca de
        // contexto entre as 50 threads
    }
}

