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
    private Texture autoMenuTex;


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
        mainmenuTex = new Texture("SnakeGame_NewBackground.png");
        pauseMenuTex = new Texture("Pause_Buttons.png");
        RankingTex = new Texture("RankingBackground.png");
        autoMenuTex = new Texture("Pause_AutoDual.png");
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
        if(!gameBoard.isAutoDual())
        pauseMenuTex.bind(0);
        else autoMenuTex.bind(0);
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

    private void setFocusforDual(DualCamera camera, Board board){

        //head - 1???
        //List<Snakebody> head

        Transform focus = new Transform();
        focus.scale = new Vector3f(16,16,1);
        focus.pos = new Vector3f(camera.getCenter(head.get(0), head.get(1)));
        camera.getPosition().lerp(focus.pos.mul(-board.getScale(), new Vector3f()), 0.1f);
        camera.setDualProjection(head.get(0), head.get(1));
    }

    public void renderforDual(Shader shader, DualCamera camera,Board board) {
        setFocusforDual(camera,board);
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
        int count = 0;
        for (Point2D point2D : fruit) {
            //System.out.println("point2D = " + point2D);
            fruitPosition.pos.set((float) fruit.get(count).getX(), (float) fruit.get(count).getY(),0);
            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", fruitPosition.getProjection(camera.getProjection()));
            appleTex.bind(0);
            model.render();
            count = count + 1;
        }
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
                //return;
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
                id = ranks.get(i).getId();
                score = ranks.get(i).getScore();
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

    public void render_result(){
        boolean is_dual = gameBoard.isAutoDual();
        if(!is_dual){
            //solo-mode??? result??????
            int scored = gameBoard.getScores().get(0);
            String gui_username = "<<" + gameBoard.getNickname()+">>";
            fontRenderer.renderString(fontTexture,gui_username,350,400,new Vector3f(10,10,0));
            String gui_string = "your score = " + scored;
            fontRenderer.renderString(fontTexture,gui_string,350,500,new Vector3f(10,10,0));
            String gui_ranking = "your ranking = " + gameBoard.ranking();
            fontRenderer.renderString(fontTexture,gui_ranking,350,600,new Vector3f(10,10,0));

        }
        else{
            int score_0 = gameBoard.getScores().get(0);
            int score_1 = gameBoard.getScores().get(1);
            int winner = 0;
            boolean is_draw = false;
            if(score_0>score_1) winner = 1;
            else if(score_1>score_0) winner = 2;
            else is_draw = true;
            if(is_draw){
                String gui_result_draw = "DRAW!!";
                fontRenderer.renderString(fontTexture,gui_result_draw,350,550,new Vector3f(10,10,0));
            }
            else{
                String gui_winner = "";
                if(gameBoard.isAuto()){
                    if(winner == 2)
                    gui_winner = "You win!";
                    else gui_winner = "You Lose!";
                }
                else
                gui_winner = "winner is player" + winner;
                fontRenderer.renderString(fontTexture,gui_winner,350,400,new Vector3f(10,10,0));
                String gui_score_versis = score_0 + " vs " + score_1;
                fontRenderer.renderString(fontTexture,gui_score_versis,350,500,new Vector3f(10,10,0));
            }
        }
        String pressESC = "Press ESC to go main menu!";
        fontRenderer.renderString(fontTexture,pressESC,250,900,new Vector3f(11,11,0));
    }
}
