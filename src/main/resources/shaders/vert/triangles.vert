#version 430

layout (location = 0) in vec3 Position;

void
main(){
    gl_position = vec4(position, 1.0);
}