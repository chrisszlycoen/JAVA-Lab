package Activity;
// 4. Calling superclass constructors
// When an instance of a subclass such as B is created, the constructor of the superclass is called automatically. If we need to pass a parameter to the superclass constructor we can do that using the super keyword
// Modify class A's constructor so that it takes a string parameter, which is used to initialize the value of message
// Add a default constructor to class B, i.e. no parameters.
// Add the following to the start of the constructor: super("Hello from B");


class A3 {
    String message;
    
    public A3(){};
    public A3(String messag){
        this.message = messag;
    }

    public void hello(){
        System.out.println(message);
    }
}

class B3 extends A3{
    public B3(){
        super("Hello from B");
    }
}

class Exercise03 {
    public static void main(String[] args) {
        A3 s1A3 = new B3();
        s1A3.hello();
    }
}
