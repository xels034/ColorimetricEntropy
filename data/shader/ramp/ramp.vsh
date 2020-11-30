#version 420

layout(location = 0) in dvec3 vPos;
layout(location = 1) in dvec4 vCol;

out vec4 gPos;
out vec4 gCol;

void main(void) {
	gPos = vec4(vPos, 1);
	gCol = vec4(vCol);
    gl_Position = gPos;
}