package tech.tfletcher.game;

import tech.tfletcher.engine.GameObject;
import tech.tfletcher.engine.IGameLogic;
import tech.tfletcher.engine.rendering.Renderer;
import tech.tfletcher.engine.rendering.Window;
import tech.tfletcher.engine.rendering.Primitives.*;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class TestGame implements IGameLogic {
    private int direction = 0;
    private float color = 0.0f;
    private final Renderer renderer;

    GameObject[] gameObjects = new GameObject[1];

    public TestGame(){
        renderer = new Renderer();
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Triangle meshT = new Triangle();

        gameObjects[0] = new GameObject(meshT.mesh());
        gameObjects[0].setPosition(0,0,-3);

    }

    @Override
    public void input(Window window) {
        if(window.isKeyPressed(GLFW_KEY_UP)){
            direction = 1;
        } else if(window.isKeyPressed(GLFW_KEY_DOWN)){
            direction = -1;
        } else{
            direction = 0;
        }
    }

    @Override
    public void update(float interval) {
        color += direction * 0.01f;
        if(color > 1){
            color = 1.0f;
        } else if(color < 0){
            color = 0;
        }
        float y = gameObjects[0].getRotation().y;
        y += direction;
        gameObjects[0].setRotation(0, y, 0);
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);

        renderer.renderMesh(window, gameObjects);

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }
}
