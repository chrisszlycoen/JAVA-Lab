package Threads;
public class Main {
   public static void main(String[] args) {
       Thread thread = new Thread(new Person());
       thread.start();
       try {
           for(int i=5;i>0;i--){
               System.out.println("Main Thread:"+i);
               Thread.sleep(1000);
           }
       }catch(InterruptedException e){
           System.out.println("Interrupted");
       }
System.out.println("Main thread exiting");

   }}