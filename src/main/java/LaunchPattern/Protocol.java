package LaunchPattern;

import Game.Grid;
import ServerPattern.IContext;
import ServerPattern.IProtocol;

import java.io.*;
import java.util.Arrays;

/**
 * <h1>Protocol</h1>
 * Using a Protocol allows a more general implementation, which can be easily modified.
 * @see "SOLID Principle"
 */
public class Protocol implements IProtocol {

    /**
     * Executes a certain context ({@link IContext}).
     * Represents the Protocol from the Pattern.
     * @param anInputStream
     * @param anOutputStream
     */
    @Override
    public void execute(IContext context, InputStream anInputStream, OutputStream anOutputStream) {

        Grid grid = (Grid) context;
        String inputReq;

        while (true) {

            try {

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(anOutputStream);

                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(anInputStream));
                PrintStream outputStream = new PrintStream(anOutputStream);

                if ((inputReq = bufferReader.readLine()) != null) {

                    String[] messages = inputReq.split(" ");
                    System.out.println("Message received: " + Arrays.toString(messages));

                    if (messages[0].contentEquals("red")) {
                        int columnNumber = Integer.parseInt(messages[1]);

                        grid.askPlay(columnNumber+1, "red");
                        outputStream.println("Played: red " + columnNumber);
                        outputStream.flush();
                    }
                    if (messages[0].contentEquals("yellow")) {
                        int columnNumber = Integer.parseInt(messages[1]);

                        grid.askPlay(columnNumber+1, "yellow");
                        outputStream.println("Played: yellow " + columnNumber);
                        outputStream.flush();
                    }
                    if (messages[0].contentEquals("GiveMeAColor")) {
                        String color = grid.giveColor();

                        // send it to the client
                        outputStream.println(color);
                        outputStream.flush();
                        System.out.println("Current color: " + color);
                    }
                    if (messages[0].contentEquals("MyTurnToPlay?")) {
                        String currentTurn = grid.askTurn();

                        // send it to the client
                        outputStream.println(currentTurn);
                        outputStream.flush();
                        System.out.println("Current turn: " + currentTurn);
                    }
                    if (messages[0].contentEquals("GiveMeTheGrid")) {

                        // send it to the client
                        outputStream.println(grid.toString());
                        outputStream.flush();
                        System.out.println("Grid sent");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
