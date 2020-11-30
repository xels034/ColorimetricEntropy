// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import java.nio.Buffer;

public class ivec2 extends Vector
{
    private static final long serialVersionUID = -3068017465925763122L;
    public int x;
    public int y;
    
    public ivec2() {
        this.SIZE = 2;
        this.PREC = 0;
    }
    
    public ivec2(final int x) {
        this();
        this.x = x;
        this.y = x;
    }
    
    public ivec2(final int x, final int y) {
        this();
        this.x = x;
        this.y = y;
    }
    
    public ivec2(final dvec2 v) {
        this();
        this.x = (int)v.x;
        this.y = (int)v.y;
    }
    
    public ivec2(final vec2 v) {
        this();
        this.x = (int)v.x;
        this.y = (int)v.y;
    }
    
    public ivec2(final ivec2 v) {
        this();
        this.x = v.x;
        this.y = v.y;
    }
    
    @Override
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }
    
    @Override
    public ivec2 negate() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }
    
    @Override
    public ivec2 scale(final double scale) {
        this.x *= scale;
        this.y *= scale;
        return this;
    }
    
    public static int dot(final ivec2 a, final ivec2 b) {
        return a.x * b.x + a.y * b.y;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof IntBuffer)) {
            throw new IllegalArgumentException("Provide a siutable IntBuffer!");
        }
        if (buf.capacity() != 2) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final IntBuffer ib = (IntBuffer)buf;
        this.x = ib.get();
        this.y = ib.get();
        ib.flip();
    }
    
    @Override
    public IntBuffer store() {
        final IntBuffer ib = BufferUtils.createIntBuffer(2);
        ib.put(this.x);
        ib.put(this.y);
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
    }
    
    @Override
    public String toString() {
        return "Vector2i[" + this.x + ", " + this.y + "]";
    }
    
    @Override
    public Vector copy() {
        return new ivec2(this);
    }
    
    @Override
    public Vector add(final Vector v) {
        if (!(v instanceof ivec2)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final ivec2 adder = (ivec2)v;
        this.x += adder.x;
        this.y += adder.y;
        return this;
    }
    
    @Override
    public Vector subtract(final Vector v) {
        if (!(v instanceof ivec2)) {
            throw new IllegalArgumentException("Vectors not of same size and precision");
        }
        final ivec2 adder = (ivec2)v;
        this.x -= adder.x;
        this.y -= adder.y;
        return this;
    }
}
