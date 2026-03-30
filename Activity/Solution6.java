package Activity;
// 6. Encapsulation
// Objects in Java should have protected data members which are accessed via getter and setter methods.
// Create a class called User with a String property called name and an int property called age
// Create getter and setter methods for each property
// Create a constructor which takes parameters for name and age
// Create a Java program which initializes 3 instances of User
// In its main method, print the name and age of each instance using its getter methods

 class User {
    private String name;
    private int age;

    public String setName(String name){
        return this.name = name;
    }
    public int setAge(int age){
        return this.age = age;
    }

    public String getName(){
        return this.name;
    }
    public int getAge(){
        return this.age;
    }

    public User(){};
    public User(String name, int age){
        this.setName(name);
        this.setAge(age);
    };
}

class Program{
    public static void main(String[] args) {
        User user1 = new User("Uhiriwe",15);
        User user2 = new User("jane", 13);
        User user3 = new User("Sonia", 14);

        System.out.println(
            "The use details are:\n "+
            "\tname 1: "+ user1.getName()+
            "\tage 1: "+ user1.getAge()+"\n"+
            "\tname 2: "+user2.getName()+
            "\tAge 2: "+user2.getAge()+"\n"+
            "\tname 3: "+user3.getName()+
            "\tAge 3: "+user3.getAge()
        );
        
    }
}
