package Server;

import Client.Account;
import Shared.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Scanner;

public class SteamService implements Runnable {
    private Socket socket;
    private Scanner in;
//    private PrintWriter out;

    //Constructor

    public SteamService(Socket socket) {
        this.socket = socket;
    }

    //Service-Providing
    @Override
    public void run() {
        try {
            try {
                in = new Scanner(socket.getInputStream());
                doService();
            } finally {
                in.close();
            }
        } catch (IOException ioException){
            ioException.printStackTrace();
        } finally {
            System.out.println("Fucked up mate!");
        }
    }

    public void doService() {
        while (true) {
            while (!in.hasNext()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            String jsonString = in.nextLine();
            System.out.println("Received " + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            String requestType = jsonObject.getString("requestType");
            if (requestType.equals("QUIT")) {
                return;
            } else {
                executeRequest(jsonObject);
            }
        }
    }

    public void executeRequest(JSONObject jsonObject) {
        String requestType = jsonObject.getString("requestType");
        if (requestType.equals("SIGN UP")) {
            //Query to database
            if (!QueryDB.usernameExist(jsonObject.getString("username"))) {
                //Insertion into database
                new Account(jsonObject.getString("username"), jsonObject.getString("password"), LocalDate.parse(jsonObject.getString("dateOfBirth")));
                //Sending response
                Response.sign_up_res(socket, false);
            } else {
                //Sending response
                Response.sign_up_res(socket, true);
            }
        }
        else if (requestType.equals("SIGN IN")){
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            //Query to database and sending response
            Account account = QueryDB.accountLogin(username, password);
            if (account != null){
                JSONObject jsonAccount = new JSONObject();
                jsonAccount.put("account_id", account.getAccount_id());
                jsonAccount.put("username", account.getUsername());
                jsonAccount.put("password", account.getPassword());
                jsonAccount.put("date_of_birth", account.getDate_of_birth());
                Response.sign_in_res(socket, true, jsonAccount);
            } else {
                Response.sign_in_res(socket, false, null);
            }
        }
    }
}
