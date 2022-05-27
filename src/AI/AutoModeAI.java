package src.AI;

import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import src.board.Board;
import src.board.TileRenderer;
import src.controller.GameState;
import src.domain.GameBoard;
import src.domain.Direction;
import src.domain.Ranking;
import src.domain.Renderer;
import src.domain.Snake;
import src.domain.snakeBody;
import src.font.FontRenderer;
import src.font.FontTexture;
import src.models.Camera;
import src.models.Shader;
import src.windowhandle.MouseHandler;
import src.windowhandle.Window;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutoModeAI {

	private Window mainwindow;
	private Shader shader;
	private Camera cam;
	private Board mainboard;
	private Renderer renderer;
	private MouseHandler mouseListener;
	private boolean on_Running = true;
	private String input = "";
	private TileRenderer tilerender;
	private FontRenderer fontRenderer;
	private Font font;
	private FontTexture fontTexture;

	private GameState state = GameState.GAME_ACTIVE;
	private GameBoard gameboard;

	public AutoModeAI(GameBoard gameboard, Window window, Shader shader, Camera cam, Board board) {
		this.mainwindow = window;
		this.shader = shader;
		this.cam = cam;
		this.gameboard = gameboard;
		this.mainboard = board;
		tilerender = new TileRenderer();
	}

	public void run() {
		init();
		loop();
		terminate();
	}

	private void init() {

		fontRenderer = new FontRenderer();
		font = new Font("Time Roman", Font.PLAIN, 40);
		fontTexture = new FontTexture(font, "US-ASCII");

		mouseListener = new MouseHandler(mainwindow.getWindow());
		state = GameState.GAME_INIT;
		renderer = new Renderer();
		GLFWKeyCallback keyCallback_ESC = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				switch (state) {
				case GAME_ACTIVE -> {
					float fruit_X = gameboard.getFruitPositionX();
					float fruit_Y = gameboard.getFruitPositionY();
					List<Point2D> fruitPosition = gameboard.getFruitPosition();
					float headX = 1;
					float headY = 1;
					Point2D Head = new Point2D.Float(headX, headY);
					Direction direction = snakeBody.getDirection();
					if (direction == Direction.WEST) {
						if (fruit_X == headX) {
							if (fruit_Y > headY)	//과일 위치와 SnakeHead의 위치 및 방향를 비교해 다음 방향을 결정, 누락된 경우는 직진
								gameboard.change_Direction_Snake(Direction.NORTH);
							if (fruit_Y < headY)
								gameboard.change_Direction_Snake(Direction.SOUTH);
						}
						if (fruit_Y == headY) {
							if (fruit_X < headX)
								gameboard.change_Direction_Snake(Direction.NORTH);
						}
						if (fruit_X > headX) {
							if (fruit_Y > headY)
								gameboard.change_Direction_Snake(Direction.NORTH);
							if (fruit_Y < headY)
								gameboard.change_Direction_Snake(Direction.SOUTH);
						}
						if (headX < 4) {	//벽과의 위치를 임의로 4로 설정 후, 오른쪽으로 방향을 전환
							if(headY > -42)
								gameboard.change_Direction_Snake(Direction.SOUTH);
							if (headY < -42)
								gameboard.change_Direction_Snake(Direction.NORTH);
						}
					}
					if (direction == Direction.EAST) {
						if (fruit_X == headX) { // Fruit와 SnakeHead의 x좌표 값이 같지만 y좌표가 다른 경우
							if (fruit_Y > headY)
								gameboard.change_Direction_Snake(Direction.NORTH);
							if (fruit_Y < headY)
								gameboard.change_Direction_Snake(Direction.SOUTH);
						}
						if (fruit_Y == headY) {
							if (fruit_X > headX)
								gameboard.change_Direction_Snake(Direction.SOUTH);

						}

						if (fruit_X < headX) {
							if (fruit_Y > headY)
								gameboard.change_Direction_Snake(Direction.NORTH);
							if (fruit_Y < headY)
								gameboard.change_Direction_Snake(Direction.SOUTH);
						}
						if (headX > 80) {
							if(headY > -42)
								gameboard.change_Direction_Snake(Direction.SOUTH);
							if (headY < -42)
								gameboard.change_Direction_Snake(Direction.NORTH);
						}
					}
					if (direction == Direction.NORTH) {
						if (fruit_X == headX) {

							if (fruit_Y < headY)
								gameboard.change_Direction_Snake(Direction.EAST);
						}
						if (fruit_Y == headY) {
							if (fruit_X > headX)
								gameboard.change_Direction_Snake(Direction.EAST);
							if (fruit_X < headX)
								gameboard.change_Direction_Snake(Direction.WEST);
						}
						if (fruit_X > headX) {

							if (fruit_Y < headY)
								gameboard.change_Direction_Snake(Direction.EAST);
						}
						if (fruit_X < headX) {

							if (fruit_Y < headY)
								gameboard.change_Direction_Snake(Direction.WEST);
						}
						if (headY < -4) {
							if(headX > 42)
								gameboard.change_Direction_Snake(Direction.WEST);
							if (headX < 42)
								gameboard.change_Direction_Snake(Direction.EAST);
						}
					}
					if (direction == Direction.SOUTH) {
						if (fruit_X == headX) {
							if (fruit_Y > headY)
								gameboard.change_Direction_Snake(Direction.WEST);

						}
						if (fruit_Y == headY) {
							if (fruit_X > headX)
								gameboard.change_Direction_Snake(Direction.EAST);
							if (fruit_X < headX)
								gameboard.change_Direction_Snake(Direction.WEST);
						}
						if (fruit_X > headX) {
							if (fruit_Y > headY)
								gameboard.change_Direction_Snake(Direction.EAST);

						}
						if (fruit_X < headX) {
							if (fruit_Y > headY)
								gameboard.change_Direction_Snake(Direction.WEST);

						}
						if (headY > -80) {
							if(headX > 42)
								gameboard.change_Direction_Snake(Direction.WEST);
							if (headX < 42)
								gameboard.change_Direction_Snake(Direction.EAST);
						}
					}

				}
				}
			}
		};
		GLFW.glfwSetKeyCallback(mainwindow.getWindow(), keyCallback_ESC);
	}

	private void terminate() {
		Callbacks.glfwFreeCallbacks(mainwindow.getWindow());
		GLFW.glfwDestroyWindow(mainwindow.getWindow());
	}

	private void loop() {
		while (on_Running) {
			while (gameboard.gameRunning()) {
				switch (state) {
				case GAME_INIT -> {
					if (mainwindow.isUpdating()) {
						mouseOptionHandle();
						mainwindow.update();
						renderer.mainmenurender(shader, mainboard);
						mainwindow.swapBuffer();
					}
				}
				case GAME_ACTIVE -> {
					if (mainwindow.isUpdating()) {
						mainwindow.update();
						gameboard.move_Snake();
						gameboard.check_Fruit_Overlap();
						gameboard.check_Game_Terminated();
						renderer.setBoard(gameboard);
						mainboard.correctCameara(cam, mainwindow);
						mainboard.render(tilerender, shader, cam, mainwindow);
						renderer.render(shader, cam, mainboard);
						scoreRender();
						mainwindow.swapBuffer();
					}
					break;
				}
				case GAME_MENU -> {
					mouseOptionHandle();
					mainwindow.update();
					mainboard.correctCameara(cam, mainwindow);
					renderer.pausemenurender(shader, mainboard);
					mainwindow.swapBuffer();
					mainwindow.timeHandle();
					break;
				}
				case GAME_TYPING -> {
					mainwindow.update();
					mainboard.correctCameara(cam, mainwindow);
					mainboard.render(tilerender, shader, cam, mainwindow);
					renderer.inputRendering(shader, mainboard);
					renderNickname();
					mainwindow.swapBuffer();
					mainwindow.timeHandle();
					break;
				}
				case GAME_RANKING -> {
					mainwindow.update();
					mainboard.correctCameara(cam, mainwindow);
					renderer.rankingRendering(shader, mainboard);
					renderRankings();
					mainwindow.swapBuffer();
					mainwindow.timeHandle();
					break;
				}
				}
			}
			state = GameState.GAME_INIT;
			gameboard.re_Play();
			renderer.setZeroFocus(cam, mainboard);
		}
	}

	private void scoreRender() {
		int score = gameboard.getScore();
		String guiString = "score : " + score;
		fontRenderer.renderString(fontTexture, guiString, 700, 10, new Vector3f(11, 11, 0));
		if (gameboard.getRankings().isEmpty()) {
			return;
		} else if (score > gameboard.getRankings().get(0).getScore()) {
			fontRenderer.renderString(fontTexture, "new record!", 700, 70, new Vector3f(11, 11, 0));
		}
	}

	private void renderNickname() {

		String nick_show = "Your nickname : " + input;
		fontRenderer.renderString(fontTexture, nick_show, 100, 500, new Vector3f(11, 11, 0));
	}

	private void mouseOptionHandle() {
		double mouse_X = mouseListener.getMousePressedX();
		double mouse_Y = mouseListener.getMousePressedY();
		mouseListener.eventsUpdater();
		mouseHandle(mouse_X, mouse_Y);
	}

	private void mouseHandle(double cursorX, double cursorY) {
		if (cursorX == 0 && cursorY == 0)
			return;
		Point2D cursor = new Point2D.Float((float) cursorX, (float) cursorY);
		System.out.println("cursor = " + cursor);
		switch (state) {
		case GAME_INIT -> {
			if (cursorX >= 253 && cursorX <= 396 && cursorY >= 224 && cursorY <= 270) {
				gameboard.re_Play();
				state = GameState.GAME_TYPING;
			} else if (cursorX >= 253 && cursorX <= 396 && cursorY >= 311 && cursorY <= 360) {
				try {
					gameboard.loadGame();
					mainboard.correctCameara(cam, mainwindow);
					mainboard.render(tilerender, shader, cam, mainwindow);
				} catch (Exception e) {
					System.out.println("e = " + e);
				}
				state = GameState.GAME_ACTIVE;
			} else if (cursorX >= 191 && cursorX <= 460 && cursorY >= 398 && cursorY <= 446) {
				state = GameState.GAME_RANKING;
			} else if (cursorX >= 262 && cursorX <= 390 && cursorY >= 490 && cursorY <= 535) {
				try {
					gameboard.save_Ranking();
				} catch (IOException e) {
					e.printStackTrace();
				}
				gameboard.gameTerminate();
				on_Running = false;
			}
		}
		case GAME_MENU -> {
			if (cursorX >= 206 && cursorX <= 441 && cursorY >= 272 && cursorY <= 320) {
				state = GameState.GAME_ACTIVE;
				// resume
			} else if (cursorX >= 195 && cursorX <= 445 && cursorY >= 356 && cursorY <= 405) {
				gameboard.re_Play();
				state = GameState.GAME_ACTIVE;
				// replay
			} else if (cursorX >= 252 && cursorX <= 395 && cursorY >= 443 && cursorY <= 492) {
				try {
					gameboard.save_This_Game();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// game save
			} else if (cursorX >= 263 && cursorX <= 385 && cursorY >= 530 && cursorY <= 574) {
				state = GameState.GAME_INIT;
			}
			break;
		}
		}
	}

	private boolean isValid(int ascii) {
		if ((ascii >= 48 && ascii <= 57) || (ascii >= 65 && ascii <= 90) || (ascii >= 97 && ascii <= 122))
			return true;
		return false;
	}

	private void renderRankings() {
		List<Ranking> ranks = gameboard.getRankings();
		String id = "";
		int score = 0;
		String text;
		if (ranks.size() >= 5) {
			for (int i = 0; i < 5; i++) {
				id = ranks.get(0).getId();
				score = ranks.get(0).getScore();
				text = "rank" + (i + 1) + "   nickname = " + id + "   Score : " + score;
				fontRenderer.renderString(fontTexture, text, 100, 300 + 100 * i, new Vector3f(11, 11, 0));
			}
		} else {
			int count = 0;
			for (Ranking rank : ranks) {
				id = rank.getId();
				score = rank.getScore();
				text = "rank" + (count + 1) + "   nickname = " + id + "           Score : " + score;
				fontRenderer.renderString(fontTexture, text, 100, 300 + 100 * count, new Vector3f(11, 11, 0));
				count = count + 1;
			}
		}
		String pressESC = "Press ESC to back!";
		fontRenderer.renderString(fontTexture, pressESC, 335, 900, new Vector3f(11, 11, 0));
	}

}