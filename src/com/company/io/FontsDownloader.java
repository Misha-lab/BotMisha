package com.company.io;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class FontsDownloader {
    public static final String FOLDER_NAME = "fonts";

    public static final Font font1 = new Font("Balsamiq Sans Bold", Font.PLAIN, 30);
    public static final Font font2 = new Font("Balsamiq Sans Regular", Font.PLAIN, 25);
    public static final Font font3 = new Font("Balsamiq Sans Bold", Font.PLAIN, 16);
    public static final Font font4 = new Font("Sherif", Font.BOLD, 25);
    public static final Font font5 = new Font("Sherif", Font.BOLD, 14);
    public static final Font font6 = new Font("Balsamiq Sans Regular", Font.PLAIN, 22);

    public static void addTtfFont(String filename) {
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(filename)));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public static void printAllFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        System.out.println(Arrays.toString(ge.getAllFonts()));
    }

    public static void addAllFonts() {
        File folder = new File(FOLDER_NAME);
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    addTtfFont(FOLDER_NAME + "/" + file.getName());
                }
            }
        }
    }
}
