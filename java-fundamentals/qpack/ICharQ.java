package qpack;

public interface ICharQ {
    // Put a character into the queue.
    void put(char ch);
    // Get a character from the queue.
    char get();
    default void reset() {
        int putloc,getloc;
        putloc=getloc=0;
    }
}
