package tech.tfletcher.engine.rendering.Primitives;

public class Triangle{

    private Mesh mesh;

    public Triangle(){
        float[] positions = new float[]{
                0.0f, 1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f
        };

        float[] colors = new float[]{
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f,
        };

        int[] indices = new int[]{
                2, 1, 0
        };

        mesh = new Mesh(positions, colors, indices);
    }

    public Mesh mesh(){
        return mesh;
    }

}
