/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.common.utils.ImageUtils
   Module Description   :

   Date Created      : 2012/5/24
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jeffma
 * 
 */
public class ImageUtils {

	public static Logger logger = LoggerFactory.getLogger(ImageUtils.class);

	public static File BACKGROUNDIMG = null;

	public static final int IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;

	private static Font font = new Font(null, Font.PLAIN, 20);
	private static boolean deepColor = false;
	private static int randLine = 5;

	/** default constructors */
	private ImageUtils() {
	}

	public static BufferedImage create(int width, int height, String text) {
		BufferedImage image;
		if (BACKGROUNDIMG != null) {
			try {
				image = ImageIO.read(BACKGROUNDIMG);
				if ((image.getWidth() != width) || (image.getHeight() != height)) {
					image = resize(image, width, height);
				}
			} catch (Exception e) {
				e.printStackTrace();
				image = new BufferedImage(width, height, IMAGE_TYPE);
			}
		} else {
			image = new BufferedImage(width, height, IMAGE_TYPE);
		}
		generateImage(image, text);

		return image;
	}

	public static BufferedImage resize(BufferedImage oldImg, int width, int height) {
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graph = newImg.createGraphics();

		// way1
		// graph.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		// graph.drawImage(oldImg, 0, 0, width, height, null);
		// way2
		AffineTransform xform = AffineTransform.getScaleInstance(width / oldImg.getWidth(), height / oldImg.getHeight());
		graph.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graph.drawImage(oldImg, xform, null);

		graph.dispose();
		return newImg;
	}

	public static void generateImage(BufferedImage image, String text) {
		Graphics graph = image.getGraphics();// createGraphics();
		if (BACKGROUNDIMG == null) {
			if (deepColor) {
				graph.setColor(Color.white);
			} else {
				graph.setColor(Color.darkGray);
			}
			graph.fillRect(0, 0, image.getWidth(), image.getHeight());
		}
		Random rand = new Random(Calendar.getInstance().getTimeInMillis());
		char[] t = text.toCharArray();
		int x = 5 + ((image.getWidth() - 20) / (t.length * 2));
		int y = image.getHeight() / 2;
		graph.setFont(font);
		for (int i = 0; i < t.length; i++) {
			graph.setColor(getColor(rand, deepColor));
			graph.drawString(String.valueOf(t[i]), x + rand.nextInt(5), y + rand.nextInt(20));
			// logger.debug("char:{},x:{},y:{}", new Object[] { t[i], x, y });
			x += ((image.getWidth() - 20) / t.length);
		}

		x = 5 + ((image.getWidth() - 20) / (t.length * 2));
		for (int i = 0; i < randLine; i++) {
			graph.setColor(getColor(rand, !deepColor));
			int len = ((image.getWidth() - 20) / randLine) + rand.nextInt(20);
			graph.drawLine(x, y + rand.nextInt(50), x + len, y + rand.nextInt(50));
			x += ((image.getWidth() - 20) / randLine);
		}
		graph.dispose();
	}

	private static Color getColor(Random rand, boolean deep) {
		// for rgb
		int[] c = { 127, 127, 127 };
		for (int i = 0; i < 3; i++) {
			int j = Math.abs(rand.nextInt(127));
			if (deep) {
				c[i] -= j;
			} else {
				c[i] += j;
			}
		}
		Color color = new Color(c[0], c[1], c[2]);

		// for hsb(色彩HUE飽和度Saturation和亮度Brightness)
		// float[] c = { 0.0f, 0.5f, 0.5f };
		// c[0] = Math.abs(rand.nextInt(360));
		// if (deep) {
		// c[1] += Math.abs(rand.nextFloat() / 2);
		// c[2] -= Math.abs(rand.nextFloat() / 3);
		// } else {
		// c[1] -= Math.abs(rand.nextFloat() / 2);
		// c[2] += Math.abs(rand.nextFloat() / 2);
		// }
		// Color color = Color.getHSBColor(c[0], c[1], c[2]);
		// logger.debug("color:{}/{}", color, c);
		return color;
	}

}
