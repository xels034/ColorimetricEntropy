// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics;

import org.lwjgl.opengl.GL30;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL40;
import java.util.Map;
import util.vector.dmat4;
import util.vector.mat4;
import util.vector.dvec4;
import util.vector.dvec3;
import util.vector.dvec2;
import util.vector.ivec4;
import util.vector.ivec3;
import util.vector.ivec2;
import util.vector.vec4;
import util.vector.vec3;
import util.vector.vec2;
import org.lwjgl.opengl.GL20;
import util.SimpleLogger;
import java.io.IOException;
import org.lwjgl.BufferUtils;
import java.util.HashMap;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class ShaderWrapper
{
    private static FloatBuffer matrixBufferF;
    private static DoubleBuffer matrixBufferD;
    private int shaderID;
    private HashMap<String, Integer> uniformType;
    private HashMap<String, Integer> uniformHandle;
    private String vertexShader;
    private String geometryShader;
    private String fragmentShader;
    
    public ShaderWrapper(final String vs, final String gs, final String fs) {
        this.shaderID = -1;
        if (ShaderWrapper.matrixBufferF == null) {
            ShaderWrapper.matrixBufferF = BufferUtils.createFloatBuffer(16);
        }
        if (ShaderWrapper.matrixBufferD == null) {
            ShaderWrapper.matrixBufferD = BufferUtils.createDoubleBuffer(16);
        }
        try {
            this.vertexShader = readShaderFile(vs);
            this.geometryShader = readShaderFile(gs);
            this.fragmentShader = readShaderFile(fs);
            this.shaderID = compileShader(this.vertexShader, this.geometryShader, this.fragmentShader);
            this.detectUniforms();
            this.initializeUniforms();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void detectUniforms() {
        this.uniformType = new HashMap<>();
        this.uniformHandle = new HashMap<>();
        final String workplace = String.valueOf(this.vertexShader) + "\n" + this.geometryShader + "\n" + this.fragmentShader;
        final String[] lineSet = workplace.split("\n");
        SimpleLogger.log("lineSet size=" + lineSet.length, 10, ShaderWrapper.class, "detectUniforms");
        String[] array;
        for (int length = (array = lineSet).length, i = 0; i < length; ++i) {
            final String s = array[i];
            final String[] wordSet = s.split(" ");
            int wsIdx = 0;
            boolean hit = false;
            while (wsIdx < wordSet.length && !hit) {
                if (wordSet[wsIdx].equals("uniform")) {
                    hit = true;
                }
                else {
                    ++wsIdx;
                }
            }
            if (hit) {
                String uniName = wordSet[wsIdx + 2];
                uniName = uniName.substring(0, uniName.length() - 1);
                this.uniformType.put(uniName, getVarIDX(wordSet[wsIdx + 1]));
                this.uniformHandle.put(uniName, GL20.glGetUniformLocation(this.shaderID, uniName));
            }
        }
        SimpleLogger.log("Detected " + this.uniformType.size() + " uniforms.", 10, ShaderWrapper.class, "detectUniforms");
        this.printUniforms(10);
    }
    
    private void initializeUniforms() {
        for (final String s : this.uniformHandle.keySet()) {
            final int t = this.uniformType.get(s);
            Object o = null;
            switch (t) {
                case 0: {
                    o = Integer.valueOf(0);
                    break;
                }
                case 1: {
                    o = new Integer[0];
                    break;
                }
                case 2: {
                    o = Float.valueOf(0.0f);
                    break;
                }
                case 3: {
                    o = new Float[0];
                    break;
                }
                case 4: {
                    o = new vec2();
                    break;
                }
                case 5: {
                    o = new vec3();
                    break;
                }
                case 6: {
                    o = new vec4();
                    break;
                }
                case 7: {
                    o = new ivec2();
                    break;
                }
                case 8: {
                    o = new ivec3();
                    break;
                }
                case 9: {
                    o = new ivec4();
                    break;
                }
                case 10: {
                    o = new dvec2();
                    break;
                }
                case 11: {
                    o = new dvec3();
                    break;
                }
                case 12: {
                    o = new dvec4();
                    break;
                }
                case 13: {
                    o = new mat4();
                    break;
                }
                case 14: {
                    o = new dmat4();
                    break;
                }
                default: {
                    break;
                }
            }
            this.storeUniform(s, o);
        }
    }
    
    public HashMap<String, Integer> getUniformNames() {
        return (HashMap<String, Integer>)this.uniformType.clone();
    }
    
    public void storeUniform(final String name, final Object value) {
        GL20.glUseProgram(this.shaderID);
        if (this.uniformType.containsKey(name)) {
            final int type = this.uniformType.get(name);
            final int handle = this.uniformHandle.get(name);
            switch (type) {
                case 0: {
                    storeInteger(handle, (int)value);
                    break;
                }
                case 1: {
                    storeIntegerArray(handle, (Integer[])value);
                    break;
                }
                case 2: {
                    storeFloat(handle, (float)value);
                    break;
                }
                case 3: {
                    storeFloatArray(handle, (Float[])value);
                    break;
                }
                case 4: {
                    storeVector(handle, (vec2)value);
                    break;
                }
                case 5: {
                    storeVector(handle, (vec3)value);
                    break;
                }
                case 6: {
                    storeVector(handle, (vec4)value);
                    break;
                }
                case 7: {
                    storeVector(handle, (ivec2)value);
                    break;
                }
                case 8: {
                    storeVector(handle, (ivec3)value);
                    break;
                }
                case 9: {
                    storeVector(handle, (ivec4)value);
                    break;
                }
                case 10: {
                    storeVector(handle, (dvec2)value);
                    break;
                }
                case 11: {
                    storeVector(handle, (dvec3)value);
                    break;
                }
                case 12: {
                    storeVector(handle, (dvec4)value);
                    break;
                }
                case 13: {
                    storeMatrix(handle, (mat4)value);
                    break;
                }
                case 14: {
                    storeMatrix(handle, (dmat4)value);
                    break;
                }
                default: {
                    SimpleLogger.log("Unsupported type (" + type + ")", -1, ShaderWrapper.class, "storeUniform");
                    break;
                }
            }
        }
        else if (!name.equals("fboTex")) {
            SimpleLogger.log("Warning: Shader has no uniform named " + name, -1, ShaderWrapper.class, "storeUniform");
        }
        else {
            SimpleLogger.log("Shader has no uniform named fboTex. (Probalby attempt from FrameBufferManager", 10, ShaderWrapper.class, "storeUniform");
        }
    }
    
    public int getShaderID() {
        return this.shaderID;
    }
    
    public void printUniforms(final int logLevel) {
        if (SimpleLogger.level >= logLevel) {
            for (final Map.Entry<String, Integer> e : this.uniformType.entrySet()) {
                SimpleLogger.log(String.valueOf(getVarName(e.getValue())) + " " + e.getKey(), logLevel, ShaderWrapper.class, "printUniforms");
            }
        }
    }
    
    public static int getVarIDX(final String varType) {
        if (varType.equals("sampler1D")) {
            return 0;
        }
        if (varType.equals("sampler2D")) {
            return 0;
        }
        if (varType.equals("sampler2DMS")) {
            return 0;
        }
        if (varType.equals("samplerCube")) {
            return 0;
        }
        if (varType.equals("int")) {
            return 0;
        }
        if (varType.matches("int\\[.+\\]")) {
            return 1;
        }
        if (varType.equals("float")) {
            return 2;
        }
        if (varType.matches("float\\[.+\\]")) {
            return 3;
        }
        if (varType.equals("vec2")) {
            return 4;
        }
        if (varType.equals("vec3")) {
            return 5;
        }
        if (varType.equals("vec4")) {
            return 6;
        }
        if (varType.equals("ivec2")) {
            return 7;
        }
        if (varType.equals("ivec3")) {
            return 8;
        }
        if (varType.equals("ivec4")) {
            return 9;
        }
        if (varType.equals("dvec2")) {
            return 10;
        }
        if (varType.equals("dvec3")) {
            return 11;
        }
        if (varType.equals("dvec4")) {
            return 12;
        }
        if (varType.equals("mat4")) {
            return 13;
        }
        if (varType.equals("dmat4")) {
            return 14;
        }
        return -1;
    }
    
    public static String getVarName(final int varIDX) {
        switch (varIDX) {
            case 0: {
                return "int";
            }
            case 1: {
                return "int[]";
            }
            case 2: {
                return "float";
            }
            case 3: {
                return "float[]";
            }
            case 4: {
                return "vec2";
            }
            case 5: {
                return "vec3";
            }
            case 6: {
                return "vec4";
            }
            case 7: {
                return "ivec2";
            }
            case 8: {
                return "ivec3";
            }
            case 9: {
                return "ivec4";
            }
            case 10: {
                return "dvec2";
            }
            case 11: {
                return "dvec3";
            }
            case 12: {
                return "dvec4";
            }
            case 13: {
                return "mat4";
            }
            case 14: {
                return "dmat4";
            }
            default: {
                return "unknown";
            }
        }
    }
    
    private static void storeMatrix(final int uniformHandle, final mat4 mat) {
        mat.store(ShaderWrapper.matrixBufferF);
        ShaderWrapper.matrixBufferF.flip();
        GL20.glUniformMatrix4fv(uniformHandle, false, ShaderWrapper.matrixBufferF);
    }
    
    private static void storeMatrix(final int uniformHandle, final dmat4 mat) {
        mat.store(ShaderWrapper.matrixBufferD);
        ShaderWrapper.matrixBufferD.flip();
        GL40.glUniformMatrix4dv(uniformHandle, false, ShaderWrapper.matrixBufferD);
    }
    
    private static void storeInteger(final int uniformHandle, final int i) {
        GL20.glUniform1i(uniformHandle, i);
    }
    
    private static void storeIntegerArray(final int uniformHandle, final Integer[] i) {
        final ByteBuffer bb = BufferUtils.createByteBuffer(4 * i.length);
        for (int l = 0; l < i.length; ++l) {
            bb.putInt(i[l]);
        }
        bb.flip();
        GL20.glUniform1iv(uniformHandle, i.length, bb);
    }
    
    private static void storeFloat(final int uniformHandle, final float f) {
        GL20.glUniform1f(uniformHandle, f);
    }
    
    private static void storeFloatArray(final int uniformHandle, final Float[] f) {
        final ByteBuffer bb = BufferUtils.createByteBuffer(4 * f.length);
        for (int i = 0; i < f.length; ++i) {
            bb.putFloat(f[i]);
        }
        bb.flip();
        GL20.glUniform1fv(uniformHandle, f.length, bb);
    }
    
    private static void storeVector(final int uniformHandle, final vec2 vec) {
        GL20.glUniform2f(uniformHandle, vec.x, vec.y);
    }
    
    private static void storeVector(final int uniformHandle, final vec3 vec) {
        GL20.glUniform3f(uniformHandle, vec.x, vec.y, vec.z);
    }
    
    private static void storeVector(final int uniformHandle, final vec4 vec) {
        GL20.glUniform4f(uniformHandle, vec.x, vec.y, vec.z, vec.w);
    }
    
    private static void storeVector(final int uniformHandle, final ivec2 vec) {
        GL20.glUniform2i(uniformHandle, vec.x, vec.y);
    }
    
    private static void storeVector(final int uniformHandle, final ivec3 vec) {
        GL20.glUniform3i(uniformHandle, vec.x, vec.y, vec.z);
    }
    
    private static void storeVector(final int uniformHandle, final ivec4 vec) {
        GL20.glUniform4i(uniformHandle, vec.x, vec.y, vec.z, vec.w);
    }
    
    private static void storeVector(final int uniformHandle, final dvec2 vec) {
        GL40.glUniform2d(uniformHandle, vec.x, vec.y);
    }
    
    private static void storeVector(final int uniformHandle, final dvec3 vec) {
        GL40.glUniform3d(uniformHandle, vec.x, vec.y, vec.z);
    }
    
    private static void storeVector(final int uniformHandle, final dvec4 vec) {
        GL40.glUniform4d(uniformHandle, vec.x, vec.y, vec.z, vec.w);
    }
    
    private static String readShaderFile(final String fn) throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fn), "UTF-8"));
        final StringBuilder sb = new StringBuilder();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            sb.append(String.valueOf(line) + "\n");
        }
        br.close();
        return sb.toString();
    }
    
    private static int compileShader(final String vertex, final String geometry, final String fragment) {
        final int IDX_v = GL20.glCreateShader(35633);
        GL20.glShaderSource(IDX_v, vertex);
        GL20.glCompileShader(IDX_v);
        int status = GL20.glGetShaderi(IDX_v, 35713);
        if (status != 1) {
            throw new OpenGLException("ERROR VSH:\n" + GL20.glGetShaderInfoLog(IDX_v, 512) +"\n\n" + vertex);
        }
        SimpleLogger.log("V_SH: SUCCESSFULL", 10, ShaderWrapper.class, "compileShader");
        final int IDX_g = GL20.glCreateShader(36313);
        GL20.glShaderSource(IDX_g, geometry);
        GL20.glCompileShader(IDX_g);
        status = GL20.glGetShaderi(IDX_g, 35713);
        if (status != 1) {
            throw new OpenGLException("ERROR GSH:\n" + GL20.glGetShaderInfoLog(IDX_g, 512) + "\n\n"+geometry);
        }
        SimpleLogger.log("V_SH: SUCCESSFULL", 10, ShaderWrapper.class, "compileShader");
        final int IDX_f = GL20.glCreateShader(35632);
        GL20.glShaderSource(IDX_f, fragment);
        GL20.glCompileShader(IDX_f);
        status = GL20.glGetShaderi(IDX_f, 35713);
        if (status != 1) {
            throw new OpenGLException("ERROR FSH:\n" + GL20.glGetShaderInfoLog(IDX_f, 512) + "\n\n"+fragment);
        }
        SimpleLogger.log("F_SH: SUCCESSFULL", 10, ShaderWrapper.class, "compileShader");
        final int IDX_prog = GL20.glCreateProgram();
        GL20.glAttachShader(IDX_prog, IDX_v);
        GL20.glAttachShader(IDX_prog, IDX_g);
        GL20.glAttachShader(IDX_prog, IDX_f);
        GL30.glBindFragDataLocation(IDX_prog, 0, "outColor");
        GL20.glLinkProgram(IDX_prog);
        GL20.glDetachShader(IDX_prog, IDX_v);
        GL20.glDetachShader(IDX_prog, IDX_g);
        GL20.glDetachShader(IDX_prog, IDX_f);
        GL20.glDeleteShader(IDX_v);
        GL20.glDeleteShader(IDX_g);
        GL20.glDeleteShader(IDX_f);
        if (GL20.glGetProgrami(IDX_prog, 35714) == 0) {
            throw new OpenGLException("Shader Program linking failed");
        }
        return IDX_prog;
    }
    
    public void releaseShader() {
        GL20.glDeleteProgram(this.shaderID);
    }
}
