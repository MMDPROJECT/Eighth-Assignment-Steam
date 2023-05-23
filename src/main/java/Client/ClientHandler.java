package Client;

import Shared.Request;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

public class ClientHandler {
    static Scanner input = new Scanner(System.in);
    private Socket clientSocket;
    private Scanner in;

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
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("requestType", "SIGN UP");
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                    jsonObject.put("dateOfBirth", date_of_birth);
                    //Sending Request
                    System.out.println("SENDING: SIGN UP REQUEST...");
                    Request.sign_up_req(clientSocket, jsonObject);
                    //Receiving Response Doesn't work for now
//                    Thread.sleep(500);
//                    while(!in.hasNext())
//                        continue;
                    try {
                        System.out.println(clientSocket.isClosed());
                        in = new Scanner(clientSocket.getInputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String response = in.nextLine();
                    JSONObject jsonResponse = new JSONObject(response);
                    System.out.println("RECEIVING: " + jsonResponse.getString("response"));
                }
                case 2 -> {
                    //SIGN IN
                    System.out.println("Enter username:");
                    String username = input.nextLine();
                    System.out.println("Enter password:");
                    String password = input.nextLine();
                    //Json
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("requestType", "SIGN IN");
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                    //Sending Request
                    Request.sign_in_req(clientSocket, jsonObject);
                    //Receiving Response Doesn't work for now

                    Thread.sleep(500);
                    String response = in.nextLine();
                    JSONObject jsonResponse = new JSONObject(response);
                    String validation = jsonResponse.getString("response");
                    if (validation.equals("VALID LOGIN")){
                        Account account = new Account(UUID.fromString(jsonResponse.getString("account_id")), jsonResponse.getString("username"), jsonResponse.getString("password"), LocalDate.parse(jsonResponse.getString("date_of_birth")));
                        userPage(account);
                    } else {
                        System.out.println("RECEIVING INVALID USERNAME OR PASSWORD");
                    }
                }
            }
        }
    }

    public void userPage(Account account){
        //TODO
    }
}
