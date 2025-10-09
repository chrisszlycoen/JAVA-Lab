package Activity;

// 1. Simple inheritance
// Create a class A with a string property called message and a method called hello that prints that string to the console
// Create a class B which extends A
// Create a class Exercise01 with a main method
// In the main method, create instances of A and B, and call the hello method on both objects

class A {

    String message = "Hello from class A";
    public A(){};
    public A(String mess){
        this.message = mess;
    }

    public void hello(){
        System.out.println(message);
    }
    
}

 class B extends A {

}

class Exercise1 {

    public static void main(String[] args) {
        A s1 = new A();
        B s2 = new B();
        s1.hello();
        s2.hello();
    }
    
}
