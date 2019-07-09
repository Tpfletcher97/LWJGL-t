package tech.tfletcher.engine.rendering;

import jdk.swing.interop.LightweightContentWrapper;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import tech.tfletcher.engine.GameObject;
import tech.tfletcher.engine.Utility.Camera;
import tech.tfletcher.engine.Utility.Utils;
import tech.tfletcher.engine.Utility.Transform;
import tech.tfletcher.engine.rendering.Models.Mesh;


import static org.lwjgl.opengl.GL11.*;



public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private Transform transform;

    private ShaderProgram shaderProgram;
    private boolean oldGL = true;

    private float specularPower;

    public Renderer(){
        transform = new Transform();
        specularPower = 10f;
    }

    public void init() throws Exception{


        shaderProgram = new ShaderProgram();


        if (oldGL){
            shaderProgram.createVertexShader(Utils.loadResource("/shaders/vert/mesh-130.vert"));
            shaderProgram.createFragmentShader(Utils.loadResource("/shaders/frag/mesh-130.frag"));
        } else{

            shaderProgram.createVertexShader(Utils.loadResource("/shaders/vert/passThrough-330.vert"));
            shaderProgram.createFragmentShader(Utils.loadResource("/shaders/frag/passThrough-330.frag"));
        }
        shaderProgram.link();

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");

        shaderProgram.createMaterialUniform("material");

        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");

    }

    public void renderMesh(Window window, Camera camera, GameObject[] gameObjects, Vector3f ambientLight, PointLight pointLight){
        clear();

        if(window.isResized()){
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();


        Matrix4f projectionMatrix = transform.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = transform.getViewMatrix(camera);

        shaderProgram.setUniform("ambientLight", ambientLight);
        shaderProgram.setUniform("specularPower", specularPower);

        PointLight currPointLight = new PointLight(pointLight);
        Vector3f lightPos = currPointLight.getPosition();
        Matrix4f aux = transform.getLightModelViewMatrix(currPointLight, viewMatrix);

        lightPos.mulPosition(aux);

        currPointLight.setPosition(lightPos);
        shaderProgram.setUniform("pointLight", currPointLight);


        shaderProgram.setUniform("texture_sampler", 0);



        for (GameObject gameObject : gameObjects){
            Mesh mesh = gameObject.getMesh();
            Matrix4f modelViewMatrix = transform.getModelViewMatrix(gameObject, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            shaderProgram.setUniform("material",gameObject.getMesh().getMaterial());
            mesh.render();
        }

        shaderProgram.unbind();

    }

    private void clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup(){
        if(shaderProgram != null){
            shaderProgram.cleanup();
        }
    }
}
