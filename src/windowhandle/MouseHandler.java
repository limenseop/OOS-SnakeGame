/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package src.windowhandle;

import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

public class MouseHandler {
    private long window;
    private boolean[] mousebuttons = new boolean[GLFW_MOUSE_BUTTON_LAST];
    public MouseHandler(long window) {
        this.window = window;
    }
    private MouseHandler() {

    }
    public boolean isMouseDown ( int mouseButton){
        return glfwGetMouseButton(window, mouseButton) == 1;
    }
    public boolean isMousePressed ( int mouseButton){
        return isMouseDown(mouseButton) && !mousebuttons[mouseButton];
    }
    public boolean isMouseReleased ( int mouseButton){
        return !isMouseDown(mouseButton) && mousebuttons[mouseButton];
    }
    public void eventsUpdater () {
        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++)
            mousebuttons[i] = isMouseDown(i);
    }
    public double getMouseX () {
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, buffer, null);
        return buffer.get(0);
    }
    public double getMouseY () {
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, null, buffer);
        return buffer.get(0);
    }

    public double getMousePressedX(){
        if(isMousePressed(GLFW_MOUSE_BUTTON_LEFT)){
            return getMouseX();
        }
        else{
            return 0;
        }
    }

    public double getMousePressedY(){
        if(isMousePressed(GLFW_MOUSE_BUTTON_LEFT) && !isMouseReleased(GLFW_MOUSE_BUTTON_LEFT)){
            return getMouseY();
        }
        else{
            return 0;
        }
    }
}