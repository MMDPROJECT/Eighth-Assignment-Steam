package Client;

import Server.ConnectionDB;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class Account {
    private UUID account_id;
    private String username;
    private String password;
    private LocalDate date_of_birth;

    //Constructor

    public Account(String username, String password, LocalDate date_of_birth) {
        this.account_id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.date_of_birth = date_of_birth;
        insertAccount();
    }

    public Account(UUID account_id, String username, String password, LocalDate date_of_birth) {
        this.account_id = account_id;
        this.username = username;
        this.password = password;
        this.date_of_birth = date_of_birth;
    }

    //Getter and Setters

    public UUID getAccount_id() {
        return account_id;
    }

    public void setAccount_id(UUID account_id) {
        this.account_id = account_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    private void insertAccount(){
        try {
            //Connection and Query to database
            Connection conn = ConnectionDB.connectDB();
            String query = "INSERT INTO Accounts (account_id, username, password, date_of_birth) VALUES(?,?,?,?)";
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, this.account_id.toString());
            psmt.setString(2, this.username);
            psmt.setString(3, hashPassword(password));
            psmt.setString(4, this.date_of_birth.toString());
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static Boolean checkPassword(String password, String hashedPass){
        return BCrypt.checkpw(password, hashedPass);
    }
}
