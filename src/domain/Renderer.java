/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */



package src.domain;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import src.board.Board;
import src.entity.Transform;
import src.font.FontRenderer;
import src.font.FontTexture;
import src.models.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Renderer {

    Transform transform;

    private GameBoard gameBoard;

    private Texture SnakeBodyTex;
    private Texture SnakeHeadTex;
    private Texture appleTex;
    private Texture mainmenuTex;
    private Texture pauseMenuTex;
    private Texture RankingTex;


    private Basicmodel model;
    private Snakemodel snakemodel;
    private List<List<snakeBody>> snakes;

    private List<snakeBody> head;
    private List<Point2D> fruit;
    private Transform fruitPosition;
    private Transform zeroTransform;
    private Menumodel meunmodel;

    private FontRenderer fontRenderer;
    private Font font;
    private FontTexture fontTexture;


    public Renderer() {
        SnakeBodyTex = new Texture("SnakeGame_SnakeBody.png");
        SnakeHeadTex = new Texture("New Piskel.png");
        appleTex = new Texture("apple.png");
        mainmenuTex = new Texture("Background.png");
        pauseMenuTex = new Texture("Pause_Buttons.png");
        RankingTex = new Texture("RankingBackground.png");
        transform = new Transform();
        transform.scale = new Vector3f(16,16,1);
        transform.pos = new Vector3f(0,0,0);
        fruitPosition = new Transform();
        fruitPosition.scale = new Vector3f(16,16,1);
        fruitPosition.pos = new Vector3f(0,0,0);
        model = new Basicmodel();
        snakemodel = new Snakemodel();
        meunmodel = new Menumodel();
        zeroTransform = new Transform();
        zeroTransform.scale = new Vector3f(16,16,1);
        zeroTransform.pos = new Vector3f(0,0,0);
        fontRenderer = new FontRenderer();
        font = new Font("Time Roman", Font.PLAIN, 40);
        fontTexture = new FontTexture(font, "US-ASCII");

        head = new ArrayList<>();
        snakes = new ArrayList<>();
    }

    public void setBoard(GameBoard board){
        gameBoard = board;
        snakes.clear();
        head.clear();
        List<Snake> list = board.getSnake();
        int count = 0;
        for (Snake snake : list) {
            snakes.add(snake.getBody());
            head.add(snake.getHead());
            count = count + 1;
        }
        fruit = board.getFruitPosition();
    }

    public void render(Shader shader, Camera camera,Board board) {
        fruitPosition.pos.set((float) fruit.get(0).getX(), (float) fruit.get(0).getY(),0);
        setFocus(camera,board);
        for (List<snakeBody> snake : snakes) {
            transform.pos.set(snake.get(0).getPositionX(),snake.get(0).getPositionY(),0);
            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", transform.getProjection(camera.getProjection()));
            SnakeHeadTex.bind(0);
            snakemodel.spin(snake.get(0).getDirection());
            snakemodel.render();
            for (int i = 3; i < snake.size(); i++) {
                transform.pos.set(snake.get(i).getPositionX(), snake.get(i).getPositionY(), 0);
                shader.bind();
                shader.setUniform("sampler", 0);
                shader.setUniform("projection", transform.getProjection(camera.getProjection()));
                SnakeBodyTex.bind(0);
                model.render();
            }
        }
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", fruitPosition.getProjection(camera.getProjection()));
        appleTex.bind(0);
        model.render();
    }

    public void mainmenurender(Shader shader, Board mainboard) {
            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", new Matrix4f().setOrtho2D(-mainboard.getWidth() / 2, mainboard.getWidth() / 2, -mainboard.getHeight() / 2, mainboard.getHeight() / 2));
            mainmenuTex.bind(0);
            meunmodel.render();
    }

    public void pausemenurender(Shader shader, Board mainboard) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", new Matrix4f().setOrtho2D(-mainboard.getWidth() / 2, mainboard.getWidth() / 2, -mainboard.getHeight() / 2, mainboard.getHeight() / 2));
        pauseMenuTex.bind(0);
        meunmodel.render();
    }

    public void rankingRendering(Shader shader, Board mainboard) {
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", new Matrix4f().setOrtho2D(-mainboard.getWidth() / 2, mainboard.getWidth() / 2, -mainboard.getHeight() / 2, mainboard.getHeight() / 2));
        RankingTex.bind(0);
        meunmodel.render();
    }

    public void inputRendering(Shader shader, Board mainboard){
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", new Matrix4f().setOrtho2D(-mainboard.getWidth() / 2, mainboard.getWidth() / 2, -mainboard.getHeight() / 1, mainboard.getHeight() / 1));
        RankingTex.bind(0);
        meunmodel.render();
    }


    private void setFocus(Camera camera, Board board){
        Transform focus = new Transform();
        focus.scale = new Vector3f(16,16,1);
        focus.pos = new Vector3f((float) head.get(0).getPositionX(), (float) head.get(0).getPositionY(),0);
        camera.getPosition().lerp(focus.pos.mul(-board.getScale(), new Vector3f()), 0.1f);
    }

    public void setZeroFocus(Camera camera, Board board){
        camera.getPosition().lerp(zeroTransform.pos.mul(-board.getScale(), new Vector3f()), 0.1f);
    }

    public void scoreRender() {
        List<Integer> scores = gameBoard.getScores();
        int idx = 0;
        for (Integer score : scores) {
            String guiString = "score : " + score;
            fontRenderer.renderString(fontTexture,guiString,50 + 690*idx,10,new Vector3f(11,11,0));
            if(gameBoard.getRankings().isEmpty()){
                return;
            }
            else if(score > gameBoard.getRankings().get(0).getScore() && gameBoard.getPlayer_num() == 1){
                fontRenderer.renderString(fontTexture,"new record!",50,70,new Vector3f(11,11,0));
            }
            idx = idx + 1;
        }
    }

    public void nicknameRender(String input) {
        String nick_show = "Your nickname : " + input;
        fontRenderer.renderString(fontTexture,nick_show,100,500,new Vector3f(11,11,0));
    }

    public void rankingRender(){
        List<Ranking> ranks = gameBoard.getRankings();
        String id = "";
        int score = 0;
        String text;
        if(ranks.size()>=5){
            for(int i = 0;i<5;i++){
                id = ranks.get(0).getId();
                score = ranks.get(0).getScore();
                text = "rank"+(i+1)+"   nickname = " + id + "   Score : " + score;
                fontRenderer.renderString(fontTexture,text,100,300 + 100 * i,new Vector3f(11,11,0));
            }
        }
        else{
            int count = 0;
            for (Ranking rank : ranks) {
                id = rank.getId();
                score = rank.getScore();
                text = "rank"+(count+1)+"   nickname = " + id + "           Score : " + score;
                fontRenderer.renderString(fontTexture,text,100,300 + 100 * count,new Vector3f(11,11,0));
                count = count + 1;
            }
        }
        String pressESC = "Press ESC to back!";
        fontRenderer.renderString(fontTexture,pressESC,335,900,new Vector3f(11,11,0));
    }
}
