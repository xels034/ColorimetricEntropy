// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.nio.DoubleBuffer;
import java.nio.Buffer;

public class dvec1 extends Vector
{
    private static final long serialVersionUID = 1L;
    public double x;
    
    public dvec1() {
        this.SIZE = 1;
        this.PREC = 2;
    }
    
    public dvec1(final double d) {
        this();
        this.x = d;
    }
    
    public dvec1(final dvec1 v) {
        this();
        this.x = v.x;
    }
    
    public dvec1(final vec1 v) {
        this();
        this.x = v.x;
    }
    
    public dvec1(final ivec1 v) {
        this();
        this.x = v.x;
    }
    
    @Override
    public double lengthSquared() {
        return this.x * this.x;
    }
    
    @Override
    public dvec1 negate() {
        this.x = -this.x;
        return this;
    }
    
    @Override
    public dvec1 scale(final double scale) {
        this.x *= scale;
        return this;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof DoubleBuffer)) {
            throw new IllegalArgumentException("Provide a siutable DoubleBuffer!");
        }
        if (buf.capacity() != this.SIZE) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final DoubleBuffer db = (DoubleBuffer)buf;
        this.x = db.get();
        db.flip();
    }
    
    @Override
    public DoubleBuffer store() {
        final DoubleBuffer db = BufferUtils.createDoubleBuffer(this.SIZE);
        db.put(this.x);
        db.flip();
        return db;
    }
    
    @Override
    public void insertStore(final ByteBuffer bb) {
        if (bb.position() + this.SIZE * Vector.getBytePerPrecision(this.PREC) > bb.capacity()) {
            throw new IllegalArgumentException("Not Enough space to store this Vector!");
        }
        bb.putDouble(this.x);
    }
    
    @Override
    public String toString() {
        return "Vector1d[" + this.x + "]";
    }
    
    @Override
    public Vector copy() {
        return new dvec1(this);
    }
    
    @Override
    public Vector add(final Vector v) {
        if (!(v instanceof dvec1)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final dvec1 adder = (dvec1)v;
        this.x += adder.x;
        return this;
    }
    
    @Override
    public Vector subtract(final Vector v) {
        if (!(v instanceof dvec1)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final dvec1 adder = (dvec1)v;
        this.x -= adder.x;
        return this;
    }
}
