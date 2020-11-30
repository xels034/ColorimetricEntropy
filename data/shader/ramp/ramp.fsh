#version 420

in vec4 fPos;
in vec4 fCol;

out vec4 outColor;

void main(void) {
	outColor = fCol;
}
