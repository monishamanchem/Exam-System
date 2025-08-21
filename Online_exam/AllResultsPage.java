package Online_exam;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AllResultsPage extends JFrame {
    JTable table;

    public AllResultsPage() {
        setTitle("All User Results");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {"Username", "Score", "Total Questions", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        fetchResults(model);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void fetchResults(DefaultTableModel model) {
        String sql = "SELECT username, score, total_questions, exam_date FROM results ORDER BY exam_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                int score = rs.getInt("score");
                int total = rs.getInt("total_questions");
                String date = rs.getString("exam_date");

                model.addRow(new Object[]{username, score, total, date});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to fetch results: " + e.getMessage());
        }
    }
}
