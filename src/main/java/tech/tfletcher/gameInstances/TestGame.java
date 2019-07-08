package tech.tfletcher.gameInstances;

import org.joml.Vector2f;
import org.joml.Vector3f;
import tech.tfletcher.engine.GameObject;
import tech.tfletcher.engine.IGameLogic;
import tech.tfletcher.engine.Utility.Camera;
import tech.tfletcher.engine.Utility.MouseInput;
import tech.tfletcher.engine.rendering.Renderer;
import tech.tfletcher.engine.rendering.Window;
import tech.tfletcher.engine.rendering.Models.*;

import static org.lwjgl.glfw.GLFW.*;

public class TestGame implements IGameLogic {
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;

    public Vector3f cameraInc;

    private float dx = 0;
    private float dy = 0;
    private float dz = 0;

    private final Renderer renderer;

    private final Camera camera;


    private GameObject[] gameObjects = new GameObject[3];

    public TestGame(){
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0,0,0);
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Cube meshC = new Cube();

        gameObjects[0] = new GameObject(meshC.mesh());
        gameObjects[0].setScale(0.1f);
        gameObjects[0].setPosition(0.1f,0.0f,-2);

        gameObjects[1] = new GameObject(meshC.mesh());
        gameObjects[1].setScale(0.1f);
        gameObjects[1].setPosition(0,0,-2);

        gameObjects[2] = new GameObject(meshC.mesh());
        gameObjects[2].setScale(0.1f);
        gameObjects[2].setPosition(0,0,-2.1f);

        camera.setPosition(0,0, -1.5f);

    }

    @Override
    public void input(Window window, MouseInput mInput) {

        cameraInc.set(0,0,0);
        if(window.isKeyPressed(GLFW_KEY_A)){
            cameraInc.x = -0.1f;
        } else if(window.isKeyPressed(GLFW_KEY_D)){
            cameraInc.x = 0.1f;
        } else{
            cameraInc.x = 0;
        }

        if(window.isKeyPressed(GLFW_KEY_W)){
            cameraInc.z = -0.1f;
        } else if(window.isKeyPressed(GLFW_KEY_S)){
            cameraInc.z = 0.1f;
        } else{
            cameraInc.z = 0;
        }

        if(window.isKeyPressed(GLFW_KEY_SPACE)){
            cameraInc.y = 0.1f;
        } else if(window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
            cameraInc.y = -0.1f;
        } else{
            cameraInc.y = 0;
        }


    }

    @Override
    public void update(float interval, MouseInput mInput) {
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        if(mInput.isLeftButtonPressed()){
            Vector2f rotVec = mInput.getDisplVec();
            camera.moveRoation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        float cubeRotation = gameObjects[0].getRotation().x + 1.5f;
        if(cubeRotation > 360){
            cubeRotation = 0;
        }

        gameObjects[0].setRotation(cubeRotation, cubeRotation, cubeRotation);
    }

    @Override
    public void render(Window window) {
        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        renderer.renderMesh(window, gameObjects, camera);

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for(GameObject gameObject : gameObjects){
            gameObject.getMesh().cleanUp();
        }
    }
}
