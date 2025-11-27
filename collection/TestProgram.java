package collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TestProgram {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
Point<Number> nums=new Point<Number>();
nums.x=  5;
nums.y= 10;
System.out.println(" My point is:("+nums.x+ ","+nums.y+")");
	
	
Person p1=new Person(100,"ange","Ange@gmail.com");
Person p2=new Person(200,"Mugisha","mugisha@gmail.com");
Person p3=new Person(100,"Anaise","Anaise@gmail.com");
Person p4=new Person(200,"Ange","ange@gmail.com");
Person p5=new Person(10,"Ange","angella@gmail.com");
Person p6=new Person(400,"Annet","annet@gmail.com");
System.out.println("Person 1 and Person 2 "+p1.equals(p2));
System.out.println("Person 1 and Person 3 "+p1.equals(p3));
List<Person> people=new ArrayList<Person>();
people.add(p1);
people.add(p2);
people.add(p3);
people.add(p4);
people.add(p5);
people.add(p6);
System.out.println("ArrayList before sorting"+people.size());
for(Person p:people) {
	System.out.println(p);
}
Collections.sort(people);
System.out.println("ArrayList After sorting"+people.size());
for(Person p:people) {
	System.out.println(p);
}

Set<Person> peoplee=new HashSet<Person>();
peoplee.add(p1);
peoplee.add(p2);
peoplee.add(p3);
peoplee.add(p4);
peoplee.add(p5);
peoplee.add(p6);
System.out.println("hashset"+peoplee.size());
for(Person p:peoplee) {
	System.out.println(p);
}
Set<Person> peopleee=new TreeSet<Person>();
peopleee.add(p1);
peopleee.add(p2);
peopleee.add(p3);
peopleee.add(p4);
peopleee.add(p5);
peopleee.add(p6);
System.out.println("Treeset Size"+peopleee.size());
for(Person p:peopleee) {
	System.out.println(p);
}
}
}
