import javax.swing.*; //a package that is used to create GUI interface
import java.awt.*;

public class LoginScreen extends JFrame {

    //Creating input fields and a dropdown menu (Jcombobox)
    private JTextField nameField;
    private JTextField idField;
    private JComboBox<String> roleBox;


    //Constructor
    public LoginScreen() {

//the setters from JFrame class to set the title,size ,layout and closing of window where Exit on close is a constant that closes the application n window closing
        setTitle("Online Exam Portal - Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        // Create or Add components
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField();

        JLabel roleLabel = new JLabel("Role:");
        roleBox = new JComboBox<>(new String[]{"Student", "Teacher"});

        //button
        JButton loginButton = new JButton("Login");

        // Action listener for login button
        loginButton.addActionListener(e -> {
            String name = nameField.getText();
            String id = idField.getText();
            String role = (String) roleBox.getSelectedItem();

            if (role.equals("Student")) {
                new StudentExamWindow(name, id);  // Opens Student Exam Window
                dispose(); // Close the login window
            } else if (role.equals("Teacher")) {
                // Here we can simulate the Teacher login with basic hardcoded credentials
                String password = JOptionPane.showInputDialog(this, "Enter Teacher Password:");

                // Simulating teacher login with username and password
                if (password != null && password.equals("teacher123")) {
                    new TeacherWindow(name);  // Opens Teacher Management Window
                    dispose();  // Close the login window
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid password. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add components to frame
        add(nameLabel);
        add(nameField);

        add(idLabel);
        add(idField);

        add(roleLabel);
        add(roleBox);

        add(new JLabel()); // Empty cell to align the button
        add(loginButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginScreen();  // Start the Login Screen
    }
}