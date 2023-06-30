package com.company.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainPageView extends JPanel {

    public static final String MAIN_PAGE_NAME = "MAIN PAGE";
    private Client client;
    private JLayeredPane layeredPane;
    private BufferedImage logo2Pic;

    public MainPageView(Client client) {
        this.client = client;

        try {
            logo2Pic = ImageIO.read(new File("pictures/logo2.png"));
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

        mainPageView();
    }

    public void mainPageView() {
        setName(MAIN_PAGE_NAME);
        setBounds(0,0,client.getFullScreenWidth(), client.getFullScreenHeight());
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(client.getFullScreenWidth(), client.getFullScreenHeight()));

        layeredPane.add(BackgroundView.backgroundPicture(client.getFullScreenWidth(), client.getFullScreenHeight(), 1), Integer.valueOf(3));

        SideMenuView sideMenuView = new SideMenuView(client);
        layeredPane.add(sideMenuView, Integer.valueOf(10));

        /*JLabel logo2PicLabel = new JLabel(new ImageIcon(logo2Pic));
        logo2PicLabel.setBounds(0, 0,
                sideMenuView.getWidth(), sideMenuView.getWidth());
        logo2PicLabel.setHorizontalAlignment(JLabel.CENTER);
        logo2PicLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel logo2panel = new JPanel();
        logo2panel.setBounds(10, sideMenuView.getY() + sideMenuView.getHeight() + 30,
                sideMenuView.getWidth(), sideMenuView.getWidth());
        logo2panel.setBackground(new Color(0,0,0,180));
        logo2panel.add(logo2PicLabel);

        layeredPane.add(logo2panel, Integer.valueOf(15));*/

        add(layeredPane);
    }

}
