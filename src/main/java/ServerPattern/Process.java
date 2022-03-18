package ServerPattern;

import java.io.IOException;
import java.net.Socket;

/**
 * <h1>Process</h1>
 * Represents a process, created on a given Thread when a Client accepts a connection of the TCP Server.
 * @see ServerTCP#go()
 */
public class Process extends Thread{

    private final Socket clientSocket;
    private final ServerTCP myServerTCP;

    public Process(Socket aSocket, ServerTCP aServer) {
        super("ServerThread");
        clientSocket = aSocket;
        System.out.println("[Process] Connection with Client on: " + clientSocket);
        myServerTCP = aServer;
    }

    /**
     * Executes the process.
     */
    public void run() {

        try {
            myServerTCP.getProtocol().execute(myServerTCP.getContext(),clientSocket.getInputStream(),clientSocket.getOutputStream());
            System.out.println("[Process] Finished");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
