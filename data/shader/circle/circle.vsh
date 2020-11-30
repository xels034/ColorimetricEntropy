#version 420

layout(location = 0) in dvec3 vPos;
layout(location = 1) in dvec2 vIdx;

out vec4 gPos;
out vec4 gMPos;
out int  gIdx;

uniform dmat4 MVP;
uniform dmat4 M;
uniform dmat4 V;
uniform dmat4 P;

void main(void) {

    dvec4 dvPos = dvec4(vPos, 1);
    
    gPos = vec4(MVP * dvPos);
	gMPos = vec4(vPos, 1);
	gIdx = int(vIdx.x);

    gl_Position = gPos;
}
