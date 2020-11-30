// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import java.nio.Buffer;

public class ivec4 extends Vector
{
    private static final long serialVersionUID = -3068017465925763122L;
    public int x;
    public int y;
    public int z;
    public int w;
    
    public ivec4() {
        this.SIZE = 4;
        this.PREC = 0;
    }
    
    public ivec4(final int x) {
        this();
        this.x = x;
        this.y = x;
        this.z = x;
        this.w = x;
    }
    
    public ivec4(final int x, final int y, final int z, final int w) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public ivec4(final dvec4 v) {
        this();
        this.x = (int)v.x;
        this.y = (int)v.y;
        this.z = (int)v.z;
        this.w = (int)v.w;
    }
    
    public ivec4(final vec4 v) {
        this();
        this.x = (int)v.x;
        this.y = (int)v.y;
        this.z = (int)v.z;
        this.w = (int)v.w;
    }
    
    public ivec4(final ivec4 v) {
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
    public ivec4 negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
        return this;
    }
    
    @Override
    public ivec4 scale(final double scale) {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
        this.w *= scale;
        return this;
    }
    
    public static int dot(final ivec4 a, final ivec4 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof IntBuffer)) {
            throw new IllegalArgumentException("Provide a siutable IntBuffer!");
        }
        if (buf.capacity() != 4) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final IntBuffer ib = (IntBuffer)buf;
        this.x = ib.get();
        this.y = ib.get();
        this.z = ib.get();
        this.w = ib.get();
        ib.flip();
    }
    
    @Override
    public IntBuffer store() {
        final IntBuffer ib = BufferUtils.createIntBuffer(4);
        ib.put(this.x);
        ib.put(this.y);
        ib.put(this.z);
        ib.put(this.w);
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
        bb.putInt(this.w);
    }
    
    @Override
    public String toString() {
        return "Vector4d[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
    }
    
    @Override
    public Vector copy() {
        return new ivec4(this);
    }
    
    @Override
    public Vector add(final Vector v) {
        if (!(v instanceof ivec4)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final ivec4 adder = (ivec4)v;
        this.x += adder.x;
        this.y += adder.y;
        this.z += adder.z;
        this.w += adder.w;
        return this;
    }
    
    @Override
    public Vector subtract(final Vector v) {
        if (!(v instanceof ivec4)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final ivec4 adder = (ivec4)v;
        this.x -= adder.x;
        this.y -= adder.y;
        this.z -= adder.z;
        this.w -= adder.w;
        return this;
    }
}
