package src;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ATM{
    private double Amount;
    public void initialAmount(double value){
        this.Amount = value;
    }
    public double balance(){
        return Amount;
    }
    public boolean deposit(double value) {
        if(value > 0){
            Amount += value;
            return true;
        }
        System.out.println("Invalid amount.");
        return false;
    }
    public boolean withdraw(double value) {
        if (value <= 0) {
            System.out.println("Invalid amount.");
            return false;
        }
        if (value > Amount) {
            System.out.println("Insufficient balance.");
            return false;
        }
        Amount -= value;
        return true;
    }
    public boolean fastCash(double value) {
        if (value <= 0) {
            System.out.println("Invalid amount.");
            return false;
        }
        if (value > Amount) {
            System.out.println("Insufficient balance.");
            return false;
        }
        Amount -= value;
        return true;
    }

    public void start(Scanner scan){
        String url = "jdbc:mysql://localhost:3306/bank_db";
        String dbUsername = "root";
        String dbPassword = "********";
        ATM obj = new ATM();
        Bank bank = new Bank();
        Main main = new Main();
        try {
            Connection con = DriverManager.getConnection(url, dbUsername, dbPassword);
            System.out.print("Enter your Account number: ");
            int accNum = scan.nextInt();
            PreparedStatement pstmt = con.prepareStatement("SELECT pin FROM accounts WHERE account_no = ?");
            pstmt.setInt(1, accNum);
            ResultSet rese = pstmt.executeQuery();
            if(rese.next()){
                int dbPin = rese.getInt("pin");
                System.out.print("Enter your PIN: ");
                int enteredPin = scan.nextInt();
                if(enteredPin != dbPin){
                    System.out.println("Incorrect PIN.");
                    rese.close();
                    pstmt.close();
                    con.close();
                    return;
                }
                System.out.println("PIN verified successfully.");
                PreparedStatement pstmt2 = con.prepareStatement("SELECT balance FROM accounts WHERE account_no = ?");
                pstmt2.setInt(1, accNum);
                ResultSet rese2 = pstmt2.executeQuery();
                if(rese2.next()){
                    double balance = rese2.getDouble("balance");
                    obj.initialAmount(balance);
                }
                rese2.close();
                pstmt2.close();
            }else{
                System.out.println("Account number not found.");
            }
            rese.close();
            pstmt.close();
        
            while (true) {
                System.out.println("===== ATM Machine =====");
                System.out.println("1. Deposit the money.");
                System.out.println("2. Withdraw the money.");
                System.out.println("3. Check the balance.");
                System.out.println("4. Transaction History.");
                System.out.println("5. Change PIN.");
                System.out.println("6. Fast Cash.");
                System.out.println("7. Back.");
                System.out.println("8. Exit.");
                System.out.print("Select the option (1-8) :");
                int choice = scan.nextInt();
                if (choice == 1) {
                    System.out.print("How much money would you like to deposit : ₹");
                    double value = scan.nextDouble();
                    if (obj.deposit(value)) {
                        String info = "Deposited: ₹" + value;
                        PreparedStatement ps = con.prepareStatement("INSERT INTO data(Information) VALUES(?)");
                        ps.setString(1, info);
                        ps.executeUpdate();
                        System.out.println("Deposit successful.");
                        System.out.println("Current Balance: ₹" + obj.balance());
                        ps.close();
                    }
                } else if (choice == 2) {
                    System.out.print("How much money would you like to Withdraw : ₹");
                    double value = scan.nextDouble();
                    if (obj.withdraw(value)) {
                        String info = "Withdrawn: ₹" + value;
                        PreparedStatement ps = con.prepareStatement("INSERT INTO data(Information) VALUES(?)");
                        ps.setString(1, info);
                        ps.executeUpdate();
                        System.out.println("Withdrawal successful.");
                        System.out.println("Available balance: ₹" + obj.balance());
                        ps.close();
                    }
                } else if (choice == 3) {
                    System.out.println("Available balance : ₹" + obj.balance());
                } else if (choice == 4) {
                    PreparedStatement ps = con.prepareStatement(
                            "SELECT * FROM data ORDER BY id");

                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        System.out.println("No transactions found.");
                    } else {

                        do {
                            System.out.println(rs.getInt("id") + ". "
                                    + rs.getString("Information"));
                        } while (rs.next());
                    }
                    rs.close();
                    ps.close();
                } else if (choice == 5) {
                    System.out.print("Enter new PIN: ");
                    int newPin = scan.nextInt();
                    System.out.print("Confirm new PIN: ");
                    int confirmPin = scan.nextInt();
                    if(newPin != confirmPin){
                        System.out.println("PINs do not match.");
                    }else{
                        PreparedStatement ps = con.prepareStatement("UPDATE accounts SET PIN = ? WHERE account_no = ?");
                        ps.setInt(1, newPin);
                        ps.setInt(2, accNum);
                        int rows = ps.executeUpdate();
                        if(rows > 0){
                            System.out.println("PIN updated successfully.");
                        }else{
                            System.out.println("Failed to update PIN.");
                        }
                        ps.close();
                    }
                } else if (choice == 6) {
                    System.out.println("Fast Cash.....");
                    System.out.print("How much money would you like to Fast cash : ₹");
                    double value = scan.nextDouble();
                    if (obj.fastCash(value)) {
                        String info = "Fast Cash: ₹" + value;
                        PreparedStatement ps = con.prepareStatement("INSERT INTO data(Information) VALUES(?)");
                        ps.setString(1, info);
                        ps.executeUpdate();
                        System.out.println("Fast cash successful.");
                        System.out.println("Available balance: ₹" + obj.balance());
                        ps.close();
                    }
                }else if(choice == 7){
                    return;
                } else if(choice == 8){
                    System.out.println("Thank you for using ATM.");
                    con.close();
                    return;
                }else{
                    System.out.println("Invalid Choice.");
                }
            }
        }catch(SQLException e){
            System.out.println("Database Error : " + e.getMessage());
        }
    }
}