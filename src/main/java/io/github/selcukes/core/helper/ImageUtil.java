/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.core.helper;


import io.github.selcukes.core.exception.SnapshotException;
import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class ImageUtil {
    private final String ERROR_WHILE_CONVERTING_IMAGE = "Error while converting image";

    public BufferedImage toBufferedImage(InputStream is) {

        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            throw new SnapshotException(ERROR_WHILE_CONVERTING_IMAGE, e);
        }
    }

    public BufferedImage toBufferedImage(byte[] bytes) {

        try (ByteArrayInputStream imageArrayStream = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(imageArrayStream);
        } catch (IOException e) {
            throw new SnapshotException(ERROR_WHILE_CONVERTING_IMAGE, e);
        }
    }

    public byte[] toByteArray(BufferedImage image) {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SnapshotException(ERROR_WHILE_CONVERTING_IMAGE, e);
        }
    }

    private BufferedImage stitchImages(BufferedImage image1, BufferedImage image2, boolean asOverlay) {
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

    public BufferedImage generateImageWithLogo(String text, BufferedImage image) {
        BufferedImage logoAndTextImage = generateImageWithLogoAndText(text, image.getWidth());
        return stitchImages(image, logoAndTextImage, false);
    }

    private BufferedImage generateImageWithLogoAndText(String text, int screenshotWidth) {
        BufferedImage textImage = generateImageWithText(text, screenshotWidth, 200);
        BufferedImage logo = toBufferedImage(ImageUtil.class.getClassLoader().getResourceAsStream("selcukes_logo.png"));
        return stitchImages(textImage, logo, true);
    }

    private BufferedImage generateImageWithText(String text, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(new Color(220, 218, 218));
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        drawStringMultiLine(graphics, text, (width - 200) - 10);
        return image;
    }

    private void drawStringMultiLine(Graphics g, String text, int lineWidth) {
        FontMetrics m = g.getFontMetrics();
        int xPosition = 10;
        int yPosition = 25;
        String[] words = text.trim().split("\\b");

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
    }
}


