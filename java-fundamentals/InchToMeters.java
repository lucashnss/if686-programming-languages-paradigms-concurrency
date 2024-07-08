class InchToMeters {
    public static void main(String[] args) {
        double inches,meters;
        int counter;

        counter = 0;

        for (inches = 1; inches <= 120; inches++) {
            meters = inches * 39.37;
            System.out.println(inches + " inches is " + meters + " meters");

            counter ++;
            if (counter == 12) {
                counter = 0;
                System.out.println();
            }
        }
    }
}
