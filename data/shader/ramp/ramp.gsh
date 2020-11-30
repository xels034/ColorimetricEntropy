#version 420

layout(lines) in;
layout(line_strip, max_vertices = 2) out;

in vec4[] gPos;
in vec4[] gCol;

out vec4 fPos;
out vec4 fCol;

void main(void) {
	for(int i=0; i<2; i++){
		fPos = gPos[i];
		fCol = gCol[i];
		gl_Position = fPos;
		EmitVertex();
	}
	EndPrimitive();
}
