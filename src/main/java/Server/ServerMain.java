package Server;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) {
        try {
//            System.out.println("Loading Resources");
//            loadResourcesFiles();
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("SERVER HAS STARTED LISTENING ON PORT `8888`.");
            while (true){
                //Assigning each client an single thread
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

    public static void loadResourcesFiles(){
        File directory = new File("src\\main\\java\\Server\\Resources");
        File[] files;
        System.out.println(directory.getPath());
        System.out.println(directory.getAbsoluteFile());
        files = directory.listFiles();
        assert files != null;
        for (File file : files){
            ArrayList<String> arr = new ArrayList<>();
            if (file.getName().endsWith(".txt")){
                Scanner inputFile = null;
                try {
                    inputFile = new Scanner(file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                while (inputFile.hasNextLine()){
                    arr.add(inputFile.nextLine());
                }
                System.out.println("passing" + file.getPath());
                QueryDB.insertFileToDB(arr, file.getPath());
            }
        }
        System.out.println("Resources has been loaded completely");
    }
}
