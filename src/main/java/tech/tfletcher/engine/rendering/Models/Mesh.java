package tech.tfletcher.engine.rendering.Models;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;
import tech.tfletcher.engine.rendering.Material;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private final int vaoId;
    private final int posVboId;
    private final int idxVboId;
    private final int texVboId;
    private final int normVboId;
    private final int vertexCount;


    private Material material = null;

    public Mesh(float[] positions, float[] texCoords, float[] normals ,int[] indices){


        FloatBuffer posBuffer = null;
        FloatBuffer texCoordsBuffer = null;
        FloatBuffer normBuffer = null;
        IntBuffer indicesBuffer = null;


        try{
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

            //normVbo
            normVboId = glGenBuffers();
            normBuffer = MemoryUtil.memAllocFloat(normals.length);
            normBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, normVboId);
            glBufferData(GL_ARRAY_BUFFER, normBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

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



        //vba cleanup
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public void render(){
        if(material.isTextured()){

            glActiveTexture(GL_TEXTURE0 + material.getTexture().getId());
            glBindTexture(GL_TEXTURE0, material.getTexture().getId());
        }


        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE, 0);
    }

    public Material getMaterial(){
        return material;
    }

    public void setMaterial(Material material){
        this.material = material;
    }

}
