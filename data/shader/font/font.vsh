#version 420

layout(location = 0) in dvec3 vPos;
layout(location = 1) in dvec3 vNor;
layout(location = 2) in dvec2 vUV;

uniform dmat4 MVP;
uniform dmat4 FONT;

uniform dmat4 M;
uniform dmat4 V;
uniform dmat4 P;

out vec4 gPos;
out vec2 gUV;

void main(void) {
	
	dvec4 uv = FONT * dvec4(vUV,0,1);

	gPos = vec4(MVP * dvec4(vPos, 1));
	gUV =  vec2(uv.xy);

	gl_Position = gPos;
}
