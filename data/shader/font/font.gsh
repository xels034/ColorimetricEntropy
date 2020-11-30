#version 420

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in vec4[] gPos;
in vec2[] gUV;

out vec4 fPos;
out vec2 fUV;

void main(void) {
	for(int i=0; i<3; i++){
		fPos = gPos[i];
 		gl_Position = fPos;
		fUV = gUV[i];
    	EmitVertex();
    }
	EndPrimitive();
}