// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.nio.DoubleBuffer;
import java.nio.Buffer;

public class dvec4 extends Vector
{
    private static final long serialVersionUID = -3069137468524763122L;
    public double x;
    public double y;
    public double z;
    public double w;
    
    public dvec4() {
        this.SIZE = 4;
        this.PREC = 2;
    }
    
    public dvec4(final double x) {
        this();
        this.x = x;
        this.y = x;
        this.z = x;
        this.w = x;
    }
    
    public dvec4(final double x, final double y, final double z, final double w) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public dvec4(final dvec4 v) {
        this();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }
    
    public dvec4(final vec4 v) {
        this();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }
    
    public dvec4(final ivec4 v) {
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
    public dvec4 negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z -= this.z;
        this.w = -this.w;
        return this;
    }
    
    @Override
    public dvec4 scale(final double scale) {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
        this.w *= scale;
        return this;
    }
    
    public static double dot(final dvec4 a, final dvec4 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof DoubleBuffer)) {
            throw new IllegalArgumentException("Provide a siutable DoubleBuffer!");
        }
        if (buf.capacity() != 4) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final DoubleBuffer db = (DoubleBuffer)buf;
        this.x = db.get();
        this.y = db.get();
        this.z = db.get();
        this.w = db.get();
        db.flip();
    }
    
    @Override
    public DoubleBuffer store() {
        final DoubleBuffer db = BufferUtils.createDoubleBuffer(4);
        db.put(this.x);
        db.put(this.y);
        db.put(this.z);
        db.put(this.w);
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
        bb.putDouble(this.z);
        bb.putDouble(this.w);
    }
    
    @Override
    public String toString() {
        return "Vector4d[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
    }
    
    @Override
    public Vector copy() {
        return new dvec4(this);
    }
    
    @Override
    public Vector add(final Vector v) {
        if (!(v instanceof dvec4)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final dvec4 adder = (dvec4)v;
        this.x += adder.x;
        this.y += adder.y;
        this.z += adder.z;
        this.w += adder.w;
        return this;
    }
    
    @Override
    public Vector subtract(final Vector v) {
        if (!(v instanceof dvec4)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final dvec4 adder = (dvec4)v;
        this.x -= adder.x;
        this.y -= adder.y;
        this.z -= adder.z;
        this.w -= adder.w;
        return this;
    }
}
