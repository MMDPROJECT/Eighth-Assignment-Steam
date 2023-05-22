package Shared;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Request {
    public static void sign_in_req(Socket socket, JSONObject jsonObject) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(socket.getOutputStream());
            out.println("SIGN IN");
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
