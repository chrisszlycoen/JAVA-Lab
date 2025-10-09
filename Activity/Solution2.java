package Activity;
// 2. Overriding a method
// Override the hello method in B by declaring a hello method with the same name and return type (void)
// In the main method, assign an object of type B to a variable of type A, i.e. A c = new B();
// Call the hello method on that variable, and observe that it calls the hello method of B, even thought the variable is of class A

class A1 {

    String message = "Hello from clas";
    public A1(){};
    public A1(String mess){
        this.message = mess;
    }

    public void hello(){
        System.out.println(message);
    }
    
}

class B1 extends A1 {

    @Override
    public void hello(){
        System.out.println("Hello from class B");
    }

}

class Exercise01 {
    public static void main(String[] args) {
        A1 s1 = new B1();
        s1.hello();
    }
}
