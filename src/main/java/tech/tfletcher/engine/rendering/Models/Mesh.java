package tech.tfletcher.engine.rendering.Models;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;
import tech.tfletcher.engine.Utility.Texture;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private final int vaoId;
    private final int posVboId;
    private final int idxVboId;
    private final int texVboId;
    private final int vertexCount;

    private final Texture texture;

    public Mesh(float[] positions, float[] texCoords, int[] indices, Texture texture){



        FloatBuffer posBuffer = null;
        FloatBuffer texCoordsBuffer = null;
        IntBuffer indicesBuffer = null;


        try{
            this.texture = texture;
            vertexCount = indices.length;

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            //posVbo
            posVboId = glGenBuffers();
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            //texVbo
            texVboId = glGenBuffers();
            texCoordsBuffer = MemoryUtil.memAllocFloat(texCoords.length);
            texCoordsBuffer.put(texCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, texVboId);
            glBufferData(GL_ARRAY_BUFFER, texCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            //idxVbo
            idxVboId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);


        } finally {
            if (posBuffer != null){
                MemoryUtil.memFree(posBuffer);
            }
            if (indicesBuffer != null){
                MemoryUtil.memFree(indicesBuffer);
            }
            if(texCoordsBuffer != null){
                MemoryUtil.memFree(texCoordsBuffer);
            }

        }
    }

    public int getVaoId(){
        return vaoId;
    }

    public int getVertexCount(){
        return vertexCount;
    }

    public void cleanUp(){
        glDisableVertexAttribArray(0);

        //vbo cleanup
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(idxVboId);
        glDeleteBuffers(texVboId);

        texture.cleanup();

        //vba cleanup
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public void render(){

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE0, texture.getId());

        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

}
