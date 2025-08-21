package Online_exam;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class QuestionBank {
    private List<Question> questions;

    public QuestionBank() {
        questions = new ArrayList<>();
    }

public void loadQuestionsFromFile(String filename) {
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = br.readLine()) != null) {
            String questionText = line;
            String[] options = new String[4];
            for (int i = 0; i < 4; i++) {
                options[i] = br.readLine();
            }

            line = br.readLine(); // line containing correct option index
            if (line == null || line.trim().isEmpty()) {
                System.out.println("❌ Missing answer index after question: " + questionText);
                continue;
            }

            int correctIndex = Integer.parseInt(line.trim());

            questions.add(new Question(questionText, options, correctIndex));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void loadQuestionsFromDB() {
    questions.clear(); // Clear existing list

    String sql = "SELECT * FROM questions";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String questionText = rs.getString("question_text");
            String[] options = {
                rs.getString("option1"),
                rs.getString("option2"),
                rs.getString("option3"),
                rs.getString("option4")
            };
            int correctIndex = rs.getInt("correct_option");

            questions.add(new Question(questionText, options, correctIndex));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}



    public List<Question> getQuestions() {
        return questions;
    }
}
