#version 420

layout(location = 0) in dvec3 vPos;
layout(location = 1) in dvec3 vNor;
layout(location = 2) in dvec2 vUV;

out vec4 gPos;
out vec3 gWPos;
out vec4 gNor;
out vec2 gUV;

out vec4 gEye;

uniform dmat4 MVP;
uniform dmat4 M;
uniform dmat4 V;
uniform dmat4 P;

void main(void) {

    dvec4 dvPos = dvec4(vPos, 1);
    dvec4 dvNor = dvec4(vNor, 0);
    
    gPos  = vec4(MVP * dvPos);
    gWPos = vec3((M  * dvPos).xyz);
    gNor  = vec4(transpose(inverse(M)) * dvNor);

    dvec4 pos = inverse(V)*dvec4(0,0,0,1);
    gEye = vec4(normalize(pos - dvPos));

    gUV = vec2(vUV);
    gl_Position = gPos;
}
