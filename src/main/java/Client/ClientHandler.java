package Client;

import Shared.Request;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

public class ClientHandler {
    static Scanner input = new Scanner(System.in);
    private Socket clientSocket;
    private Scanner in;

    //Constructor

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.in = new Scanner(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Main

    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 8888);
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.runMenu();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //Methods

    public void runMenu() throws InterruptedException {
        while (true) {
            System.out.println("""
                    SELECT FROM BELOW BY IT'S NUMBER
                    1- SIGN UP
                    2- SIGN IN
                    """);
            int optionMenu = input.nextInt();
            input.nextLine();
            switch (optionMenu) {
                case 1 -> {
                    //SIGN UP
                    System.out.println("Enter Username:");
                    String username = input.nextLine();
                    System.out.println("Enter Password:");
                    String password = input.nextLine();
                    System.out.println("Enter Date of your birth:(in such format: yyyy-mm-dd):");
                    String date_of_birth = input.nextLine();

                    //Json
                    JsonObject jsonRequest = new JsonObject();
                    jsonRequest.addProperty("requestType", "SIGN UP");
                    jsonRequest.addProperty("username", username);
                    jsonRequest.addProperty("password", password);
                    jsonRequest.addProperty("date_of_birth", date_of_birth);

                    //Sending Request
                    System.out.println("SENDING: SIGN UP REQUEST");
                    Request.sign_up_req(clientSocket, jsonRequest);

                    //Receiving Response
                    Thread.sleep(500);
                    String response = in.nextLine();
                    JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                    System.out.println("RECEIVING: " + jsonResponse.get("response").getAsString());
                }
                case 2 -> {
                    //SIGN IN
                    System.out.println("Enter username:");
                    String username = input.nextLine();
                    System.out.println("Enter password:");
                    String password = input.nextLine();

                    //Json
                    JsonObject jsonRequest = new JsonObject();
                    jsonRequest.addProperty("requestType", "SIGN IN");
                    jsonRequest.addProperty("username", username);
                    jsonRequest.addProperty("password", password);

                    //Sending Request
                    System.out.println("SENDING: SIGN IN REQUEST");
                    Request.sign_in_req(clientSocket, jsonRequest);

                    //Receiving Response
                    Thread.sleep(500);
                    String response = in.nextLine();
                    JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                    String validation = jsonResponse.get("response").getAsString();
                    System.out.println("RECEIVING: " + jsonResponse.get("response").getAsString());

                    //Login validation
                    if (validation.equals("VALID LOGIN")) {
                        JsonObject jsonAccount = jsonResponse.get("Account").getAsJsonObject();
                        Account account = new Account(UUID.fromString(jsonAccount.get("account_id").getAsString()),
                                jsonAccount.get("username").getAsString(),
                                jsonAccount.get("password").getAsString(),
                                LocalDate.parse(jsonAccount.get("date_of_birth").getAsString()));
                        userPage(account);
                    }
                }
            }
        }
    }

    public void userPage(Account account) throws InterruptedException {
        System.out.println("""
                SELECT FROM BELOW BY IT'S NUMBER
                1- SHOW ALL AVAILABLE GAMES
                2- SHOW AN SPECIFIC GAME
                3- MANAGE DOWNLOAD
                    -SHOW DOWNLOADED GAMES
                    -DOWNLOAD A GAME
                """);
        int optionMenu = input.nextInt();
        input.nextLine();
        switch (optionMenu) {
            case 1 -> {
                //SHOW ALL AVAILABLE GAMES

                //Json
                JsonObject jsonRequest = new JsonObject();
                jsonRequest.addProperty("requestType", "SHOW ALL AVAILABLE GAMES");

                //Sending Request
                System.out.println("SENDING :" + "SHOW ALL AVAILABLE GAMES REQUEST");
                Request.show_all_games_req(clientSocket, jsonRequest);

                //Receiving response
                Thread.sleep(500);
                String response = in.nextLine();
                JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                System.out.println("RECEIVING :" + jsonResponse.get("response").getAsString());

                //Show table
                showGameTable(jsonResponse);
            }
            case 2 -> {
                //SHOW AN SPECIFIC GAME

                System.out.println("Enter game_id");
                String game_id = input.nextLine();

                //Json
                JsonObject jsonRequest = new JsonObject();
                jsonRequest.addProperty("requestType", "SHOW AN SPECIFIC GAME");
                jsonRequest.addProperty("game_id", game_id);

                //Sending Request
                Request.show_specific_game_req(clientSocket, jsonRequest);
                System.out.println("SENDING : SHOW AN SPECIFIC GAME REQUEST");

                //Receiving Response
                Thread.sleep(500);
                String response = in.nextLine();
                JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                System.out.println("RECEIVING :" + jsonResponse.get("response").getAsString());

                //Show Table
                showGameTable(jsonResponse);
            }
            case 3 -> {
                //MANAGE DOWNLOAD
                System.out.println("""
                        SELECT FROM BELOW BY IT'S NUMBER
                        1- SHOW DOWNLOADED GAMES
                        2- DOWNLOAD A GAME
                        """);
                optionMenu = input.nextInt();
                input.nextLine();
                switch (optionMenu) {
                    case 1 -> {
                        //SHOW DOWNLOADED GAMES
                        //TODO
                    }
                    case 2 -> {
                        //DOWNLOAD A GAME
                        //TODO
                    }
                }
            }
        }
    }

    public static void showGameTable(JsonObject table) {
        int rowCount = table.get("rowCount").getAsInt();
        int columnCount = table.get("columnCount").getAsInt();

        System.out.println("Table:");
        System.out.printf("%-50s%-50s%-50s%-50s%-50s%-50s%-50s%-50s%-50s%-50s", "|game_id|", "|title|", "|developer|", "|genre|", "|price|", "|release_year|", "|controller_support|", "|reviews|", "|size|", "|file_path|");

        //Iterating through rows
        for (int i = 1; i <= rowCount; i++) {
            JsonArray row = table.get("row" + i).getAsJsonArray();
            //Iterating through columns
            for (int j = 0; j < columnCount; j++) {
                System.out.printf("%-50s", "|" + row.get(j) + "|");
            }
            System.out.println();
        }
    }
}
