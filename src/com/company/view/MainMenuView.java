package com.company.view;

import com.company.io.FontsDownloader;
import com.company.model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenuView extends JPanel {
    private Client client;

    private BufferedImage confirmPic;
    private BufferedImage logoPic;


    public static final int HEIGHT_UP = 250;
    public static final int HEIGHT_CENTER = 250;


    public MainMenuView(Client client) {
        this.client = client;

        try {
            confirmPic = ImageIO.read(new File("pictures/enterButton.png"));
            logoPic = ImageIO.read(new File("pictures/logo.png"));
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

        mainMenu();
    }

    public void mainMenu() {
        client.setBounds(0,0,600, 500);
        client.setResizable(false);

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setVgap(0);
        setLayout(flowLayout);

        //setPreferredSize(new Dimension(client.getWidth(), client.getHeight()));
        setBackground(Color.WHITE);

        JPanel up = new JPanel();
        up.setPreferredSize(new Dimension(client.getWidth(), HEIGHT_UP));
        up.setBackground(new Color(0, 20, 35));
        up.setLayout(null);

        JLabel label = new JLabel("Введите имя:");
        label.setBounds(client.getWidth() - 300, 40, 250, 50);
        label.setFont(FontsDownloader.font1);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        up.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(client.getWidth() - 300, label.getY() + label.getHeight() , 250, 50);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(FontsDownloader.font2);
        textField.setForeground(Color.BLACK);
        up.add(textField);

        JLabel confirmButton = new JLabel(new ImageIcon(confirmPic));
        confirmButton.setBounds(client.getWidth() - 300, textField.getY() + textField.getHeight() + 20, 250, confirmPic.getHeight());
        confirmButton.setHorizontalAlignment(SwingConstants.CENTER);
        up.add(confirmButton);

        JLabel logo = new JLabel(new ImageIcon(logoPic));
        //logo.setBounds((client.getWidth() - logoPic.getWidth())/2, 50, logoPic.getWidth(), logoPic.getHeight());
        logo.setBounds(50, 25, logoPic.getWidth(), logoPic.getHeight());
        logo.setHorizontalAlignment(SwingConstants.RIGHT);
        up.add(logo);

        add(up);


        JPanel center = new JPanel();
        center.setPreferredSize(new Dimension(client.getWidth(), HEIGHT_CENTER));
        center.setBackground(new Color(255, 242, 5));
        center.setLayout(null);

        add(center);

        JLabel inputAgain = new JLabel("Вы ничего не ввели!");
        inputAgain.setBounds(client.getWidth() - 300, confirmButton.getY() + confirmButton.getHeight(), 250, 30);
        inputAgain.setFont(FontsDownloader.font3);
        inputAgain.setHorizontalAlignment(SwingConstants.CENTER);
        inputAgain.setForeground(Color.RED);
        inputAgain.setVisible(false);
        up.add(inputAgain);

        confirmButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(!textField.getText().trim().equals("")) {
                    client.setUser(new User(textField.getText().trim()));
                    Container c = client.getContentPane();
                    c.remove(c.getComponentCount() - 1);

                    MainPageView mainPageView = new MainPageView(client);
                    c.add(mainPageView);

                    client.revalidate();
                }
                else {
                    inputAgain.setVisible(true);
                    repaint();
                }
            }
        });
    }
}
