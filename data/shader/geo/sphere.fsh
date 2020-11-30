#version 410

layout(early_fragment_tests) in;

in vec4 fPos;
in vec3 fWPos;
in vec4 fNor;
in vec4 fEye;
in vec2 fUV;

in float multiplier;

out vec4 outColor;

uniform vec3 lightDir;

uniform float heat;
uniform float time;

vec4 bNor;

float diffuse(float fallOff, vec4 nor){
    float l;
   
    vec4 ld = normalize(vec4(lightDir,0));
    l = clamp(dot(-ld,nor),0,1);
    l = 1-pow(1-l,fallOff); //make it look more like oren-nayar by multiplying the inverse
    return l;
}


//use either a specified normal or the default one
float diffuse(float f){
    return diffuse(f, bNor);
}


float phong(float fallOff){
    vec4 ld = normalize(vec4(lightDir, 0));
    vec4 r = reflect(ld,bNor);    
    return pow(clamp(dot(fEye,r),0,1),fallOff);
}

//generic fresnel-like ramp. actually its just 1 - lambert
float fresnel(float fallOff, float strength, float boost){
    float fresnel = 1-clamp(dot(fEye,bNor),0,1);
    fresnel = pow(fresnel,fallOff); 
    fresnel *= strength; 
    fresnel += boost;
    return fresnel;
}

//fake shadow. the closer a fragment is to the ground, the darker it gets
float shadow(float str){
    float s = 1-fWPos.z ;
    s *= str;
    return 1-clamp(s,0,1);
}

//from a given value, determine the color and luminocity of the heat radiation
vec3 radiate(float gradient, sampler1D heatTex){
    float temp = max((heat-600),0);
    temp *= gradient;
    vec3 c = texture(heatTex, temp/8000).rgb;//heatTex has the color encoded as a function of temperature from 0-8000°C
    temp *= 0.003;
    return c* pow(temp, 2); //gives a nicer differentiation in luminocity
}
vec3 rgb2hsv(vec3 c){
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

vec3 hsv2rgb(vec3 c){
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}


void main(void) {
    bNor = fNor;
    //sky reflection
    vec3 refVec = reflect(fEye,bNor).xyz;
    refVec.z = -refVec.z; //cubemap reflection was upway down
    vec3 refDiff = vec3(0);//textureLod(worldTex, bNor.xyz, 8).rgb; //fake blurry reflections by using a mipmap level
    vec3 refSpec = vec3(0);//textureLod(worldTex, refVec,0).rgb; //fake blurry reflections by using a mipmap level

	refDiff = pow(refDiff, vec3(2)) * 2;
	refSpec = pow(refSpec, vec3(2)) * 2;

    vec4 mapCol =  vec4(0,0,0,1);
    vec4 mapLava = vec4(0,0,0,0);

    //base color. multiplied with the lava color and darkened in the end
    vec3 base = mapCol.rgb * mix(mapLava.rgb, vec3(1), .75) * 0.2;

    //specular map with strength 0.2
    float dirt = mix(1, length(mapCol.rgb), 0.4f);

    float lambert = diffuse(1, bNor);
    //used for mixing reflections
    float f = fresnel(3, 0.85, 0.025);
    //f=1;
    vec3 sum;

    sum = mix(base*lambert, refSpec*dirt, f); //diffuse+specular reflections
    sum += base*fresnel(1,0.2,0); //give the stone a rough feeling by highlighting the edges
    sum *= shadow(0.4);
    sum += refDiff*base*0.35; //diffuse sky reflection

    float p = phong(40)*f*30; //used for sunlight reflection
    sum += vec3(1,0.9,0.8)*p;
    
    float rampStr = 6;
    float g = length(mapLava.rgb)/3;//averadge of rgb

    g *= rampStr;
    g = max(1-g,0);
    g += fresnel(1,0.55,0.1);//gives a tiny hint of translucency of molten parts

	vec3 radCol = vec3(0);//radiate(g+0.3) * multiplier;
	
	radCol = rgb2hsv(radCol);
	radCol.r *=10;
	radCol.r += time*0.0001;
	radCol = hsv2rgb(radCol);	

    sum += radCol;

    outColor = vec4(sum,1);
}