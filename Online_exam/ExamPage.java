package Online_exam;

import javax.swing.*;
import java.awt.event.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
// import java.util.ArrayList;
// import java.util.TimerTask;

public class ExamPage extends JFrame implements ActionListener {
    JLabel questionLabel, timerLabel;
    JRadioButton[] options;
    JButton nextButton, finishButton;
    JButton previousButton;
    JButton viewResultsButton;


    ButtonGroup bg;

    List<Question> questions;
    int current = 0;
    int score = 0;
    int totalTime = 600; // 10 minutes in seconds
    int[] selectedOptions; // stores user-selected options (1-based index)


    String username;

    Timer timer;

    public ExamPage(String username) {
        this.username = username;
        QuestionBank qb = new QuestionBank();
        qb.loadQuestionsFromDB();


        questions = qb.getQuestions();
        selectedOptions = new int[questions.size()];
for (int i = 0; i < selectedOptions.length; i++) {
    selectedOptions[i] = -1; // -1 = not answered
}


        setTitle("Online Exam - Welcome " + username);
        setSize(600, 400);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        questionLabel = new JLabel();
        questionLabel.setBounds(30, 30, 520, 30);
        add(questionLabel);

        timerLabel = new JLabel("Time Left: 10:00");
        timerLabel.setBounds(450, 10, 100, 20);
        add(timerLabel);

        options = new JRadioButton[4];
        bg = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setBounds(50, 80 + i * 30, 400, 30);
            bg.add(options[i]);
            add(options[i]);
        }

        nextButton = new JButton("Next");
        finishButton = new JButton("Finish");
        previousButton = new JButton("Previous");
previousButton.setBounds(30, 250, 100, 30);
add(previousButton);
previousButton.addActionListener(this);
viewResultsButton = new JButton("View Past Results");
viewResultsButton.setBounds(390, 250, 160, 30);
add(viewResultsButton);
viewResultsButton.addActionListener(this);



       nextButton.setBounds(150, 250, 100, 30);
    finishButton.setBounds(270, 250, 100, 30);


        add(nextButton);
        add(finishButton);

        nextButton.addActionListener(this);
        finishButton.addActionListener(this);

        showQuestion(current);
        startTimer();

        setVisible(true);
    }

void showQuestion(int index) {
    if (index < questions.size()) {
        Question q = questions.get(index);
        questionLabel.setText("Q" + (index + 1) + ": " + q.getQuestionText());
        String[] opts = q.getOptions();
        for (int i = 0; i < 4; i++) {
            options[i].setText(opts[i]);
        }

        bg.clearSelection();
        if (selectedOptions[index] != -1) {
            options[selectedOptions[index] - 1].setSelected(true);
        }

        // Enable/disable previous and next buttons
        previousButton.setEnabled(index > 0);
        nextButton.setEnabled(index < questions.size() - 1);
    }
}


    boolean isCorrectAnswerSelected() {
        int selected = -1;
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                selected = i + 1; // 1-based index
                break;
            }
        }
        return selected == questions.get(current).getCorrectOptionIndex();
    }

public void actionPerformed(ActionEvent e) {
    if (e.getSource() == nextButton) {
        saveSelectedAnswer();
        current++;
        showQuestion(current);
    } else if (e.getSource() == previousButton) {
        saveSelectedAnswer();
        current--;
        showQuestion(current);
    } else if (e.getSource() == finishButton) {
        saveSelectedAnswer();  // make sure latest answer is saved
        calculateScore();
        if (timer != null) timer.stop();
        showResult();
} else if (e.getSource() == viewResultsButton) {
    new ViewResultsPage(username);
}
}


void startTimer() {
    final int[] remaining = { totalTime };

    timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int minutes = remaining[0] / 60;
            int seconds = remaining[0] % 60;

            timerLabel.setText("Time Left: " + minutes + ":" + seconds);



            remaining[0]--;

            if (remaining[0] < 0) {
                timer.stop();
                showResult();
            }
        }
    });
    timer.start();
}


void showResult() {
    System.out.println("Inside showResult()");
    saveResultToDatabase();  // ✅ call this instead of writing to file
    JOptionPane.showMessageDialog(this,
            "Exam finished!\nUser: " + username + "\nScore: " + score + " / " + questions.size());
    dispose();
}

int getSelectedOption() {
    for (int i = 0; i < 4; i++) {
        if (options[i].isSelected()) {
            return i + 1; // 1-based index
        }
    }
    return -1;
}

void saveSelectedAnswer() {
    int selected = getSelectedOption();
    if (selected != -1) {
        selectedOptions[current] = selected;
    }
}

void calculateScore() {
    score = 0;
    for (int i = 0; i < questions.size(); i++) {
        if (selectedOptions[i] == questions.get(i).getCorrectOptionIndex()) {
            score++;
        }
    }
}
void saveResultToDatabase() {
    String sql = "INSERT INTO results (username, score, total_questions) VALUES (?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, username);
        stmt.setInt(2, score);
        stmt.setInt(3, questions.size());

        stmt.executeUpdate();
        System.out.println("✅ Result saved to DB!");

    } catch (SQLException e) {
        System.out.println("❌ Error saving result: " + e.getMessage());
    }
}

}

