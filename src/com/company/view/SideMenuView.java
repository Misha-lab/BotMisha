package com.company.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SideMenuView extends JPanel {

    private boolean isShowed = true;
    private Client client;
    private int currentHeight;

    public SideMenuView(Client client) {
        this.client = client;
        client.setBounds(0, 0, client.getFullScreenWidth(), client.getFullScreenHeight());
        setLayout(null);
        //setPreferredSize(new Dimension(300, 700));
        setForeground(Color.WHITE);

        JPanel menuPic = SmallPanel.getPanelWithImage("pictures/menuOC.png");
        add(menuPic);
        String[] texts = {"Главная", "Каталог", "Собрать корзину", "Режим симулятора", "Настройки"};
        JPanel[] panels = new JPanel[texts.length];
        for (int i = 0; i < texts.length; i++) {
            panels[i] = SmallPanel.getPanel(texts[i], SmallPanel.SIDE_MENU_TYPE);
            panels[i].setBounds(0, menuPic.getHeight() + panels[i].getHeight() * i, panels[i].getWidth(), panels[i].getHeight());
            add(panels[i]);
        }

        int whenShowedHeight = panels[panels.length - 1].getY() + panels[panels.length - 1].getHeight();
        currentHeight = whenShowedHeight;

        setBounds(10, 10, 250, currentHeight);

        menuPic.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isShowed)
                    currentHeight = menuPic.getHeight();
                else currentHeight = whenShowedHeight;
                setBounds(10, 10, 250, currentHeight);

                for (int i = 0; i < texts.length; i++) {
                    panels[i].setVisible(!isShowed);
                }
                isShowed = !isShowed;
            }
        });

        panels[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Container c = client.getContentPane();
                String componentName = c.getComponent(c.getComponentCount() - 1).getName();
                if (componentName != null) { //ничего не делает, если и были на этой вкладке
                    if (componentName.equals(MainPageView.MAIN_PAGE_NAME)) {
                        return;
                    }
                }
                c.remove(c.getComponentCount() - 1);
                MainPageView mainPageView = new MainPageView(client);
                c.add(mainPageView);
                client.revalidate();
            }
        });

        panels[1].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Container c = client.getContentPane();
                c.remove(c.getComponentCount() - 1);
                CatalogView catalogView = new CatalogView(client);
                c.add(catalogView);
                c.repaint();
                client.revalidate();
            }
        });

        panels[2].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Container c = client.getContentPane();
                c.remove(c.getComponentCount() - 1);
                BasketMenuView basketMenuView = new BasketMenuView(client);
                c.add(basketMenuView);
                c.repaint();
                client.revalidate();
            }
        });

        panels[3].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Container c = client.getContentPane();
                c.remove(c.getComponentCount() - 1);
                LearningSimulatorView learningSimulatorView = new LearningSimulatorView(client);
                c.add(learningSimulatorView);
                c.repaint();
                client.revalidate();
            }
        });

        panels[4].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Container c = client.getContentPane();
                c.remove(c.getComponentCount() - 1);
                SettingsMenuView settingsMenuView = new SettingsMenuView(client);
                c.add(settingsMenuView);
                c.repaint();
                client.revalidate();
            }
        });
    }
}