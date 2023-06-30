package com.company.view;

import com.company.io.CategoryLoader;
import com.company.io.FontsDownloader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class CatalogView extends JPanel {
    private JLayeredPane layeredPane;

    private JPanel catalogPanel;
    private SideMenuView sideMenuView;
    private JPanel categoriesPanel;
    private JPanel contentPanel;
    private JPanel up;

    private JLabel backPicLabel;
    private JLabel categoryLabel;
    private JLabel typeLabel;

    private JPanel[] panels;
    private JPanel[] categoryPanels;
    private JPanel[] contentPanels;

    private ProductsView productsView;

    private ArrayList<String> categories;
    private ArrayList<ArrayList<String>> types;

    private BufferedImage backButtonPic;

    private Client client;

    private int scene;
    private int indexType;

    private String categoryName;
    private String typeName;

    private static final int CATEGORIES_SCENE = 0;
    private static final int TYPES_SCENE = 1;
    private static final int GOODS_SCENE = 2;

    public CatalogView(final Client client) {
        this.client = client;

        try {
            backButtonPic = ImageIO.read(new File("pictures/backButton.png"));
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

        catalogView();
    }

    public void catalogView() {
        CategoryLoader categoryLoader = new CategoryLoader();
        categories = categoryLoader.getCategories();
        types = categoryLoader.getTypes();

        setBounds(0, 0, client.getFullScreenWidth(), client.getFullScreenHeight());
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(client.getFullScreenWidth(), client.getFullScreenHeight()));

        layeredPane.add(BackgroundView.backgroundPicture(client.getFullScreenWidth(), client.getFullScreenHeight(), 0), Integer.valueOf(3));

        sideMenuView = new SideMenuView(client);
        layeredPane.add(sideMenuView, Integer.valueOf(10));


        int x = sideMenuView.getWidth() + sideMenuView.getX() + 20;
        catalogPanel(x, sideMenuView.getY(),client.getFullScreenWidth() - x - 50, 650);
        layeredPane.add(catalogPanel, Integer.valueOf(15));

        add(layeredPane);
    }

    private void backPicLabel() {
        backPicLabel = new JLabel(new ImageIcon(backButtonPic));
        backPicLabel.setBounds(20, 0, backButtonPic.getWidth(), backButtonPic.getHeight());

        backPicLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(scene == GOODS_SCENE) {
                    catalogPanel.remove(productsView);
                    contentPanel(indexType);
                    catalogPanel.add(contentPanel);
                    scene = TYPES_SCENE;
                    typeLabel.setVisible(false);
                    client.revalidate();
                    repaint();
                }
                else if(scene == TYPES_SCENE) {
                    catalogPanel.remove(contentPanel);
                    catalogPanel.add(categoriesPanel);
                    scene = CATEGORIES_SCENE;
                    backPicLabel.setVisible(false);
                    categoryLabel.setVisible(false);
                    client.revalidate();
                    repaint();
                }
            }
        });
    }

    private void catalogPanel(int x, int y, int width, int height) {
        catalogPanel = new JPanel();
        catalogPanel.setBounds(x, y, width, height);
        catalogPanel.setLayout(null);
        catalogPanel.setBackground(new Color(255,255,255,70));

        up = new JPanel();
        up.setOpaque(false);
        up.setLayout(null);
        up.setBounds(0, 0, width, 50);

        backPicLabel();
        backPicLabel.setVisible(false);
        up.add(backPicLabel);

        categoryLabel = new JLabel();
        categoryLabel.setBounds(backPicLabel.getX() + backPicLabel.getWidth() + 20, 0,
                500, up.getHeight());
        categoryLabel.setHorizontalAlignment(JLabel.LEFT);
        categoryLabel.setForeground(Color.BLACK);
        categoryLabel.setFont(FontsDownloader.font1);
        categoryLabel.setVisible(false);

        typeLabel = new JLabel();
        typeLabel.setBounds(categoryLabel.getX() + categoryLabel.getWidth() + 30, 0,
                400, up.getHeight());
        typeLabel.setHorizontalAlignment(JLabel.RIGHT);
        typeLabel.setForeground(Color.BLACK);
        typeLabel.setFont(FontsDownloader.font2);
        typeLabel.setVisible(false);

        up.add(categoryLabel);
        up.add(typeLabel);

        categoriesPanel();
        scene = CATEGORIES_SCENE;
        catalogPanel.add(up);
    }

    private void categoriesPanel() {
        if (contentPanel != null)
            catalogPanel.remove(contentPanel);
        categoryPanels = new JPanel[categories.size()];

        categoriesPanel = new JPanel();
        categoriesPanel.setOpaque(false);

        JLayeredPane categoriesLayer = new JLayeredPane();

        JScrollPane pane = new JScrollPane(categoriesLayer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setPreferredSize(new Dimension(catalogPanel.getWidth(), 10 + (SmallPanel.HEIGHT + 5) * 10));
        pane.getVerticalScrollBar().setUnitIncrement(20);
        //pane.getVerticalScrollBar().setEnabled(false);
        //pane.getVerticalScrollBar().setVisible(false);
        pane.setOpaque(false);
        pane.getViewport().setOpaque(false);
        pane.setBorder(null);

        pane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                client.repaint();
            }
        });


        int j = 0;
        for (int i = 0; i < categoryPanels.length; ) {
            categoryPanels[i] = SmallPanel.getPanel(categories.get(i), SmallPanel.SETTINGS_CATEGORIES_PANEL_TYPE);
            categoryPanels[i].setBounds(50 + (categoryPanels[i].getWidth() + 50) * (i % 3),
                    10 + (categoryPanels[i].getHeight() + 5) * j,
                    categoryPanels[i].getWidth(), categoryPanels[i].getHeight());
            categoryPanels[i].setBorder(new LineBorder(Color.BLACK, 2));

            int index = i;
            categoryPanels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    catalogPanel.remove(categoriesPanel);

                    categoryName = categories.get(index);
                    categoryLabel.setText(categoryName);
                    categoryLabel.setVisible(true);

                    contentPanel(index);
                    indexType = index;

                    catalogPanel.add(contentPanel);
                    scene = TYPES_SCENE;
                    backPicLabel.setVisible(true);

                    client.revalidate();
                    repaint();
                }
            });
            categoriesLayer.add(categoryPanels[i], Integer.valueOf(5));

            i++;
            if (i % 3 == 0) {
                j++;
            }
        }

        categoriesLayer.setPreferredSize(new Dimension(catalogPanel.getWidth(), categoryPanels[categoryPanels.length - 1].getY()
                + categoryPanels[categoryPanels.length - 1].getHeight() + 50));
        categoriesLayer.setOpaque(false);

        categoriesPanel.setBounds(0, up.getHeight() + up.getX(), catalogPanel.getWidth(), catalogPanel.getHeight());
        categoriesPanel.add(pane);
        catalogPanel.add(categoriesPanel);
    }

    private void contentPanel(int categoryIndex) {
        ArrayList<String> contentList = types.get(categoryIndex);

        assert contentList != null;
        contentPanels = new JPanel[contentList.size()];

        contentPanel = new JPanel();
        contentPanel.setOpaque(false);

        JLayeredPane contentLayer = new JLayeredPane();

        JScrollPane pane = new JScrollPane(contentLayer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setPreferredSize(new Dimension(catalogPanel.getWidth(), 10 + (SmallPanel.HEIGHT + 5) * 10));
        pane.getVerticalScrollBar().setUnitIncrement(20);
        //pane.getVerticalScrollBar().setEnabled(false);
        //pane.getVerticalScrollBar().setVisible(false);
        pane.setOpaque(false);
        pane.getViewport().setOpaque(false);
        pane.setBorder(null);

        pane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                client.repaint();
            }
        });


        int j = 0;
        for (int i = 0; i < contentPanels.length; ) {
            contentPanels[i] = SmallPanel.getPanel(contentList.get(i), SmallPanel.SETTINGS_CONTENT_PANEL_TYPE);
            contentPanels[i].setBounds(50 + (contentPanels[i].getWidth() + 50) * (i % 3),
                    10 + (contentPanels[i].getHeight() + 5) * j,
                    contentPanels[i].getWidth(), contentPanels[i].getHeight());
            contentPanels[i].setBorder(new LineBorder(Color.BLACK, 2));

            int index = i;
            contentPanels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    catalogPanel.remove(contentPanel);

                    typeName = types.get(categoryIndex).get(index);
                    typeLabel.setText(typeName);
                    typeLabel.setVisible(true);

                    productsView = new ProductsView(categoryName, typeName, backButtonPic.getWidth(),
                            up.getY() + up.getHeight() + 50);
                    catalogPanel.add(productsView);
                    scene = GOODS_SCENE;

                    backPicLabel.setVisible(true);

                    client.revalidate();
                    repaint();
                }
            });

            contentLayer.add(contentPanels[i], Integer.valueOf(5));

            i++;
            if (i % 3 == 0) {
                j++;
            }
        }
        contentLayer.setPreferredSize(new Dimension(catalogPanel.getWidth(), contentPanels[contentPanels.length - 1].getY()
                + contentPanels[contentPanels.length - 1].getHeight() + 5));
        contentLayer.setOpaque(false);

        contentPanel.setBounds(0, up.getHeight() + up.getX(), catalogPanel.getWidth(), catalogPanel.getHeight());
        contentPanel.add(pane);
        catalogPanel.add(contentPanel);
    }

}
