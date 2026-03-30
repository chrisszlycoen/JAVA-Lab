package JAVA_Exceptions;

import java.io.*;
public class FileNotFoundExceptionExample {
   public static void main(String args[]) throws IOException {//throws IOException {
       BufferedReader br = new BufferedReader(new FileReader("projects/myFile.txt"));
           String data = null;
           while ((data = br.readLine()) != null) {
               System.out.println(data);
           }
     
   }
}

