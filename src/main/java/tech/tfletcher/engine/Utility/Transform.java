package tech.tfletcher.engine.Utility;

import org.joml.Vector3f;
import org.joml.Matrix4f;
import tech.tfletcher.engine.GameObject;
import tech.tfletcher.engine.rendering.PointLight;

import java.awt.*;

public class Transform {
    private final Matrix4f projectionMatrix;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f viewMatrix;

    public Transform(){
        modelViewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar){
        float aspectRatio = width / height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    public Matrix4f getModelViewMatrix(GameObject gameObject, Matrix4f viewMatrix){
        Vector3f rotation = gameObject.getRotation();
        modelViewMatrix.set(viewMatrix).translate(gameObject.getPosition())
                .rotateX((float)Math.toRadians(-rotation.x))
                .rotateY((float)Math.toRadians(-rotation.y))
                .rotateZ((float)Math.toRadians(-rotation.z))
                .scale(gameObject.getScale());

        return modelViewMatrix;
    }

    public Matrix4f getLightModelViewMatrix(PointLight light, Matrix4f viewMatrix){
        modelViewMatrix.set(viewMatrix).translate(light.getPosition());
        return modelViewMatrix;
    }

    public Matrix4f getViewMatrix(Camera camera){

        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        viewMatrix.identity();

        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        return viewMatrix;
    }
}
