package Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

import Resources.Player;

public class ServerController {
    ServerModel model;
    ServerSocket server;
    public ServerController()
    {
        model = new ServerModel(this);
        init();
    }
/*
Server Logic:
Concurrent Connections: Handles multiple concurrent socket connections with players.
Database: Stores player info, including password hashes and balances. Optionally, consider including leaderboard data.
Game and Betting Logic: Implements actual game and betting logic.
No Back-End Support: Server does not require a separate back-end system.
*  */
    private void init() {
        // wait for connections from clients
        try {
            server = new ServerSocket(6000); //create a socket_listener on port 6000
            System.out.println("waiting for clients: ");
            Socket serverSocket = server.accept(); //socket endpoint for communication, once request is received it is filled.
            System.out.println("Client Connected");
            Thread thread = new Thread(new ServerConnectionHandler(serverSocket));
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected ArrayList<String> parseLeaderboard()
    {
        Collection<Player> leaderboard = model.getLeaderboard();
        ArrayList<String> lines = new ArrayList<String>();
        // parse leaderboard data into strings
        return lines;
    }
    public void quit()
    {
        try
        {
            server.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
