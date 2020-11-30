// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.Buffer;

public class vec4 extends Vector
{
    private static final long serialVersionUID = -3068017465925763122L;
    public float x;
    public float y;
    public float z;
    public float w;
    
    public vec4() {
        this.SIZE = 4;
        this.PREC = 1;
    }
    
    public vec4(final float x) {
        this();
        this.x = x;
        this.y = x;
        this.z = x;
        this.w = x;
    }
    
    public vec4(final float x, final float y, final float z, final float w) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public vec4(final dvec4 v) {
        this();
        this.x = (float)v.x;
        this.y = (float)v.y;
        this.z = (float)v.z;
        this.w = (float)v.w;
    }
    
    public vec4(final vec4 v) {
        this();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }
    
    public vec4(final ivec4 v) {
        this();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }
    
    @Override
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
    }
    
    @Override
    public vec4 negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
        return this;
    }
    
    @Override
    public vec4 scale(final double scale) {
        this.x *= (float)scale;
        this.y *= (float)scale;
        this.z *= (float)scale;
        this.w *= (float)scale;
        return this;
    }
    
    public static float dot(final vec4 a, final vec4 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof FloatBuffer)) {
            throw new IllegalArgumentException("Provide a siutable FloatBuffer!");
        }
        if (buf.capacity() != 4) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final FloatBuffer fb = (FloatBuffer)buf;
        this.x = fb.get();
        this.y = fb.get();
        this.z = fb.get();
        this.w = fb.get();
        fb.flip();
    }
    
    @Override
    public FloatBuffer store() {
        final FloatBuffer fb = BufferUtils.createFloatBuffer(4);
        fb.put(this.x);
        fb.put(this.y);
        fb.put(this.z);
        fb.put(this.w);
        fb.flip();
        return fb;
    }
    
    @Override
    public void insertStore(final ByteBuffer bb) {
        if (bb.position() + this.SIZE * Vector.getBytePerPrecision(this.PREC) > bb.capacity()) {
            throw new IllegalArgumentException("Not Enough space to store this Vector!");
        }
        bb.putFloat(this.x);
        bb.putFloat(this.y);
        bb.putFloat(this.z);
        bb.putFloat(this.w);
    }
    
    @Override
    public String toString() {
        return "Vector4d[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
    }
    
    @Override
    public Vector copy() {
        return new vec4(this);
    }
    
    @Override
    public Vector add(final Vector v) {
        if (!(v instanceof vec4)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final vec4 adder = (vec4)v;
        this.x += adder.x;
        this.y += adder.y;
        this.z += adder.z;
        this.w += adder.w;
        return this;
    }
    
    @Override
    public Vector subtract(final Vector v) {
        if (!(v instanceof vec4)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final vec4 adder = (vec4)v;
        this.x -= adder.x;
        this.y -= adder.y;
        this.z -= adder.z;
        this.w -= adder.w;
        return this;
    }
}
