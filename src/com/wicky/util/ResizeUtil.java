package com.wicky.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ResizeUtil {
    BufferedImage bufImage;
    int width;
    int height;

    public ResizeUtil() {
        // TODO Auto-generated constructor stub
    }

    public ResizeUtil(String srcPath, int width, int height) {
        this.width = width;
        this.height = height;
        try {
            this.bufImage = ImageIO.read(new File(srcPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage rize(BufferedImage srcBufImage, int width, int height) {

        BufferedImage bufTarget = null;

        double sx = (double) width / srcBufImage.getWidth();
        double sy = (double) height / srcBufImage.getHeight();

        int type = srcBufImage.getType();
        if (type == BufferedImage.TYPE_CUSTOM) {
            ColorModel cm = srcBufImage.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            bufTarget = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else bufTarget = new BufferedImage(width, height, type);

        Graphics2D g = bufTarget.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(srcBufImage, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return bufTarget;
    }

    public static ImageIcon resizeImageToScreenSize(ImageIcon imageIcon) {
        Image img = imageIcon.getImage();
        int imageWidth = img.getWidth(null);
        int imageHeight = img.getHeight(null);
        int screenWidth = DrawableUtils.getScreenWidth();
        int screenHeight = DrawableUtils.getScreenHeight();

        if (screenWidth == imageWidth && screenHeight == imageHeight) {
            return imageIcon;
        }

        // Draw the image on to the buffered image
        BufferedImage bimage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return new ImageIcon(ResizeUtil.rize(bimage, screenWidth, screenHeight));
    }
}
