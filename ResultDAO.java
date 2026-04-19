import java.sql.*;

public class ResultDAO {

    public static void saveResult(String username, int score) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO results(username, score) VALUES (?, ?)";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setInt(2, score);

            ps.executeUpdate();

            System.out.println("Result saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}