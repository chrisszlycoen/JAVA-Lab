package Activity;

// 8. Overriding equals(...)
// Using == to compare two objects only checks to see if the variables reference the same object in memory. If we want to compare the content of two objects we can override the equals method of Object to perform a comparison based on all of that classes data members.
// Override equals in the User class to return true only if the user's have the same name and age
// Create 3 instances of User called u1, u2, u3 where u1 and u3 have the same name and age
// In the main method, print to the console the result of:
// u1 == u3
// u1.equals(u3)
// u1.equals(u2)
 class User2 {
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

    public User2(){};
    public User2(String name, int age){
        this.setName(name);
        this.setAge(age);
    };
    @Override
    public boolean equals(Object obj){
        if(this == obj)return true;
        if(!(obj instanceof User2))return false;
        User2 other = (User2) obj;
        return this.age == other.age && this.name == other.name;
    }
}

class Program3{
    public static void main(String[] args) {
        User2 user1 = new User2("Uhiriwe",15);
        User2 user2 = new User2("Uhiriwe", 15);
        User2 user3 = new User2("Sonia", 14);
        System.out.println(user1.equals(user2));
        System.out.println(user1.equals(user3));
        System.out.println(user2.equals(user3));
    }
}