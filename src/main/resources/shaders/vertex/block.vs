// VERTEX SHADER INFORMATION
#version 400 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec3 pos;
out vec3 normal;
out vec3 blockColour;

void main(){
	gl_Position = projection * view * model * vec4(aPos, 1.0);
	pos = vec3(model * vec4(aPos, 1.0));
	normal = aNormal;
	blockColour = vec3((abs(normal.x) * 0.2) + (abs(normal.y) * 0.6) + (abs(normal.z) * 0.4));
}