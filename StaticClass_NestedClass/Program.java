package StaticClass_NestedClass;

public class Program {
    void aPrint(){
        System.out.println("Hello");
    }

    static void bPrint(){
        System.out.println("bPrint");
    }
    public static void main(String[] args) {
        bPrint();
        //aPrint() cannot work because it belongs to object but if it was static it could work 
        Program program1 = new Program();
        program1.aPrint();
        
        Outer.NestedDemo.print();

        Outer.NestedDemo demo = new Outer.NestedDemo();
        demo.printAgain();

        Outer outer = new Outer();
        outer.printAgain();
        Outer.InnerClass inner = outer.new InnerClass();

        inner.display();
    }
}
