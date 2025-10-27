package JAVA_Exceptions;

public class Throwprogram {
    static void throwOne() throws IllegalAccessException {
        System.out.println("Inside throw one.");
        throw new IllegalAccessException("demo");
    }

    public static void main(String[] args) {
        try {
            throwOne();
        } catch (IllegalAccessException e) {
            System.out.println("caught "+e);
        }
    }
}
