package src.WindowHandler;

interface EventLister {
    abstract boolean isKeyDown(int keyCode);
    abstract boolean isMouseDown(int mouseButton);
    abstract boolean isKeyPressed(int keyCode);
    abstract boolean isMousePressed(int mouseButton);
    abstract boolean isKeyReleased(int keyCode);
    abstract boolean isMouseReleased(int mouseButton);
    abstract void eventsUpdater();
    abstract double getMouseX();
    abstract double getMouseY();
}