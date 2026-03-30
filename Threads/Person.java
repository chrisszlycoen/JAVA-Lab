package Threads;
class Person  implements Runnable {
   public void run() {
       System.out.println("Hello from a thread!");
       String name = Thread.currentThread().getName();
       System.out.println( name+ "starting");
     try{
         for(int i=5;i>0;i--){
             System.out.println("Child Thread"+i);
             Thread.sleep(1000);
         }
     } catch (InterruptedException e) {
         System.out.println("Interrupted");
     }
   System.out.println( name +"exiting");

   }
}