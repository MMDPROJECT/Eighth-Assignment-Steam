package Server;

import Client.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class QueryDB {
    public static Connection conn;

    static {
        try {
            conn = ConnectionDB.connectDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean usernameExist(String username){
        String query = "SELECT * FROM Accounts WHERE username = ?";
        try {
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, username);
            ResultSet resultSet = psmt.executeQuery();
            //check if username found or not
            return resultSet.next();
        } catch (SQLException sqlException){
            sqlException.getStackTrace();
        }
        return false;
    }

    public static Account accountLogin(String username, String password){
        String query = "SELECT account_id , username, password, date_of_birth FROM Accounts WHERE username = ?";
        try {
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, username);
            ResultSet resultSet = psmt.executeQuery();
            String hashedPass = resultSet.getString("password");
//            return Account.checkPassword(password, hashedPass);
            if (Account.checkPassword(password, hashedPass)){   //compare actual password's hash with provided one
                //username and password is valid
                //Creating account and returning it back
                UUID account_id = UUID.fromString(resultSet.getString("account_id"));
                LocalDate date_of_birth = LocalDate.parse(resultSet.getString("date_of_birth"));
                Account account = new Account(account_id, username, password, date_of_birth);
                return account;
            }
        } catch (SQLException sqlException){
            sqlException.getStackTrace();
        }
        return null;
//        return false;
    }
}
