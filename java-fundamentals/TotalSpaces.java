import java.io.IOException;

public class TotalSpaces {
    public static void main(String[] args) {
        try {
            char input = 's';
            int count = 0;

            System.out.println("Enter a character input: ");
            while (input != '.'){
                input = (char) System.in.read();
                if (input == ' '){
                    count++;
                }
            }

            System.out.println("Total spaces: " + count);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
