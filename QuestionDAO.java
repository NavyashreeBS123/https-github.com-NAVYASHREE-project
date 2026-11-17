import java.sql.*;
import java.util.*;

public class QuestionDAO {
    List<Question> questions = QuestionDAO.getQuestions();

    public static List<Question> getQuestions() {

        List<Question> list = new ArrayList<>();

        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM questions";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Question q = new Question();

                q.id = rs.getInt("id");
                q.question = rs.getString("question");

                q.options = new String[4];
                q.options[0] = rs.getString("option1");
                q.options[1] = rs.getString("option2");
                q.options[2] = rs.getString("option3");
                q.options[3] = rs.getString("option4");

                q.answer = rs.getInt("answer");

                list.add(q);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}