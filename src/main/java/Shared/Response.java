package Shared;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Response {
    public static void sign_up_res(Socket socket, Boolean flag){    //flag == false means account can be created, flag == true means account already exists.
        PrintWriter out = null;
        try {
            OutputStream outputStream = socket.getOutputStream();
            out = new PrintWriter(outputStream);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("responseType", "SIGN UP");
            if (!flag){
                jsonObject.put("response", "ACCOUNT HAS BEEN SUCCESSFULLY SIGNED UP");
            } else {
                jsonObject.put("response", "THIS USERNAME ALREADY EXISTS");
            }
            //Sending json object over socket
            System.out.println("Sent and json to " + socket.getRemoteSocketAddress());
            out.println(jsonObject);
            out.flush();
        }
        catch (IOException io){
            io.printStackTrace();
        }
        System.out.println(socket.isClosed());
//        finally {
//            assert out != null;
//            out.close();
//        }
    }

    public static void sign_in_res(Socket socket, Boolean flag){    //flag == false means username or password is wrong, flag == true means user successfully logged in
        PrintWriter out = null;
        try {
            System.out.println("socket is " + socket.isClosed());
            OutputStream outputStream = socket.getOutputStream();
            out = new PrintWriter(outputStream);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("responseType", "SIGN IN");
            if (flag){
                jsonObject.put("response", "VALID LOGIN");
            } else {
                jsonObject.put("response", "INVALID LOGIN");
            }
            //Sending json object over the socket
            out.println(jsonObject);
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
