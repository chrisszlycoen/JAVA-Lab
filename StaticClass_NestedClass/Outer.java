package StaticClass_NestedClass;

public class Outer {
    public static  String message = "hello";

    //we can make a class static if only nested 
    //static class can not be inherited
    //static class can not be instantiated
    static class NestedDemo {
        public static void print(){
            System.out.println("Message from static class "+message);
        }
        public void printAgain(){
            System.out.println("Message again: "+message);
        }
        
    }
    public class InnerClass {
        
        public void display(){
            System.out.println("message from non-static or nested class: "+message);
        }
    }

    static void print(){
        System.out.println("Outer static method: "+ message);
    }
    void printAgain(){
        System.out.println("from Outer non Static: "+message);
    }
}
