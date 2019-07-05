package tech.tfletcher.game;

import tech.tfletcher.engine.IGameLogic;
import tech.tfletcher.engine.rendering.Renderer;
import tech.tfletcher.engine.rendering.Window;
import tech.tfletcher.engine.rendering.Mesh;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class TestGame implements IGameLogic {
    private int direction = 0;
    private float color = 0.0f;
    private final Renderer renderer;

    Mesh mesh;

    public TestGame(){
        renderer = new Renderer();
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        float[] positions = new float[]{
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
        };

        int[] indicies = new int[]{
          0, 1, 3, 3, 1, 2
        };

        mesh = new Mesh(positions, indicies);

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
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, mesh);

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }
}
