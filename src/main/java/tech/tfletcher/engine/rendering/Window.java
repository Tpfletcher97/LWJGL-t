package tech.tfletcher.engine.rendering;

import org.joml.Matrix4f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {



    private long window;
    private int width;
    private int height;

    private String title;

    private boolean resized;

    public Window(int width, int height, String title){
        this.width = width;
        this.height = height;
        this.title = title;

        this.resized = false;

    }


    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        //Create the window
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if(window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        //Key management
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        //Gets thread stack and pushes new frame
        try(MemoryStack stack = stackPush()){
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            //Passes to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            //Detect monitor resolution
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }
        glfwSetFramebufferSizeCallback(window, (window, width, height) ->{
            Window.this.width = width;
            Window.this.height = height;
        });

        glfwMakeContextCurrent(window);

        //v-sync
        glfwSwapInterval(1);

        glfwShowWindow(window);

        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_DEPTH_TEST);
       // glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
    }//Pops stack frame automatically

    public void setClearColor(float r, float g, float b, float alpha){
        glClearColor(r, g, b, alpha);
    }

    public boolean isKeyPressed(int keyCode){
        return glfwGetKey(window, keyCode) == GLFW_PRESS;
    }

    public void update(){
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public void close(){
        //Window cleanup
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public long getWindow(){
        return window;
    }

    public void setResized(boolean b){
        resized = b;
    }

    public boolean isResized(){
        return resized;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public boolean windowShouldClose(){
        return glfwWindowShouldClose(window);
    }
}
