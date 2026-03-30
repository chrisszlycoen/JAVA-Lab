package Activity;
// 7. Overriding toString()
// The System.out.println(...) method has an overload which takes an Object, thus you can pass an instance of User directly to that method. However it will print something like oopintro.User@19821f which comes from the toString method of Object. This can be overridden though...
// Override toString in the User class to return a string containing the name and age of the user
// Modify the main method to print each instance of User directly

// 6. Encapsulation
// Objects in Java should have protected data members which are accessed via getter and setter methods.
// Create a class called User with a String property called name and an int property called age
// Create getter and setter methods for each property
// Create a constructor which takes parameters for name and age
// Create a Java program which initializes 3 instances of User
// In its main method, print the name and age of each instance using its getter methods

class User1 {
    private String name;
    private int age;

    public int setAge(int age){
        return this.age = age;
    }

    public String setName(String name){
        return this.name = name;
    }

    public int getAge(){
        return age;
    }
    public String getName(){
        return name;
    }

    public User1(String name, int age){
        this.setName(name);
        this.setAge(age);
    }
    @Override
    public String toString(){
       return "Name:\t " + name + ", Age:\t" + age;
    }

}

class Program1 {
    public static void main(String[] args) {
        User1 user1 = new User1("Uhiriwe",15);
        User1 user2 = new User1("jane", 13);
        User1 user3 = new User1("Sonia", 14);
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);
    }
}

