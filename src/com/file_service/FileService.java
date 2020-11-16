/*
 * Created by Kirill Dragunov on 25 oct. 2020 1:01:04
 */
package com.file_service;

import com.config.Configuration;
import org.ejml.simple.SimpleMatrix;

import java.io.*;

/**
 * File service. Is used for creating a summary file containing
 * processing configuration information, as well as training and processing results information
 */
public class FileService {
    private static String summary = "";

    public static void learningSummary(SimpleMatrix firstLayerWeights,
                                       SimpleMatrix secondLayerWeights,
                                       double error,
                                       int steps) {
        summary = "Summary:" +
                "\n\tBlock height: " + Configuration.rectangle_height +
                "\n\tBlock width: " + Configuration.rectangle_width +
                "\n\tNumber of hidden layer neurons: " + Configuration.second_layer_neurons_number +
                "\n\tLearning rate: " + Configuration.learning_rate +
                "\n\tLearning steps: " + steps +
                "\n\tFinal error: " + error +
                "\n\tFinal 1st layer weight matrix:\n\t\t" + firstLayerWeights +
                "\n\tFinal 2nd layer weight matrix:\n\t\t" + secondLayerWeights +
                "\n";
    }

    public static void compressionQuotient(double quotient) {
        summary += "Compression quotient: " + quotient + "\n";
    }

    public static void out() {
        try {
            File file = new File(Configuration.output_info_path);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            Writer writer = new BufferedWriter(outputStreamWriter);
            writer.write(summary);
            writer.close();
        } catch (Exception ignored) { }
    }
}
