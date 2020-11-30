// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import java.nio.Buffer;

public class ivec1 extends Vector
{
    private static final long serialVersionUID = 1L;
    public int x;
    
    public ivec1() {
        this.SIZE = 1;
        this.PREC = 0;
    }
    
    public ivec1(final int i) {
        this();
        this.x = i;
    }
    
    public ivec1(final dvec1 v) {
        this();
        this.x = (int)v.x;
    }
    
    public ivec1(final vec1 v) {
        this();
        this.x = (int)v.x;
    }
    
    public ivec1(final ivec1 v) {
        this();
        this.x = v.x;
    }
    
    @Override
    public double lengthSquared() {
        return this.x * this.x;
    }
    
    @Override
    public ivec1 negate() {
        this.x = -this.x;
        return this;
    }
    
    @Override
    public ivec1 scale(final double scale) {
        this.x *= scale;
        return this;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof IntBuffer)) {
            throw new IllegalArgumentException("Provide a siutable IntBuffer!");
        }
        if (buf.capacity() != this.SIZE) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final IntBuffer ib = (IntBuffer)buf;
        this.x = ib.get();
        ib.flip();
    }
    
    @Override
    public IntBuffer store() {
        final IntBuffer ib = BufferUtils.createIntBuffer(this.SIZE);
        ib.put(this.x);
        ib.flip();
        return ib;
    }
    
    @Override
    public void insertStore(final ByteBuffer bb) {
        if (bb.position() + this.SIZE * Vector.getBytePerPrecision(this.PREC) > bb.capacity()) {
            throw new IllegalArgumentException("Not Enough space to store this Vector!");
        }
        bb.putInt(this.x);
    }
    
    @Override
    public String toString() {
        return "Vector1i[" + this.x + "]";
    }
    
    @Override
    public Vector copy() {
        return new ivec1(this);
    }
    
    @Override
    public Vector add(final Vector v) {
        if (!(v instanceof ivec1)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final ivec1 adder = (ivec1)v;
        this.x += adder.x;
        return this;
    }
    
    @Override
    public Vector subtract(final Vector v) {
        if (!(v instanceof ivec1)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final ivec1 adder = (ivec1)v;
        this.x -= adder.x;
        return this;
    }
}
