import java.util.Random;

public class MinMax {
    public static int[] MinMaxFunc (int[] arr){
        int min,max;
        min = max = 0;

        for(int i:arr){
            if (i>max){
                max = i;
            }
            if (i<min){
                min = i;
            }
        }

        return new int[]{min,max};
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

        int[] result = MinMaxFunc(sorted);
        for (int i:sorted){
            System.out.print(i+" ");
        }
        System.out.println();
        System.out.println("Min element: "+result[0] + " Max element: "+ result[1]);
    }
}
