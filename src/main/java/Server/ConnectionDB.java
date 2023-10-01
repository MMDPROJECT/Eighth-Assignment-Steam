package Server;

import java.sql.*;

public class ConnectionDB {
    public static Connection connectDB() throws SQLException{
        //SQLite connection string
        String url = "jdbc:sqlite:SteamDB.db";
        Connection conn = null;
        try {
            Connection connection = DriverManager.getConnection(url);
            return connection;
//            System.out.println("Connection to database has been established!\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
