import java.io.*;
class SpacesIntoHyphes {
    public static void main(String args[]) throws IOException
    {
        int i;
// Open and manage two files via the try statement.
        try (FileInputStream fin = new FileInputStream("text1");
             FileOutputStream fout = new FileOutputStream("text2"))
        {
            do {
                i = fin.read();
                if(i != -1) {
                    if (i == ' '){
                        fout.write('-');
                    }
                    else {
                        fout.write(i);
                    }
                }
            } while(i != -1);
        } catch(IOException exc) {
            System.out.println("I/O Error: " + exc);
        }
    }
}