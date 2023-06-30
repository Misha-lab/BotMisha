package com.company.view;

import com.company.algorithms.MathPart;
import com.company.io.FontsDownloader;
import com.company.model.FoodBasket;
import com.company.model.Product;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BasketMenuView extends JPanel {
    private Client client;
    private JLayeredPane layeredPane;

    private JPanel firstPanel;
    private JPanel secondPanel;
    private JPanel basketButton;
    private JPanel approvedBasketPanel;
    private JPanel priceInfoPanel;

    private JLabel budgetLabel;
    private SideMenuView sideMenuView;
    private RatingLegendView ratingLegendView;

    private static ArrayList<Product> approvedBasket;

    private BufferedImage collectPic;
    private BufferedImage approvedPic;
    private BufferedImage basketPic;

    public static final int WIDTH_FIRST_PANEL = 300;
    public static final int HEIGHT_FIRST_PANEL = 250;

    public static final int FIRST_PANEL_ID = 1;
    public static final int SECOND_PANEL_ID = 2;
    public static final int BASKET_PANEL_ID = 3;

    private boolean isRated;
    private int panelId;
    private int prevPanelId;

    private FoodBasket foodBasket;
    private int budget;

    public BasketMenuView(Client client) {
        this.client = client;
        try {
            collectPic = ImageIO.read(new File("pictures/collectBasket.png"));
            approvedPic = ImageIO.read(new File("pictures/approved.jpg"));
            basketPic = ImageIO.read(new File("pictures/basketButton.png"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        basketMenuView();
    }

    public void basketMenuView() {
        setBounds(0, 0, client.getFullScreenWidth(), client.getFullScreenHeight());
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(client.getFullScreenWidth(), client.getFullScreenHeight()));
        layeredPane.setOpaque(false);
        layeredPane.add(BackgroundView.backgroundPicture(client.getFullScreenWidth(), client.getFullScreenHeight(), 0), Integer.valueOf(3));

        sideMenuView = new SideMenuView(client);
        layeredPane.add(sideMenuView, Integer.valueOf(10));

        basketButton();
        layeredPane.add(basketButton, Integer.valueOf(10));

        firstPanel();
        layeredPane.add(firstPanel, Integer.valueOf(15));
        panelId = FIRST_PANEL_ID;

        add(layeredPane);
    }

    private void firstPanel() {
        firstPanel = new JPanel();
        firstPanel.setBounds((client.getFullScreenWidth() - WIDTH_FIRST_PANEL) / 2,
                (client.getFullScreenHeight() - HEIGHT_FIRST_PANEL) / 2, WIDTH_FIRST_PANEL, HEIGHT_FIRST_PANEL);
        firstPanel.setBackground(new Color(0, 20, 35));
        firstPanel.setLayout(null);

        JLabel label = new JLabel("Введите бюджет (100-3000 руб.):");
        label.setBounds(0, 20, WIDTH_FIRST_PANEL, 50);
        label.setFont(FontsDownloader.font3);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        firstPanel.add(label);


        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel();
        spinnerNumberModel.setMinimum(1);
        //spinnerNumberModel.setMaximum(3001);
        spinnerNumberModel.setStepSize(100);
        spinnerNumberModel.setValue(500);

        int spinWidth = 100;
        JSpinner spin = new JSpinner(spinnerNumberModel);
        spin.setBounds((firstPanel.getWidth() - spinWidth) / 2, label.getY() + label.getHeight(), spinWidth, 50);
        spin.setFont(FontsDownloader.font2);

        JComponent comp = spin.getEditor();
        JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        firstPanel.add(spin);

        JLabel confirmButton = new JLabel(new ImageIcon(collectPic));
        confirmButton.setBounds(0, spin.getY() + spin.getHeight() + 20, WIDTH_FIRST_PANEL, collectPic.getHeight());
        confirmButton.setHorizontalAlignment(SwingConstants.CENTER);
        firstPanel.add(confirmButton);

        JLabel inputAgain = new JLabel("Введите корректное значение!");
        inputAgain.setBounds(0, confirmButton.getY() + confirmButton.getHeight(), WIDTH_FIRST_PANEL, 30);
        inputAgain.setFont(FontsDownloader.font3);
        inputAgain.setHorizontalAlignment(SwingConstants.CENTER);
        inputAgain.setForeground(Color.RED);
        inputAgain.setVisible(false);
        firstPanel.add(inputAgain);

        confirmButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                budget = (int) spin.getValue();
                if (budget < 100 || budget > 3000) {
                    inputAgain.setVisible(true);
                    repaint();
                } else {
                    layeredPane.remove(firstPanel);

                    secondPanel();
                    layeredPane.add(secondPanel, Integer.valueOf(105));
                    priceInfoPanel(foodBasket.getCurrentCost(), 0);
                    layeredPane.add(priceInfoPanel, Integer.valueOf(105));

                    prevPanelId = FIRST_PANEL_ID;
                    panelId = SECOND_PANEL_ID;

                    client.revalidate();
                }
            }
        });
    }

    private void priceInfoPanel(double cost, int type) {
        priceInfoPanel = new JPanel();
        priceInfoPanel.setBounds(client.getFullScreenWidth() - ProductsView.WIDTH - 15, 0, ProductsView.WIDTH - 15,100);
        priceInfoPanel.setOpaque(false);

        budgetLabel = new JLabel();
        budgetLabel.setBounds(0,0,priceInfoPanel.getWidth(), priceInfoPanel.getHeight());
        String formatCost = new DecimalFormat("#0.00").format(cost);
        if(type == 0)
            budgetLabel.setText("Стоимость корзины: " + formatCost + " ||| Бюджет: " + budget);
        else if(type == 1)
            budgetLabel.setText("Стоимость корзины: " + formatCost);
        budgetLabel.setForeground(Color.BLACK);
        budgetLabel.setFont(FontsDownloader.font4);
        priceInfoPanel.add(budgetLabel);
    }

    private void secondPanel() {
        secondPanel = new JPanel();
        secondPanel.setLayout(null);
        //secondPanel.setBackground(new Color(0, 87, 202));

        foodBasket = new FoodBasket(client.getUser());
        ArrayList<Product> products = foodBasket.generateBasket(budget);
        ProductsView productsView1 = new ProductsView(products, true);

        secondPanel.setBounds(client.getFullScreenWidth() - ProductsView.WIDTH - 15, 100, ProductsView.WIDTH - 15, ProductsView.HEIGHT + 200);
        secondPanel.add(productsView1);
        secondPanel.setOpaque(false);

        ratingLegendView = new RatingLegendView(sideMenuView.getX(),
                sideMenuView.getY() + sideMenuView.getHeight() + 30);
        layeredPane.add(ratingLegendView, Integer.valueOf(20));

        ProductView[] panels = productsView1.getProductPanels();

        JPanel panelRate = new JPanel();
        panelRate.setBounds((secondPanel.getWidth() - 420) / 2, productsView1.getY() + productsView1.getHeight() + 20, 420, 50);
        panelRate.setBackground(new Color(0, 20, 35, 150));
        panelRate.setLayout(null);
        JLabel rateLabel = new JLabel("Оценить товары!");
        rateLabel.setBounds(0, 0, 420, 50);
        rateLabel.setHorizontalAlignment(JLabel.CENTER);
        rateLabel.setFont(FontsDownloader.font1);
        rateLabel.setForeground(Color.WHITE);

        JPanel resultPanel = new JPanel();
        resultPanel.setBounds((secondPanel.getWidth() - 420) / 2,
                panelRate.getY() + panelRate.getHeight(), 420, 40);
        resultPanel.setBackground(new Color(255, 255, 255, 80));
        resultPanel.setVisible(false);
        JLabel resultRatedLabel = new JLabel();
        resultRatedLabel.setBounds(0, 0, 420, 40);
        resultRatedLabel.setFont(FontsDownloader.font2);
        resultRatedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //alreadyRatedLabel.setForeground(Color.RED);
        resultPanel.add(resultRatedLabel);
        secondPanel.add(resultPanel);

        JLabel approvedButton = new JLabel(new ImageIcon(approvedPic));
        approvedButton.setBounds((secondPanel.getWidth() - approvedPic.getWidth()) / 2,
                panelRate.getY() + panelRate.getHeight() + resultPanel.getHeight(),
                approvedPic.getWidth(), approvedPic.getHeight());
        approvedButton.setHorizontalAlignment(SwingConstants.CENTER);
        approvedButton.setVisible(false);
        secondPanel.add(approvedButton);


        rateLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (!isRated) {
                    approvedBasket = MathPart.unionArrays(approvedBasket, foodBasket.chooseApprovedProducts(products));
                    for (int i = 0; i < panels.length; i++) {
                        if (panels[i].getSelected() != 0) {
                            client.getUser().writeProductToFile(panels[i].getProduct(), panels[i].getSelected());
                        }
                    }
                    resultRatedLabel.setText("Товары добавлены в корзину!");
                    resultRatedLabel.setForeground(new Color(32, 134, 0));

                    approvedButton.setVisible(true);
                    resultPanel.setVisible(true);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            Timer timer = new Timer(1000, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                }
                            });
                            timer.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    approvedButton.setVisible(false);
                                    resultPanel.setVisible(false);
                                    timer.stop();
                                }
                            });
                            timer.start();
                        }
                    };
                    thread.start();
                    client.getUser().saveWorkbooksToFile();
                    isRated = true;
                } else {
                    resultRatedLabel.setText("Вы уже оценили товары!");
                    resultRatedLabel.setForeground(Color.red);
                    resultPanel.setVisible(true);
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            Timer timer = new Timer(1000, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                }
                            });
                            timer.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    resultPanel.setVisible(false);
                                    timer.stop();
                                }
                            });
                            timer.start();
                        }
                    };
                    thread.start();
                }
            }
        });
        panelRate.add(rateLabel);
        secondPanel.add(panelRate);
    }

    public void basketButton() {
        basketButton = new JPanel();
        basketButton.setBounds(client.getFullScreenWidth() - basketPic.getWidth() - 50, 10,
                basketPic.getWidth() + 10, basketPic.getHeight() + 10);
        basketButton.setBackground(new Color(0, 0, 0, 50));

        JLabel basketPicLabel = new JLabel(new ImageIcon(basketPic));
        basketPicLabel.setBounds(0, 0, basketPic.getWidth(), basketPic.getHeight());
        basketPicLabel.setHorizontalAlignment(SwingConstants.CENTER);
        basketButton.add(basketPicLabel);

        basketButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(panelId == BASKET_PANEL_ID) {
                    //layeredPane.remove(basketButton);
                    /*if(prevPanelId == FIRST_PANEL_ID) {
                        firstPanel();
                        layeredPane.add(firstPanel, Integer.valueOf(105));
                        panelId = FIRST_PANEL_ID;
                    }
                    else if(prevPanelId == SECOND_PANEL_ID) {
                        secondPanel(budget);
                        layeredPane.add(secondPanel, Integer.valueOf(105));
                        panelId = SECOND_PANEL_ID;
                    }*/
                    if(priceInfoPanel != null)
                        layeredPane.remove(priceInfoPanel);
                    layeredPane.remove(approvedBasketPanel);
                    isRated = false;

                    firstPanel();
                    layeredPane.add(firstPanel, Integer.valueOf(105));
                    panelId = FIRST_PANEL_ID;
                    prevPanelId = BASKET_PANEL_ID;
                }
                else if (panelId == FIRST_PANEL_ID) {
                    if(priceInfoPanel != null)
                        layeredPane.remove(priceInfoPanel);
                    layeredPane.remove(firstPanel);

                    approvedBasketPanel();
                    layeredPane.add(approvedBasketPanel, Integer.valueOf(105));

                    prevPanelId = FIRST_PANEL_ID;
                    panelId = BASKET_PANEL_ID;
                }
                else if(panelId == SECOND_PANEL_ID) {
                    if(priceInfoPanel != null)
                        layeredPane.remove(priceInfoPanel);
                    layeredPane.remove(secondPanel);

                    approvedBasketPanel();
                    layeredPane.add(approvedBasketPanel, Integer.valueOf(105));

                    prevPanelId = SECOND_PANEL_ID;
                    panelId = BASKET_PANEL_ID;
                }
                repaint();
                client.revalidate();
            }
        });
    }

    public void approvedBasketPanel() {
        approvedBasketPanel = new JPanel();
        approvedBasketPanel.setLayout(null);

        ProductsView productsView1 = null;

        if(approvedBasket != null) {
            double cost = 0;
            for (int i = 0; i < approvedBasket.size(); i++) {
                cost += approvedBasket.get(i).getDiscountPrice();
            }
            priceInfoPanel(cost, 1);
            layeredPane.add(priceInfoPanel, Integer.valueOf(15));
            if (approvedBasket.size() != 0) {

                productsView1 = new ProductsView(approvedBasket, false);
                approvedBasketPanel.add(productsView1);
            } else {
                // вывести на экран, что корзина пуста
            }
        }
        else {
            // вывести на экран, что корзина пуста
        }

        approvedBasketPanel.setBounds(client.getFullScreenWidth() - ProductsView.WIDTH - 15, 100, ProductsView.WIDTH - 15, ProductsView.HEIGHT + 200);
        approvedBasketPanel.setOpaque(false);
    }
}
