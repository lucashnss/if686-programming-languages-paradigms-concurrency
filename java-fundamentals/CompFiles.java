import java.io.*;

public class CompFiles {
    public static void main(String args[]) {
        int i, j;
        int position = 0;

        // Compare the files.
        try (FileInputStream f1 = new FileInputStream("text1");
             FileInputStream f2 = new FileInputStream("text2")) {
            do {
                i = f1.read();
                j = f2.read();
                position++;

                // Only convert to lowercase if not end of file
                if (i != -1 && j != -1) {
                    i = Character.toLowerCase(i);
                    j = Character.toLowerCase(j);
                }

                if (i != j) break;
            } while (i != -1 && j != -1);

            if (i != j)
                System.out.println("Files differ at position " + position);
            else
                System.out.println("Files are the same.");
        } catch (IOException exc) {
            System.out.println("I/O Error: " + exc);
        }
    }
}
