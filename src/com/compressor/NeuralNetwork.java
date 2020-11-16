/*
 * Created by Kirill Dragunov on 20 oct. 2020 15:10:40
 */
package com.compressor;

import com.file_service.FileService;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.Random;

/**
 * Neural network. Is used for image processing.
 */
public class NeuralNetwork {
    private final double maxAllowedError;
    private final double learningRate;
    private SimpleMatrix firstLayerWeightMatrix;
    private SimpleMatrix secondLayerWeightMatrix;
    private final ArrayList<Rectangle> rectangles;

    /**
     * @param referenceVectorSize - parameter N
     * @param numberOf2ndLayerNeurons - parameter p (usually is less or equal to 2 * N)
     * @param maxAllowedError - parameter e - maximum allowed error in range (0; 0.1*p]
     * @param learningRate - parameter alpha (alpha is less or equal to e)
     */
    public NeuralNetwork(int referenceVectorSize,
                         int numberOf2ndLayerNeurons,
                         double maxAllowedError,
                         double learningRate,
                         ArrayList<Rectangle> rectangles) {
        this.maxAllowedError = maxAllowedError;
        this.learningRate = learningRate;
        this.rectangles = rectangles;

        firstLayerWeightMatrix = SimpleMatrix.random_DDRM(
                referenceVectorSize,
                numberOf2ndLayerNeurons,
                -1, 1,
                new Random()
        );
        secondLayerWeightMatrix = firstLayerWeightMatrix.transpose();

        train();
    }

    private void train() {
        int i = 1;

        while (true) {
            double totalMSE = 0;

            for (var item : rectangles) {
                SimpleMatrix X = new SimpleMatrix(item.getReferenceVector());
                SimpleMatrix Y = X.mult(firstLayerWeightMatrix);
                SimpleMatrix X_ = Y.mult(secondLayerWeightMatrix);
                SimpleMatrix deltaX = X_.minus(X);

                firstLayerWeightMatrix = firstLayerWeightMatrix.minus(
                        ((X.transpose().scale(learningRate)).mult(deltaX)).mult(secondLayerWeightMatrix.transpose())
                );

                secondLayerWeightMatrix = secondLayerWeightMatrix.minus(
                        (Y.transpose().scale(learningRate)).mult(deltaX)
                );

                totalMSE += (deltaX.elementMult(deltaX)).elementSum();
            }

            System.out.println("Learning iteration " + i++ + ":\n\t\tTotal MSE: " + totalMSE);

            if (totalMSE <= maxAllowedError) {
                FileService.learningSummary(firstLayerWeightMatrix, secondLayerWeightMatrix, totalMSE, i - 1);
                break;
            }
        }

        System.out.println("Training complete.");
    }

    public ArrayList<Rectangle> process() {
        for (var item : rectangles) {
            SimpleMatrix X = new SimpleMatrix(item.getReferenceVector());
            SimpleMatrix Y = X.mult(firstLayerWeightMatrix);
            SimpleMatrix X_ = Y.mult(secondLayerWeightMatrix);

            item.setReferenceVector(X_);
            item.generateNewRGB();
        }

        return rectangles;
    }
}