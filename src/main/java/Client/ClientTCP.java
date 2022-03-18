package Client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * <h1>ClientTCP</h1>
 * Represents a TCP Client based on a {@link Socket}. It is initialized with a Server name and a port number.
 */
public class ClientTCP {

    private final int PortNumber;
    private final String ServerName;
    private BufferedReader socIn;
    private PrintStream socOut;
    private Socket socketServer;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

    public ClientTCP(String aServerName, int aPortNumber) {
        PortNumber = aPortNumber;
        ServerName = aServerName;
    }

    /**
     * Connects to the TCP Server while creating a {@link Socket}.
     * @return true/false
     */
    public boolean connectServer() {
        System.out.println("Connect to Server");
        boolean connected = false;
        try {
            System.out.println("Trying: " + ServerName + " -- " + PortNumber);
            socketServer = new Socket(ServerName, PortNumber);
            socOut = new PrintStream(socketServer.getOutputStream());
            socIn = new BufferedReader(new InputStreamReader(socketServer.getInputStream()));
            inputStream = socketServer.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            System.out.println("Connection successful");
            connected = true;
        }
        catch (UnknownHostException e) {
            System.err.println("Unknown server: " + e);
        }
        catch (ConnectException e) {
            System.out.println("Please activate the server");
            System.err.println("Server offline: " + e);
        }
        catch (Exception e) {
            System.err.println("Exception: " + e);
        }
        return connected;
    }

    /**
     * Deconnects to the TCP Server, closing the {@link Socket}.
     */
    public void deconnectServer() {
        System.out.println("Deconnect from Server");
        try {
            socOut.close();
            socIn.close();
            inputStream.close();
            objectInputStream.close();
            socketServer.close();
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }
    }

    /**
     * Transmits a command as a String on the {@link Socket} and gives the Server's response.
     * This method requires an active connection.
     *
     * @param aCommand a String containing the command
     * @return response as String
     */
    public String transmitCommand(String aCommand) {
        String messageServer;
        try {

            System.out.println("Client: " + aCommand);

            socOut.println(aCommand);
            socOut.flush();

            messageServer = socIn.readLine();

            if ( messageServer.contains("|") ) {
                System.out.println("Server: Grid sent");
            } else {
                System.out.println("Server: " + messageServer);
            }
            return messageServer;

        } catch (UnknownHostException e) {
            System.err.println("Unknown Server: " + e);
            return null;
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            return null;
        }
    }
}

