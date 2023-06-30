package com.company.view;

import javax.swing.*;
import java.awt.*;

public class RatingLegendView extends JPanel {

    public RatingLegendView(int x, int y) {
        JPanel panelGreen = SmallPanel.getLegendPanel("pictures/good.png", " - Если товар " +
                        "понравился (по цене, качеству, вкусовым предпочтениям)", Color.green);
        JPanel panelYellow = SmallPanel.getLegendPanel("pictures/normal.png", " - Если товар " +
                "неплохой, но смущает цена или нет желания покупать этот товар в данный момент", Color.yellow);
        JPanel panelRed = SmallPanel.getLegendPanel("pictures/bad.png", " - Если товар " +
                "не соответствует вкусовым предпочтениям", Color.red);
        panelGreen.setBounds(0, 0, panelGreen.getWidth(), panelGreen.getHeight());
        panelYellow.setBounds(0, panelGreen.getY() + panelGreen.getHeight() + 20,
                panelYellow.getWidth(), panelYellow.getHeight());
        panelRed.setBounds(0, panelYellow.getY() + panelYellow.getHeight() + 20,
                panelRed.getWidth(), panelRed.getHeight());
        setBounds(x, y, SmallPanel.WIDTH, panelRed.getY() + panelRed.getHeight() + 70);
        System.out.println(panelRed.getY() + " " +  panelRed.getHeight());
        add(panelGreen);
        add(panelYellow);
        add(panelRed);
        setOpaque(false);
    }
}
