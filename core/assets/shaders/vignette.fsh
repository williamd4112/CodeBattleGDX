varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_texture;

uniform vec2 resolution;

//RADIUS of our vignette, where 0.5 results in a circle fitting the screen
const float RADIUS = 0.75;

//softness of our vignette, between 0.0 and 1.0
const float SOFTNESS = 0.45;

//sepia colour, adjust to taste
const vec3 SEPIA = vec3(1.2, 1.0, 0.8); 

void main() 
{
	vec4 texColor = texture2D(u_texture, v_texCoord0) * (v_color);

	//determine center position
    vec2 position = (gl_FragCoord.xy / resolution.xy) - vec2(0.5);

	//determine the vector length of the center position
    float len = length(position);
    float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);
	float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));	
	vec3 sepiaColor = vec3(gray) * SEPIA;
 	
	//texColor.rgb = mix(texColor.rgb, sepiaColor, 0.5);
	texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 1);

    gl_FragColor =  texColor; 
}