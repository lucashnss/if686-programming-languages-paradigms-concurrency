import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

class ArvoreBusca {
    private class Node{
        int value;
        Node left, right;

        Node(int value){
            this.value = value;
            left = right = null;
        }
    }

    private Node root;
    private final ReentrantLock lock = new ReentrantLock();

    public ArvoreBusca(){
        root = null;
    }

    public void insert (int value){
        try{
            root = insertRec(root, value);
        } finally {
            lock.unlock();
        }
    }
    private Node insertRec(Node root, int value){
        if (root == null){
            root = new Node(value);
            return root;
        }

        if (value < root.value){
            insertRec(root.left, value);
        } else if (value >= root.value){
            insertRec(root.right, value);
        }
        return root;
    }

    public int countNodes (){
        return countRec(root);
    }

    private int countRec (Node root){
        if (root == null){
            return 0;
        } else{
            return 1 + countRec(root.right) + countRec(root.left);
        }
    }


    public static void main (String[] args){
        final ArvoreBusca tree = new ArvoreBusca();
        int numThreads = 50;
        int insertsPerThread = 2000;
        Thread[] threads = new Thread[numThreads];
        Random rand = new Random();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++){
            threads[i] = new Thread(() -> {
                for (int j = 0; j < insertsPerThread; j++){
                    int num = rand.nextInt(10000);
                    tree.insert(num);
                }
            });
            threads[i].start();
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Total nodes in the tree " + tree.countNodes());
        System.out.println("Time taken with multi-threading: " + duration);

        startTime = System.currentTimeMillis();

        final ArvoreBusca tree2 = new ArvoreBusca();

        for (int i = 0; i < numThreads * insertsPerThread; i++){
            int num = rand.nextInt(10000);
            tree.insert(num);
        }

        endTime = System.currentTimeMillis();
        duration = endTime - startTime;

        System.out.println("Time taken sequentially: " + duration);
    }
}
