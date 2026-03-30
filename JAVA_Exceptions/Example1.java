package JAVA_Exceptions;
public class Example1 {
    public static void main(String[] args) {
        int a,b,c;
        System.out.println("exception");
        try{
        a = 180;
        b = 0;
        c = (a/b);
        System.out.println(c);
        }
        catch(ArithmeticException e){
            System.out.println(e.getMessage());
        }
        finally{
            System.out.println("finally Block");
        }
        System.out.println("exception 2");
    }
}
