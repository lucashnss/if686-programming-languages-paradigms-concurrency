package qpack;

public class CircularDynQueue implements ICharQ {
    private char q[]; // this array holds the queue
    private int putloc, getloc; // the put and get indices
    // Construct an empty queue given its size.
    public CircularDynQueue(int size) {
        q = new char[size]; // allocate memory for queue
        putloc = getloc = 0;
    }
    public void put(char ch) {
        if(putloc+1==getloc |
                ((putloc==q.length-1) & (getloc==0))) {
            char t[] = new char[q.length * 2];
// copy elements into new queue
            for(int i=0; i < q.length; i++)
                t[i] = q[i];
            q = t;
        }
        if(putloc==q.length) putloc = 0;
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
}
