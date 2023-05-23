package Shared;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Request {
    public static void sign_up_req(Socket socket, JSONObject jsonObject) {
        PrintWriter out = null;
        try {
            //Sending request to server
            out = new PrintWriter(socket.getOutputStream());
            out.println(jsonObject);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }
    public static void sign_in_req(Socket socket, JSONObject jsonObject){
        PrintWriter out = null;
        try {
            //Sending request to server
            out = new PrintWriter(socket.getOutputStream());
            out.println(jsonObject);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }
    //TODO
}
