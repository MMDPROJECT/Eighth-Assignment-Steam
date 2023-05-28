package Server;

import Client.Account;
import Shared.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.io.*;
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
//            while (!in.hasNextLine()){
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }

            //Receiving data
            String jsonString = in.nextLine();
            System.out.println("RECEIVING " + jsonString);

            JsonObject jsonRequest = new Gson().fromJson(jsonString, JsonObject.class);
            String requestType = jsonRequest.get("requestType").getAsString();
            System.out.println("requesttype :" + requestType);
            if (requestType.equals("QUIT")) {
                Response.exit_res(serverSocket);
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
                    new Account(jsonRequest.get("username").getAsString(), jsonRequest.get("password").getAsString(), LocalDate.parse(jsonRequest.get("date_of_birth").getAsString()));
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
            case "SHOW DOWNLOADED GAMES" -> {
                //Json
                String account_id = jsonRequest.get("account_id").getAsString();
                //Query to database
                JsonObject jsonResponse = QueryDB.selectDownloadedGames(account_id);
                if (jsonResponse != null){
                    //Sending Response
                    Response.show_downloaded_games_res(serverSocket, true, jsonResponse);
                }
                else {
                    Response.show_downloaded_games_res(serverSocket, false, jsonResponse);
                }
            }
            case "DOWNLOAD GAME" -> {
                //Json
                String game_id = jsonRequest.get("game_id").getAsString();
                String account_id = jsonRequest.get("account_id").getAsString();
                //Query to database
                String file_path = QueryDB.selectPNGPath(game_id);

                if (file_path != null){

                    if (QueryDB.hasDownloadedGame(account_id, game_id)){
                        System.out.println("has downloaded that before");
                        QueryDB.downloadGame(account_id, game_id);
                    } else {
                        QueryDB.insertToDownloads(account_id, game_id);
                        System.out.println("hasn't");
                    }
                    //Sending Response
                    Response.download_game_res(serverSocket, game_id);
                    try {
                        sendPNG(file_path);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    Response.download_game_res(serverSocket, game_id);
                }
            }
        }
    }

    private void sendPNG(String path) throws Exception {
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        OutputStream outputStream = serverSocket.getOutputStream();

        //Send the file size to client
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        long fileSize = file.length();
        dataOutputStream.writeLong(fileSize);

        //Send the file data
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        dataOutputStream.flush();
        outputStream.flush();
        fileInputStream.close();
    }
}
