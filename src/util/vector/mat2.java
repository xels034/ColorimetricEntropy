// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.Buffer;
import java.io.Serializable;

public class mat2 extends Matrix implements Serializable
{
    private static final long serialVersionUID = 1L;
    public float m00;
    public float m01;
    public float m10;
    public float m11;
    
    private mat2(final boolean a) {
        this.SIZE = 2;
        this.PREC = 1;
    }
    
    public mat2() {
        this(true);
        this.setIdentity();
    }
    
    public mat2(final mat2 src) {
        this(true);
        this.load(src);
    }
    
    public dmat2 asDouble() {
        final dmat2 m = new dmat2();
        m.m00 = this.m00;
        m.m01 = this.m01;
        m.m10 = this.m10;
        m.m11 = this.m11;
        return m;
    }
    
    public mat2 load(final mat2 src) {
        return load(src, this);
    }
    
    public static mat2 load(final mat2 src, mat2 dest) {
        if (dest == null) {
            dest = new mat2();
        }
        dest.m00 = src.m00;
        dest.m01 = src.m01;
        dest.m10 = src.m10;
        dest.m11 = src.m11;
        return dest;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof FloatBuffer)) {
            throw new IllegalArgumentException("Provide a siutable FloatBuffer!");
        }
        if (buf.capacity() != 4) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final FloatBuffer fb = (FloatBuffer)buf;
        this.m00 = fb.get();
        this.m01 = fb.get();
        this.m10 = fb.get();
        this.m11 = fb.get();
        fb.flip();
    }
    
    @Override
    public void store(final Buffer buf) {
        if (!(buf instanceof FloatBuffer)) {
            throw new IllegalArgumentException("Provide a siutable FloatBuffer!");
        }
        if (buf.position() + 4 > buf.capacity()) {
            throw new IllegalArgumentException("Not Enough space to store this Matrix!");
        }
        final FloatBuffer fb = (FloatBuffer)buf;
        fb.put(this.m00);
        fb.put(this.m01);
        fb.put(this.m10);
        fb.put(this.m11);
    }
    
    @Override
    public FloatBuffer store() {
        final FloatBuffer fb = BufferUtils.createFloatBuffer(2);
        fb.put(this.m00);
        fb.put(this.m01);
        fb.put(this.m10);
        fb.put(this.m11);
        fb.flip();
        return fb;
    }
    
    public mat2 add(final mat2 right) {
        this.m00 += right.m00;
        this.m01 += right.m01;
        this.m10 += right.m10;
        this.m11 += right.m11;
        return this;
    }
    
    public mat2 subtract(final mat2 right) {
        this.m00 -= right.m00;
        this.m01 -= right.m01;
        this.m10 -= right.m10;
        this.m11 -= right.m11;
        return this;
    }
    
    public mat2 mul(final mat2 right) {
        final float tm00 = this.m00 * right.m00 + this.m10 * right.m01;
        final float tm2 = this.m01 * right.m00 + this.m11 * right.m01;
        final float tm3 = this.m00 * right.m10 + this.m10 * right.m11;
        final float tm4 = this.m01 * right.m10 + this.m11 * right.m11;
        this.m00 = tm00;
        this.m01 = tm2;
        this.m10 = tm3;
        this.m11 = tm4;
        return this;
    }
    
    public vec2 transform(final vec2 v) {
        final float x = this.m00 * v.x + this.m10 * v.y;
        final float y = this.m01 * v.x + this.m11 * v.y;
        v.x = x;
        v.y = y;
        return v;
    }
    
    @Override
    public Matrix transpose() {
        final float tm01 = this.m10;
        final float tm2 = this.m01;
        this.m01 = tm01;
        this.m10 = tm2;
        return this;
    }
    
    @Override
    public Matrix invert() {
        final float determinant_inv = (float)(1.0 / this.determinant());
        final float tm00 = this.m11 * determinant_inv;
        final float tm2 = -this.m01 * determinant_inv;
        final float tm3 = this.m00 * determinant_inv;
        final float tm4 = -this.m10 * determinant_inv;
        this.m00 = tm00;
        this.m01 = tm2;
        this.m10 = tm4;
        this.m11 = tm3;
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(this.m00).append(' ').append(this.m10).append(' ').append('\n');
        buf.append(this.m01).append(' ').append(this.m11).append(' ').append('\n');
        return buf.toString();
    }
    
    @Override
    public Matrix negate() {
        this.m00 = -this.m00;
        this.m01 = -this.m01;
        this.m10 = -this.m10;
        this.m11 = -this.m11;
        return this;
    }
    
    @Override
    public Matrix setIdentity() {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        return this;
    }
    
    @Override
    public Matrix setZero() {
        this.m00 = 0.0f;
        this.m01 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 0.0f;
        return this;
    }
    
    @Override
    public double determinant() {
        return this.m00 * this.m11 - this.m01 * this.m10;
    }
}
