package tech.tfletcher.engine.Utility;

import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    private final int id;

    public Texture(String fileName) throws Exception{
        this(loadTexture(fileName));
    }

    public Texture(int id){
        this.id = id;
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getId(){
        return id;
    }

    private static int loadTexture(String fileName) throws Exception{
        int width;
        int height;
        ByteBuffer buf;

        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            URL url = Texture.class.getResource(fileName);
            File file = Paths.get(url.toURI()).toFile();
            String filePath = file.getAbsolutePath();
            buf = stbi_load(filePath, w, h, channels, 4);

            if(buf == null){
                throw new Exception("Image file[" + filePath + "] not loaded:" + stbi_failure_reason());
            }

            width = w.get();
            height = h.get();
        }

        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);

        glGenerateMipmap(GL_TEXTURE_2D);



        stbi_image_free(buf);

        return textureId;
    }

    public void cleanup(){
        glDeleteTextures(id);
    }
}
