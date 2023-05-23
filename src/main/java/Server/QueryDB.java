package Server;

import Client.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public static void insertFileToDB(ArrayList<String> arr, String pngPath){
        String game_id = arr.get(0);
        String title = arr.get(1);
        String developer = arr.get(2);
        String genre = arr.get(3);
        float price = Float.parseFloat(arr.get(4));
        int release_year = Integer.parseInt(arr.get(5));
        boolean controller_support = Boolean.parseBoolean(arr.get(6));
        int reviews = Integer.parseInt(arr.get(7));
        int size = Integer.parseInt(arr.get(8));
        System.out.println("inserting" + arr);
        try {
            String query = "INSERT INTO Games (game_id, title, developer, genre, price, release_year, controller_support, reviews, size, file_path) VALUES (?,?,?,?,?,?,?,?,?,?)";
            Connection conn = ConnectionDB.connectDB();
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, game_id);
            psmt.setString(2, title);
            psmt.setString(3, developer);
            psmt.setString(4, genre);
            psmt.setDouble(5, price);
            psmt.setInt(6, release_year);
            psmt.setBoolean(7, controller_support);
            psmt.setInt(8, reviews);
            psmt.setInt(9, size);
            psmt.setString(10, pngPath);
            psmt.executeUpdate();
            System.out.println("inserted " + pngPath);

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
    }
}
