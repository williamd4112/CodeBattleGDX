varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_texture;
uniform sampler2D u_lightmap;
uniform vec2 resolution;

const vec4 ambientColor = vec4(0.3f, 0.3f, 0.7f, 1.0f);

void main() 
{	
	vec4 diffuseColor = texture2D(u_texture, v_texCoord0);
	vec3 ambient = ambientColor.rgb * ambientColor.a;
	vec3 final = v_color.rgb * diffuseColor.rgb * ambient;
	
	vec2 lighCoord = (gl_FragCoord.xy / resolution.xy);
	vec4 light = texture2D(u_lightmap, lighCoord);	

    vec3 intensity = ambient + light.rgb;
    vec3 finalColor = diffuseColor.rgb * intensity;

	gl_FragColor = v_color * vec4(finalColor, diffuseColor.a);

}