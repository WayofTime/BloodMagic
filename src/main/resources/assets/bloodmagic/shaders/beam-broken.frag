 uniform sampler2D bgl_RenderedTexture;
 uniform int time;
 
 void main() {
    vec2 texcoord = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(bgl_RenderedTexture, texcoord);
    float r = sin(texcoord.x * 6 - 1.5 + sin(texcoord.y - time / 3.0)) * 1.1; //(sin((texcoord.x - texcoord.y) * 4 - time) + 1) / 2;
    
    gl_FragColor = vec4(color.r * gl_Color.r, color.g * gl_Color.g, max(color.b * gl_Color.b, r), color.a * gl_Color.a);
 }
