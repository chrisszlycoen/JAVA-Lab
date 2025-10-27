package IOStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BiteStreamProgram {
    public static void main(String[] args) throws IOException{
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream("IOStream/inputText.txt");
            out = new FileOutputStream("IOStream/outputText.txt");
            int c;
            while((c = in.read()) != -1){
                out.write(c);
            }
        } finally {
            if(in != null){
                in.close();
            }
            if(out != null){
                out.close();
            }
        }
        System.out.println("Program finished");     
    }
}
