package Activity;

// 3. Overriding a variable, and using super
// Override the message variable in class B
// In the hello method of class B, call super.hello()
// Run the program and observe that super.hello() calls the hello method of A

class A2 {
    String message = "Hello from class A";

    public void hello(){
        System.out.println(message);
    }
}

class B2 extends A2{
    String message = "Hello from class B";
    public void hello(){
        super.hello();
        System.out.println(message);
    }
}