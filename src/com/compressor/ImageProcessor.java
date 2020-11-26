/*
 * Created by Kirill Dragunov on 20 oct. 2020 14:59:48
 */
package com.compressor;

import com.config.Configuration;
import com.file_service.FileService;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;

/**
 *  Neural network-based image processor.
 *  Splits image into rectangles, creates neural network,
 *  passes rectangles into it for processing and recreates an image from processed rectangles.
 */
public class ImageProcessor {
    private final int blockHeight;
    private final int blockWidth;
    private final BufferedImage image;
    private final ArrayList<Rectangle> rectangles = new ArrayList<>();

    /**
     * @param blockHeight - parameter n
     * @param blockWidth - parameter m
     * @param image - image
     */
    public ImageProcessor(int blockHeight, int blockWidth, BufferedImage image) {
        this.blockHeight = blockHeight;
        this.blockWidth = blockWidth;
        this.image = image;

        generatePixelRectangles();
        generateReferenceVectors();
    }

    private void generatePixelRectangles() {
        Raster imageData = image.getData();
        int remainderHeight = imageData.getHeight() % blockHeight;
        int remainderWidth = imageData.getWidth() % blockWidth;

        int x; int y;
        //taking all blocks except remainder by x
        for (x = 0; x < imageData.getWidth() - remainderWidth; x += blockWidth) {
            for (y = 0; y < imageData.getHeight() - remainderHeight; y += blockHeight) {
                rectangles.add(new Rectangle(x, y,
                        image.getSubimage(x, y, blockWidth, blockHeight))
                );
            }

            if (remainderHeight != 0) {
                y -= (blockHeight - remainderHeight);
                //taking remainder by y
                rectangles.add(new Rectangle(x, y,
                        image.getSubimage(x, y, blockWidth, blockHeight))
                );
            }
        }
        //taking the rest of the blocks
        if (remainderWidth != 0) {
            x -= (blockWidth - remainderWidth);
            for (y = 0; y < imageData.getHeight() - remainderHeight; y += blockHeight) {
                rectangles.add(new Rectangle(x, y,
                        image.getSubimage(x, y, blockWidth, blockHeight))
                );
            }

            if (remainderHeight != 0) {
                y -= (blockHeight - remainderHeight);
                rectangles.add(new Rectangle(x, y,
                        image.getSubimage(x, y, blockWidth, blockHeight))
                );
            }
        }
    }

    private void generateReferenceVectors() {
        for (var item : rectangles) {
            item.generateReferenceVector();
        }
    }

    public BufferedImage process() {
        int referenceVectorSize = rectangles.get(0).getReferenceVectorSize();

        NeuralNetwork neuralNetwork = new NeuralNetwork(
                referenceVectorSize,
                Configuration.second_layer_neurons_number,
                Configuration.max_allowed_error,
                Configuration.learning_rate,
                rectangles
        );

        ArrayList<Rectangle> newRectangles = neuralNetwork.process();

        BufferedImage processedImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        for (var item : newRectangles) {
            processedImage.getGraphics().drawImage(
                    item.getImage(),
                    item.getX(),
                    item.getY(),
                    null
            );
        }

        double compressionQuotient = (referenceVectorSize * rectangles.size())
                / (double) ((referenceVectorSize + rectangles.size())
                * Configuration.second_layer_neurons_number + 2);

        System.out.println("Compression complete. Compression quotient: " + compressionQuotient);
        FileService.compressionQuotient(compressionQuotient);

        return processedImage;
    }
}