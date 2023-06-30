package com.company.view;

import com.company.model.GoodsDatabase;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LearningSimulatorView extends JPanel {
    private JLayeredPane secondLayer;
    private JLayeredPane layeredPane;
    public static final int WIDTH = 900;

    private GoodsDatabase goodsDatabase;
    private Client client;
    private JPanel panel;

    private JPanel startPanel;

    private JLabel background0;
    private JLabel background1;

    public LearningSimulatorView(final Client client) {
        this.client = client;
        learningSimulatorView();
    }

    public void learningSimulatorView() {
        setBounds(0,0,client.getFullScreenWidth(), client.getFullScreenHeight());
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(client.getFullScreenWidth(), client.getFullScreenHeight()));

        JLabel background0 = BackgroundView.backgroundPicture(client.getFullScreenWidth(), client.getFullScreenHeight(), 0);
        JLabel background1 = BackgroundView.backgroundPicture(client.getFullScreenWidth(), client.getFullScreenHeight(), 1);

        layeredPane.add((background1), Integer.valueOf(3));

        SideMenuView sideMenuView = new SideMenuView(client);
        layeredPane.add(sideMenuView, Integer.valueOf(10));

        goodsDatabase = new GoodsDatabase();

        try {
            BufferedImage first = ImageIO.read(new File("pictures/button0.png"));
            startPanel = new JPanel();
            startPanel.setLayout(null);
            startPanel.setOpaque(false);
            startPanel.setBounds((client.getFullScreenWidth() - first.getWidth())/2,
                    (client.getFullScreenHeight() - first.getHeight())/2, first.getWidth(), first.getHeight());

            secondLayer = new JLayeredPane();
            secondLayer.setBounds(new Rectangle(first.getWidth(), first.getHeight()));

            JLabel firstPic = new JLabel(new ImageIcon(first));
            firstPic.setBounds(0,0, first.getWidth(), first.getHeight());

            JLabel label = new JLabel("НАЧАТЬ!");
            label.setBounds(0,0, first.getWidth(), first.getHeight());
            label.setHorizontalAlignment(JLabel.CENTER);
            Font font = new Font("Sherif", Font.BOLD, 30);
            label.setFont(font);
            label.setForeground(Color.BLACK);

            secondLayer.add(firstPic, Integer.valueOf(0));
            secondLayer.add(label, Integer.valueOf(1));
            startPanel.add(secondLayer);

            layeredPane.add(startPanel, Integer.valueOf(10));

            startPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    layeredPane.remove(background1);
                    layeredPane.add(background0, Integer.valueOf(3));
                    showProducts();
                    client.revalidate();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        add(layeredPane);
    }

    public void showProducts() {
        layeredPane.remove(startPanel);
        if(panel != null) {
            layeredPane.remove(panel);
        }
        panel = new JPanel();
        //panel.setOpaque(false);

        secondLayer = new JLayeredPane();
        //layeredPane.setOpaque(false);
        //panel.setBackground(new Color(107, 107, 107, 107));
        panel.setBackground(new Color(0, 0, 0, 70));

        int lpHeight = (ProductView.HEIGHT + 20) + 150;
        secondLayer.setPreferredSize(new Dimension(WIDTH - 20, lpHeight));
        //secondLayer.setBackground(new Color(107, 107, 107, 107));
        secondLayer.setBackground(new Color(0, 0, 0, 70));
        panel.setBounds(client.getContentPane().getWidth() - WIDTH, 100, WIDTH - 20, lpHeight);

        JPanel panelText = new JPanel();
        int textWidth = 500;
        panelText.setBounds((WIDTH - textWidth)/2, 10, textWidth, 50);
        //panelNext.setBackground(new Color(93, 255, 101));
        panelText.setBackground(new Color(0, 0, 0, 70));
        panelText.setLayout(null);

        Font font = new Font("Sherif", Font.BOLD, 20);
        JLabel labelMessage = new JLabel("Пожалуйста, оцените предложенные товары");
        labelMessage.setBounds(0, 0, textWidth, 50);
        labelMessage.setHorizontalAlignment(JLabel.CENTER);
        labelMessage.setVerticalAlignment(JLabel.CENTER);
        labelMessage.setFont(font);
        labelMessage.setForeground(Color.WHITE);

        panelText.add(labelMessage);
        secondLayer.add(panelText, Integer.valueOf(5));

        final ProductView[] panels = new ProductView[4];
        for (int i = 0; i < 4; i++) {
            panels[i] = new ProductView(goodsDatabase.generateRandProduct());
            panels[i].addRatingButtons();
            panels[i].setBounds(10 + (panels[i].getWidth() + 20) * i,
                    panelText.getHeight() + panelText.getY() + 10, panels[i].getWidth(), panels[i].getHeight());
            secondLayer.add(panels[i], Integer.valueOf((10)));
        }

        JPanel panelNext = new JPanel();
        panelNext.setBounds(230, panels[0].getY() + panels[0].getHeight() + 20, 420, 50);
        //panelNext.setBackground(new Color(93, 255, 101));
        panelNext.setBackground(new Color(255, 255, 255, 70));
        panelNext.setLayout(null);

        JLabel showNextLabel = new JLabel("Показать следующие товары!");
        showNextLabel.setBounds(0, 0, 420, 50);
        showNextLabel.setHorizontalAlignment(JLabel.CENTER);
        showNextLabel.setVerticalAlignment(JLabel.CENTER);
        showNextLabel.setFont(font);
        showNextLabel.setForeground(Color.BLACK);

        panelNext.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < panels.length; i++) {
                    if(panels[i].getSelected() != 0)
                        client.getUser().writeProductToFile(panels[i].getProduct(), panels[i].getSelected());
                }
                client.getUser().saveWorkbooksToFile();
                showProducts();

            }
        });
        panelNext.add(showNextLabel);
        secondLayer.add(panelNext, Integer.valueOf(10));
        panel.add(secondLayer);
        layeredPane.add(panel, Integer.valueOf(20));
        repaint();
    }
}
