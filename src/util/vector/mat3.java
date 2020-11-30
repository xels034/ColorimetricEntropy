// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.Buffer;
import java.io.Serializable;

public class mat3 extends Matrix implements Serializable
{
    private static final long serialVersionUID = 1L;
    public float m00;
    public float m01;
    public float m02;
    public float m10;
    public float m11;
    public float m12;
    public float m20;
    public float m21;
    public float m22;
    
    private mat3(final boolean a) {
        this.SIZE = 3;
        this.PREC = 1;
    }
    
    public mat3() {
        this(true);
        this.setIdentity();
    }
    
    public mat3(final mat3 src) {
        this(true);
        load(src, this);
    }
    
    public dmat3 asDouble() {
        final dmat3 m = new dmat3();
        m.m00 = this.m00;
        m.m01 = this.m01;
        m.m02 = this.m02;
        m.m10 = this.m10;
        m.m11 = this.m11;
        m.m12 = this.m12;
        m.m20 = this.m20;
        m.m21 = this.m21;
        m.m22 = this.m22;
        return m;
    }
    
    public mat3 load(final mat3 src) {
        return load(src, this);
    }
    
    public static mat3 load(final mat3 src, mat3 dest) {
        if (dest == null) {
            dest = new mat3();
        }
        dest.m00 = src.m00;
        dest.m10 = src.m10;
        dest.m20 = src.m20;
        dest.m01 = src.m01;
        dest.m11 = src.m11;
        dest.m21 = src.m21;
        dest.m02 = src.m02;
        dest.m12 = src.m12;
        dest.m22 = src.m22;
        return dest;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof FloatBuffer)) {
            throw new IllegalArgumentException("Provide a siutable FloatBuffer!");
        }
        if (buf.capacity() != 9) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final FloatBuffer fb = (FloatBuffer)buf;
        this.m00 = fb.get();
        this.m01 = fb.get();
        this.m02 = fb.get();
        this.m10 = fb.get();
        this.m11 = fb.get();
        this.m12 = fb.get();
        this.m20 = fb.get();
        this.m21 = fb.get();
        this.m22 = fb.get();
        fb.flip();
    }
    
    @Override
    public void store(final Buffer buf) {
        if (!(buf instanceof FloatBuffer)) {
            throw new IllegalArgumentException("Provide a siutable FloatBuffer!");
        }
        if (buf.position() + 9 > buf.capacity()) {
            throw new IllegalArgumentException("Not Enough space to store this Matrix!");
        }
        final FloatBuffer fb = (FloatBuffer)buf;
        fb.put(this.m00);
        fb.put(this.m01);
        fb.put(this.m02);
        fb.put(this.m10);
        fb.put(this.m11);
        fb.put(this.m12);
        fb.put(this.m20);
        fb.put(this.m21);
        fb.put(this.m22);
    }
    
    @Override
    public FloatBuffer store() {
        final FloatBuffer fb = BufferUtils.createFloatBuffer(9);
        fb.put(this.m00);
        fb.put(this.m01);
        fb.put(this.m02);
        fb.put(this.m10);
        fb.put(this.m11);
        fb.put(this.m12);
        fb.put(this.m20);
        fb.put(this.m21);
        fb.put(this.m22);
        fb.flip();
        return fb;
    }
    
    public mat3 add(final mat3 right) {
        this.m00 += right.m00;
        this.m01 += right.m01;
        this.m02 += right.m02;
        this.m10 += right.m10;
        this.m11 += right.m11;
        this.m12 += right.m12;
        this.m20 += right.m20;
        this.m21 += right.m21;
        this.m22 += right.m22;
        return this;
    }
    
    public mat3 subtract(final mat3 right) {
        this.m00 -= right.m00;
        this.m01 -= right.m01;
        this.m02 -= right.m02;
        this.m10 -= right.m10;
        this.m11 -= right.m11;
        this.m12 -= right.m12;
        this.m20 -= right.m20;
        this.m21 -= right.m21;
        this.m22 -= right.m22;
        return this;
    }
    
    public mat3 mul(final mat3 right) {
        final float tm00 = this.m00 * right.m00 + this.m10 * right.m01 + this.m20 * right.m02;
        final float tm2 = this.m01 * right.m00 + this.m11 * right.m01 + this.m21 * right.m02;
        final float tm3 = this.m02 * right.m00 + this.m12 * right.m01 + this.m22 * right.m02;
        final float tm4 = this.m00 * right.m10 + this.m10 * right.m11 + this.m20 * right.m12;
        final float tm5 = this.m01 * right.m10 + this.m11 * right.m11 + this.m21 * right.m12;
        final float tm6 = this.m02 * right.m10 + this.m12 * right.m11 + this.m22 * right.m12;
        final float tm7 = this.m00 * right.m20 + this.m10 * right.m21 + this.m20 * right.m22;
        final float tm8 = this.m01 * right.m20 + this.m11 * right.m21 + this.m21 * right.m22;
        final float tm9 = this.m02 * right.m20 + this.m12 * right.m21 + this.m22 * right.m22;
        this.m00 = tm00;
        this.m01 = tm2;
        this.m02 = tm3;
        this.m10 = tm4;
        this.m11 = tm5;
        this.m12 = tm6;
        this.m20 = tm7;
        this.m21 = tm8;
        this.m22 = tm9;
        return this;
    }
    
    public vec3 transform(final vec3 v) {
        final float x = this.m00 * v.x + this.m10 * v.y + this.m20 * v.z;
        final float y = this.m01 * v.x + this.m11 * v.y + this.m21 * v.z;
        final float z = this.m02 * v.x + this.m12 * v.y + this.m22 * v.z;
        v.x = x;
        v.y = y;
        v.z = z;
        return v;
    }
    
    @Override
    public Matrix transpose() {
        final float tm00 = this.m00;
        final float tm2 = this.m10;
        final float tm3 = this.m20;
        final float tm4 = this.m01;
        final float tm5 = this.m11;
        final float tm6 = this.m21;
        final float tm7 = this.m02;
        final float tm8 = this.m12;
        final float tm9 = this.m22;
        this.m00 = tm00;
        this.m01 = tm2;
        this.m02 = tm3;
        this.m10 = tm4;
        this.m11 = tm5;
        this.m12 = tm6;
        this.m20 = tm7;
        this.m21 = tm8;
        this.m22 = tm9;
        return this;
    }
    
    @Override
    public Matrix invert() {
        final double determinant = this.determinant();
        if (determinant != 0.0) {
            final float determinant_inv = (float)(1.0 / this.determinant());
            final float tm00 = this.m11 * this.m22 - this.m12 * this.m21;
            final float tm2 = -this.m10 * this.m22 + this.m12 * this.m20;
            final float tm3 = this.m10 * this.m21 - this.m11 * this.m20;
            final float tm4 = -this.m01 * this.m22 + this.m02 * this.m21;
            final float tm5 = this.m00 * this.m22 - this.m02 * this.m20;
            final float tm6 = -this.m00 * this.m21 + this.m01 * this.m20;
            final float tm7 = this.m01 * this.m12 - this.m02 * this.m11;
            final float tm8 = -this.m00 * this.m12 + this.m02 * this.m10;
            final float tm9 = this.m00 * this.m11 - this.m01 * this.m10;
            this.m00 = tm00 * determinant_inv;
            this.m11 = tm5 * determinant_inv;
            this.m22 = tm9 * determinant_inv;
            this.m01 = tm4 * determinant_inv;
            this.m10 = tm2 * determinant_inv;
            this.m20 = tm3 * determinant_inv;
            this.m02 = tm7 * determinant_inv;
            this.m12 = tm8 * determinant_inv;
            this.m21 = tm6 * determinant_inv;
            return this;
        }
        return null;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(this.m00).append(' ').append(this.m10).append(' ').append(this.m20).append(' ').append('\n');
        buf.append(this.m01).append(' ').append(this.m11).append(' ').append(this.m21).append(' ').append('\n');
        buf.append(this.m02).append(' ').append(this.m12).append(' ').append(this.m22).append(' ').append('\n');
        return buf.toString();
    }
    
    @Override
    public Matrix negate() {
        this.m00 = -this.m00;
        this.m01 = -this.m02;
        this.m02 = -this.m01;
        this.m10 = -this.m10;
        this.m11 = -this.m12;
        this.m12 = -this.m11;
        this.m20 = -this.m20;
        this.m21 = -this.m22;
        this.m22 = -this.m21;
        return this;
    }
    
    @Override
    public Matrix setIdentity() {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        return this;
    }
    
    @Override
    public Matrix setZero() {
        this.m00 = 0.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 0.0f;
        this.m12 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 0.0f;
        return this;
    }
    
    @Override
    public double determinant() {
        final double d = this.m00 * (this.m11 * this.m22 - this.m12 * this.m21) + this.m01 * (this.m12 * this.m20 - this.m10 * this.m22) + this.m02 * (this.m10 * this.m21 - this.m11 * this.m20);
        return d;
    }
}
