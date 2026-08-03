package src;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bank_db";
        String dbUsername = "root";
        String dbPassword = "********";
        Scanner scan = new Scanner(System.in);
        ATM atm = new ATM();
        Bank bank = new Bank();
        Main main = new Main();
        try {
            Connection con = DriverManager.getConnection(url, dbUsername, dbPassword);
            while (true) {
                System.out.println("===== Bank Management =====");
                System.out.println("1. Register.");
                System.out.println("2. Create Account.");
                System.out.println("3. Login.");
                System.out.println("4. ATM.");
                System.out.println("5. Close Account.");
                System.out.println("6. Exit.");
                System.out.print("Select the option (1-9): ");
                int choice = scan.nextInt();
                if(choice == 1){
                    scan.nextLine();
                    System.out.print("Enter Name : ");
                    String name = scan.nextLine();
                    System.out.print("Enter Email : ");
                    String email = scan.nextLine();
                    System.out.print("Enter Phone : ");
                    String phone = scan.nextLine();
                    System.out.print("Enter Address : ");
                    String address = scan.nextLine();
                    System.out.println("1. Customer");
                    System.out.println("2. Admin");
                    System.out.print("Select Registration Type : ");
                    int type = scan.nextInt();
                    if(type == 1){
                        PreparedStatement ps = con.prepareStatement("INSERT INTO customers(name,email,phone,address,kyc_status) VALUES(?,?,?,?,?)");
                        ps.setString(1,name);
                        ps.setString(2,email);
                        ps.setString(3,phone);
                        ps.setString(4,address);
                        ps.setString(5,"Pending");
                        ps.executeUpdate();
                        System.out.println("Customer Registered Successfully");
                        System.out.println("Now Create Account");
                    }
                    else if(type == 2){
                        scan.nextLine();
                        System.out.print("Create Username : ");
                        String username = scan.nextLine();
                        System.out.print("Create Password : ");
                        String password = scan.nextLine();
                        PreparedStatement ps = con.prepareStatement("INSERT INTO admins(username,password,name,email) VALUES(?,?,?,?)");
                        ps.setString(1,username);
                        ps.setString(2,password);
                        ps.setString(3,name);
                        ps.setString(4,email);
                        ps.executeUpdate();
                        System.out.println("Admin Registered Successfully");
                    }
                }else if (choice == 2){
                    System.out.print("Enter Customer ID : ");
                    int customerId = scan.nextInt();
                    System.out.println("1. Savings Account");
                    System.out.println("2. Current Account");
                    System.out.println("3. Fixed Deposit Account");
                    System.out.print("Select Account Type : ");
                    int type = scan.nextInt();
                    String accountType="";
                    if(type==1) accountType="Savings";
                    else if(type==2) accountType="Current";
                    else if(type==3) accountType="Fixed Deposit";
                    else{
                        System.out.println("Invalid Account Type");
                        return;
                    }
                    System.out.print("Create PIN : ");
                    int pin = scan.nextInt();
                    int accountNumber = (int)(Math.random()*900000)+100000;
                    PreparedStatement ps =con.prepareStatement("INSERT INTO accounts(account_no,customer_id,account_type,balance,pin,status) VALUES(?,?,?,?,?,?)");
                    ps.setInt(1,accountNumber);
                    ps.setInt(2,customerId);
                    ps.setString(3,accountType);
                    ps.setDouble(4,0);
                    ps.setInt(5,pin);
                    ps.setString(6,"ACTIVE");
                    ps.executeUpdate();
                    System.out.println("Account Created Successfully");
                    System.out.println("Your Account Number : "+accountNumber);
                }else if (choice == 3) {
                    scan.nextLine();
                    System.out.println("1. Customer Login");
                    System.out.println("2. Admin Login");
                    System.out.print("Select Login Type : ");
                    int type = scan.nextInt();
                    if(type==1){
                        System.out.print("Enter Account Number : ");
                        int account = scan.nextInt();
                        System.out.print("Enter PIN : ");
                        int pin = scan.nextInt();
                        PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE account_no=? AND pin=? AND status='ACTIVE'");
                        ps.setInt(1,account);
                        ps.setInt(2,pin);
                        ResultSet rs = ps.executeQuery();
                        if(rs.next()){
                            System.out.println("Customer Login Successful");
                            Customer customer = new Customer();
                            customer.customerPanel(account, scan);
                        }else{
                            System.out.println("Invalid Account Number or PIN");
                        }
                    }
                    else if(type==2){
                        scan.nextLine();
                        System.out.print("Username : ");
                        String username = scan.nextLine();
                        System.out.print("Password : ");
                        String password = scan.nextLine();
                        PreparedStatement ps = con.prepareStatement("SELECT * FROM admins WHERE username=? AND password=?");
                        ps.setString(1,username);
                        ps.setString(2,password);
                        ResultSet rs = ps.executeQuery();
                        if(rs.next()){
                            System.out.println("Admin Login Successful");
                            Admin admin = new Admin();
                            admin.adminPanel(scan);
                        }else{
                            System.out.println("Wrong Admin Credentials");
                        }
                    }
                }else if (choice == 4) {
                    atm.start(scan);
                }else if (choice == 5) {
                    System.out.print("Enter Account Number : ");
                    int account = scan.nextInt();
                    PreparedStatement ps = con.prepareStatement("UPDATE accounts SET status='CLOSED' WHERE account_no=?");
                    ps.setInt(1,account);
                    int result = ps.executeUpdate();
                    if(result>0) System.out.println("Account Closed Successfully");
                    else System.out.println("Account Not Found");
                }else if (choice == 6) {
                    System.out.println("Thank you for using Bank Management System.");
                    con.close();
                    scan.close();
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
