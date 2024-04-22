package Client;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class ClientView
{
    private ClientController controller;
    private JFrame frame;
    private JPanel switchPanel;
    private JPanel loggedIn;
    private JPanel loggedOut;
    private JLabel title;
    private JButton playButton;
    private JButton loginButton;
    private JButton logoutButton;
    private JButton quitButton;
    private JButton quitButton2;
    private JButton leaderboardButton;
    private ImageIcon icon;
    private boolean isLoggedIn = false;
    private boolean isGameViewOpen = false;


    public ClientView(ClientController controller)
    {
        this.controller = controller;
        init();
    }
    private void init()
    {
        frame = new JFrame("Coin Flip Client");
        loggedIn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loggedOut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        switchPanel = new JPanel(new CardLayout());
        title = getIcon();
        initButtons();
        loggedIn.add(playButton);
        loggedIn.add(logoutButton);
        loggedIn.add(leaderboardButton);
        loggedIn.add(quitButton2);
        loggedOut.add(loginButton);
        loggedOut.add(quitButton);
        switchPanel.add(loggedOut, "loggedOut");
        switchPanel.add(loggedIn, "loggedIn");
        frame.add(title, BorderLayout.NORTH);
        frame.add(switchPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent)
            {
                controller.logout();
            }
        });
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
    private JLabel getIcon()
    {
        try
        {
            icon = new ImageIcon(getClass().getResource("/Resources/icon.png"));
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        if (icon != null) return new JLabel(icon);
        else return new JLabel("image not found");
    }
    private void openGameView()
    {
        new ClientGameView(controller);
    }
    private void openLoginPane()
    {
        new ClientLoginPane(controller);
    }
    public void switchPanelLogin()
    {
        CardLayout layout = (CardLayout) switchPanel.getLayout();
        if (isLoggedIn) layout.show(switchPanel, "loggedIn");
        else layout.show(switchPanel, "loggedOut");
    }
    private void openLeaderboard()
    {
        new ClientLeaderboardPane(controller);
    }
    public void setIsLoggedIn(boolean isLoggedIn)
    {
        this.isLoggedIn = isLoggedIn;
        switchPanelLogin();
    }
    public void setGameViewOpen(boolean isOpen)
    {
        isGameViewOpen = isOpen;
    }
    public void showWindow()
    {
        frame.setVisible(true);
    }
    private void initButtons()
    {
        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                isGameViewOpen = true;
                openGameView();
                frame.setVisible(false);
            }
        });
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                openLoginPane();
            }
        });
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.logout();
                JOptionPane.showMessageDialog(null, "Logged Out", "", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.quit();
            }
        });
        quitButton2 = new JButton("Quit");
        quitButton2.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.quit();
            }
        });
        leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                openLeaderboard();
            }
        });
    }
}
