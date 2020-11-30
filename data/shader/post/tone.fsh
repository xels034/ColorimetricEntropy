#version 410

layout(early_fragment_tests) in;

in vec4 fPos;
in vec3 fWPos;
in vec4 fNorm;
in vec4 fEye;
in vec2 fUV;

uniform sampler2D screen;
uniform sampler2D colRamp;

uniform float time;
uniform float heat;
uniform float centerHue;

out vec4 outColor;


//bit wrangling, one-at-a-time hash function
//#justBobJenkinsThings
uint hash(uint x){
    x += ( x << 10u);
    x ^= ( x >> 6u);
    x += ( x << 3u);
    x ^= ( x >> 11u);
    x += ( x << 15u);
    return x;
}

uint hash( uvec2 v ) { return hash( v.x ^ v.y             ); } //XOR ALL THE THINGS!
uint hash( uvec3 v ) { return hash( v.x ^ v.y ^ v.z       ); }
uint hash( uvec4 v ) { return hash( v.x ^ v.y ^ v.z ^ v.w ); }

float floatConstruct (uint m) {
    const uint ieeeMantissa = 0x007FFFFFu; //mantissa mask of a float, in unsigned notation
    const uint ieeeOne      = 0x3F800000u; //one in a float, in unsigned notation, according to the IEEE specifications

    //sooo. we have random bits in m, but want a float [0,1]
    //we take the part of the randomBitSausage-thingy that is for the floating part, and preserve it with &=mantissa
    //we then set all the bits for the exponent part to 1 with |=one. So there arent any random bits in there.
    //we can "add" (using or operation) easily, but removing ONLY the one part seems tricky. so we have a 1+random bits in fraction part

    m &= ieeeMantissa; 
    m |= ieeeOne;

    float f = uintBitsToFloat (m); //convert our bitSausage back to a proper float. like (float)unit without any conversions
    return f - 1.0; //subtract the one from before, because now its easy, the GPU knows how to subtract a float (we dont, apparently)
}

float random (float x) {return floatConstruct(hash(floatBitsToUint(x)));} //actually a cast (uint)float without any conversions
float random (vec2  x) {return floatConstruct(hash(floatBitsToUint(x)));}
float random (vec3  x) {return floatConstruct(hash(floatBitsToUint(x)));}
float random (vec4  x) {return floatConstruct(hash(floatBitsToUint(x)));}


vec4 rrt(vec4 x){
	const float A=0.22;//Shoulder Strength
	const float B=0.30;//Linear Strength
	const float C=0.20;//Linear Angle
	const float D=0.20;//Toe Strength
	const float E=0.01;//Toe Numerator
	const float F=0.30;//Toe Denominator
	const float LWP=11.2; //linear white point
	return ((x*(A*x+C*B)+D*E)/(x*(A*x+B)+D*F)) - E/F;
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

float deHue(float x){
	const float s = 0.1; //breadth of the gauss function

	//x -= centerHue;
	x -= 0.5;
	if     (x > 0.5) x = 1-x;
	else if(x < 0)   x = 0-x;

	return exp(-(x*x) / (2*s*s));
}	

void main(void){
	vec3 coords = vec3(fUV, time);

    vec4 noise = vec4(random(coords), random(coords-1), random(coords+1), 1);
    noise = pow(noise,vec4(10))*3; //create more spiky noise

    vec4 color = texture(screen, fUV);

    vec4 LC = rrt(color);
	vec4 LW = rrt(vec4(4));

	vec4 toneColor = LC/LW;

	//toneColor = pow(toneColor, vec4(1.15));
	toneColor = color;

	float luma = 1-min((toneColor.r + toneColor.g + toneColor.b)/3, 1);
    outColor = mix(toneColor, noise, luma*0);

	float span = 11;	

	outColor.r *= texture(colRamp, vec2(outColor.r/span,0)).r;
	outColor.g *= texture(colRamp, vec2(outColor.g/span,0)).g;
	outColor.b *= texture(colRamp, vec2(outColor.b/span,0)).b;

	vec3 hsv = rgb2hsv(outColor.rgb);
	hsv.g *= texture(colRamp, vec2(hsv.z/span,0)).a;

	//float oldS = hsv.g;
	//hsv.g *= deHue(hsv.r);
	//float newS = hsv.g;

	//hsv.g = mix(oldS, newS, 0.50);
	//hsv.r = mix(hsv.r, 0.5, 0.5);
	
	outColor.rgb = hsv2rgb(hsv);

	outColor.a=1;

	//outColor = texture(colRamp, fUV);
	//outColor.a = 1;
}