package src;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;


class Customer{
    public void customerPanel(int accountNo, Scanner scan){
        String url = "jdbc:mysql://localhost:3306/bank_db";
        String dbUsername = "root";
        String dbPassword = "********";
        ATM atm = new ATM();
        Main main = new Main();
        try{
            Connection con = DriverManager.getConnection(url, dbUsername, dbPassword);
            while (true) {
                System.out.println("===== Bank Management =====");
                System.out.println("1. Check the balance.");
                System.out.println("2. Deposit Money.");
                System.out.println("3. Transfer Money.");
                System.out.println("4. Change PIN.");
                System.out.println("5. Check History.");
                System.out.println("6. eKYC.");
                System.out.println("7. Loggout.");
                System.out.println("8. Close Account.");
                System.out.println("9. Exit.");
                System.out.print("Select the option (1-9): ");
                int choice = scan.nextInt();
                if(choice == 1){
                    PreparedStatement ps = con.prepareStatement("SELECT balance,status FROM accounts WHERE account_no=?");
                    ps.setInt(1,accountNo);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        System.out.println("Current Balance : $"+rs.getDouble("balance"));
                        System.out.println("Account Status : "+rs.getString("status"));
                    }else{
                        System.out.println("Account not found");
                    }
                }else if (choice == 2){
                    System.out.print("Enter Amount : $");
                    double amount = scan.nextDouble();
                    PreparedStatement update = con.prepareStatement("UPDATE accounts SET balance=balance+? WHERE account_no=?");
                    update.setDouble(1,amount);
                    update.setInt(2,accountNo);
                    int result = update.executeUpdate();
                    if(result>0){
                        PreparedStatement ps = con.prepareStatement("INSERT INTO transactions(account_no,transaction_type,amount,description) VALUES(?,?,?,?)");
                        ps.setInt(1,accountNo);
                        ps.setString(2,"Deposit");
                        ps.setDouble(3,amount);
                        ps.setString(4,"Cash Deposit");
                        ps.executeUpdate();
                        System.out.println("Deposit Successful");
                    }else{
                        System.out.println("Deposit Failed");
                    }
                }else if (choice == 3){
                    System.out.print("Enter Receiver Account Number : ");
                    int receiver = scan.nextInt();
                    System.out.print("Enter Amount : $");
                    double amount = scan.nextDouble();
                    PreparedStatement check = con.prepareStatement("SELECT balance FROM accounts WHERE account_no=?");
                    check.setInt(1, accountNo);
                    ResultSet rs = check.executeQuery();
                    if(rs.next()){
                        double balance = rs.getDouble("balance");
                        if(balance >= amount){
                            PreparedStatement debit = con.prepareStatement("UPDATE accounts SET balance=balance-? WHERE account_no=?");
                            debit.setDouble(1, amount);
                            debit.setInt(2, accountNo);
                            debit.executeUpdate();
                            PreparedStatement credit = con.prepareStatement("UPDATE accounts SET balance=balance+? WHERE account_no=?");
                            credit.setDouble(1,amount);
                            credit.setInt(2,receiver);
                            int r = credit.executeUpdate();
                            if(r>0){
                                PreparedStatement ps = con.prepareStatement("INSERT INTO transactions(account_no,transaction_type,amount,description) VALUES(?,?,?,?)");
                                ps.setInt(1, accountNo);
                                ps.setString(2,"Transfer");
                                ps.setDouble(3,amount);
                                ps.setString(4,"Transferred to Account "+receiver);
                                ps.executeUpdate();
                                System.out.println("Transfer Successful");
                            }
                        }else{
                            System.out.println("Insufficient Balance");
                        }
                    }
                }else if (choice == 4){
                    System.out.print("Enter New PIN : ");
                    int pin = scan.nextInt();
                    PreparedStatement ps = con.prepareStatement("UPDATE accounts SET pin=? WHERE account_no=?");
                    ps.setInt(1,pin);
                    ps.setInt(2,accountNo);
                    ps.executeUpdate();
                    System.out.println("PIN Changed Successfully");
                }else if (choice == 5){
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM transactions WHERE account_no=? ORDER BY transaction_date DESC");
                    ps.setInt(1,accountNo);
                    ResultSet rs = ps.executeQuery();
                    while(rs.next()){
                        System.out.println("----------------");
                        System.out.println("Transaction ID : "+rs.getInt("transaction_id"));
                        System.out.println("Type : "+rs.getString("transaction_type"));
                        System.out.println("Amount : $"+rs.getDouble("amount"));
                        System.out.println("Description : "+rs.getString("description"));
                        System.out.println("Date : "+rs.getTimestamp("transaction_date"));
                    }
                }else if (choice == 6){
                    System.out.print("Enter Aadhar Number : ");
                    String aadhar = scan.next();
                    PreparedStatement ps = con.prepareStatement("UPDATE customers SET aadhar_no=?, kyc_status='Approved' WHERE customer_id=(SELECT customer_id FROM accounts WHERE account_no=?)");
                    ps.setString(1,aadhar);
                    ps.setInt(2,accountNo);
                    ps.executeUpdate();
                    System.out.println("eKYC Completed Successfully");
                }else if (choice == 7){
                    System.out.println("Logout Successful");
                        con.close();
                        return;
                }else if (choice == 8){
                    PreparedStatement ps = con.prepareStatement("UPDATE accounts SET status='CLOSED' WHERE account_no=?");
                    ps.setInt(1,accountNo);
                    ps.executeUpdate();
                    System.out.println("Account Closed Successfully");
                }else if (choice == 9){
                    System.out.println("Thank you for using Bank Management System.");
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

class Admin{
    public void adminPanel(Scanner scan){
        String url = "jdbc:mysql://localhost:3306/bank_db";
        String dbUsername = "root";
        String dbPassword = "********";
        ATM atm = new ATM();
        Main main = new Main();
        try{
            Connection con = DriverManager.getConnection(url, dbUsername, dbPassword);
            while (true) {
                System.out.println("===== Bank Management =====");
                System.out.println("1. View All Customers");
                System.out.println("2. Add New Customer");
                System.out.println("3. Remove Customer");
                System.out.println("4. View All Accounts");
                System.out.println("5. Approve Loans");
                System.out.println("6. Block Account");
                System.out.println("7. View Transactions");
                System.out.println("8. Generate Reports");
                System.out.println("9. Logout");
                System.out.println("10. Close Account");
                System.out.print("Select the option (1-10): ");
                int choice = scan.nextInt();
                if(choice == 1){
                    String query ="SELECT * FROM customers";
                    PreparedStatement ps =con.prepareStatement(query);
                    ResultSet rs =ps.executeQuery();
                    boolean found = false;
                    while(rs.next()){
                        found = true;
                        System.out.println("--------------------");
                        System.out.println("Customer ID : "+ rs.getInt("customer_id"));
                        System.out.println("Name : "+ rs.getString("name"));
                        System.out.println("Email : "+ rs.getString("email"));
                        System.out.println("Phone : "+ rs.getString("phone"));
                        System.out.println("KYC : "+ rs.getString("kyc_status"));
                    }
                    if (!found) {
                        System.out.println("No customers found.");
                    }
                }else if (choice == 2) {
                    scan.nextLine();
                    System.out.print("Enter Name : ");
                    String name = scan.nextLine();
                    System.out.print("Enter Email : ");
                    String email = scan.nextLine();
                    System.out.print("Enter Phone : ");
                    String phone = scan.nextLine();
                    System.out.print("Enter Address : ");
                    String address = scan.nextLine();
                    String query ="INSERT INTO customers(name,email,phone,address,kyc_status) VALUES(?,?,?,?,?)";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1,name);
                    ps.setString(2,email);
                    ps.setString(3,phone);
                    ps.setString(4,address);
                    ps.setString(5,"Pending");
                    ps.executeUpdate();
                    System.out.println("Customer Added Successfully");
                }else if (choice == 3) {
                    System.out.print("Enter Customer ID : ");
                    int id = scan.nextInt();
                    PreparedStatement ps =
                    con.prepareStatement("DELETE FROM customers WHERE customer_id=?");
                    ps.setInt(1,id);
                    int result = ps.executeUpdate();
                    if(result>0) System.out.println("Customer Removed");
                    else System.out.println("Customer Not Found");
                }else if (choice == 4) {
                    PreparedStatement ps =con.prepareStatement("SELECT * FROM accounts");
                    ResultSet rs =ps.executeQuery();
                    while(rs.next()){
                        System.out.println("----------------");
                        System.out.println("Account No : "+ rs.getInt("account_no"));
                        System.out.println("Type : "+ rs.getString("account_type"));
                        System.out.println("Balance : $"+ rs.getDouble("balance"));
                        System.out.println("Status : "+ rs.getString("status"));
                    }
                }else if (choice == 5) {
                    System.out.print("Enter Loan ID : ");
                    int id =scan.nextInt();
                    PreparedStatement ps =con.prepareStatement("UPDATE loans SET loan_status='APPROVED' WHERE loan_id=?");
                    ps.setInt(1,id);
                    int result =ps.executeUpdate();
                    if(result>0) System.out.println("Loan Approved");
                    else System.out.println("Loan Not Found");
                }else if (choice == 6) {
                    System.out.print("Enter Account Number : ");
                    int acc =scan.nextInt();
                    PreparedStatement ps =con.prepareStatement("UPDATE accounts SET status='BLOCKED' WHERE account_no=?");
                    ps.setInt(1,acc);
                    ps.executeUpdate();
                    System.out.println("Account Blocked");
                }else if (choice == 7) {
                    System.out.print("Enter Account Number: ");
                    int accountNo = scan.nextInt();
                    PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM transactions WHERE account_no=? ORDER BY transaction_date DESC"
                    );
                    ps.setInt(1, accountNo);
                    ResultSet rs = ps.executeQuery();
                    boolean found = false;
                    while(rs.next()){
                        found = true;
                        System.out.println("----------------------");
                        System.out.println("Transaction ID : "+ rs.getInt("transaction_id"));
                        System.out.println("Type : "+ rs.getString("transaction_type"));
                        System.out.println("Amount : ₹"+ rs.getDouble("amount"));
                        System.out.println("Description : "+ rs.getString("description"));
                        System.out.println("Date : "+ rs.getTimestamp("transaction_date"));
                    }
                    if(!found){
                        System.out.println(
                        "No transaction found for this account.");
                    }
                    ps.close();
                }else if(choice == 8){
                    Statement st = con.createStatement();
                    ResultSet rs =st.executeQuery("SELECT COUNT(*) FROM customers");
                    rs.next();
                    System.out.println("Total Customers : "+rs.getInt(1));
                    rs =st.executeQuery("SELECT COUNT(*) FROM accounts");
                    rs.next();
                    System.out.println("Total Accounts : "+rs.getInt(1));
                    rs = st.executeQuery("SELECT SUM(balance) FROM accounts");
                    rs.next();
                    System.out.println("Total Bank Balance : $"+rs.getDouble(1));
                }else if(choice == 9){
                    System.out.println("Admin Logout Successful");
                        con.close();
                        return;
                }else if(choice == 10){
                    System.out.print("Enter Account Number : ");
                    int acc =scan.nextInt();
                    PreparedStatement ps =con.prepareStatement("UPDATE accounts SET status='CLOSED' WHERE account_no=?");
                    ps.setInt(1,acc);
                    ps.executeUpdate();
                    System.out.println("Account Closed");
                }else if (choice == 11) {
                    System.out.println("Thank you for using Bank Management System.");
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

public class Bank {

}
