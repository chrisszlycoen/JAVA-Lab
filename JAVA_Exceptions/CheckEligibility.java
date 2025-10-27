package JAVA_Exceptions;

public class CheckEligibility {
    static boolean isEligible(int age) throws IdentityException{
        if(age > 16)
        throw new IdentityException("Not over 16");
        return true;
    }
}
