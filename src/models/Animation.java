/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */


package src.models;

import src.windowhandle.Window;

public class Animation {
    private Texture[] frames;
    private int pointer;

    private double elapsedTime;
    private double currentTime;
    private double lastTime;
    private double fps;

    public Animation(int amount, int fps, String filename) {
        this.pointer = 0;
        this.elapsedTime = 0;
        this.currentTime = 0;
        this.lastTime = Window.getTime();
        this.fps = 1.0/(double)fps;

        this.frames = new Texture[amount];
        for (int i = 0; i < amount; i++) {
            this.frames[i] = new Texture(
                    "animate/" + filename + "_" + i + ".png");
        }
    }
    public void bind() { bind(0); }
    public void bind(int sampler) {
        this.currentTime = Window.getTime();
        this.elapsedTime += currentTime - lastTime;

        if (elapsedTime >= fps) {
            elapsedTime -= fps;
            pointer++;
        }
        if (pointer > frames.length)
            pointer = 0;

        this.lastTime = currentTime;
        frames[pointer].bind(sampler);
    }

}
