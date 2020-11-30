#version 410

layout(triangles) in;
layout(triangle_strip, max_vertices = 9) out;

in vec4[] gPos;
in vec3[] gWPos;
in vec4[] gNor;
in vec4[] gEye;
in vec2[] gUV;

out vec4 fPos;
out vec3 fWPos;
out vec4 fNor;
out vec4 fEye;
out vec2 fUV;

out float multiplier;

uniform float heat;

void spike(){
	for(int i=0; i<3; i++){
      	fPos = gPos[i];
      	fWPos = gWPos[i];
      	fNor = gNor[i];
      	fEye=gEye[i];
     	 fUV = gUV[i];	
	  	float ext = max(0, (heat-600));

	  	if(i==0){gl_Position = fPos+fNor*ext*0.01;}
	  	else {gl_Position = fPos;}
      
	  	multiplier = 1;
      	EmitVertex();
   	}
   	EndPrimitive();
}

void inset(){
	for(int i=0; i<3; i++){
		fPos = gPos[i];
      	fWPos = gWPos[i];
      	fNor = gNor[i];
      	fEye=gEye[i];
     	 fUV = gUV[i];	
	  	float ext = max(0, (heat-600));

	  	
	  	gl_Position = fPos - fNor*ext*0.001;
      
	  	multiplier = 0;
      	EmitVertex();
	}
	EndPrimitive();
}

void outset(){
	vec3 n1 = normalize(gPos[0] -gPos[1]).xyz;
	vec3 n2 = normalize(gPos[2] -gPos[1]).xyz;

	vec3 nor = cross(n1,n2);
	
	vec4 center = (gPos[0]+gPos[1]+gPos[2])/3;

	for(int i=0; i<3; i++){
		fPos = gPos[i];
      	fWPos = gWPos[i];
      	fNor = gNor[i];
      	fEye=gEye[i];
     	fUV = gUV[i];	
	  	float ext = max(0, (heat-600)) * 0.003;

		vec4 toCenter = center - gPos[i];

		toCenter *= 1-pow(0.5, ext*3);
		toCenter.w=1;

		ext = pow(ext*6 - 0.1, 1.2);

	  	gl_Position = fPos +toCenter + vec4(nor,0)*ext;
      
	  	multiplier = 3;
      	EmitVertex();
	}
	
	EndPrimitive();
}

void main(void) {
	spike();
	inset();
	outset();
	
   	
   
   
}