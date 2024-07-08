package qpack;

public class FixedQueue implements ICharQ {
    private char q[]; // this array holds the queue
    private int putloc, getloc; // the put and get indices
    // Construct an empty queue given its size.
    public FixedQueue(int size) {
        q = new char[size]; // allocate memory for queue
        putloc = getloc = 0;
    }
    // Put a character into the queue.
    public void put(char ch) {
        if(putloc==q.length) {
            System.out.println(" - Queue is full.");
            return;
        }
        q[putloc++] = ch;
    }
    // Get a character from the queue.
    public char get() {
        if(getloc == putloc) {
            System.out.println(" - Queue is empty.");
            return (char) 0;
        }
        return q[getloc++];
    }

    public static void copyQueue(ICharQ src, ICharQ dest) {
        char ch;
        // Backup the current state of the source queue
        char[] backup = new char[100]; // Assuming a max size of 100 for simplicity
        int index = 0;

        // Read from source and store in destination
        while ((ch = src.get()) != (char) 0) {
            dest.put(ch);
            backup[index++] = ch;
        }

        // Restore the source queue state
        for (int i = 0; i < index; i++) {
            src.put(backup[i]);
        }
    }
}