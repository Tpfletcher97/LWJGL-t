package tech.tfletcher.gameInstances;

import org.joml.Vector2f;
import org.joml.Vector3f;
import tech.tfletcher.engine.GameObject;
import tech.tfletcher.engine.IGameLogic;
import tech.tfletcher.engine.Utility.Camera;
import tech.tfletcher.engine.Utility.MouseInput;
import tech.tfletcher.engine.Utility.OBJLoader;
import tech.tfletcher.engine.Utility.Texture;
import tech.tfletcher.engine.rendering.Material;
import tech.tfletcher.engine.rendering.Models.Mesh;
import tech.tfletcher.engine.rendering.PointLight;
import tech.tfletcher.engine.rendering.Renderer;
import tech.tfletcher.engine.rendering.Window;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class HexTest implements IGameLogic {
    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;

    public Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Vector3f ambientLight;
    private List<PointLight> pointLights = new ArrayList<>();


    private List<GameObject> gameObjects = new ArrayList<>();

    public HexTest(){
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0,0,0);
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        float reflectance = 1f;

        Mesh meshC = OBJLoader.loadMesh("/models/cube.obj");
        Texture texture = new Texture("/textures/block.png");
        Material mat = new Material(texture, reflectance);
        meshC.setMaterial(mat);

        GameObject cube = new GameObject(meshC);
        cube.setScale(0.03f);
        cube.setPosition(0, 0.25f ,0);
        gameObjects.add(cube);

        for(int i = 1; i < 10; i++){
            GameObject cube2 = new GameObject(meshC);
            cube2.setScale(0.03f);
            cube2.setPosition( 0, 0.25f, (i * 0.3f));
            gameObjects.add(cube2);
        }

        camera.setPosition(0.5f,0f, 0.5f);
        camera.setRotation(0, -45, 0);

        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);

        Vector3f lightColor = new Vector3f(0.5f, 0.5f, 0.5f);
        Vector3f lightPosition = new Vector3f(0,0f,0f);
        float lightIntensity = 1.0f;

        PointLight pointLight = new PointLight(lightColor, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.5f, 1.0f, 1.0f);
        pointLight.setAttenuation(att);

        pointLights.add(pointLight);
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

        //Debug camera fly
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

        //TODO: Minimise get calls

        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        if(mInput.isLeftButtonPressed()){
            Vector2f rotVec = mInput.getDisplVec();
            camera.moveRoation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        float cubeRotation = gameObjects.get(0).getRotation().x + 1.5f;
        if(cubeRotation > 360){
            cubeRotation = 0;
        }
        for(int i = 0; i < 10; i++){
            gameObjects.get(i).setRotation(cubeRotation, cubeRotation, cubeRotation);
        }

    }

    @Override
    public void render(Window window) {
        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        renderer.renderMesh(window, camera, gameObjects, ambientLight, pointLights);

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for(GameObject gameObject : gameObjects){
            gameObject.getMesh().cleanUp();
        }
    }
}
