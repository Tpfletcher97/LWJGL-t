package tech.tfletcher.engine.rendering;

import org.joml.Matrix4f;
import tech.tfletcher.engine.GameObject;
import tech.tfletcher.engine.Utility.Camera;
import tech.tfletcher.engine.Utility.Utils;
import tech.tfletcher.engine.Utility.Transform;


import static org.lwjgl.opengl.GL11.*;



public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private Transform transform;

    private ShaderProgram shaderProgram;
    private boolean oldGL = true;

    public Renderer(){
        transform = new Transform();
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


    }

    public void renderMesh(Window window, GameObject[] gameObjects, Camera camera){
        clear();

        if(window.isResized()){
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        Matrix4f projectionMatrix = transform.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = transform.getViewMatrix(camera);

        shaderProgram.setUniform("texture_sampler", 0);



        for (GameObject gameObject : gameObjects){
            Matrix4f modelViewMatrix =
                    transform.getModelViewMatrix(gameObject, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            gameObject.getMesh().render();
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
