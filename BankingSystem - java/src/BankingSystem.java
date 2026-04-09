import javax.swing.*;
import java.awt.*;
import java.sql.*;


public class BankingSystem extends JFrame {

    // ===== DATABASE CONFIG =====
    static final String URL = "jdbc:mysql://localhost:3306/banking_app";
    static final String USER = "root";
    static final String PASS = "mysql66";

    JTextField usernameField, amountField;
    JPasswordField passwordField;
    JLabel balanceLabel;

    String loggedUser = null;

    public BankingSystem() {
        setTitle("Banking System");
        setSize(400,300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginUI();
        setVisible(true);
    }


    // ===== LOGIN UI =====
    void loginUI() {
        getContentPane().removeAll();
        setLayout(new GridLayout(4,2));


        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);


        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);


        JButton loginBtn = new JButton("Login");
        add(loginBtn); 
        
        loginBtn.addActionListener(e -> login());

        revalidate();
        repaint();
    }


    // ===== DASHBOARD UI =====

    void dashboardUI(){
        getContentPane().removeAll();
        setLayout(new GridLayout(0,1,10,10));


        balanceLabel = new JLabel();
        updateBalance();
        add(balanceLabel);


        add(new JLabel("Enter Amount:"));   
        amountField = new JTextField();
        amountField.setToolTipText("Enter amount in rupees");
        add(amountField);


        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton logoutButton = new JButton("Logout");


        add(depositButton);
        add(withdrawButton);
        add(logoutButton);


        depositButton.addActionListener(e -> deposit());
        withdrawButton.addActionListener(e -> withdraw());
        logoutButton.addActionListener(e -> {
                loggedUser = null;
                loginUI();
        });

        revalidate();
        repaint();
    }


    // ===== LOGIN =====

    void login() {
        try (Connection con = DriverManager.getConnection(URL,USER,PASS)) {

            String sql = "SELECT * FROM USERS WHERE USERNAME=? AND PASSWORD=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usernameField.getText());
            ps.setString(2, new String(passwordField.getPassword()));


            ResultSet rs  = ps.executeQuery();

            if (rs.next()) {
                loggedUser = usernameField.getText();
                dashboardUI();
            }else{
                JOptionPane.showMessageDialog(this,"Invalid Login");
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ===== BALANCE =====
    void updateBalance() {
        try (Connection con = DriverManager.getConnection(URL,USER,PASS)) {


            PreparedStatement ps = con.prepareStatement("SELECT balance FROM users WHERE username=?");
            ps.setString(1,loggedUser); 

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                balanceLabel.setText("Balance: Rs. " + rs.getDouble(1));
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    // ===== DEPOSIT =====

    void deposit() {
        String input = amountField.getText();

        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,"Please enter amount");
            return;
        }

        double amt;


        try {
            amt = Double.parseDouble(input);
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid number");
            return;
        }


        try (Connection con  = DriverManager.getConnection(URL, USER, PASS)) {

            con.setAutoCommit(false);

            PreparedStatement ps1 =con.prepareStatement("UPDATE users SET balance = balance + ? WHERE username=?");

            ps1.setDouble(1, amt);
            ps1.setString(2,loggedUser);
            ps1.executeUpdate();


            PreparedStatement ps2  = con.prepareStatement("INSERT INTO transactions(username, description) VALUES(?,?)");
            ps2.setString(1, loggedUser);
            ps2.setString(2, "Deposited Rs." + amt);
            ps2.executeUpdate();

            con.commit();

            updateBalance();
            JOptionPane.showMessageDialog(this, "Deposit Successful");
        
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ===== WITHDRAW =====
    
    void withdraw() {
        String input = amountField.getText();

        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter amount");
            return;
        }

        double amt;

        
        try {
            amt = Double.parseDouble(input);
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid number");
            return;
        }


        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            PreparedStatement check = con.prepareStatement("SELECT balance FROM users WHERE username=?");
            check.setString(1, loggedUser);

            ResultSet rs = check.executeQuery();
            rs.next();


            if (rs.getDouble(1) < amt) {
                JOptionPane.showMessageDialog(this, "Insufficient Balance");
                return;
            }

            PreparedStatement ps1 = con.prepareStatement("UPDATE users SET balance = balance - ? WHERE username=?");
            ps1.setDouble(1, amt);
            ps1.setString(2, loggedUser);
            ps1.executeUpdate();


            PreparedStatement ps2 = con.prepareStatement("INSERT INTO transactions(username, description) VALUES(?,?)");
            ps2.setString(1,loggedUser);
            ps2.setString(2, "Withdrawn Rs." + amt);
            ps2.executeUpdate();

            updateBalance();
            JOptionPane.showMessageDialog(this, "Withdrawal Successful");
        
        
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //===== MAIN =====

    public static void main(String[] args) {
        new BankingSystem();
    }
}