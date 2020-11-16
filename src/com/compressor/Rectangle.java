/*
 * Created by Kirill Dragunov on 21 oct. 2020 13:36:18
 */
package com.compressor;

import org.ejml.simple.SimpleMatrix;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class Rectangle {
    private int x;
    private int y;
    private BufferedImage image;
    private SimpleMatrix referenceVector; //X0

    public Rectangle(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public SimpleMatrix getReferenceVector() {
        return referenceVector;
    }

    public void setReferenceVector(SimpleMatrix referenceVector) {
        this.referenceVector = referenceVector;
    }

    public int getReferenceVectorSize() {
        return referenceVector.getNumElements();
    }

    public void generateReferenceVector() {
        ArrayList<Double> referenceVector_ = new ArrayList<>();

        for (int j = 0; j < image.getWidth(); j++) {
            for (int k = 0; k < image.getHeight(); k++) {
                int RGB = image.getRGB(j, k);
                referenceVector_.add(2 * (double) ((RGB & 0xff0000) >> 16) / 255 - 1); //R
                referenceVector_.add(2 * (double) ((RGB & 0xff00) >> 8) / 255 - 1); //G
                referenceVector_.add(2 * (double) (RGB & 0xff) / 255 - 1); //B
            }
        }

        double[][] values = new double[1][referenceVector_.size()];
        for (int i = 0; i < referenceVector_.size(); i++) {
            values[0][i] = referenceVector_.get(i);
        }
        referenceVector = new SimpleMatrix(values);
    }

    public void generateNewRGB() {
        referenceVector = (referenceVector.plus(1)).scale(255 / 2.0);

        int i = 0;
        for (int j = 0; j < image.getWidth(); j++) {
            for (int k = 0; k < image.getHeight(); k++) {
                Color color = new Color(
                        Math.max(Math.min((int) referenceVector.get(i++), 255), 0),
                        Math.max(Math.min((int) referenceVector.get(i++), 255), 0),
                        Math.max(Math.min((int) referenceVector.get(i++), 255), 0)
                );

                image.setRGB(j, k, color.getRGB());
            }
        }
    }
}
