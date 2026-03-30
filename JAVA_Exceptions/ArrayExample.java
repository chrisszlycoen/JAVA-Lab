package JAVA_Exceptions;

public class ArrayExample {
    public static void main(String[] args) {
        try {
            int []nums = new int[5];
            nums[0] = 10;
            nums[1]= 20;
            nums[2] = 30;
            nums[3] = 40;
            nums[4] = 50;
            System.out.println("the eightth element is: "+ nums[7]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        finally{
            System.out.println();
        }
        System.out.println("exception");
    }
}
