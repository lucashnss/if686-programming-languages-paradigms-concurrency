import java.util.EmptyStackException;

public class Stack {
    static int size = 0;
    static Node top = null;

    public class Node{
        int data;
        Node next;

        public Node(int data){
            this.data = data;
        }
    }

    public int stack_size(){
        return size;
    }

    public boolean empty(){
        return size == 0;
    }

    public int peek(){
        if (size == 0){
            throw new EmptyStackException();
        } else {
            return top.data;
        }
    }

    public void pop() throws StackEmptyException {
        if (size == 0){
            throw new StackEmptyException();
        } else {
            top = top.next;
            size = size - 1;
        }
    }

    public void push(int data){
        Node new_node = new Node(data);
        new_node.next = top;
        size = size + 1;
        top = new_node;

    }

    public static void main(String[] args) {
        Stack stack = new Stack();
        System.out.println(stack.empty());
        System.out.println(stack.stack_size());
        for (int i = 0; i < 10; i++){
            stack.push(i);
        };

        System.out.println(stack.peek());
        System.out.println(stack.stack_size());

        for (int i = 0; i < 5; i++){
            try {
                stack.pop();
                System.out.print("Top: " + stack.peek() + " ");
                System.out.print("Size: " + stack.stack_size() + " ");
            }
            catch (StackEmptyException e) {
                System.out.println(e);
            }
        }
    }
}
