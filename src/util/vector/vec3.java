// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.Buffer;

public class vec3 extends Vector
{
    private static final long serialVersionUID = -3068017465925763122L;
    public float x;
    public float y;
    public float z;
    
    public vec3() {
        this.SIZE = 3;
        this.PREC = 1;
    }
    
    public vec3(final float x) {
        this();
        this.x = x;
        this.y = x;
        this.z = x;
    }
    
    public vec3(final float x, final float y, final float z) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public vec3(final dvec3 v) {
        this();
        this.x = (float)v.x;
        this.y = (float)v.y;
        this.z = (float)v.z;
    }
    
    public vec3(final vec3 v) {
        this();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    public vec3(final ivec3 v) {
        this();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    @Override
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
    
    @Override
    public vec3 negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }
    
    @Override
    public vec3 scale(final double scale) {
        this.x *= (float)scale;
        this.y *= (float)scale;
        this.z *= (float)scale;
        return this;
    }
    
    public static float dot(final vec3 a, final vec3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
    
    public static vec3 cross(final vec3 a, final vec3 b) {
        final vec3 c = new vec3();
        c.x = a.y * b.z - a.z * b.y;
        c.y = a.z * b.x - a.x * b.z;
        c.z = a.x * b.y - a.y * b.x;
        return c;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof FloatBuffer)) {
            throw new IllegalArgumentException("Provide a siutable FloatBuffer!");
        }
        if (buf.capacity() != 3) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final FloatBuffer fb = (FloatBuffer)buf;
        this.x = fb.get();
        this.y = fb.get();
        this.z = fb.get();
        fb.flip();
    }
    
    @Override
    public FloatBuffer store() {
        final FloatBuffer fb = BufferUtils.createFloatBuffer(3);
        fb.put(this.x);
        fb.put(this.y);
        fb.put(this.z);
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
    }
    
    @Override
    public String toString() {
        return "Vector3f[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
    
    @Override
    public Vector copy() {
        return new vec3(this);
    }
    
    @Override
    public Vector add(final Vector v) {
        if (!(v instanceof vec3)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final vec3 adder = (vec3)v;
        this.x += adder.x;
        this.y += adder.y;
        this.z += adder.z;
        return this;
    }
    
    @Override
    public Vector subtract(final Vector v) {
        if (!(v instanceof vec3)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final vec3 adder = (vec3)v;
        this.x -= adder.x;
        this.y -= adder.y;
        this.z -= adder.z;
        return this;
    }
}
