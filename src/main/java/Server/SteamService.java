package Server;

import Client.Account;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;

public class SteamService implements Runnable {
    private Socket socket;
    private Scanner in;

    //Constructor

    public SteamService(Socket socket) {
        this.socket = socket;
        try {
            this.in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Getter and Setters

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Scanner getIn() {
        return in;
    }

    public void setIn(Scanner in) {
        this.in = in;
    }

    //Service-Providing
    @Override
    public void run() {
        try {
            doService();
        }
        finally {
            in.close();
        }
    }

    public void doService() {
        while (true){
            if (!in.hasNext()){return;}
            String requestType = in.nextLine();
            if (requestType.equalsIgnoreCase("QUIT")){return;}
            else {executeRequest(requestType);}
        }
    }

    public void executeRequest(String requestType) {
        if (requestType.equalsIgnoreCase("SIGN IN")){
            String jsonString = in.nextLine();
            JSONObject jsonObject = new JSONObject(jsonString);
            //Insertion to database
            new Account(jsonObject.getString("username"), jsonObject.getString("password"), LocalDate.parse(jsonObject.getString("dateOfBirth")));
        }
    }
}
