// FRAGMENT SHADER INFORMATION
#version 400 core

in vec3 pos;
in vec3 normal;
in vec3 blockColour;

uniform vec3 highlightColour;

uniform vec3 intersection;
uniform vec3 intersectionNormal;

uniform float mineSize;

out vec4 FragColour;

float distance(vec3 v1, vec3 v2);

void main(){

	vec3 col = blockColour;
	if(abs(dot(intersectionNormal, normalize(pos - intersection))) < 0.1 && dot(intersectionNormal, normal) > 0.9){
		float distToIntersection = distance(pos, intersection);
		if(distToIntersection < mineSize){
			col = (col * 0.4) + (highlightColour * 0.6);
		}
	}

	FragColour = vec4(col, 1.0);
}

float distance(vec3 v1, vec3 v2){
	return max(max(abs(v1.x - v2.x), abs(v1.y - v2.y)), abs(v1.z - v2.z));
}