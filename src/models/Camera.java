/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package src.models;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private final int width, height;

    private int dynamicewidth, dynamicheight;
    private Vector3f position;
    private Matrix4f projection;

    private final double MAX_CAM_VIEW = 4.5;
    //TODO 넓게

    public Camera(int width, int height) {
        this.width = width;
        this.height = height;
        dynamicewidth = width;
        dynamicheight = height;
        position = new Vector3f(0, 0, 0);
        projection = new Matrix4f().setOrtho2D(-width/2, width/2, -height/2,height/2);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
    public void addPosition(Vector3f position) {
        this.position.add(position);
    }
    public Vector3f getPosition() {
        return position;
    }
    public Matrix4f getProjection() {
        return projection.translate(position, new Matrix4f());
    }
    //mainwindow.getWidth(), mainwindow.getHeight())
    public void reinit(int width, int height){
        position = new Vector3f(0, 0, 0);
        projection = new Matrix4f().setOrtho2D(-width/2, width/2, -height/2,height/2);
    }
    public int getWidth() {
        return dynamicewidth;
    }
    public int getHeight() {
        return dynamicheight;
    }
    protected void setViewsize(double size) {
        if (size > MAX_CAM_VIEW)
            size = MAX_CAM_VIEW;
        dynamicewidth = (int)(width * size);
        dynamicheight = (int)(height * size);
        projection.setOrtho2D(-dynamicewidth/2, dynamicewidth/2, -dynamicheight/2, dynamicheight/2);
    }
}
