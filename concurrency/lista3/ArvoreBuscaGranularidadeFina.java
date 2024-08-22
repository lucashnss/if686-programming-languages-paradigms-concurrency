import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ArvoreBuscaGranularidadeFina {
    private Node root;

    private class Node {
        int value;
        Node left, right;
        final Lock lock = new ReentrantLock();

        Node(int value) {
            this.value = value;
        }
    }

    public void add(int value) {
        root = addRecursive(root, value);
    }

    private Node addRecursive(Node current, int value) {
        if (current == null) {
            return new Node(value);
        }

        current.lock.lock();
        try {
            if (value < current.value) {
                current.left = addRecursive(current.left, value);
            } else if (value > current.value) {
                current.right = addRecursive(current.right, value);
            }
        } finally {
            current.lock.unlock();
        }
        return current;
    }

    // Método para contar os nós (após todas as inserções)
    public int countNodes() {
        return countNodesRecursive(root);
    }

    private int countNodesRecursive(Node current) {
        if (current == null) {
            return 0;
        }
        return 1 + countNodesRecursive(current.left) + countNodesRecursive(current.right);
    }

    public static void main(String[] args) {
        ArvoreBuscaGranularidadeFina arvore = new ArvoreBuscaGranularidadeFina();
        int numeroThreads = 50;
        int numerosPorThread = 2000;

        Thread[] threads = new Thread[numeroThreads];

        for (int i = 0; i < numeroThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < numerosPorThread; j++) {
                    int numeroAleatorio = (int) (Math.random() * 10000);
                    arvore.add(numeroAleatorio);
                }
            });
        }

        long inicio = System.currentTimeMillis();

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long fim = System.currentTimeMillis();
        // 110ms, sem o travamento por nó foi 118ms e sequencial foi 43ms
        System.out.println("Total de nós na árvore: " + arvore.countNodes());
        System.out.println("Tempo com travamento por nó: " + (fim - inicio) + " ms");
    }
}
