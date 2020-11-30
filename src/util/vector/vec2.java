// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.Buffer;

public class vec2 extends Vector
{
    private static final long serialVersionUID = -3068017465925763122L;
    public float x;
    public float y;
    
    public vec2() {
        this.SIZE = 2;
        this.PREC = 1;
    }
    
    public vec2(final float x) {
        this();
        this.x = x;
        this.y = x;
    }
    
    public vec2(final float x, final float y) {
        this();
        this.x = x;
        this.y = y;
    }
    
    public vec2(final dvec2 v) {
        this();
        this.x = (float)v.x;
        this.y = (float)v.y;
    }
    
    public vec2(final vec2 v) {
        this();
        this.x = v.x;
        this.y = v.y;
    }
    
    public vec2(final ivec2 v) {
        this();
        this.x = v.x;
        this.y = v.y;
    }
    
    @Override
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }
    
    @Override
    public vec2 negate() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }
    
    @Override
    public vec2 scale(final double scale) {
        this.x *= (float)scale;
        this.y *= (float)scale;
        return this;
    }
    
    public static float dot(final vec2 a, final vec2 b) {
        return a.x * b.x + a.y * b.y;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof FloatBuffer)) {
            throw new IllegalArgumentException("Provide a siutable FloatBuffer!");
        }
        if (buf.capacity() != 2) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final FloatBuffer fb = (FloatBuffer)buf;
        this.x = fb.get();
        this.y = fb.get();
        fb.flip();
    }
    
    @Override
    public FloatBuffer store() {
        final FloatBuffer fb = BufferUtils.createFloatBuffer(2);
        fb.put(this.x);
        fb.put(this.y);
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
    }
    
    @Override
    public String toString() {
        return "Vector2f[" + this.x + ", " + this.y + "]";
    }
    
    @Override
    public Vector copy() {
        return new vec2(this);
    }
    
    @Override
    public Vector add(final Vector v) {
        if (!(v instanceof vec2)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final vec2 adder = (vec2)v;
        this.x += adder.x;
        this.y += adder.y;
        return this;
    }
    
    @Override
    public Vector subtract(final Vector v) {
        if (!(v instanceof vec2)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final vec2 adder = (vec2)v;
        this.x -= adder.x;
        this.y -= adder.y;
        return this;
    }
}
