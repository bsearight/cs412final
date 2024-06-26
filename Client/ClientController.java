package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.codec.digest.DigestUtils;
import Resources.Player;
// tried using MessageDigest for password encryption, but wasn't sure how to handly byte arrays
// https://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-l
// which led me to finding apache commons codec
// https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/digest/DigestUtils.html#md5Hex(byte%5B%5D)
// https://commons.apache.org/proper/commons-codec/

public class ClientController
{
    private final ClientModel model;
    public final ClientMainMenuView view;
    boolean loginSuccess = false;
    private String phash;
    Socket clientSocket;
    InputStreamReader inputStreamReader;
    BufferedReader reader;
    PrintWriter writer;
    String retval;
    boolean offline = false;
    
    public ClientController()
    {
        model = new ClientModel(this);
        view = new ClientMainMenuView(this);
        init();
    }
    private void init()
    {
        // initiate connection with server
        try
        {
            clientSocket = new Socket("127.0.0.1", 6000);
            inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
            reader = new BufferedReader(inputStreamReader);
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
        }
        catch (IOException ex)
        {
            System.out.println("Failed to connect to server");
            offline = true;
            ex.printStackTrace();
        }
    }
    public boolean login(String username, String password)
    {
        if (offline == true)
        {
            view.setIsLoggedIn(true);
            return true;
        }
        phash = DigestUtils.md5Hex(password);
        model.setCurrentPlayer(username, phash);
        boolean retval = isLoggedIn();
        view.setIsLoggedIn(retval);
        return retval;
    }
    public void logout()
    {
        view.setIsLoggedIn(false);
    }
    public boolean isLoggedIn()
    {
        Player current = model.getCurrentPlayer();
        writer.println("auth_user");
        writer.println(current.getUsername());
        writer.println(current.getPHash());
        try
        {
            String retval = reader.readLine();
            if (retval.equals("auth_confirm")) return true;
            else return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public String getLeaderboard()
    {
        writer.println("get_leaderboard");
        StringBuilder builder = new StringBuilder();
        String retval = "";
        try
        {
            retval = reader.readLine();
            builder.append(retval).append("\n");
            retval = reader.readLine();
            builder.append(retval).append("\n");
            retval = reader.readLine();
            builder.append(retval).append("\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return builder.toString();
    }
    protected int getCoinFlip()
    {
        int flipResult = 0;
        writer.println("get_coinflip");
        String retval = "";
        try 
        {
            retval = reader.readLine();
            flipResult = Integer.parseInt(retval);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return flipResult; // 1 is heads, 0 is tails
    }
    protected int getDiceRoll()
    {
        int rollResult = 0;
        writer.println("get_diceroll");
        String retval = "";
        try 
        {
            retval = reader.readLine();
            rollResult = Integer.parseInt(retval);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return rollResult;
    }
    protected void confirmBetting(String bet, String option)
    {
        writer.println("confirm_bet");
        writer.println(option);
        writer.println(bet);
    }
    protected boolean registerUser(String username, String password)
    {
        phash = DigestUtils.md5Hex(password);
        model.setCurrentPlayer(username, phash);
        boolean retval = createPlayer();
        return retval;
    }
    private boolean createPlayer()
    {
        Player current = model.getCurrentPlayer();
        writer.println("create_user");
        writer.println(current.getUsername());
        writer.println(current.getPHash());
        String retval = "";
        try
        {
            retval = reader.readLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if (retval.contains("create_confirm")) return true;
        else return false;
    }
    protected String getUsername()
    {
        Player current = model.getCurrentPlayer();
        return current.getUsername();
    }
    protected String getBalance()
    {
        writer.println("get_balance");
        String retval = "";
        try
        {
            retval = reader.readLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return retval;
    }
    public void quit()
    {
        try 
        {
            logout();
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
