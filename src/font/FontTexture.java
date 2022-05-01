package src.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;


public class FontTexture {
	private Font font;

	private Texture texture;

	private String[] additionalStrings;

	private int width;
	private int height;

	Map<Character, CharInfo> charMap;

	public FontTexture(Font font, String... additionals) {
		this.font = font;
		charMap = new HashMap<Character, CharInfo>();
		this.additionalStrings = additionals;

		buildTexture();
	}

	private String getAllLetters() {
		StringBuilder result = new StringBuilder();

		for (String str : this.additionalStrings) {
			for (int i = 0; i < str.length(); i++) {
				String c = str.substring(i, i + 1);
				if (result.indexOf(c) == -1) {
					result.append(c);
				}
			}
		}

		for (int i = 32; i < 126; i++) {
			result.append((char) i);
		}
	
		return result.toString();
	}

	private void buildTexture() {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = img.createGraphics();
		g2D.setFont(this.font);
		FontMetrics fontMetric = g2D.getFontMetrics();

		String allChars = getAllLetters();

		this.width = 0;
		this.height = 0;

		for (char c : allChars.toCharArray()) {
			CharInfo charInfo = new CharInfo(width, fontMetric.charWidth(c));
			charMap.put(c, charInfo);
			width += charInfo.getWidth();
			height = Math.max(height, fontMetric.getHeight());
		}
		g2D.dispose();

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2D = img.createGraphics();
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setFont(font);
		fontMetric = g2D.getFontMetrics();
		g2D.setColor(Color.WHITE);

		g2D.drawString(allChars, 0, fontMetric.getAscent());
		g2D.dispose();

		try {
			ImageIO.write(img, "PNG", new java.io.File("text.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int[] pixels = new int[width * height];
		img.getRGB(0, 0, width, height, pixels, 0, width);

		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = pixels[i * width + j];

				// put rgba
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		buffer.flip();

		// Create texture
		texture = new Texture(width, height, buffer);
		buffer.clear();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Texture getTexture() {
		return texture;
	}

}
