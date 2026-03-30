package Matcher_matcher;

import java.util.regex.Pattern;
import java.util.regex.Matcher;;

public class Programa {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("[a-z0-9]{{8}");
        Matcher matcher = pattern.matcher("rwanda123isthebest");

        while(matcher.find()){
            String group = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            System.out.println(group+" from "+start+" to "+end);
        }
        
    }
}
