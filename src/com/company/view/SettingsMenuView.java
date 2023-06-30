package com.company.view;

import com.company.io.CategoryLoader;
import com.company.model.Favorite;
import com.company.model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsMenuView extends JPanel {

    public static final int TYPES_ID = 100;
    public static final int BRANDS_ID = 101;

    private Client client;
    private User user;
    private JLayeredPane layeredPane;

    private JPanel allFavoritePanel;
    private JPanel settingsPicPanel;
    private SideMenuView sideMenuView;
    private JPanel categoriesPanel;
    private JPanel contentPanel;
    private JPanel up;

    private JPanel[] panels;
    private JPanel[] categoryPanels;
    private JPanel[] contentPanels;

    private JPanel closePicPanel;

    private ArrayList<String> categories;
    private ArrayList<ArrayList<String>> types;
    private ArrayList<ArrayList<String>> brands;
    private ArrayList<Favorite> favoriteCategories;
    private ArrayList<Favorite> favoriteCategoriesInBrands;

    private BufferedImage settingsPic;
    private BufferedImage savePic;
    private BufferedImage closePic;
    private BufferedImage logo2Pic;

    private int mode;

    public SettingsMenuView(Client client) {
        this.client = client;
        user = client.getUser();
        try {
            settingsPic = ImageIO.read(new File("pictures/settings.png"));
            savePic = ImageIO.read(new File("pictures/saveButton.png"));
            closePic = ImageIO.read(new File("pictures/closeButton.png"));
            logo2Pic = ImageIO.read(new File("pictures/logo2.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        settingsMenuView();
    }

    public void settingsMenuView() {
        CategoryLoader categoryLoader = new CategoryLoader();
        categories = categoryLoader.getCategories();
        types = categoryLoader.getTypes();
        brands = categoryLoader.getBrands();

        setBounds(0, 0, client.getFullScreenWidth(), client.getFullScreenHeight());
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(client.getFullScreenWidth(), client.getFullScreenHeight()));

        layeredPane.add(BackgroundView.backgroundPicture(client.getFullScreenWidth(), client.getFullScreenHeight(), 0), Integer.valueOf(3));

        sideMenuView = new SideMenuView(client);
        layeredPane.add(sideMenuView, Integer.valueOf(10));

        settingsPicPanel();
        layeredPane.add(settingsPicPanel, Integer.valueOf(10));

        allFavoritePanel(sideMenuView.getWidth() + sideMenuView.getX() + 50, sideMenuView.getY(),
                /*client.getFullScreenWidth() - (sideMenuView.getWidth() + sideMenuView.getX() + 50)
                        - (client.getFullScreenWidth() - settingsPicPanel.getX() + 50)*/
                900, 650);
        layeredPane.add(allFavoritePanel, Integer.valueOf(15));

        JLabel logo2PicLabel = new JLabel(new ImageIcon(logo2Pic));
        logo2PicLabel.setBounds(0, 0,
                sideMenuView.getWidth(), sideMenuView.getWidth() - 30);
        logo2PicLabel.setHorizontalAlignment(JLabel.CENTER);
        logo2PicLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel logo2panel = new JPanel();
        logo2panel.setBounds(10, sideMenuView.getY() + sideMenuView.getHeight() + 30,
                sideMenuView.getWidth(), sideMenuView.getWidth() - 30);
        logo2panel.setBackground(new Color(0,0,0,80));
        logo2panel.add(logo2PicLabel);

        layeredPane.add(logo2panel, Integer.valueOf(15));

        add(layeredPane);
    }

    private void allFavoritePanel(int x, int y, int width, int height) {
        allFavoritePanel = new JPanel();
        allFavoritePanel.setBounds(x, y, width, height);
        allFavoritePanel.setBackground(new Color(255, 255, 255, 70));
        allFavoritePanel.setLayout(null);

        up = new JPanel();
        up.setOpaque(false);
        up.setLayout(null);
        up.setBounds(0, 0, width, 50);

        String[] texts = {"Типы товаров", "Торговые марки"};
        panels = new JPanel[texts.length];

        for (int i = 0; i < texts.length; i++) {
            panels[i] = SmallPanel.getPanel(texts[i], SmallPanel.SETTINGS_UP_PANEL_TYPE);
            panels[i].setBounds((panels[i].getWidth()) * i, 0, panels[i].getWidth(), panels[i].getHeight());
            int index = i;
            panels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    boolean isSame = selectOneSmallPanel(panels, index);
                    if (texts[index].equals("Типы товаров")) {
                        if (categoriesPanel != null)
                            allFavoritePanel.remove(categoriesPanel);
                        mode = TYPES_ID;
                        categoriesPanel(mode);
                        client.revalidate();
                        repaint();

                    } else if (texts[index].equals("Торговые марки")) {
                        if (categoriesPanel != null)
                            allFavoritePanel.remove(categoriesPanel);
                        mode = BRANDS_ID;
                        categoriesPanel(mode);
                        client.revalidate();
                        repaint();

                    }
                }
            });
            up.add(panels[i]);
        }

        closePicLabel();
        closePicPanel.setVisible(false);
        up.add(closePicPanel);

        up.add(savePicLabel());


        allFavoritePanel.add(up);

    }

    private boolean selectOneSmallPanel(JPanel[] panels, int index) {
        boolean isSame = false;
        for (int i = 0; i < panels.length; i++) {
            if (i == index) {
                if (panels[i].getBackground() == SmallPanel.WHEN_SELECTED_COLOR) {
                    isSame = true;
                } else panels[i].setBackground(SmallPanel.WHEN_SELECTED_COLOR);
            } else {
                panels[i].setBackground(SmallPanel.USUAL_COLOR);
            }
        }
        return isSame;
    }

    private void categoriesPanel(int mode) {
        if (contentPanel != null)
            allFavoritePanel.remove(contentPanel);
        categoryPanels = new JPanel[categories.size()];

        categoriesPanel = new JPanel();
        categoriesPanel.setOpaque(false);

        JLayeredPane categoriesLayer = new JLayeredPane();

        JScrollPane pane = new JScrollPane(categoriesLayer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setPreferredSize(new Dimension(allFavoritePanel.getWidth(), 10 + (SmallPanel.HEIGHT + 5) * 10));
        pane.getVerticalScrollBar().setUnitIncrement(20);
        pane.setOpaque(false);
        pane.getViewport().setOpaque(false);
        pane.setBorder(null);

        pane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                client.repaint();
            }
        });

        favoriteCategories = user.getFavoriteCategories();
        if (mode == BRANDS_ID)
            favoriteCategories = user.getFavoriteCategoriesInBrands();
        //System.out.println(favoriteCategories);
        int j = 0;
        for (int i = 0; i < categoryPanels.length; ) {
            categoryPanels[i] = SmallPanel.getPanel(categories.get(i), SmallPanel.SETTINGS_CATEGORIES_PANEL_TYPE);
            categoryPanels[i].setBounds(20 + (categoryPanels[i].getWidth() + 10) * (i % 3),
                    10 + (categoryPanels[i].getHeight() + 5) * j,
                    categoryPanels[i].getWidth(), categoryPanels[i].getHeight());
            categoryPanels[i].setBorder(new LineBorder(Color.BLACK, 2));

            int index = i;
            categoryPanels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    allFavoritePanel.remove(categoriesPanel);
                    contentPanel(mode, index);
                    allFavoritePanel.add(contentPanel);
                    client.revalidate();
                    repaint();
                }
            });
            if (favoriteCategories.get(i).isFavorite())
                categoryPanels[i].setBackground(SmallPanel.WHEN_SELECTED_COLOR);
            categoriesLayer.add(categoryPanels[i], Integer.valueOf(5));

            i++;
            if (i % 3 == 0) {
                j++;
            }
        }

        categoriesLayer.setPreferredSize(new Dimension(allFavoritePanel.getWidth(), categoryPanels[categoryPanels.length - 1].getY()
                + categoryPanels[categoryPanels.length - 1].getHeight() + 30));
        categoriesLayer.setOpaque(false);

        categoriesPanel.setBounds(0, up.getHeight() + up.getX(), allFavoritePanel.getWidth(), allFavoritePanel.getHeight());
        categoriesPanel.add(pane);
        allFavoritePanel.add(categoriesPanel);

        closePicPanel.setVisible(false);
    }

    private void contentPanel(int mode, int categoryIndex) {
        ArrayList<String> contentList = null;
        if (mode == TYPES_ID) {
            contentList = types.get(categoryIndex);
        } else if (mode == BRANDS_ID) {
            contentList = brands.get(categoryIndex);
        }
        assert contentList != null;
        contentPanels = new JPanel[contentList.size()];

        //System.out.println(contentList);

        contentPanel = new JPanel();
        contentPanel.setOpaque(false);

        JLayeredPane contentLayer = new JLayeredPane();

        JScrollPane pane = new JScrollPane(contentLayer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setPreferredSize(new Dimension(allFavoritePanel.getWidth(), 10 + (SmallPanel.HEIGHT + 5) * 10));
        pane.getVerticalScrollBar().setUnitIncrement(20);
        pane.setOpaque(false);
        pane.getViewport().setOpaque(false);
        pane.setBorder(null);

        pane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                client.repaint();
            }
        });

        //System.out.println(categories.get(categoryIndex));
        //System.out.println(user.getFavoriteBrandsInCategory(categories.get(categoryIndex)));
        //System.out.println(user.getFavoriteTypes().get(categoryIndex));

        ArrayList<Favorite> favoriteTypesInCategory = user.getFavoriteTypes().get(categoryIndex);
        if (mode == BRANDS_ID)
            favoriteTypesInCategory = user.getFavoriteBrandsInCategory(categories.get(categoryIndex));

        int j = 0;
        for (int i = 0; i < contentPanels.length; ) {
            contentPanels[i] = SmallPanel.getPanel(contentList.get(i), SmallPanel.SETTINGS_CONTENT_PANEL_TYPE);
            contentPanels[i].setBounds(20 + (contentPanels[i].getWidth() + 10) * (i % 3),
                    10 + (contentPanels[i].getHeight() + 5) * j,
                    contentPanels[i].getWidth(), contentPanels[i].getHeight());
            contentPanels[i].setBorder(new LineBorder(Color.BLACK, 2));

            if (favoriteTypesInCategory.get(i).isFavorite())
                contentPanels[i].setBackground(SmallPanel.WHEN_SELECTED_COLOR);

            int index = i;
            ArrayList<Favorite> finalFavoriteTypesInCategory = favoriteTypesInCategory;
            contentPanels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (contentPanels[index].getBackground() == SmallPanel.USUAL_COLOR) {
                        contentPanels[index].setBackground(SmallPanel.WHEN_SELECTED_COLOR);
                        finalFavoriteTypesInCategory.get(index).setFavorite(true);

                        favoriteCategories.get(categoryIndex).setFavorite(true);
                    } else if (contentPanels[index].getBackground() == SmallPanel.WHEN_SELECTED_COLOR) {
                        contentPanels[index].setBackground(SmallPanel.USUAL_COLOR);
                        finalFavoriteTypesInCategory.get(index).setFavorite(false);

                        int favorite_count = 0;
                        for (int k = 0; k < finalFavoriteTypesInCategory.size(); k++) {
                            if (finalFavoriteTypesInCategory.get(k).isFavorite())
                                favorite_count++;
                        }
                        if (favorite_count == 0)
                            favoriteCategories.get(categoryIndex).setFavorite(false);
                    }
                }
            });

            contentLayer.add(contentPanels[i], Integer.valueOf(5));

            i++;
            if (i % 3 == 0) {
                j++;
            }
        }
        contentLayer.setPreferredSize(new Dimension(allFavoritePanel.getWidth(), contentPanels[contentPanels.length - 1].getY()
                + contentPanels[contentPanels.length - 1].getHeight() + 30));
        contentLayer.setOpaque(false);

        contentPanel.setBounds(0, up.getHeight() + up.getX(), allFavoritePanel.getWidth(), allFavoritePanel.getHeight());
        contentPanel.add(pane);
        allFavoritePanel.add(contentPanel);
        closePicPanel.setVisible(true);
    }


    private void settingsPicPanel() {
        settingsPicPanel = new JPanel();
        settingsPicPanel.setBounds(client.getFullScreenWidth() - settingsPic.getWidth() - 50, 10,
                settingsPic.getWidth() + 10, settingsPic.getHeight() + 10);
        settingsPicPanel.setBackground(new Color(0, 0, 0, 110));

        JLabel settingsPicLabel = new JLabel(new ImageIcon(settingsPic));
        settingsPicLabel.setBounds(0, 0, settingsPic.getWidth(), settingsPic.getHeight());
        settingsPicLabel.setHorizontalAlignment(SwingConstants.CENTER);
        settingsPicPanel.add(settingsPicLabel);
    }

    private JLabel savePicLabel() {
        JLabel savePicLabel = new JLabel(new ImageIcon(savePic));
        savePicLabel.setBounds(up.getWidth() - savePic.getWidth() - closePicPanel.getWidth() - 60,
                0, savePic.getWidth(), savePic.getHeight());
        savePicLabel.setHorizontalAlignment(SwingConstants.CENTER);

        savePicLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                user.saveTextFiles();
            }
        });
        return savePicLabel;
    }

    private void closePicLabel() {
        closePicPanel = new JPanel();
        closePicPanel.setLayout(null);
        closePicPanel.setBounds(up.getWidth() - closePic.getWidth() - 30,
                5, closePic.getWidth(), closePic.getHeight());
        closePicPanel.setBackground(new Color(0, 0, 0, 110));

        JLabel closePicLabel = new JLabel(new ImageIcon(closePic));
        closePicLabel.setBounds(0, 0, closePic.getWidth(), closePic.getHeight());
        closePicLabel.setHorizontalAlignment(SwingConstants.CENTER);

        closePicPanel.add(closePicLabel);
        closePicPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                allFavoritePanel.remove(contentPanel);
                categoriesPanel(mode);
                client.revalidate();
                repaint();
            }
        });
    }
}
