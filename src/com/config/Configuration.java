/*
 * Created by Kirill Dragunov on 22 oct. 2020 12:29:20
 */
package com.config;

/**
 * Configuration. Is used to set different parameters of image processing and network learning
 */
public class Configuration {
    public static final int rectangle_width = 8;
    public static final int rectangle_height = 8;

    public static final int second_layer_neurons_number = 64;
    public static final double max_allowed_error = 1000;
    public static final double learning_rate = 0.001;

    public static final String image_name = "5";
    public static final String image_type = "jpg";
    public static final String input_image_path =
            System.getProperty("user.dir") + "\\image\\imageIn\\" + image_name + "." + image_type;
    public static final String output_image_path =
            System.getProperty("user.dir") + "\\image\\imageOut\\" + image_name + "\\ResultImage." + image_type;
    public static final String output_info_path =
            System.getProperty("user.dir") + "\\image\\imageOut\\" + image_name + "\\info.txt";
}
