#version 410

layout(early_fragment_tests) in;

in vec4 fPos;
in vec3 fWPos;
in vec4 fNorm;
in vec4 fEye;
in vec2 fUV;

uniform sampler2D glare;
uniform sampler2D original;

out vec4 outColor;

void main(void){
	outColor = texture(original, fUV) + texture(glare, fUV);
	outColor.a=1;
}
