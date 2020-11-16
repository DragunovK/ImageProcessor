/*
 * Created by Kirill Dragunov on 20 oct. 2020 14:58:46
 */
package com;

import com.compressor.ImageProcessor;
import com.config.Configuration;
import com.file_service.FileService;
import com.file_service.ImageService;

import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        ImageService imageService = new ImageService();

        ImageProcessor compressor = new ImageProcessor(
                Configuration.rectangle_height,
                Configuration.rectangle_width,
                imageService.read(Configuration.input_image_path)
        );

        BufferedImage processedImage = compressor.process();

        imageService.write(
                Configuration.output_image_path,
                Configuration.image_type,
                processedImage
        );

        FileService.out();
    }
}
