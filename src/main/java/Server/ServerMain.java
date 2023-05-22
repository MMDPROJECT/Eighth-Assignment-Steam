package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("SERVER HAS STARTED LISTENING ON PORT `8888`.");
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("CLIENT WITH IP ADDRESS:" + socket.getLocalAddress() + " HAS BEEN ACCEPTED.");
                SteamService steamService = new SteamService(socket);
                Thread thread = new Thread(steamService);
                thread.start();
            }
        } catch (IOException ioException){
            ioException.printStackTrace();
        }
    }
}
