package tech.tfletcher.engine.rendering;

import tech.tfletcher.engine.Utils;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;


public class Renderer {
    private ShaderProgram shaderProgram;
    private boolean oldGL = true;

    public Renderer(){
    }

    public void init() throws Exception{

        shaderProgram = new ShaderProgram();

        if (oldGL){
            shaderProgram.createVertexShader(Utils.loadResource("/shaders/vert/passThrough-130.vert"));
            shaderProgram.createFragmentShader(Utils.loadResource("/shaders/frag/passThrough-130.frag"));
        } else{

            shaderProgram.createVertexShader(Utils.loadResource("/shaders/vert/passThrough-330.vert"));
            shaderProgram.createFragmentShader(Utils.loadResource("/shaders/frag/passThrough-330.frag"));
        }
        shaderProgram.link();


    }

    public void render(Window window, Mesh mesh){
        clear();

        if(window.isResized()){
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        glBindVertexArray(mesh.getVaoId());
        glEnableVertexAttribArray(0);

        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shaderProgram.unbind();

    }

    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup(){
        if(shaderProgram != null){
            shaderProgram.cleanup();
        }
    }
}
