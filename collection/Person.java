package collection;

public class Person {
    private int code;
    private String name;
    private String email;

    public Person(){}

    public Person(int code, String n, String e){
        this.code = code;
        this.name = n;
        this.email = e;
    }

    public void setCode(int code){
        this.code = code;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public int getCode(){
        return code;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String toString(){
        return "Code: "+code+" Name: "+name+" Email: "+email;
    }


    
}