import java.util.Scanner;

public class LowerToUpper {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter a string (ends with an '.'): ");
        String input = scanner.nextLine();
        int changes = 0;

        StringBuilder result = new StringBuilder();
        for(int i=0;i<input.length();i++) {
            char current_char = input.charAt(i);
            if (current_char >= 'a' && current_char <= 'z') {
                changes++;
                current_char = (char) (current_char - 32);
            }  else if (current_char >= 'A' && current_char <= 'Z') {
                changes++;
                current_char = (char) (current_char + 32);
            }

            result.append(current_char);
        }

        System.out.println("New string is: " + result.toString());
        System.out.println("Case changes: " + changes);
        scanner.close();
    }
}
