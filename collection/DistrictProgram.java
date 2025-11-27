package collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class DistrictProgram {
    public static void main(String[] args){
        List items = new ArrayList();
        items.add(123);
        items.add("Hello");
        items.add(123);
        System.out.println(items.size());

        Set items2 = new HashSet();
        items2.add(123);
        items2.add("Hello");
        items2.add(123);
        System.out.println(items2.size());

        List<String> districts = new ArrayList<String>();
        districts.add("Rubavu");
        districts.add("Nyabihu");
        districts.add("Rubavu");
        districts.add("Musanze");
        districts.add("Gasabo");
        districts.add("Rwamagana");
        districts.add("Nyanza");

        for(int i = 0; i < districts.size(); i++)
            System.out.println(districts.get(i));

        for(String district : districts)
            System.out.println(district);
        
        
    }
}

/*
public static void main(String[] args){
        List items = new ArrayList();
        items.add(123);
        items.add("Hello");
        items.add(123);
        System.out.println(items.size());

        Set items2 = new HashSet();
        items2.add(123);
        items2.add("Hello");
        items2.add(123);
        System.out.println(items2.size());

        List<String> districts = new ArrayList<String>();
        districts.add("Rubavu");
        districts.add("Nyabihu");
        districts.add("Rubavu");
        districts.add("Musanze");
        districts.add("Gasabo");
        districts.add("Rwamagana");
        districts.add("Nyanza");

        for(int i = 0; i < districts.size(); i++)
            System.out.println(districts.get(i));

        for(String district : districts)
            System.out.println(district);

        System.out.println(districts);
        System.out.println(items);

    }
 */