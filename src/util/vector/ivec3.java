// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import java.nio.Buffer;

public class ivec3 extends Vector
{
    private static final long serialVersionUID = -3068017465925763122L;
    public int x;
    public int y;
    public int z;
    
    public ivec3() {
        this.SIZE = 3;
        this.PREC = 0;
    }
    
    public ivec3(final int x) {
        this();
        this.x = x;
        this.y = x;
        this.z = x;
    }
    
    public ivec3(final int x, final int y, final int z) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public ivec3(final dvec3 v) {
        this();
        this.x = (int)v.x;
        this.y = (int)v.y;
        this.z = (int)v.z;
    }
    
    public ivec3(final vec3 v) {
        this();
        this.x = (int)v.x;
        this.y = (int)v.y;
        this.z = (int)v.z;
    }
    
    public ivec3(final ivec3 v) {
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
    public ivec3 negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }
    
    @Override
    public ivec3 scale(final double scale) {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
        return this;
    }
    
    public static int dot(final ivec3 a, final ivec3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
    
    public static ivec3 cross(final ivec3 a, final ivec3 b) {
        final ivec3 c = new ivec3();
        c.x = a.y * b.z - a.z * b.y;
        c.y = a.z * b.x - a.x * b.z;
        c.z = a.x * b.y - a.y * b.x;
        return c;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof IntBuffer)) {
            throw new IllegalArgumentException("Provide a siutable IntBuffer!");
        }
        if (buf.capacity() != 3) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final IntBuffer ib = (IntBuffer)buf;
        this.x = ib.get();
        this.y = ib.get();
        this.z = ib.get();
        ib.flip();
    }
    
    @Override
    public IntBuffer store() {
        final IntBuffer ib = BufferUtils.createIntBuffer(3);
        ib.put(this.x);
        ib.put(this.y);
        ib.put(this.z);
        ib.flip();
        return ib;
    }
    
    @Override
    public void insertStore(final ByteBuffer bb) {
        if (bb.position() + this.SIZE * Vector.getBytePerPrecision(this.PREC) > bb.capacity()) {
            throw new IllegalArgumentException("Not Enough space to store this Vector!");
        }
        bb.putInt(this.x);
        bb.putInt(this.y);
        bb.putInt(this.z);
    }
    
    @Override
    public String toString() {
        return "Vector3i[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
    
    @Override
    public Vector copy() {
        return new ivec3(this);
    }
    
    @Override
    public Vector add(final Vector v) {
        if (!(v instanceof ivec3)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final ivec3 adder = (ivec3)v;
        this.x += adder.x;
        this.y += adder.y;
        this.z += adder.z;
        return this;
    }
    
    @Override
    public Vector subtract(final Vector v) {
        if (!(v instanceof ivec3)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final ivec3 adder = (ivec3)v;
        this.x -= adder.x;
        this.y -= adder.y;
        this.z -= adder.z;
        return this;
    }
}
