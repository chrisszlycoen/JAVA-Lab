package Activity;
// 5. Inheritance family tree
// Create the following family tree of animals as classes which extend each other:
// Organism (has a default constructor which prints something to the console)
// Animal (has a string called sound, initialized by a constructor parameter, printed to console in a method called hello)
// Canine (has a constructor which takes a sound parameter and passes it to super(...))
// Dog
// Wolf
// Feline (has a constructor which takes a sound parameter and passes it to super(...))
// Lion
// Cat
// The Dog, Wolf, Lion and Cat subclasses should use super(...) to pass a parameter to the Animal constructor which is a string of the sound that animal makes. Create a program which has an array of those 4 animals, and using a for loop, call each of their hello methods.
// Solution: Exercise05.java

class Organism {
    public Organism(){
        System.out.println("I am an organism ");
    }
    
}

class Animal extends Organism {
    String sound;
    public Animal(){};
    public Animal(String soun){
        this.sound = soun;
    };

    public void hello(){
        System.out.println("This animal sound is: "+sound);
    }
}

class Canine extends Animal{
    public Canine(){};
    public Canine(String sound){
        super(sound);
    }
}

class Dog extends Canine {
    public Dog(){
        super("barks");
    };
    
}

class Wolf extends Canine {
    public Wolf(){
        super("Awooo");
    }
}

class Feline extends Animal {
    public Feline(){};
    public Feline(String sound){
        super(sound);
    }
}

class Lion extends Feline{
    public Lion(){
        super("roars");
    }
}

class Cat extends Feline{
    public Cat(){
        super("meow");
    }
}

class Exercise04 {
    public static void main(String[] args) {
        Animal[] animals = {new Dog(), new Wolf(), new Lion(), new Cat()};

        for(Animal a : animals){
            a.hello();
        }
    }
}