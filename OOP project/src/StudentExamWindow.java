import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class StudentExamWindow extends JFrame {
    private List<Question> questions;
    private Map<Question, JComponent> answerComponents;
    private JLabel timerLabel;
    private Timer timer;
    private int timeLeft = 60; // seconds

    public StudentExamWindow(String studentName, String studentId) {
        setTitle("Student Exam - " + studentName);
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        questions = loadQuestions();
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No questions available. Please ask the teacher to add some.",
                    "No Questions",
                    JOptionPane.WARNING_MESSAGE
            );
            dispose();
            return;
        }

        answerComponents = new HashMap<>();
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

        int qNumber = 1;
        for (Question q : questions) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder("Question " + qNumber++));

            JLabel qLabel = new JLabel(q.questionText);
            panel.add(qLabel, BorderLayout.NORTH);

            if (q instanceof FillInTheBlanks) {
                JTextField answerField = new JTextField();
                panel.add(answerField, BorderLayout.CENTER);
                answerComponents.put(q, answerField);
            } else if (q instanceof MCQQuestion) {
                JPanel optionsPanel = new JPanel(new GridLayout(0, 1));
                ButtonGroup group = new ButtonGroup();
                for (String option : ((MCQQuestion) q).options) {
                    JRadioButton optionBtn = new JRadioButton(option);
                    group.add(optionBtn);
                    optionsPanel.add(optionBtn);
                }
                panel.add(optionsPanel, BorderLayout.CENTER);
                answerComponents.put(q, optionsPanel);
            }

            questionPanel.add(panel);
        }

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            timer.stop();
            simulateAutoSubmit();
        });

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();  // Close the current student exam window
            new LoginScreen();  // Go back to login screen
        });

        // Timer label
        timerLabel = new JLabel("Time left: " + timeLeft + "s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        startTimer();

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(timerLabel, BorderLayout.NORTH);
        bottomPanel.add(submitButton, BorderLayout.SOUTH);
        bottomPanel.add(logoutButton, BorderLayout.WEST); // Adding logout button to the bottom panel

        add(new JScrollPane(questionPanel), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time left: " + timeLeft + "s");

            if (timeLeft <= 0) {
                timer.stop();
                simulateAutoSubmit();
            }
        });
        timer.start();
    }

    private void simulateAutoSubmit() {
        int score = 0;

        for (Question q : questions) {
            String answerGiven = "";

            if (q instanceof FillInTheBlanks) {
                JTextField tf = (JTextField) answerComponents.get(q);
                answerGiven = tf.getText().trim();
            } else if (q instanceof MCQQuestion) {
                JPanel panel = (JPanel) answerComponents.get(q);
                for (Component comp : panel.getComponents()) {
                    if (comp instanceof JRadioButton) {
                        JRadioButton rb = (JRadioButton) comp;
                        if (rb.isSelected()) {
                            answerGiven = rb.getText().trim();
                        }
                    }
                }
            }

            if (answerGiven.equalsIgnoreCase(q.correctAnswer)) {
                score++;
            }
        }

        JOptionPane.showMessageDialog(this,
                "Time's up or submitted!\nYour score: " + score + " out of " + questions.size(),
                "Result",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose(); // Close window after showing result
    }

    private List<Question> loadQuestions() {
        File file = new File("questions.ser");
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Question>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}