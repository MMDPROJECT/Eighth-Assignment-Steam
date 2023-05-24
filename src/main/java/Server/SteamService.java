package Server;

import Client.Account;
import Shared.Response;
import com.google.gson.Gson;
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

            JsonObject jsonRequest = new Gson().fromJson(jsonString, JsonObject.class);
            String requestType = jsonRequest.get("requestType").getAsString();
            if (requestType.equals("QUIT")) {
                return;
            } else {
                executeRequest(jsonRequest);
            }
        }
    }

    public void executeRequest(JsonObject jsonRequest) {
        String requestType = jsonRequest.get("requestType").getAsString();

        switch (requestType) {
            case "SIGN UP" -> {
                //Query to database
                if (!QueryDB.usernameExist(jsonRequest.get("username").getAsString())) {
                    //Insertion into database
                    new Account(jsonRequest.get("username").getAsString(), jsonRequest.get("password").getAsString(), LocalDate.parse(jsonRequest.get("dateOfBirth").getAsString()));
                    //Sending response
                    Response.sign_up_res(serverSocket, false);
                } else {
                    //Sending response
                    Response.sign_up_res(serverSocket, true);
                }
            }
            case "SIGN IN" -> {
                String username = jsonRequest.get("username").getAsString();
                String password = jsonRequest.get("password").getAsString();
                //Query to database and sending response
                Account account = QueryDB.accountLogin(username, password);
                if (account != null) {
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
            case "SHOW ALL AVAILABLE GAMES" -> {
                //Query to database
                JsonObject jsonResponse = QueryDB.selectAllGames();
                //Sending response
                Response.show_all_game_res(serverSocket, jsonResponse);
            }
            case "SHOW AN SPECIFIC GAME" -> {
                //Json
//                System.out.println("Haha");
                String game_id = jsonRequest.get("game_id").getAsString();

                //Query to database
                JsonObject jsonResponse = QueryDB.selectSpecificGame(game_id);
                if (jsonResponse != null){
                    //Sending response
                    Response.show_specified_game_res(serverSocket, true,jsonResponse);
                }
                else {
                    Response.show_specified_game_res(serverSocket, false, jsonResponse);
                }
            }
        }
    }
}
