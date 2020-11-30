// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.nio.DoubleBuffer;
import java.nio.Buffer;

public class dvec3 extends Vector
{
    private static final long serialVersionUID = -3068017468524763122L;
    public double x;
    public double y;
    public double z;
    
    public dvec3() {
        this.SIZE = 3;
        this.PREC = 2;
    }
    
    public dvec3(final double x) {
        this();
        this.x = x;
        this.y = x;
        this.z = x;
    }
    
    public dvec3(final double x, final double y, final double z) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public dvec3(final dvec3 v) {
        this();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    public dvec3(final vec3 v) {
        this();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    public dvec3(final ivec3 v) {
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
    public dvec3 negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z -= this.z;
        return this;
    }
    
    @Override
    public dvec3 scale(final double scale) {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
        return this;
    }
    
    public static double dot(final dvec3 a, final dvec3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
    
    public static dvec3 cross(final dvec3 a, final dvec3 b) {
        final dvec3 c = new dvec3();
        c.x = a.y * b.z - a.z * b.y;
        c.y = a.z * b.x - a.x * b.z;
        c.z = a.x * b.y - a.y * b.x;
        return c;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof DoubleBuffer)) {
            throw new IllegalArgumentException("Provide a siutable DoubleBuffer!");
        }
        if (buf.capacity() != 3) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final DoubleBuffer db = (DoubleBuffer)buf;
        this.x = db.get();
        this.y = db.get();
        this.z = db.get();
        db.flip();
    }
    
    @Override
    public DoubleBuffer store() {
        final DoubleBuffer db = BufferUtils.createDoubleBuffer(3);
        db.put(this.x);
        db.put(this.y);
        db.put(this.z);
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
    }
    
    @Override
    public String toString() {
        return "Vector3d[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
    
    @Override
    public Vector copy() {
        return new dvec3(this);
    }
    
    @Override
    public Vector add(final Vector v) {
        if (!(v instanceof dvec3)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final dvec3 adder = (dvec3)v;
        this.x += adder.x;
        this.y += adder.y;
        this.z += adder.z;
        return this;
    }
    
    @Override
    public Vector subtract(final Vector v) {
        if (!(v instanceof dvec3)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final dvec3 adder = (dvec3)v;
        this.x -= adder.x;
        this.y -= adder.y;
        this.z -= adder.z;
        return this;
    }
}
