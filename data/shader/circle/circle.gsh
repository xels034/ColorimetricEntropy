#version 420

layout(lines) in;
layout(triangle_strip, max_vertices = 6) out;

in vec4[] gPos;
in vec4[] gMPos;
in  int[] gIdx;

out vec4 fPos;
out  int fIdx;
out float amp;

uniform float[128] freq;
uniform dmat4 MVP;

uniform float heat;

void main(void) {
	const float str= 0.0003*heat;	
	const float bst = 0.0;

	dvec4 ddir = dvec4(gMPos[0] + gMPos[1])/2;
	ddir.w=0;
	ddir = MVP * normalize(ddir);

	vec4 dir = vec4(ddir);

	float height = freq[gIdx[0]];

	height = max(0,log2(height));
	height = max(0.05, bst + height*str);

	//V1
	fIdx = gIdx[0];
	fPos = gPos[0];
	amp = 0;
	gl_Position = fPos;
    EmitVertex();

	//V2
	amp=0;
	fIdx = gIdx[1];
	fPos = gPos[1];
	gl_Position = fPos;
    EmitVertex();

	//V3
	fIdx = gIdx[0];
	fPos = gPos[0] + dir*height;
	amp = height;
	gl_Position = fPos;
    EmitVertex();
	EndPrimitive();

	//V4
	fIdx = gIdx[0];
	fPos = gPos[0] + dir*height;
	amp = height;
	gl_Position = fPos;
    EmitVertex();

	//V5
	fIdx = gIdx[1];
	fPos = gPos[1] + dir*height;
	amp = height;
	gl_Position = fPos;
    EmitVertex();

	//V6
	fIdx = gIdx[1];
	fPos = gPos[1];
	gl_Position = fPos;
    EmitVertex();
    EndPrimitive();
}