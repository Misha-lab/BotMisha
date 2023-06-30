package com.company.view;

import com.company.io.FontsDownloader;
import com.company.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Client extends JFrame {

    private User user;

    private int fullScreenWidth;
    private int fullScreenHeight;


    public Client() {
        super("Bot Misha");

        FontsDownloader.addAllFonts();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        fullScreenHeight = dimension.height - 30;
        fullScreenWidth = dimension.width;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("pictures/icon.jpg").getImage());
        Container c = getContentPane();

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        //setLayout(flowLayout);
        setLayout(null);

        //CatalogView catalogView = new CatalogView(this);
        //c.add(catalogView);

        //SideMenuView sideMenuView = new SideMenuView(this);
        //c.add(sideMenuView);
        MainMenuView menu = new MainMenuView(this);
        menu.setBounds(0, 0, getWidth(), getHeight());
        c.add(menu);

        //ProductsView productsView = new ProductsView(this, "Полуфабрикаты", "Пельмени");
        //c.add(productsView);

        setVisible(true);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getFullScreenHeight() {
        return fullScreenHeight;
    }

    public int getFullScreenWidth() {
        return fullScreenWidth;
    }
}