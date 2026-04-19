import java.sql.Connection;
import java.sql.DriverManager;

public class CheckDBConnection {

    public static void main(String[] args) {

        try {
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quizdb",
                "root",
                "password"   // 🔴 replace with your MySQL password
            );

            System.out.println("Database Connected Successfully!");

            con.close();

        } catch (Exception e) {
            System.out.println(" Connection Failed!");
            e.printStackTrace();
        }
    }
}