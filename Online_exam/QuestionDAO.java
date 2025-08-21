package Online_exam;

import java.sql.*;
import java.util.*;

public class QuestionDAO {

    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String qText = rs.getString("question_text");
                String[] options = {
                    rs.getString("option1"),
                    rs.getString("option2"),
                    rs.getString("option3"),
                    rs.getString("option4")
                };
                int correct = rs.getInt("correct_option");

                questions.add(new Question(rs.getInt("id"), qText, options, correct));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }

    public boolean addQuestion(Question q) {
        String sql = "INSERT INTO questions (question_text, option1, option2, option3, option4, correct_option) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, q.getQuestionText());
            stmt.setString(2, q.getOptions()[0]);
            stmt.setString(3, q.getOptions()[1]);
            stmt.setString(4, q.getOptions()[2]);
            stmt.setString(5, q.getOptions()[3]);
            stmt.setInt(6, q.getCorrectOptionIndex());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteQuestion(int id) {
        String sql = "DELETE FROM questions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateQuestion(Question q) {
        String sql = "UPDATE questions SET question_text=?, option1=?, option2=?, option3=?, option4=?, correct_option=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, q.getQuestionText());
            stmt.setString(2, q.getOptions()[0]);
            stmt.setString(3, q.getOptions()[1]);
            stmt.setString(4, q.getOptions()[2]);
            stmt.setString(5, q.getOptions()[3]);
            stmt.setInt(6, q.getCorrectOptionIndex());
            stmt.setInt(7, q.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
