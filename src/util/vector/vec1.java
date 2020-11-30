// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.Buffer;

public class vec1 extends Vector
{
    private static final long serialVersionUID = 1L;
    public float x;
    
    public vec1() {
        this.SIZE = 1;
        this.PREC = 1;
    }
    
    public vec1(final float f) {
        this();
        this.x = f;
    }
    
    public vec1(final dvec1 v) {
        this();
        this.x = (float)v.x;
    }
    
    public vec1(final vec1 v) {
        this();
        this.x = v.x;
    }
    
    public vec1(final ivec1 v) {
        this();
        this.x = v.x;
    }
    
    @Override
    public double lengthSquared() {
        return this.x * this.x;
    }
    
    @Override
    public vec1 negate() {
        this.x = -this.x;
        return this;
    }
    
    @Override
    public vec1 scale(final double scale) {
        this.x *= (float)scale;
        return this;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof FloatBuffer)) {
            throw new IllegalArgumentException("Provide a siutable FloatBuffer!");
        }
        if (buf.capacity() != this.SIZE) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final FloatBuffer fb = (FloatBuffer)buf;
        this.x = fb.get();
        fb.flip();
    }
    
    @Override
    public FloatBuffer store() {
        final FloatBuffer fb = BufferUtils.createFloatBuffer(this.SIZE);
        fb.put(this.x);
        fb.flip();
        return fb;
    }
    
    @Override
    public void insertStore(final ByteBuffer bb) {
        if (bb.position() + this.SIZE * Vector.getBytePerPrecision(this.PREC) > bb.capacity()) {
            throw new IllegalArgumentException("Not Enough space to store this Vector!");
        }
        bb.putFloat(this.x);
    }
    
    @Override
    public String toString() {
        return "Vector1f[" + this.x + "]";
    }
    
    @Override
    public Vector copy() {
        return new vec1(this);
    }
    
    @Override
    public Vector add(final Vector v) {
        if (!(v instanceof vec1)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final vec1 adder = (vec1)v;
        this.x += adder.x;
        return this;
    }
    
    @Override
    public Vector subtract(final Vector v) {
        if (!(v instanceof vec1)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final vec1 adder = (vec1)v;
        this.x -= adder.x;
        return this;
    }
}
