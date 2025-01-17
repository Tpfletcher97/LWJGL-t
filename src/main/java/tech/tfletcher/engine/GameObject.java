package tech.tfletcher.engine;

import org.joml.Vector3f;
import tech.tfletcher.engine.rendering.Models.Mesh;

public class GameObject {

    private final Mesh mesh;
    private final Vector3f position;
    private float scale;
    private final Vector3f rotation;

    public GameObject(Mesh mesh){
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
    }

    public Vector3f getPosition(){
        return position;
    }

    public void setPosition(float x, float y, float z){
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setPosition(Vector3f v){
        this.position.x = v.x;
        this.position.y = v.y;
        this.position.z = v.z;
    }

    public float getScale(){
        return scale;
    }

    public void setScale(float scale){
        this.scale = scale;
    }

    public Vector3f getRotation(){
        return rotation;
    }

    public void setRotation(float x, float y, float z){
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Mesh getMesh(){
        return mesh;
    }
}
