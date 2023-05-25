package Shared;

import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Request {
    public static void sign_up_req(Socket clientSocket, JsonObject jsonRequest) {
        PrintWriter out = null;
        try {
            //Sending request to server
            out = new PrintWriter(clientSocket.getOutputStream());
            out.println(jsonRequest);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
//            System.out.println("Before closing out " + socket.isClosed());
//            out.close();
//            System.out.println("After closing out " + socket.isClosed());
        }
    }
    public static void sign_in_req(Socket clientSocket, JsonObject jsonRequest){
        PrintWriter out = null;
        try {
            //Sending request to server
            out = new PrintWriter(clientSocket.getOutputStream());
            out.println(jsonRequest);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
//            System.out.println("Before closing out " + socket.isClosed());
//            out.close();
//            System.out.println("After closing out " + socket.isClosed());
        }
    }

    public static void show_all_games_req(Socket clientSocket, JsonObject jsonRequest){
        PrintWriter out = null;
        try {
            //Sending request to server
            out = new PrintWriter(clientSocket.getOutputStream());
            out.println(jsonRequest);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
//            System.out.println("Before closing out " + socket.isClosed());
//            out.close();
//            System.out.println("After closing out " + socket.isClosed());
        }
    }

    public static void show_specific_game_req(Socket clientSocket, JsonObject jsonRequest){
        PrintWriter out = null;
        try {
            //Sending request to server
            out = new PrintWriter(clientSocket.getOutputStream());
            out.println(jsonRequest);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
//            System.out.println("Before closing out " + socket.isClosed());
//            out.close();
//            System.out.println("After closing out " + socket.isClosed());
        }
    }

    public static void show_downloaded_games_req(Socket clientSocket, JsonObject jsonRequest){
        PrintWriter out = null;
        try {
            //Sending request to server
            out = new PrintWriter(clientSocket.getOutputStream());
            out.println(jsonRequest);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
//            System.out.println("Before closing out " + socket.isClosed());
//            out.close();
//            System.out.println("After closing out " + socket.isClosed());
        }
    }

    public static void download_game_req(Socket clientSocket, JsonObject jsonRequest){
        PrintWriter out = null;
        try {
            //Sending request to server
            out = new PrintWriter(clientSocket.getOutputStream());
            out.println(jsonRequest);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
//            System.out.println("Before closing out " + socket.isClosed());
//            out.close();
//            System.out.println("After closing out " + socket.isClosed());
        }
    }
}
