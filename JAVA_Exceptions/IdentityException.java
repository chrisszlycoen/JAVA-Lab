package JAVA_Exceptions;

public class IdentityException extends Exception{
    private String message;
    public IdentityException(String message){
        super(message);
    }
}

