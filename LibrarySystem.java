import javax.swing.*;
import java.awt.*;
import java.util.*;

class Book {
    int id;
    String title, author; 
    int quantity;

    Book(int id, String title, String author, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }
}

class User {
    String username;
    String password;

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

public class LibrarySystem {
    private static final Map<Integer, Book> bookMap = new HashMap<>();
    private static final Stack<Integer> returnHistory = new Stack<>();
    private static final Map<String, User> users = new HashMap<>();
    private static String currentUser = null;

    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        SwingUtilities.invokeLater(LibrarySystem::loginScreen);
    }

    private static void loginScreen() {
        JFrame loginFrame = new JFrame("📚 Library Login Portal");
        loginFrame.setSize(400, 250);
        loginFrame.setLocationRelativeTo(null); // Center the window
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(null);

        JLabel header = new JLabel("Welcome to the Library System", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBounds(30, 10, 320, 30);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");

        userLabel.setBounds(50, 60, 100, 25);
        userField.setBounds(150, 60, 180, 25);
        passLabel.setBounds(50, 100, 100, 25);
        passField.setBounds(150, 100, 180, 25);
        loginButton.setBounds(50, 150, 80, 30);
        registerButton.setBounds(150, 150, 100, 30);
        exitButton.setBounds(270, 150, 80, 30);

        loginFrame.add(header);
        loginFrame.add(userLabel);
        loginFrame.add(userField);
        loginFrame.add(passLabel);
        loginFrame.add(passField);
        loginFrame.add(loginButton);
        loginFrame.add(registerButton);
        loginFrame.add(exitButton);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            if (users.containsKey(username) && users.get(username).password.equals(password)) {
                currentUser = username;
                loginFrame.dispose();
                mainScreen();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password");
            }
        });

        registerButton.addActionListener(e -> registerNewUser());

        exitButton.addActionListener(e -> System.exit(0));

