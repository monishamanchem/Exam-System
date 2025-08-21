package Online_exam;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {
    JTable questionTable;
    DefaultTableModel model;

    JButton addBtn, editBtn, deleteBtn, refreshBtn;

    public AdminDashboard() {
        setTitle("Admin Dashboard - Manage Questions");
        setSize(900, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new String[]{"ID", "Question", "Option 1", "Option 2", "Option 3", "Option 4", "Correct"}, 0);
        questionTable = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(questionTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addBtn = new JButton("Add Question");
        editBtn = new JButton("Edit Selected");
        deleteBtn = new JButton("Delete Selected");
        refreshBtn = new JButton("Refresh");

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        JButton viewResultsBtn = new JButton("View All Results");
buttonPanel.add(viewResultsBtn);

viewResultsBtn.addActionListener(e -> {
    new AllResultsPage();  // open results table
});


        add(buttonPanel, BorderLayout.SOUTH);

        loadQuestions();

        addBtn.addActionListener(e -> openQuestionForm(false));
        editBtn.addActionListener(e -> openQuestionForm(true));
        deleteBtn.addActionListener(e -> deleteSelectedQuestion());
        refreshBtn.addActionListener(e -> loadQuestions());

        setVisible(true);
    }

    void openQuestionForm(boolean isEditMode) {
        int selectedRow = questionTable.getSelectedRow();

        JTextField questionField = new JTextField(30);
        JTextField[] options = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            options[i] = new JTextField(20);
        }
        JComboBox<String> correctCombo = new JComboBox<>(new String[]{"1", "2", "3", "4"});

        if (isEditMode) {
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a question to edit.");
                return;
            }
            questionField.setText((String) model.getValueAt(selectedRow, 1));
            for (int i = 0; i < 4; i++) {
                options[i].setText((String) model.getValueAt(selectedRow, 2 + i));
            }
            correctCombo.setSelectedItem(model.getValueAt(selectedRow, 6).toString());
        }

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Question:"));
        panel.add(questionField);
        for (int i = 0; i < 4; i++) {
            panel.add(new JLabel("Option " + (i + 1) + ":"));
            panel.add(options[i]);
        }
        panel.add(new JLabel("Correct Option (1-4):"));
        panel.add(correctCombo);

        int result = JOptionPane.showConfirmDialog(this, panel,
                (isEditMode ? "Edit" : "Add") + " Question", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String sql;
            if (isEditMode) {
                sql = "UPDATE questions SET question_text=?, option1=?, option2=?, option3=?, option4=?, correct_option=? WHERE id=?";
            } else {
                sql = "INSERT INTO questions (question_text, option1, option2, option3, option4, correct_option) VALUES (?, ?, ?, ?, ?, ?)";
            }

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, questionField.getText());
                for (int i = 0; i < 4; i++) {
                    stmt.setString(i + 2, options[i].getText());
                }
                stmt.setInt(6, Integer.parseInt((String) correctCombo.getSelectedItem()));

                if (isEditMode) {
                    int id = (int) model.getValueAt(selectedRow, 0);
                    stmt.setInt(7, id);
                }

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "✅ Question " + (isEditMode ? "updated!" : "added!"));
                loadQuestions();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        }
    }

    void deleteSelectedQuestion() {
        int selectedRow = questionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a question to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) model.getValueAt(selectedRow, 0);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM questions WHERE id=?")) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "🗑️ Question deleted.");
            loadQuestions();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadQuestions() {
        model.setRowCount(0); // Clear previous
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM questions")) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("question_text"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        rs.getInt("correct_option")
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
