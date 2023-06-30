package com.company.view;

import com.company.model.Product;
import com.company.parsers.ImageDownloader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ProductView extends JPanel {

    public static final int WIDTH = 200;
    public static final int HEIGHT = 400;

    private final Product product;

    public static final int NOT_SELECTED = 0;
    public static final int GOOD_PRODUCT_SELECTED = 1;
    public static final int NORMAL_PRODUCT_SELECTED = 2;
    public static final int BAD_PRODUCT_SELECTED = 3;

    JLabel labelSelected;
    //private int selected = 0;
    private int labelSelectedWidth;
    private int labelSelectedHeight;

    public ProductView(Product product) {
        this.product = product;
        productPanel();

        BufferedImage picSelected = null;
        try {
            picSelected = ImageIO.read(new File("pictures/selectedRating.png"));
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        assert picSelected != null;
        labelSelected = new JLabel(new ImageIcon(picSelected));
        labelSelectedWidth = picSelected.getWidth();
        labelSelectedHeight = picSelected.getHeight();
    }

    public void productPanel() {
        setBounds(new Rectangle(WIDTH, HEIGHT));
        setBackground(new Color(255, 255, 255));
        setLayout(null);
        BufferedImage pic = null;
        try {
            pic = ImageIO.read(new File("images/" + product.getCategory() + "/"
                    + product.getName() + ".jpg"));
            if(pic != null)
                pic = ImageDownloader.resize(pic, pic.getWidth()/2, pic.getHeight()/2);
        }
        catch(IOException ex) {
            try {
                pic = ImageIO.read(new File("images/noImage.jpg"));
            } catch(IOException ex1) {
                ex1.printStackTrace();
            }
            ex.printStackTrace();
        }

        assert pic != null;
        JLabel picture = new JLabel(new ImageIcon(pic));

        int x = (WIDTH - pic.getWidth())/2;
        int y = (WIDTH - pic.getHeight())/2;
        picture.setBounds(x, y, pic.getWidth(), pic.getHeight());

        add(picture);

        JTextArea nameLabel = new JTextArea(product.getName());
        nameLabel.setLineWrap(true);
        nameLabel.setWrapStyleWord(true);
        nameLabel.setEditable(false);
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Montserrat-BoldItalic.ttf")).deriveFont(12f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        nameLabel.setBounds(30, /*2 * picture.getY() + picture.getHeight()*/ WIDTH + 5, 170, 80);
        nameLabel.setFont(font);
        nameLabel.setForeground(Color.BLACK);

        add(nameLabel);

        double usualPrice = product.getPrice();
        double discountPrice = product.getDiscountPrice();

        JPanel pricesPanel = new JPanel();
        pricesPanel.setBounds(10, nameLabel.getY() + nameLabel.getHeight(), WIDTH - 10, 40);
        pricesPanel.setBackground(new Color(255, 255, 255));

        JLabel usualPriceLabel = new JLabel();
        if(usualPrice != discountPrice) {
            usualPriceLabel.setBounds(10, nameLabel.getY() + nameLabel.getHeight(), 50, 20);
            usualPriceLabel.setText("<html><strike>" + product.getPrice() + " ₽</strike></html>");
            usualPriceLabel.setFont(new Font("", Font.BOLD, 15));
            usualPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            pricesPanel.add(usualPriceLabel);
        }
        JLabel discountPriceLabel = new JLabel();
        discountPriceLabel.setBounds(usualPriceLabel.getWidth() + 20, nameLabel.getY() + nameLabel.getHeight(), 120, 30);
        discountPriceLabel.setText("<html>" + product.getDiscountPrice() + " ₽</html>");
        discountPriceLabel.setFont(new Font("", Font.BOLD, 25));
        discountPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        if(usualPrice != discountPrice) {
            discountPriceLabel.setForeground(Color.red);
        }

        pricesPanel.add(discountPriceLabel);

        add(pricesPanel);
        repaint();
    }

    public void addRatingButtons() {
        int y = HEIGHT - 60;
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, y, WIDTH, 60);
        BufferedImage picGood = null;
        BufferedImage picNormal = null;
        BufferedImage picBad = null;
        try {
            picGood = ImageIO.read(new File("pictures/good.png"));
            picNormal = ImageIO.read(new File("pictures/normal.png"));
            picBad = ImageIO.read(new File("pictures/bad.png"));
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        if(picGood != null && picNormal != null && picBad != null) {
            JLabel goodLabel = new JLabel(new ImageIcon(picGood));
            JLabel normalLabel = new JLabel(new ImageIcon(picNormal));
            JLabel badLabel = new JLabel(new ImageIcon(picBad));

            goodLabel.setBounds(10, 0, picGood.getWidth(), picGood.getHeight());
            normalLabel.setBounds(25 + picGood.getWidth(), 0, picNormal.getWidth(), picNormal.getHeight());
            badLabel.setBounds(40 + picGood.getWidth() + picNormal.getWidth(), 0, picBad.getWidth(), picBad.getHeight());

            layeredPane.add(goodLabel, Integer.valueOf(1));
            layeredPane.add(normalLabel, Integer.valueOf(1));
            layeredPane.add(badLabel, Integer.valueOf(1));

            layeredPane.add(labelSelected, Integer.valueOf(100));
            add(layeredPane);

            goodLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(product.getRating() == GOOD_PRODUCT_SELECTED) {
                        //selected = NOT_SELECTED;
                        product.setRating(NOT_SELECTED);
                        labelSelected.setVisible(false);
                    }
                    else {
                        //selected = GOOD_PRODUCT_SELECTED;
                        product.setRating(GOOD_PRODUCT_SELECTED);
                        labelSelected.setVisible(true);
                        paintSelectedRating(goodLabel);
                    }
                }
            });

            normalLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(product.getRating() == NORMAL_PRODUCT_SELECTED) {
                        //selected = NOT_SELECTED;
                        product.setRating(NOT_SELECTED);
                        labelSelected.setVisible(false);
                    }
                    else {
                        //selected = NORMAL_PRODUCT_SELECTED;
                        product.setRating(NORMAL_PRODUCT_SELECTED);
                        labelSelected.setVisible(true);
                        paintSelectedRating(normalLabel);
                    }
                }
            });

            badLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(product.getRating() == BAD_PRODUCT_SELECTED) {
                        //selected = NOT_SELECTED;
                        product.setRating(NOT_SELECTED);
                        labelSelected.setVisible(false);
                    }
                    else {
                        //selected = BAD_PRODUCT_SELECTED;
                        product.setRating(BAD_PRODUCT_SELECTED);
                        labelSelected.setVisible(true);
                        paintSelectedRating(badLabel);
                    }
                }
            });
        }
    }

    private void paintSelectedRating(JLabel label) {
        labelSelected.setBounds(label.getX(), label.getY(), labelSelectedWidth, labelSelectedHeight);
        repaint();
    }

    public int getSelected() {
        return product.getRating();
    }

    public Product getProduct() {
        return product;
    }
}
