package tech.tfletcher.engine.rendering;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private final int vaoId;
    private final int posVboId;
    private final int idxVboId;
    private final int vertexCount;

    public Mesh(float[] positions, int[] indicies){

        FloatBuffer posBuffer = null;
        IntBuffer indiciesBuffer = null;

        try{
            vertexCount = indicies.length;

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            //posVbo
            posVboId = glGenBuffers();
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            //idxVbo
            idxVboId = glGenBuffers();
            indiciesBuffer = MemoryUtil.memAllocInt(indicies.length);
            indiciesBuffer.put(indicies).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indiciesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);

        } finally {
            if (posBuffer != null){
                MemoryUtil.memFree(posBuffer);
            }
            if (indiciesBuffer != null){
                MemoryUtil.memFree(indiciesBuffer);
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

        //vba cleanup
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

}
