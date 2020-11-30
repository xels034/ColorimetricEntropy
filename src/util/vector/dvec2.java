// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.nio.DoubleBuffer;
import java.nio.Buffer;

public class dvec2 extends Vector
{
    private static final long serialVersionUID = -3068017465925763122L;
    public double x;
    public double y;
    
    public dvec2() {
        this.SIZE = 2;
        this.PREC = 2;
    }
    
    public dvec2(final double x) {
        this();
        this.x = x;
        this.y = x;
    }
    
    public dvec2(final double x, final double y) {
        this();
        this.x = x;
        this.y = y;
    }
    
    public dvec2(final dvec2 v) {
        this();
        this.x = v.x;
        this.y = v.y;
    }
    
    public dvec2(final vec2 v) {
        this();
        this.x = v.x;
        this.y = v.y;
    }
    
    public dvec2(final ivec2 v) {
        this();
        this.x = v.x;
        this.y = v.y;
    }
    
    @Override
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }
    
    @Override
    public dvec2 negate() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }
    
    @Override
    public dvec2 scale(final double scale) {
        this.x *= scale;
        this.y *= scale;
        return this;
    }
    
    public static double dot(final dvec2 a, final dvec2 b) {
        return a.x * b.x + a.y * b.y;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof DoubleBuffer)) {
            throw new IllegalArgumentException("Provide a siutable DoubleBuffer!");
        }
        if (buf.capacity() != 2) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final DoubleBuffer db = (DoubleBuffer)buf;
        this.x = db.get();
        this.y = db.get();
        db.flip();
    }
    
    @Override
    public DoubleBuffer store() {
        final DoubleBuffer db = BufferUtils.createDoubleBuffer(2);
        db.put(this.x);
        db.put(this.y);
        db.flip();
        return db;
    }
    
    @Override
    public void insertStore(final ByteBuffer bb) {
        if (bb.position() + this.SIZE * Vector.getBytePerPrecision(this.PREC) > bb.capacity()) {
            throw new IllegalArgumentException("Not Enough space to store this Vector!");
        }
        bb.putDouble(this.x);
        bb.putDouble(this.y);
    }
    
    @Override
    public String toString() {
        return "Vector2d[" + this.x + ", " + this.y + "]";
    }
    
    @Override
    public Vector copy() {
        return new dvec2(this);
    }
    
    @Override
    public Vector add(final Vector v) {
        if (!(v instanceof dvec2)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final dvec2 adder = (dvec2)v;
        this.x += adder.x;
        this.y += adder.y;
        return this;
    }
    
    @Override
    public Vector subtract(final Vector v) {
        if (!(v instanceof dvec2)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final dvec2 adder = (dvec2)v;
        this.x -= adder.x;
        this.y -= adder.y;
        return this;
    }
}
