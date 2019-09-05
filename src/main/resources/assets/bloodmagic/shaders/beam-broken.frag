 uniform sampler2D bgl_RenderedTexture;
 uniform int time;
 
 void main() {
    vec2 texcoord = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(bgl_RenderedTexture, texcoord);
    float r = sin(texcoord.x * 6.0 - 1.5 + sin(texcoord.y - float(time) / 3.0)) * 1.1; //(sin((texcoord.x - texcoord.y) * 4 - time) + 1) / 2;
    
    gl_FragColor = vec4(min(1 - r, color.r * gl_Color.r), min(1 - r, color.g * gl_Color.g), color.b * gl_Color.b, color.a * gl_Color.a);
 }
