package Online_exam;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewResultsPage extends JFrame {

    public ViewResultsPage(String username) {
        setTitle("Past Results - " + username);
        setSize(500, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = { "Score", "Total Questions", "Date" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT score, total_questions, exam_date FROM results WHERE username = ?")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int score = rs.getInt("score");
                int total = rs.getInt("total_questions");
                Timestamp date = rs.getTimestamp("exam_date");

                model.addRow(new Object[]{score, total, date});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading results: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        setVisible(true);
    }
}
