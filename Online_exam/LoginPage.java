package Online_exam;

import javax.swing.*;
import java.awt.event.*;

public class LoginPage extends JFrame implements ActionListener {
    JLabel userLabel, passLabel, messageLabel;
    JTextField userText;
    JPasswordField passText;
    JButton loginButton, registerButton;

    UserService userService;

    public LoginPage() {
        userService = new UserService();

        setTitle("Login Page");
        setSize(400, 250);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userLabel = new JLabel("Username:");
        passLabel = new JLabel("Password:");
        messageLabel = new JLabel("");

        userText = new JTextField();
        passText = new JPasswordField();

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        userLabel.setBounds(50, 30, 100, 25);
        passLabel.setBounds(50, 70, 100, 25);
        userText.setBounds(150, 30, 150, 25);
        passText.setBounds(150, 70, 150, 25);
        loginButton.setBounds(50, 120, 100, 30);
        registerButton.setBounds(200, 120, 100, 30);
        messageLabel.setBounds(50, 160, 300, 25);

        add(userLabel);
        add(passLabel);
        add(userText);
        add(passText);
        add(loginButton);
        add(registerButton);
        add(messageLabel);

        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String username = userText.getText().trim();
        String password = new String(passText.getPassword());

        if (e.getSource() == loginButton) {
            if (username.equals("admin") && password.equals("admin123")) {
                messageLabel.setText("Admin login successful!");
                dispose(); // Close login window
                new AdminDashboard(); // Go to admin dashboard
            } else if (userService.loginUser(username, password)) {
                messageLabel.setText("Login successful!");
                dispose(); // Close login window
                new ExamPage(username); // Go to exam
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        } else if (e.getSource() == registerButton) {
            if (username.equalsIgnoreCase("admin")) {
                messageLabel.setText("Cannot register as admin.");
                return;
            }

            boolean registered = userService.registerUser(new User(username, password));
            if (registered) {
                messageLabel.setText("Registration successful!");
            } else {
                messageLabel.setText("Username already exists.");
            }
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
