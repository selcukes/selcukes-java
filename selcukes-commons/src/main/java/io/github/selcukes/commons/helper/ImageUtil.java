/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.commons.helper;

import io.github.selcukes.commons.exception.SnapshotException;
import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A utility class for image processing.
 */
@UtilityClass
public class ImageUtil {
    private final String ERROR_WHILE_CONVERTING_IMAGE = "Error while converting image";

    /**
     * Convert an InputStream to a BufferedImage.
     *
     * @param  is The input stream of the image
     * @return    A BufferedImage
     */
    public BufferedImage toBufferedImage(final InputStream is) {

        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            throw new SnapshotException(ERROR_WHILE_CONVERTING_IMAGE, e);
        }
    }

    /**
     * Convert a byte array to a BufferedImage.
     *
     * @param  bytes The byte array that contains the image data.
     * @return       A BufferedImage
     */
    public BufferedImage toBufferedImage(final byte[] bytes) {

        try (ByteArrayInputStream imageArrayStream = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(imageArrayStream);
        } catch (IOException e) {
            throw new SnapshotException(ERROR_WHILE_CONVERTING_IMAGE, e);
        }
    }

    /**
     * Convert a BufferedImage to a byte array.
     *
     * @param  image The image to be converted to a byte array.
     * @return       A byte array
     */
    public byte[] toByteArray(final BufferedImage image) {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SnapshotException(ERROR_WHILE_CONVERTING_IMAGE, e);
        }
    }

    /**
     * It takes two images and stitches them together
     *
     * @param  image1    The first image to be stitched
     * @param  image2    The image to be stitched
     * @param  asOverlay If true, the images will be stitched as an overlay. If
     *                   false, the images will be stitched side by side.
     * @return           A BufferedImage
     */
    private BufferedImage stitchImages(
            final BufferedImage image1, final BufferedImage image2, final boolean asOverlay
    ) {
        if (asOverlay) {
            int x = Math.max(image1.getWidth(), image2.getWidth());
            int y = Math.max(image1.getHeight(), image2.getHeight());
            BufferedImage stitchedImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
            Graphics g = stitchedImage.getGraphics();
            g.drawImage(image1, 0, 0, null);
            g.drawImage(image2, image1.getWidth() - image2.getWidth(), image1.getHeight() - image2.getHeight(), null);
            return stitchedImage;
        } else {
            BufferedImage stitchedImage = new BufferedImage(image1.getWidth(), image1.getHeight() + image2.getHeight(),
                BufferedImage.TYPE_INT_RGB);
            Graphics graphics = stitchedImage.getGraphics();
            graphics.drawImage(image1, 0, 0, null);
            graphics.drawImage(image2, 0, image1.getHeight(), null);
            graphics.setColor(Color.BLACK);
            graphics.drawLine(0, image1.getHeight(), image1.getWidth(), image1.getHeight());
            return stitchedImage;
        }
    }

    /**
     * Generate an image with the logo and text, then stitch it to the bottom of
     * the image.
     *
     * @param  text  The text to be displayed on the image.
     * @param  image The image to be watermarked
     * @return       A BufferedImage
     */
    public BufferedImage generateImageWithLogo(final String text, final BufferedImage image) {
        BufferedImage logoAndTextImage = generateImageWithLogoAndText(text, image.getWidth());
        return stitchImages(image, logoAndTextImage, false);
    }

    /**
     * Generate an address bar image with a logo, then stitch it with another
     * image.
     *
     * @param  image1 The image that will be used for the address bar.
     * @param  image2 The image that will be stitched to the right of the
     *                address bar.
     * @return        A BufferedImage
     */
    public BufferedImage generateImageWithAddressBar(final BufferedImage image1, final BufferedImage image2) {
        BufferedImage addressBarImage = generateAddressBarImageWithLogo(image1);
        return stitchImages(addressBarImage, image2, false);
    }

    private BufferedImage generateAddressBarImageWithLogo(final BufferedImage addressBarImage) {
        BufferedImage textImage = generateImageWithText("selcukes", addressBarImage.getWidth(), 70);
        return stitchImages(textImage, addressBarImage, true);
    }

    private BufferedImage generateImageWithLogoAndText(final String text, final int screenshotWidth) {
        BufferedImage textImage = generateImageWithText(text, screenshotWidth, 200);
        BufferedImage logo = toBufferedImage(FileHelper.loadResourceAsStream("selcukes_logo.png"));
        return stitchImages(textImage, logo, true);
    }

    private BufferedImage generateImageWithText(final String text, final int width, final int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(new Color(220, 218, 218));
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.RED);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        drawMultiLineString(graphics, text, (width - 200) - 10);
        return image;
    }

    private void drawMultiLineString(final Graphics g, final String text, final int lineWidth) {
        FontMetrics m = g.getFontMetrics();
        int xPosition = 10;
        int yPosition = 25;
        for (String line : text.split("\n")) {
            String[] words = line.trim().split("\\b");

            for (String word : words) {
                if (xPosition + m.stringWidth(word) < lineWidth) {
                    g.drawString(word, xPosition, yPosition);
                    xPosition += m.stringWidth(word);
                } else {
                    xPosition = 10;
                    yPosition += m.getHeight();
                    g.drawString(word, xPosition, yPosition);
                    xPosition += m.stringWidth(word);
                }
            }
            xPosition = 10;
            yPosition += m.getHeight();
        }

    }
}
