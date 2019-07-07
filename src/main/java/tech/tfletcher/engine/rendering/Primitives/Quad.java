package tech.tfletcher.engine.rendering.Primitives;

public class Quad {

    private Mesh mesh;

    public Quad(){
        float[] positions = new float[]{
                -1.0f, 1.0f, -4.05f,
                1.0f, 1.0f, -4.05f,
                -1.0f, -1.0f, -4.05f,
                1.0f, -1.0f, -4.05f
        };

        float[] colors = new float[]{
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f
        };

        int[] indices = new int[]{
                0, 2, 1, 1, 2, 3
        };

        mesh = new Mesh(positions, colors, indices);
    }

    public Mesh mesh(){
        return mesh;
    }
}
