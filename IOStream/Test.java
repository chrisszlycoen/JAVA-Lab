package IOStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Test {
    public static void main(String[] args) {
        Student object = new Student(101,"jack", 12);
        Student sde = new Student (102,"Jacky", 16);

        String fileName = "student.ser";

        try{
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(object);
            out.writeObject(sde);

            out.close();
            file.close();
            System.out.println("object has been Serialised");

        }catch(IOException ex){
            System.out.println("IOException is caught");
        }

        Student object1 = null;
        Student object2 = null;

        try{
            FileInputStream file = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(file);

            object1 = (Student) in.readObject();
            object2 = (Student) in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialised");
            System.out.println("Code= "+ object1.code);
            System.out.println("Name= "+object1.firstName);
            System.out.println("Age= +"+object1.age);
            System.out.println("Code= "+ object2.code);
            System.out.println("Name= "+object2.firstName);
            System.out.println("Age= +"+object2.age);
        }catch(IOException ex){
            System.out.println("IOException is caught");
        }catch(ClassNotFoundException ec){
            System.out.println("ClassNotFoundException is caught "+ec.getMessage());
        }
    }
    
}
