package com.company.view;

import com.company.io.FontsDownloader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SmallPanel {
    public static final int WIDTH = 250;
    public static final int HEIGHT = 50;

    public static final Color USUAL_COLOR = new Color(255, 255, 255);
    public static final Color WHEN_MOUSE_ENTERED_COLOR = new Color(144, 144, 144);
    public static final Color WHEN_SELECTED_COLOR = new Color(61, 255, 73);
    public static final Color ALPHA_100_COLOR = new Color(0, 19, 113,100);

    public static final int SIDE_MENU_TYPE = 1;
    public static final int SETTINGS_UP_PANEL_TYPE = 2;
    public static final int SETTINGS_CATEGORIES_PANEL_TYPE = 3;
    public static final int SETTINGS_CONTENT_PANEL_TYPE = 4;

    public static JPanel getPanel(String text, int type) {
        JPanel panel = new JPanel();
        panel.setBackground(USUAL_COLOR);
        JLabel label = new JLabel(text);
        panel.add(label);

        if(type == SIDE_MENU_TYPE) {
            panel.setBounds(new Rectangle(WIDTH, HEIGHT));
            label.setBounds(10,0,WIDTH - 10, HEIGHT);
            label.setFont(FontsDownloader.font4);
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    panel.setBackground(WHEN_MOUSE_ENTERED_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    panel.setBackground(USUAL_COLOR);
                }
            });
        }
        else if(type == SETTINGS_UP_PANEL_TYPE) {
            panel.setBounds(new Rectangle(WIDTH, HEIGHT));
            label.setBounds(10,0,WIDTH - 10, HEIGHT);
            label.setFont(FontsDownloader.font4);
        }
        else if(type == SETTINGS_CATEGORIES_PANEL_TYPE || type == SETTINGS_CONTENT_PANEL_TYPE) {
            panel.setBounds(new Rectangle(WIDTH + 30, HEIGHT));
            label.setFont(FontsDownloader.font5);
            label.setBounds(0, 0, WIDTH + 30, HEIGHT);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setBackground(USUAL_COLOR);
        }
        return panel;
    }


    public static JPanel getPanelWithImage(String imagePath) {
        BufferedImage pic = null;
        try {
            pic = ImageIO.read(new File(imagePath));
        }
        catch(IOException ex) {
            try {
                pic = ImageIO.read(new File("images/noImage.jpg"));
            } catch(IOException ex1) {
                ex1.printStackTrace();
            }
            ex.printStackTrace();
        }
        JPanel panel = new JPanel();
        panel.setBounds(new Rectangle(WIDTH, HEIGHT + 10));
        panel.setBackground(USUAL_COLOR);

        assert pic != null;
        JLabel label = new JLabel(new ImageIcon(pic));
        label.setBounds((WIDTH - pic.getWidth())/2, 0, pic.getWidth(), pic.getHeight());
        panel.add(label);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(WHEN_MOUSE_ENTERED_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(USUAL_COLOR);
            }
        });
        return panel;
    }

    public static JPanel getLegendPanel(String imagePath, String text, Color borderColor) {
        BufferedImage pic = null;
        try {
            pic = ImageIO.read(new File(imagePath));
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

        JPanel panel = new JPanel();
        panel.setBounds(new Rectangle(WIDTH, HEIGHT + 10));
        panel.setBackground(USUAL_COLOR);

        assert pic != null;
        JLabel label = new JLabel(new ImageIcon(pic));
        label.setVerticalAlignment(JLabel.CENTER);
        label.setBounds(0, 0, pic.getWidth(), pic.getHeight());
        panel.add(label);

        JTextArea textLabel = new JTextArea(text);
        textLabel.setLineWrap(true);
        textLabel.setWrapStyleWord(true);
        textLabel.setEditable(false);
        int x = label.getWidth() + 20;
        textLabel.setBounds(x, 0, panel.getWidth() - x, panel.getHeight());
        textLabel.setFont(FontsDownloader.font5);
        textLabel.setForeground(Color.BLACK);
        panel.add(textLabel);

        panel.setBorder(new LineBorder(borderColor, 3));
        return panel;
    }
}
