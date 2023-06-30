package com.company.view;

import com.company.model.GoodsDatabase;
import com.company.model.Product;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProductsView extends JPanel {
    private JLayeredPane layeredPane;
    public static final int WIDTH = 900;
    public static final int HEIGHT = ProductView.HEIGHT + 20;

    private final ProductView[] panels;

    public ProductsView(String categoryName, String typeName, int x, int y) {
        setLayout(new FlowLayout());
        layeredPane = new JLayeredPane();
        setForeground(new Color(107,107,107));

        GoodsDatabase goodsDatabase = new GoodsDatabase();
        ArrayList<Product> products = goodsDatabase.loadGoodsFromTypeAndCategory(categoryName, typeName);

        int rowsCount = (products.size() / 4);
        rowsCount = (products.size() % 4 == 0) ? rowsCount : rowsCount + 1;
        int lpHeight = (rowsCount) * (ProductView.HEIGHT+20);
        layeredPane.setPreferredSize(new Dimension(WIDTH, lpHeight));
        //setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setBounds(x, y, WIDTH, HEIGHT);

        JScrollPane pane = new JScrollPane(layeredPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setPreferredSize(new Dimension(WIDTH - 15, HEIGHT-15));
        //pane.setPreferredSize(getPreferredSize());
        pane.getVerticalScrollBar().setUnitIncrement(20);
        pane.setBorder(null);

        panels = new ProductView[products.size()];
        int j = 0;
        for (int i = 0; i < products.size();) {
            panels[i] = new ProductView(products.get(i));
            //panels[i].addRatingButtons();
            panels[i].setBounds((panels[i].getWidth() + 20) * (i % 4), (panels[i].getHeight() + 20) * j, panels[i].getWidth(), panels[i].getHeight());
            layeredPane.add(panels[i], Integer.valueOf((10)));
            i++;
            if(i % 4 == 0) {
                j++;
            }
        }
        add(pane);
    }

    public ProductsView(ArrayList<Product> products, boolean needRating) {
        setLayout(new FlowLayout());
        layeredPane = new JLayeredPane();

        setForeground(new Color(107,107,107));

        int rowsCount = (products.size() / 4);
        rowsCount = (products.size() % 4 == 0) ? rowsCount : rowsCount + 1;
        int lpHeight = (rowsCount) * (ProductView.HEIGHT+20);
        layeredPane.setPreferredSize(new Dimension(WIDTH - 15, lpHeight));
        //setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setBounds(0,0, WIDTH - 15, HEIGHT);

        JScrollPane pane = new JScrollPane(layeredPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setPreferredSize(new Dimension(WIDTH - 15, HEIGHT-15));
        //pane.setPreferredSize(getPreferredSize());
        pane.getVerticalScrollBar().setUnitIncrement(20);
        pane.setBorder(null);

        panels = new ProductView[products.size()];
        int j = 0;
        for (int i = 0; i < products.size();) {
            panels[i] = new ProductView(products.get(i));
            if(needRating)
                panels[i].addRatingButtons();
            panels[i].setBounds((panels[i].getWidth() + 20) * (i % 4), (panels[i].getHeight() + 20) * j, panels[i].getWidth(), panels[i].getHeight());
            layeredPane.add(panels[i], Integer.valueOf((10)));
            i++;
            if(i % 4 == 0) {
                j++;
            }
        }
        add(pane);
    }

    public ProductView[] getProductPanels() {
        return panels;
    }
}