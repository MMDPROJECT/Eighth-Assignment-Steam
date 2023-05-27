package Server;

import Client.Account;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class QueryDB {
    public static Connection conn;

    static {
        try {
            //Establishing connection to database
            conn = ConnectionDB.connectDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonObject selectDownloadedGames(String account_id){
        JsonObject rows = new JsonObject();

        //Query to database
        String query = "SELECT * FROM Downloads WHERE account_id = ?";
        try {
            Connection conn = ConnectionDB.connectDB();
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, account_id);
            ResultSet resultSet = psmt.executeQuery();
            int counter = 1;
            while (resultSet.next()){
                //Storing each row as JsonArray
                JsonArray row = new JsonArray();
                for (int i = 1; i <= 3; i++){
                    row.add(resultSet.getString(i));
                }

                //Adding each row to rows
                rows.add("row" + counter, row);

                //Counting rows
                counter++;
            }

            //Adding row and column counts to the Json
            rows.addProperty("rowCount", counter - 1);
            rows.addProperty("columnCount", 3);
            return rows;
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return null;
    }

    public static JsonObject selectAllGames(){
        //Locked
        JsonObject rows = new JsonObject();

        //Query to database
        String query = "SELECT * FROM Games";
        try {
            Connection conn = ConnectionDB.connectDB();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            int counter = 1;
            while (resultSet.next()){
                //Storing each row as JsonArray
                JsonArray row = new JsonArray();
                for (int i = 1; i <= 10; i++){
                    row.add(resultSet.getString(i));
                }

                //Adding each row to rows
                rows.add("row" + counter, row);

                //Counting rows
                counter++;
            }

            //Adding row and column counts to the Json
            rows.addProperty("rowCount", counter - 1);
            rows.addProperty("columnCount", 10);
            return rows;
        } catch (SQLException sqlException){
            sqlException.getStackTrace();
        }
        return null;
        //Unlock
    }

    public static JsonObject selectSpecificGame(String game_id){
        //Query to database
        try {
            Connection conn = ConnectionDB.connectDB();
            String query = "SELECT * FROM Games WHERE game_id = ?";
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, game_id);
            ResultSet resultSet = psmt.executeQuery();
//            if (resultSet.next()){
                JsonObject result = new JsonObject();
                JsonArray row = new JsonArray();
                for (int i = 1; i <= 10; i++){
                    row.add(resultSet.getString(i));
                }
                result.addProperty("rowCount", 1);
                result.addProperty("columnCount", 10);
                result.add("row1", row);
                return result;
//            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        return null;
    }

    public static void insertFileToDB(ArrayList<String> arr, String pngPath){
        //getting items from array
        String game_id = arr.get(0);
        String title = arr.get(1);
        String developer = arr.get(2);
        String genre = arr.get(3);
        double price = Double.parseDouble(arr.get(4));
        int release_year = Integer.parseInt(arr.get(5));
        boolean controller_support = Boolean.parseBoolean(arr.get(6));
        int reviews = Integer.parseInt(arr.get(7));
        int size = Integer.parseInt(arr.get(8));
        pngPath = pngPath.replaceAll("txt", "png");

        //Query to database
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean usernameExist(String username){
        //Query to database
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
        //Query to database
        String query = "SELECT account_id , username, password, date_of_birth FROM Accounts WHERE username = ?";
        try {
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, username);
            ResultSet resultSet = psmt.executeQuery();
            String hashedPass = resultSet.getString("password");
            if (Account.checkPassword(password, hashedPass)){   //compare actual password's hash with provided one
                //username and password is valid
                //Creating account and returning it back
                //Parsing result set
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

    public static String selectPNGPath(String game_id){
        //Query to database
        String query = "SELECT file_path FROM Games WHERE game_id = ?";
        try {
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, game_id);
            ResultSet resultSet = psmt.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("file_path");
            }
        } catch (SQLException sqlException){
            sqlException.getStackTrace();
        }
        return null;
    }

    public static void insertToDownloads(String account_id, String game_id){
        //Query to database
        String query = "INSERT INTO Downloads (account_id, game_id, download_count) VALUES (?,?,?)";
        try {
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, account_id);
            psmt.setString(2, game_id);
            psmt.setInt(3, 1);
            psmt.executeUpdate();
        } catch (SQLException sqlException){
            sqlException.getStackTrace();
        }
    }

    public static boolean hasDownloadedGame(String account_id, String game_id){
        //Query to database
        String query = "SELECT download_count FROM Downloads WHERE account_id = ? AND game_id = ?";
        try {
            PreparedStatement psmt = conn.prepareStatement(query);
            psmt.setString(1, account_id);
            psmt.setString(2, game_id);
            ResultSet resultSet = psmt.executeQuery();
            if (resultSet.next()){
                return true;
            }
        } catch (SQLException sqlException){
            sqlException.getStackTrace();
        }
        return false;
    }

    public static void downloadGame(String account_id, String game_id){
        //Query to database
        String query = "UPDATE Downloads SET download_count = download_count + 1 WHERE account_id = " + account_id + " AND game_id = " + game_id;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException sqlException){
            sqlException.getStackTrace();
        }
    }
}
