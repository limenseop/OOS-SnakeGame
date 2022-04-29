package MainScreen;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.IntBuffer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class MainScreen extends JFrame {

	private long window;

	private Shader shader;

	private ImageIcon exitImage = new ImageIcon(MainScreen.class.getResource("../Image/SnakeGame_ExitButton.png"));
	private ImageIcon playImage = new ImageIcon(MainScreen.class.getResource("../Image/SnakeGame_PlayButton.png"));
	private ImageIcon rankingImage = new ImageIcon(
			MainScreen.class.getResource("../Image/SnakeGame_RankingButton.png"));
	private ImageIcon loadImage = new ImageIcon(MainScreen.class.getResource("../Image/SnakeGame_LoadButton.png"));

	public static int WIDTH = 650;
	public static int HEIGHT = 650;

	private JButton playButton = new JButton(playImage);
	private JButton exitButton = new JButton(exitImage);
	private JButton loadButton = new JButton(loadImage);
	private JButton rankingButton = new JButton(rankingImage);

	public void run() {
		init();
		loop();
		terminate();
	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

		window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, "SnakeGame v1.0", MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(window, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2);

		GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
					GLFW.glfwSetWindowShouldClose(window, true);
				}
			}
		};

		GLFW.glfwSetKeyCallback(window, keyCallback);

		GLFW.glfwMakeContextCurrent(window);

		GLFW.glfwSwapInterval(1);

		GLFW.glfwShowWindow(window);
	}

	float[] quadData = {

			-1.0f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f,

			-1.0f, -1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f };

	int quadVAO;
	int quadVBO;

	Texture texture;

	private void bindVAO() {
		quadVAO = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(quadVAO);

		quadVBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, quadVBO);

		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, quadData, GL15.GL_STATIC_DRAW);

		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * 4, 0);
		GL20.glEnableVertexAttribArray(0);

		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * 4, 2 * 4);
		GL20.glEnableVertexAttribArray(1);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	private void createTexture(String File) {
		texture = new Texture(File);
	}

	private void loop() {

		GL.createCapabilities();

		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		shader = new Shader("src/shaders/vertexShader.txt", "src/shaders/fragmentShader.txt");

		bindVAO();

		createTexture("Background.png");

		playButton.setBounds(250, 230, 150, 50);
		playButton.setBorderPainted(false);
		playButton.setContentAreaFilled(false);
		playButton.setFocusPainted(false);
		playButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				playButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		add(playButton);

		while (!GLFW.glfwWindowShouldClose(window)) {

			render();

			GLFW.glfwPollEvents();

			GLFW.glfwSwapBuffers(window);
		}

	}

	private void render() {

		shader.start();

		GL30.glBindVertexArray(quadVAO);

		texture.bind();

		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);

		GL30.glBindVertexArray(0);

		shader.stop();

	}

	private void terminate() {
		GL30.glDeleteVertexArrays(quadVAO);
		GL15.glDeleteBuffers(quadVBO);

		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}

	public static double getTime() {
		return GLFW.glfwGetTime();
	}

	public static void main(String[] args) {
		new MainScreen().run();
	}
}
