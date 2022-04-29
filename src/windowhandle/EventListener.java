/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package src.windowhandle;

import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

public class EventListener {
    private long window;
    private boolean[] keys = new boolean[GLFW_KEY_LAST];
    private boolean[] mousebuttons = new boolean[GLFW_MOUSE_BUTTON_LAST];
    public EventListener(long window) {
        this.window = window;
    }
    private EventListener() {

    }
    public boolean isKeyDown ( int keyCode){
        return glfwGetKey(window, keyCode) == 1;
    }
    public boolean isMouseDown ( int mouseButton){
        return glfwGetMouseButton(window, mouseButton) == 1;
    }
    public boolean isKeyPressed ( int keyCode){
        return isKeyDown(keyCode) && !keys[keyCode];
    }
    public boolean isMousePressed ( int mouseButton){
        return isMouseDown(mouseButton) && !mousebuttons[mouseButton];
    }
    public boolean isKeyReleased ( int keyCode){
        return !isKeyDown(keyCode) && keys[keyCode];
    }
    public boolean isMouseReleased ( int mouseButton){
        return !isMouseDown(mouseButton) && mousebuttons[mouseButton];
    }
    public void eventsUpdater () {
        for (int i = 0; i < GLFW_KEY_LAST; i++)
            keys[i] = isKeyDown(i);
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
}