        loginFrame.setVisible(true);
    }

    private static void registerNewUser() {
        String username = getString("Enter new username:");
        if (users.containsKey(username)) {
            showMsg("Username already exists.");
            return;
        }
        String password = getString("Enter new password:");
        users.put(username, new User(username, password));
        showMsg("User registered successfully.");
    }

    private static void mainScreen() {
        JFrame frame = new JFrame("📘 Library Dashboard - Logged in as: " + currentUser);
        frame.setSize(700, 450);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JTextArea output = new JTextArea();
        output.setBounds(20, 20, 640, 180);
        output.setEditable(false);
        output.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton addBookBtn = new JButton("Add Book");
        JButton updateBookBtn = new JButton("Update Book");
        JButton deleteBookBtn = new JButton("Delete Book");
        JButton displayBooksBtn = new JButton("Display Books");
        JButton searchByIDBtn = new JButton("Search by ID");
        JButton searchByKeywordBtn = new JButton("Search Title/Author");
        JButton issueBookBtn = new JButton("Issue Book");
        JButton returnBookBtn = new JButton("Return Book");
        JButton returnHistoryBtn = new JButton("Return History");
        JButton logoutBtn = new JButton("Logout");

        int y = 220;
        int h = 30;
        addBookBtn.setBounds(20, y, 130, h);
        updateBookBtn.setBounds(160, y, 130, h);
        deleteBookBtn.setBounds(300, y, 130, h);
        displayBooksBtn.setBounds(440, y, 130, h);

        y += 40;
        searchByIDBtn.setBounds(20, y, 130, h);
        searchByKeywordBtn.setBounds(160, y, 170, h);
        issueBookBtn.setBounds(340, y, 120, h);
        returnBookBtn.setBounds(470, y, 130, h);

        y += 40;
        returnHistoryBtn.setBounds(220, y, 160, h);
        logoutBtn.setBounds(550, y, 100, h);

        frame.add(output);
        frame.add(addBookBtn);
        frame.add(updateBookBtn);
        frame.add(deleteBookBtn);
        frame.add(displayBooksBtn);
        frame.add(searchByIDBtn);
        frame.add(searchByKeywordBtn);
        frame.add(issueBookBtn);
        frame.add(returnBookBtn);
        frame.add(returnHistoryBtn);
        frame.add(logoutBtn);

        addBookBtn.addActionListener(e -> {
            int id = getInt("Enter Book ID:");
            if (bookMap.containsKey(id)) {
                showMsg("Book already exists.");
                return;
            }
            String title = getString("Enter Title:");
            String author = getString("Enter Author:");
            int qty = getInt("Enter Quantity:");
            bookMap.put(id, new Book(id, title, author, qty));
            showMsg("Book added.");
        });

        updateBookBtn.addActionListener(e -> {
            int id = getInt("Enter Book ID to update:");
            if (!bookMap.containsKey(id)) {
                showMsg("Book not found.");
                return;
            }
            String title = getString("New Title:");
            String author = getString("New Author:");
            int qty = getInt("New Quantity:");
            bookMap.put(id, new Book(id, title, author, qty));
            showMsg("Book updated.");
        });

        deleteBookBtn.addActionListener(e -> {
            int id = getInt("Enter Book ID to delete:");
            if (bookMap.remove(id) != null) showMsg("Book deleted.");
            else showMsg("Book not found.");
        });

        displayBooksBtn.addActionListener(e -> {
            if (bookMap.isEmpty()) output.setText("No books available.\n");
            else {
                StringBuilder sb = new StringBuilder();
                for (Book b : bookMap.values()) {
                    sb.append("ID: ").append(b.id).append(", Title: ").append(b.title)
                      .append(", Author: ").append(b.author).append(", Qty: ").append(b.quantity).append("\n");
                }
                output.setText(sb.toString());
            }
        });

        searchByIDBtn.addActionListener(e -> {
            int id = getInt("Enter ID:");
            Book b = bookMap.get(id);
            if (b != null)
                showMsg("Found: " + b.title + " by " + b.author + ", Qty: " + b.quantity);
            else showMsg("Book not found.");
        });

        searchByKeywordBtn.addActionListener(e -> {
            String keyword = getString("Enter Title or Author:");
            boolean found = false;
            StringBuilder sb = new StringBuilder();
            for (Book b : bookMap.values()) {
                if (b.title.equalsIgnoreCase(keyword) || b.author.equalsIgnoreCase(keyword)) {
                    sb.append("ID: ").append(b.id).append(", Title: ").append(b.title)
                      .append(", Author: ").append(b.author).append(", Qty: ").append(b.quantity).append("\n");
                    found = true;
                }
            }
            output.setText(found ? sb.toString() : "No match found.");
        });

        issueBookBtn.addActionListener(e -> {
            int id = getInt("Enter Book ID to issue:");
            Book b = bookMap.get(id);
            if (b != null && b.quantity > 0) {
                b.quantity--;
                showMsg("Book issued.");
            } else showMsg("Book unavailable or not found.");
        });

        returnBookBtn.addActionListener(e -> {
            int id = getInt("Enter Book ID to return:");
            Book b = bookMap.get(id);
            if (b != null) {
                b.quantity++;
                returnHistory.push(id);
                showMsg("Book returned.");
            } else showMsg("Book not found.");
        });

        returnHistoryBtn.addActionListener(e -> {
            if (returnHistory.isEmpty()) output.setText("No returns yet.");
            else {
                StringBuilder sb = new StringBuilder("Return History:\n");
                for (int id : returnHistory)
                    sb.append("Book ID: ").append(id).append("\n");
                output.setText(sb.toString());
            }
        });

        logoutBtn.addActionListener(e -> {
            frame.dispose();
            currentUser = null;
            loginScreen();
        });

        frame.setVisible(true);
    }

    private static int getInt(String msg) {
        return Integer.parseInt(JOptionPane.showInputDialog(msg));
    }

    private static String getString(String msg) {
        return JOptionPane.showInputDialog(msg);
    }

    private static void showMsg(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
}
