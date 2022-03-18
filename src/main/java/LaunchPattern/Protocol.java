package LaunchPattern;

import Game.Grid;
import ServerPattern.IContext;
import ServerPattern.IProtocol;

import java.io.*;
import java.util.Arrays;

public class Protocol implements IProtocol {


    @Override
    public void execute(IContext context, InputStream anInputStream, OutputStream anOutputStream) {

        Grid grid = (Grid) context;
        String inputReq;

        while (true) {

            try {
                // create an object output stream, so we can send an object through it
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(anOutputStream);

                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(anInputStream));
                PrintStream outputStream = new PrintStream(anOutputStream);

                if ((inputReq = bufferReader.readLine()) != null) {
                    String[] messages = inputReq.split(" ");
                    System.out.println("Message received: " + Arrays.toString(messages));
                    if (messages[0].contentEquals("red")) {
                        int columnNumber = Integer.parseInt(messages[1]);

                        grid.askPlay(columnNumber + 1, "red");
                        outputStream.println("Played: red " + columnNumber);
                        outputStream.flush();
                    }
                    if (messages[0].contentEquals("yellow")) {
                        int columnNumber = Integer.parseInt(messages[1]);

                        grid.askPlay(columnNumber + 1, "yellow");
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

                        outputStream.println(grid.toString());
                        outputStream.flush();
                        System.out.println("Grid sent: " + grid.toString());

                        // send it to the client
//                        objectOutputStream.writeObject(grid);
//                        objectOutputStream.reset();
//                        grid.display_grid();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
