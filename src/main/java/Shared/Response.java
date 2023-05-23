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
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("responseType", "SIGN UP");
            if (!flag){
                jsonResponse.put("response", "ACCOUNT HAS BEEN SUCCESSFULLY SIGNED UP");
            } else {
                jsonResponse.put("response", "THIS USERNAME ALREADY EXISTS");
            }
            //Sending json object over socket
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

    public static void sign_in_res(Socket socket, Boolean flag, JSONObject jsonObject){    //flag == false means username or password is wrong, flag == true means user successfully logged in
        PrintWriter out = null;
        try {
            OutputStream outputStream = socket.getOutputStream();
            out = new PrintWriter(outputStream);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("responseType", "SIGN IN");
            if (flag){
                jsonResponse.put("response", "VALID LOGIN");
                jsonResponse.put("Account", jsonObject);
            } else {
                jsonResponse.put("response", "INVALID LOGIN");
            }
            //Sending json object over the socket
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
