import java.sql.*;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        try(Connection conn = DatabaseConnection.getConnection()){
            System.out.println("Enter your name");
            String name = scn.nextLine();
            System.out.println("Enter your code");
            String code = scn.nextLine();
            System.out.println("Enter your school");
            String school = scn.nextLine();
            System.out.println("Enter your age");
            int age = Integer.parseInt(scn.nextLine());

            String insertSql = "INSERT INTO Student(code, name, school, age) VALUES(?,?,?,?)";
            PreparedStatement psmt = conn.prepareStatement(insertSql);
            psmt.setString(1,name);
            psmt.setString(2,code);
            psmt.setString(3,school);
            psmt.setInt(3,age);
            psmt.executeUpdate();
            System.out.println("Student saved to database");

            String selectSql = "SELECT * FROM Student";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectSql);

            System.out.println("Student data");
            while (rs.next()){
                System.out.println(
                        rs.getString("code")+" | "+
                                rs.getString("name")+" | "+
                                rs.getInt("age")+" | "+
                                rs.getString("school")
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
