package Client;

import Game.Grid;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Représente un client TCP : cette classe s'appuie principalement sur un objet
 * {@link Socket}, et s'initialise par un nom de serveur et un numéro de port
 */
public class ClientTCP {

    private final int PortNumber; // ## attribute PortNumber
    private final String ServerName; // ## attribute ServerName
    private BufferedReader socIn; // ## link socIn
    private PrintStream socOut; // ## link socOut
    private Socket socketServer; // ## link socketServer
    private InputStream inputStream; //
    private ObjectInputStream objectInputStream;

    /**
     * Création d'un nouveau {@link ClientTCP} avec un nom de serveur et un numéro
     * de port
     *
     * @param aServerName
     * @param aPortNumber
     */
    public ClientTCP(String aServerName, int aPortNumber) {
        PortNumber = aPortNumber;
        ServerName = aServerName;
    }



    /**
     * Exécute la connexion au serveur, et crée la {@link Socket}
     *
     * Si une exception se produit, elle est traité en interne, et la méthode
     * renvoit false
     *
     * @return true si la connexion s'est bien déroulée
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
            System.err.println("Server offline: " + e);
            System.out.println("Please activate the server");
        }
        catch (Exception e) {
            System.err.println("Exception: " + e);
        }

        return connected;
    }

    /**
     * Commande la déconnexion au serveur
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
     * Transmet une chaine de caractères sur la Socket, et retourne la réponse sous
     * la forme d'une chaine.
     *
     * Cette méthode nécessite que la connexion soit effective
     *
     * @param aCommand
     * @return
     */
    public String transmitCommand(String aCommand) {
        String messageServer;
        try {

            System.out.println("Client: " + aCommand);

            socOut.println(aCommand);
            socOut.flush();

            messageServer = socIn.readLine();

            System.out.println("Server: " + messageServer);
            return messageServer;

        } catch (UnknownHostException e) {
            System.err.println("Unknown Server: " + e);
            return null;
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            return null;
        }
    }


    /**
     * Transmet une chaine de caractères sur la Socket, et retourne la réponse sous
     * la forme d'une chaine.
     *
     * Cette méthode nécessite que la connexion soit effective
     *
     * @param aCommand
     * @return la grille
     */
    public Grid transmitGrid(String aCommand) {
        try {

            System.out.println("Client: " + aCommand);

            socOut.println(aCommand);
            socOut.flush();

            Grid grid = (Grid) objectInputStream.readObject();

            System.out.println("Server: " + grid);
            return grid;

        } catch (UnknownHostException e) {
            System.err.println("Unknown Server: " + e);
            return null;
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            return null;
        }
    }

}

