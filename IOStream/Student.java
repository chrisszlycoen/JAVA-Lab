package IOStream;

public class Student implements java.io.Serializable {
    public int code;
    public String firstName;
    public int age;

    public Student(int code, String fName, int age){
        this.code = code;
        this.firstName = fName;
        this.age = age;
    }
}
