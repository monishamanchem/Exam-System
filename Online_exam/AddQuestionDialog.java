package Online_exam;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddQuestionDialog extends JDialog {
    JTextArea questionArea;
    JTextField[] optionFields = new JTextField[4];
    JComboBox<String> correctOptionBox;
    JButton saveBtn, cancelBtn;

    AdminDashboard parent;

    public AddQuestionDialog(AdminDashboard parent) {
        super(parent, "Add New Question", true);
        this.parent = parent;

        setSize(500, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        questionArea = new JTextArea(2, 40);
        formPanel.add(new JLabel("Question:"));
        formPanel.add(new JScrollPane(questionArea));

        for (int i = 0; i < 4; i++) {
            optionFields[i] = new JTextField();
            formPanel.add(new JLabel("Option " + (i + 1) + ":"));
            formPanel.add(optionFields[i]);
        }

        correctOptionBox = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        formPanel.add(new JLabel("Correct Option (1–4):"));
        formPanel.add(correctOptionBox);

        JPanel buttonPanel = new JPanel();
        saveBtn = new JButton("Save");
        cancelBtn = new JButton("Cancel");
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> saveQuestion());
        cancelBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    void saveQuestion() {
        String question = questionArea.getText().trim();
        String[] options = new String[4];
        for (int i = 0; i < 4; i++) {
            options[i] = optionFields[i].getText().trim();
        }
        int correct = correctOptionBox.getSelectedIndex() + 1;

        if (question.isEmpty() || options[0].isEmpty() || options[1].isEmpty() ||
            options[2].isEmpty() || options[3].isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        String sql = "INSERT INTO questions (question_text, option1, option2, option3, option4, correct_option) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, question);
            stmt.setString(2, options[0]);
            stmt.setString(3, options[1]);
            stmt.setString(4, options[2]);
            stmt.setString(5, options[3]);
            stmt.setInt(6, correct);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Question added successfully!");
            parent.loadQuestions();  // refresh table
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Failed to add question.");
        }
    }
}
