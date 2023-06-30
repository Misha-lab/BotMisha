package com.company.view;

import com.company.parsers.ImageDownloader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackgroundView {
    public static JLabel backgroundPicture(int width, int height, int num) {
        BufferedImage backgroundPic = null;
        try {
            backgroundPic = ImageIO.read(new File("pictures/background" + num + ".jpg"));
            if(backgroundPic != null)
                backgroundPic = ImageDownloader.resize(backgroundPic, width, height);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

        assert backgroundPic != null;
        JLabel background = new JLabel(new ImageIcon(backgroundPic));
        background.setBounds(0,0, backgroundPic.getWidth(), backgroundPic.getHeight());
        return background;
    }
}
