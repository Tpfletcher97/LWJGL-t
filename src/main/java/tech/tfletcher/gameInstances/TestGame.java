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
import tech.tfletcher.engine.rendering.PointLight;
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

    private Vector3f ambientLight;
    private PointLight pointLight;


    private GameObject[] gameObjects = new GameObject[2];

    public TestGame(){
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

        gameObjects[1] = new GameObject(meshC);
        gameObjects[1].setScale(0.03f);
        gameObjects[1].setPosition(0,0.5f,0);


        Mesh bunny = OBJLoader.loadMesh("/models/bunny.obj");
        Material mat2 = new Material();
        bunny.setMaterial(mat2);
        gameObjects[0] = new GameObject(bunny);
        gameObjects[0].setScale(0.1f);
        gameObjects[0].setPosition(0, 0, -2);

        camera.setPosition(0.5f,0.25f, -1.5f);
        camera.setRotation(0, -45, 0);

        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColor = new Vector3f(0.5f, 0.5f, 0.5f);
        Vector3f lightPosition = new Vector3f(0,0f,0f);
        float lightIntensity = 1.0f;
        pointLight = new PointLight(lightColor, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.5f, 1.0f, 1.0f);
        pointLight.setAttenuation(att);
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

        float lightPos = pointLight.getPosition().z;
        if(window.isKeyPressed(GLFW_KEY_N)){
            this.pointLight.getPosition().z = lightPos + 0.01f;
        } else if(window.isKeyPressed(GLFW_KEY_M)){
            this.pointLight.getPosition().z = lightPos - 0.01f;
        }



    }

    @Override
    public void update(float interval, MouseInput mInput) {
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        if(mInput.isLeftButtonPressed()){
            Vector2f rotVec = mInput.getDisplVec();
            camera.moveRoation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        float cubeRotation = gameObjects[1].getRotation().x + 1.5f;
        if(cubeRotation > 360){
            cubeRotation = 0;
        }

        gameObjects[1].setRotation(cubeRotation, cubeRotation, cubeRotation);
        Vector3f n = new Vector3f(this.pointLight.getPosition());
        n.y += 0.1f;
        n.z -= 1f;
        gameObjects[1].setPosition(n);
    }

    @Override
    public void render(Window window) {
        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        renderer.renderMesh(window, camera, gameObjects, ambientLight, pointLight);

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for(GameObject gameObject : gameObjects){
            gameObject.getMesh().cleanUp();
        }
    }
}
