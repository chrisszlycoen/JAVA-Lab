package JAVA_Exceptions;

import java.util.Random;

public class HandlerError {
    public static void main(String[] args) {
        int a = 0, b = 0, c = 0;
        Random r = new Random();
        for(int i= 0; i< 12000; i++){
            try {
                b = r.nextInt();
                c = r.nextInt();
                a = 12345/(b/c);
                String name =null;
                System.out.println(name.length());
                int [] d = {1};
                d[42] = 12;
            } catch (ArithmeticException e) {
                System.out.println("Division by 0");
                a = 0;
            }
            catch(ArrayIndexOutOfBoundsException e){
                System.out.println("Index out of bound");
            }
            catch(NullPointerException e){
                System.out.println("Null String");
            }
            System.out.println("a:"+a);
        }
    }
}
