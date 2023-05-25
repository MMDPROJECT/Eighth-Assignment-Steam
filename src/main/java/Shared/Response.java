package Shared;

import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Response {
    public static void sign_up_res(Socket serverSocket, Boolean flag){    //flag == false means account can be created, flag == true means account already exists.
        PrintWriter out = null;
        try {
            //Connecting to output stream
            OutputStream outputStream = serverSocket.getOutputStream();
            out = new PrintWriter(outputStream);

            //Json
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("responseType", "SIGN UP");
            if (!flag){
                jsonResponse.put("response", "ACCOUNT HAS BEEN SUCCESSFULLY SIGNED UP");
            } else {
                jsonResponse.put("response", "THIS USERNAME ALREADY EXISTS");
            }

            //Sending json object over socket
            System.out.println("SENDING: " + jsonResponse);
            out.println(jsonResponse);
            out.flush();
        }
        catch (IOException io){
            io.printStackTrace();
        }
//        finally {
//            assert out != null;
//            out.close();
//        }
    }

    public static void sign_in_res(Socket serverSocket, Boolean flag, JSONObject jsonObject){    //flag == false means username or password is wrong, flag == true means user successfully logged in
        PrintWriter out = null;
        try {
            //Sending json object over socket
            OutputStream outputStream = serverSocket.getOutputStream();
            out = new PrintWriter(outputStream);

            //Json
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("responseType", "SIGN IN");
            if (flag){
                jsonResponse.put("response", "VALID LOGIN");
                jsonResponse.put("Account", jsonObject);
            } else {
                jsonResponse.put("response", "INVALID LOGIN");
            }

            //Sending json object over the socket
            System.out.println("SENDING: " + jsonResponse);
            out.println(jsonResponse);
            out.flush();
        }
        catch (IOException io){
            io.printStackTrace();
        }
//        finally {
//            assert out != null;
//            out.close();
//        }
    }

    public static void show_all_game_res(Socket serverSocket, JsonObject jsonResponse){
        PrintWriter out = null;
        try {
            //Connecting to output stream
            OutputStream outputStream = serverSocket.getOutputStream();
            out = new PrintWriter(outputStream);

            //Json
            jsonResponse.addProperty("response", "SELECTED ALL AVAILABLE Games");

            //Sending json object over the socket
            System.out.println("SENDING: " + jsonResponse);
            out.println(jsonResponse);
            out.flush();
        }
        catch (IOException io){
            io.printStackTrace();
        }
//        finally {
//            assert out != null;
//            out.close();
//        }
    }

    public static void show_specified_game_res(Socket serverSocket, Boolean flag, JsonObject jsonResponse){
        PrintWriter out = null;
        try {
            //Connecting to output stream
            OutputStream outputStream = serverSocket.getOutputStream();
            out = new PrintWriter(outputStream);

            //Json
            jsonResponse.addProperty("response", "SELECTED SPECIFIED GAME");

            //Sending json object over the socket
            System.out.println("SENDING: " + jsonResponse);
            out.println(jsonResponse);
            out.flush();
        }
        catch (IOException io){
            io.printStackTrace();
        }
//        finally {
//            assert out != null;
//            out.close();
//        }
    }

    public static void show_downloaded_games_res(Socket serverSocket, Boolean flag, JsonObject jsonResponse){
        PrintWriter out = null;
        try {
            //Connecting to output stream
            OutputStream outputStream = serverSocket.getOutputStream();
            out = new PrintWriter(outputStream);

            //Json
            jsonResponse.addProperty("response", "SELECTED DOWNLOADED GAMES");

            //Sending json object over the socket
            System.out.println("SENDING: " + jsonResponse);
            out.println(jsonResponse);
            out.flush();
        }
        catch (IOException io){
            io.printStackTrace();
        }
//        finally {
//            assert out != null;
//            out.close();
//        }
    }

    public static void download_game_res(Socket serverSocket, String game_id){
        PrintWriter out = null;
        try {
            //Connecting to output stream
            OutputStream outputStream = serverSocket.getOutputStream();
            out = new PrintWriter(outputStream);

            //Json
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("response", "SELECTED DOWNLOADED GAME");
            jsonResponse.addProperty("game_id", game_id);
            //Sending json object over the socket
            System.out.println("SENDING: " + jsonResponse);
            out.println(jsonResponse);
            out.flush();
        }
        catch (IOException io){
            io.printStackTrace();
        }
//        finally {
//            assert out != null;
//            out.close();
//        }
    }
}
