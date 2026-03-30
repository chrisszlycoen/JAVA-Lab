public class Student {
    private String code;
    private String name;
    private String school;
    private int age;

    public Student(String c, String n, String school,int a){
        this.age = a;
        this.code = c;
        this.name = n;
        this.school = school;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSchool() {
        return school;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSchool(String school) {
        this.school = school;
    }

}
