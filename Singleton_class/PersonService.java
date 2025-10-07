package Singleton_class;

public class PersonService {
    
    public static PersonService getInstance(){
        return new PersonService();
    }

    private PersonService(){};
    public void print(){
        System.out.println("Singleton");
    }
}
