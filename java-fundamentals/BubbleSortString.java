import java.util.Random;

public class BubbleSortString {
    public static String[] Sort (String[] arr){
        for (int i = 0; i < arr.length-1; i++){
            for (int j = 0;j < arr.length-1 ; j++){
                if (arr[j].compareTo(arr[j+1]) > 0){
                    String temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        int[] random_arr = new int[100];

        Random rand = new Random();

        for (int i:random_arr){
            random_arr[i] = rand.nextInt(200);
        }

        int[] repeated = {1,1,1,1,7,7,7,7,7,5,2,0,3,9,11,19,36,23,7,9};

        int[] repeated_sorted = {1,1,1,1,1,1,2,2,2,2,3,3,4,4,6,11,15,20,35};

        int[] sorted = {1,5,9,13,18,25,29,35,49,70,100,150,170};

        int[] reversed_sorted = {170,150,133,100,70,50,33,25,11,5,2};

        int[] reversed_repeated = {100,70,70,70,70,65,61,61,61,61,61,55,55,55,55,43,37,25,13,5};

        String[] lexicographic = {"ceça","lucas","severino","ester","karen","douglas"};

        String[] lexicographic_ordered = {"ana","beatriz","clara","eduardo","felipe"};

        String item = "lucas";

        for (String i:lexicographic){
            System.out.print(i+" ");
        }

        System.out.println();

        String[] result = Sort(lexicographic);

        for (String i: result){
            System.out.print(i+" ");
        }
    }
}
