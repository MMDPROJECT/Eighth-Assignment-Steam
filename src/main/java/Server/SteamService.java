package Server;

import Client.Account;
import Shared.Response;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Scanner;

public class SteamService implements Runnable {
    private Socket serverSocket;
    private Scanner in;

    //Constructor

    public SteamService(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    //Service-Providing
    @Override
    public void run() {
        try {
            try {
                in = new Scanner(serverSocket.getInputStream());
                doService();
            } finally {
                in.close();
            }
        } catch (IOException ioException){
            ioException.printStackTrace();
        } finally {
            System.out.println("CLIENT " + serverSocket.getRemoteSocketAddress() + " HAS BEEN DISCONNECTED");
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

            //Receiving data
            String jsonString = in.nextLine();
            System.out.println("RECEIVED " + jsonString);
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
                Response.sign_up_res(serverSocket, false);
            } else {
                //Sending response
                Response.sign_up_res(serverSocket, true);
            }
        }

        else if (requestType.equals("SIGN IN")){
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            //Query to database and sending response
            Account account = QueryDB.accountLogin(username, password);
            if (account != null){
                //Successfully logged in
                JSONObject jsonAccount = new JSONObject();
                jsonAccount.put("account_id", account.getAccount_id());
                jsonAccount.put("username", account.getUsername());
                jsonAccount.put("password", account.getPassword());
                jsonAccount.put("date_of_birth", account.getDate_of_birth());
                //Sending response
                Response.sign_in_res(serverSocket, true, jsonAccount);
            } else {
                //Login was Unsuccessful
                Response.sign_in_res(serverSocket, false, null);
            }
        }

        else if (requestType.equals("SHOW ALL AVAILABLE GAMES")){
            //Query to database
            JsonObject jsonResponse = QueryDB.selectAllGames();
            //Sending response
            Response.show_all_game_res(serverSocket, jsonResponse);
        }
    }
}
