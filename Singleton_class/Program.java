package Singleton_class;

public class Program {
    public static void main(String[] args) {
        PersonService ps = PersonService.getInstance();
        ps.print();
    }
}
