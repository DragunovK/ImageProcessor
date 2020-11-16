/*
 * Created by Kirill Dragunov on 20 oct. 2020 14:56:30
 */
package com.file_service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Image service. Used for reading and writing images
 */
public class ImageService {
    public BufferedImage read(String path) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(path));

            System.out.println(
                    "Read image " + path + ". Dimensions: " + image.getWidth() + " by " + image.getHeight()
            );
        } catch (Exception e) {
            System.out.println("File read unsuccessful");
            e.printStackTrace();
        }

        return image;
    }

    public void write(String path, String type, BufferedImage image) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException ignored) {
            //Ignored
        }

        try {
            ImageIO.write(image, type, new File(path));
        } catch (IOException e) {
            System.out.println("File write unsuccessful");
            e.printStackTrace();
        }
    }
}
