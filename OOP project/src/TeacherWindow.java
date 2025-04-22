import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherWindow extends JFrame {

    private List<Question> questions;
    private DefaultListModel<String> questionListModel;
    private JList<String> questionList;

    public TeacherWindow(String teacherName) {
        setTitle("Teacher Panel - " + teacherName);
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        questions = loadQuestions();

        // Question input area
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JTextField questionField = new JTextField();
        JTextField answerField = new JTextField();

        JTextField opt1 = new JTextField();
        JTextField opt2 = new JTextField();
        JTextField opt3 = new JTextField();
        JTextField opt4 = new JTextField();
        JTextField correctMCQAnswer = new JTextField();

        JRadioButton fillBlankRadio = new JRadioButton("Fill in the Blank");
        JRadioButton mcqRadio = new JRadioButton("MCQ");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(fillBlankRadio);
        typeGroup.add(mcqRadio);
        fillBlankRadio.setSelected(true);

        inputPanel.add(new JLabel("Question:"));
        inputPanel.add(questionField);

        inputPanel.add(new JLabel("Answer (for Fill-in-the-Blank):"));
        inputPanel.add(answerField);

        inputPanel.add(fillBlankRadio);
        inputPanel.add(mcqRadio);

        inputPanel.add(new JLabel("Options for MCQ (only if MCQ is selected):"));
        inputPanel.add(new JLabel("Option 1:")); inputPanel.add(opt1);
        inputPanel.add(new JLabel("Option 2:")); inputPanel.add(opt2);
        inputPanel.add(new JLabel("Option 3:")); inputPanel.add(opt3);
        inputPanel.add(new JLabel("Option 4:")); inputPanel.add(opt4);
        inputPanel.add(new JLabel("Correct Answer (must match one option exactly):"));
        inputPanel.add(correctMCQAnswer);

        // Buttons
        JButton addButton = new JButton("Add Question");
        JButton saveButton = new JButton("Save All");

        inputPanel.add(addButton);
        inputPanel.add(saveButton);

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener((ActionEvent e) -> {
            dispose();  // Close the teacher's window
            new LoginScreen();  // Go back to login screen
        });
        inputPanel.add(logoutButton);  // Add the logout button to the input panel

        // Question list display
        questionListModel = new DefaultListModel<>();
        for (Question q : questions) {
            questionListModel.addElement(q.questionText);
        }

        questionList = new JList<>(questionListModel);
        JScrollPane listScroll = new JScrollPane(questionList);

        add(inputPanel, BorderLayout.WEST);
        add(listScroll, BorderLayout.CENTER);

        // Add Question Button Logic
        addButton.addActionListener((ActionEvent e) -> {
            String text = questionField.getText().trim();
            if (fillBlankRadio.isSelected()) {
                String answer = answerField.getText().trim();
                if (!text.isEmpty() && !answer.isEmpty()) {
                    questions.add(new FillInTheBlanks(text, answer));
                    questionListModel.addElement(text);
                }
            } else if (mcqRadio.isSelected()) {
                String[] options = {
                        opt1.getText().trim(),
                        opt2.getText().trim(),
                        opt3.getText().trim(),
                        opt4.getText().trim()
                };
                String correct = correctMCQAnswer.getText().trim();
                if (!text.isEmpty() && correct.length() > 0) {
                    questions.add(new MCQQuestion(text, correct, options));
                    questionListModel.addElement(text);
                }
            }

            // Clear input fields
            questionField.setText("");
            answerField.setText("");
            opt1.setText(""); opt2.setText(""); opt3.setText(""); opt4.setText("");
            correctMCQAnswer.setText("");
        });

        // Save Button Logic
        saveButton.addActionListener(e -> {
            saveQuestions();
            JOptionPane.showMessageDialog(this, "Questions saved successfully!");
        });

        setVisible(true);
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

    private void saveQuestions() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("questions.ser"))) {
            out.writeObject(questions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}