package Client;

import Shared.Request;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {

        try {
            Socket clientSocket = new Socket("127.0.0.1", 8888);
            runMenu(clientSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void runMenu(Socket socket) {
        while (true) {
            System.out.println("""
                    SELECT FROM BELOW BY IT'S NUMBER
                    1- SIGN UP
                    2- SIGN IN
                    """);
            int optionMenu = input.nextInt();
            input.nextLine();
            switch (optionMenu){
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
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                    jsonObject.put("dateOfBirth", date_of_birth);
                    //Sending Request
                    Request.sign_in_req(socket, jsonObject);
                }
                case 2 -> {
                    //SIGN IN
                    //TODO
                }
            }
        }
    }
}
