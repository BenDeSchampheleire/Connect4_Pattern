package ServerPattern;

import java.io.IOException;
import java.net.Socket;

public class Process extends Thread{

    private Socket clientSocket;
    private ServerTCP myServerTCP;

    public Process(Socket aSocket, ServerTCP aServer) {
        super("ServerThread");
        clientSocket = aSocket;
        System.out.println("[Process] Client: " + clientSocket);
        myServerTCP = aServer;
    }

    public void run() {

        try {
            myServerTCP.getProtocol().execute(myServerTCP.getContext(),clientSocket.getInputStream(),clientSocket.getOutputStream());
            System.out.println("Process done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
