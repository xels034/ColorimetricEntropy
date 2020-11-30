// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.Buffer;
import java.io.Serializable;

public class mat4 extends Matrix implements Serializable
{
    private static final long serialVersionUID = 1L;
    public float m00;
    public float m01;
    public float m02;
    public float m03;
    public float m10;
    public float m11;
    public float m12;
    public float m13;
    public float m20;
    public float m21;
    public float m22;
    public float m23;
    public float m30;
    public float m31;
    public float m32;
    public float m33;
    
    private mat4(final boolean a) {
        this.SIZE = 4;
        this.PREC = 1;
    }
    
    public mat4() {
        this(true);
        this.setIdentity();
    }
    
    public mat4(final mat4 m) {
        this(true);
        load(m, this);
    }
    
    public dmat4 asDouble() {
        final dmat4 m = new dmat4();
        m.m00 = this.m00;
        m.m01 = this.m01;
        m.m02 = this.m02;
        m.m03 = this.m03;
        m.m10 = this.m10;
        m.m11 = this.m11;
        m.m12 = this.m12;
        m.m13 = this.m13;
        m.m20 = this.m20;
        m.m21 = this.m21;
        m.m22 = this.m22;
        m.m23 = this.m23;
        m.m30 = this.m30;
        m.m31 = this.m31;
        m.m32 = this.m32;
        m.m33 = this.m33;
        return m;
    }
    
    public mat4 load(final mat4 src) {
        return load(src, this);
    }
    
    public static mat4 load(final mat4 src, mat4 dest) {
        if (dest == null) {
            dest = new mat4();
        }
        dest.m00 = src.m00;
        dest.m01 = src.m01;
        dest.m02 = src.m02;
        dest.m03 = src.m03;
        dest.m10 = src.m10;
        dest.m11 = src.m11;
        dest.m12 = src.m12;
        dest.m13 = src.m13;
        dest.m20 = src.m20;
        dest.m21 = src.m21;
        dest.m22 = src.m22;
        dest.m23 = src.m23;
        dest.m30 = src.m30;
        dest.m31 = src.m31;
        dest.m32 = src.m32;
        dest.m33 = src.m33;
        return dest;
    }
    
    @Override
    public void load(final Buffer buf) {
        if (!(buf instanceof FloatBuffer)) {
            throw new IllegalArgumentException("Provide a siutable FloatBuffer!");
        }
        if (buf.capacity() != 16) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final FloatBuffer fb = (FloatBuffer)buf;
        this.m00 = fb.get();
        this.m01 = fb.get();
        this.m02 = fb.get();
        this.m03 = fb.get();
        this.m10 = fb.get();
        this.m11 = fb.get();
        this.m12 = fb.get();
        this.m13 = fb.get();
        this.m20 = fb.get();
        this.m21 = fb.get();
        this.m22 = fb.get();
        this.m23 = fb.get();
        this.m30 = fb.get();
        this.m31 = fb.get();
        this.m32 = fb.get();
        this.m33 = fb.get();
        fb.flip();
    }
    
    @Override
    public void store(final Buffer buf) {
        if (!(buf instanceof FloatBuffer)) {
            throw new IllegalArgumentException("Provide a siutable FloatBuffer!");
        }
        if (buf.position() + 16 > buf.capacity()) {
            throw new IllegalArgumentException("Not Enough space to store this Matrix!");
        }
        final FloatBuffer fb = (FloatBuffer)buf;
        fb.put(this.m00);
        fb.put(this.m01);
        fb.put(this.m02);
        fb.put(this.m03);
        fb.put(this.m10);
        fb.put(this.m11);
        fb.put(this.m12);
        fb.put(this.m13);
        fb.put(this.m20);
        fb.put(this.m21);
        fb.put(this.m22);
        fb.put(this.m23);
        fb.put(this.m30);
        fb.put(this.m31);
        fb.put(this.m32);
        fb.put(this.m33);
    }
    
    @Override
    public FloatBuffer store() {
        final FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        fb.put(this.m00);
        fb.put(this.m01);
        fb.put(this.m02);
        fb.put(this.m03);
        fb.put(this.m10);
        fb.put(this.m11);
        fb.put(this.m12);
        fb.put(this.m13);
        fb.put(this.m20);
        fb.put(this.m21);
        fb.put(this.m22);
        fb.put(this.m23);
        fb.put(this.m30);
        fb.put(this.m31);
        fb.put(this.m32);
        fb.put(this.m33);
        fb.flip();
        return fb;
    }
    
    public mat4 add(final mat4 right) {
        this.m00 += right.m00;
        this.m01 += right.m01;
        this.m02 += right.m02;
        this.m03 += right.m03;
        this.m10 += right.m10;
        this.m11 += right.m11;
        this.m12 += right.m12;
        this.m13 += right.m13;
        this.m20 += right.m20;
        this.m21 += right.m21;
        this.m22 += right.m22;
        this.m23 += right.m23;
        this.m30 += right.m30;
        this.m31 += right.m31;
        this.m32 += right.m32;
        this.m33 += right.m33;
        return this;
    }
    
    public mat4 subtract(final mat4 right) {
        this.m00 -= right.m00;
        this.m01 -= right.m01;
        this.m02 -= right.m02;
        this.m03 -= right.m03;
        this.m10 -= right.m10;
        this.m11 -= right.m11;
        this.m12 -= right.m12;
        this.m13 -= right.m13;
        this.m20 -= right.m20;
        this.m21 -= right.m21;
        this.m22 -= right.m22;
        this.m23 -= right.m23;
        this.m30 -= right.m30;
        this.m31 -= right.m31;
        this.m32 -= right.m32;
        this.m33 -= right.m33;
        return this;
    }
    
    public mat4 mul(final mat4 right) {
        final float tm00 = this.m00 * right.m00 + this.m10 * right.m01 + this.m20 * right.m02 + this.m30 * right.m03;
        final float tm2 = this.m01 * right.m00 + this.m11 * right.m01 + this.m21 * right.m02 + this.m31 * right.m03;
        final float tm3 = this.m02 * right.m00 + this.m12 * right.m01 + this.m22 * right.m02 + this.m32 * right.m03;
        final float tm4 = this.m03 * right.m00 + this.m13 * right.m01 + this.m23 * right.m02 + this.m33 * right.m03;
        final float tm5 = this.m00 * right.m10 + this.m10 * right.m11 + this.m20 * right.m12 + this.m30 * right.m13;
        final float tm6 = this.m01 * right.m10 + this.m11 * right.m11 + this.m21 * right.m12 + this.m31 * right.m13;
        final float tm7 = this.m02 * right.m10 + this.m12 * right.m11 + this.m22 * right.m12 + this.m32 * right.m13;
        final float tm8 = this.m03 * right.m10 + this.m13 * right.m11 + this.m23 * right.m12 + this.m33 * right.m13;
        final float tm9 = this.m00 * right.m20 + this.m10 * right.m21 + this.m20 * right.m22 + this.m30 * right.m23;
        final float tm10 = this.m01 * right.m20 + this.m11 * right.m21 + this.m21 * right.m22 + this.m31 * right.m23;
        final float tm11 = this.m02 * right.m20 + this.m12 * right.m21 + this.m22 * right.m22 + this.m32 * right.m23;
        final float tm12 = this.m03 * right.m20 + this.m13 * right.m21 + this.m23 * right.m22 + this.m33 * right.m23;
        final float tm13 = this.m00 * right.m30 + this.m10 * right.m31 + this.m20 * right.m32 + this.m30 * right.m33;
        final float tm14 = this.m01 * right.m30 + this.m11 * right.m31 + this.m21 * right.m32 + this.m31 * right.m33;
        final float tm15 = this.m02 * right.m30 + this.m12 * right.m31 + this.m22 * right.m32 + this.m32 * right.m33;
        final float tm16 = this.m03 * right.m30 + this.m13 * right.m31 + this.m23 * right.m32 + this.m33 * right.m33;
        this.m00 = tm00;
        this.m01 = tm2;
        this.m02 = tm3;
        this.m03 = tm4;
        this.m10 = tm5;
        this.m11 = tm6;
        this.m12 = tm7;
        this.m13 = tm8;
        this.m20 = tm9;
        this.m21 = tm10;
        this.m22 = tm11;
        this.m23 = tm12;
        this.m30 = tm13;
        this.m31 = tm14;
        this.m32 = tm15;
        this.m33 = tm16;
        return this;
    }
    
    public vec4 transform(final vec4 v) {
        final float x = this.m00 * v.x + this.m10 * v.y + this.m20 * v.z + this.m30 * v.w;
        final float y = this.m01 * v.x + this.m11 * v.y + this.m21 * v.z + this.m31 * v.w;
        final float z = this.m02 * v.x + this.m12 * v.y + this.m22 * v.z + this.m32 * v.w;
        final float w = this.m03 * v.x + this.m13 * v.y + this.m23 * v.z + this.m33 * v.w;
        v.x = x;
        v.y = y;
        v.z = z;
        v.w = w;
        return v;
    }
    
    @Override
    public Matrix transpose() {
        final float tm00 = this.m00;
        final float tm2 = this.m10;
        final float tm3 = this.m20;
        final float tm4 = this.m30;
        final float tm5 = this.m01;
        final float tm6 = this.m11;
        final float tm7 = this.m21;
        final float tm8 = this.m31;
        final float tm9 = this.m02;
        final float tm10 = this.m12;
        final float tm11 = this.m22;
        final float tm12 = this.m32;
        final float tm13 = this.m03;
        final float tm14 = this.m13;
        final float tm15 = this.m23;
        final float tm16 = this.m33;
        this.m00 = tm00;
        this.m01 = tm2;
        this.m02 = tm3;
        this.m03 = tm4;
        this.m10 = tm5;
        this.m11 = tm6;
        this.m12 = tm7;
        this.m13 = tm8;
        this.m20 = tm9;
        this.m21 = tm10;
        this.m22 = tm11;
        this.m23 = tm12;
        this.m30 = tm13;
        this.m31 = tm14;
        this.m32 = tm15;
        this.m33 = tm16;
        return this;
    }
    
    private static float determinant3x3(final float t00, final float t01, final float t02, final float t10, final float t11, final float t12, final float t20, final float t21, final float t22) {
        return t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22) + t02 * (t10 * t21 - t11 * t20);
    }
    
    @Override
    public Matrix invert() {
        final double determinant = this.determinant();
        if (determinant != 0.0) {
            final float determinant_inv = (float)(1.0 / determinant);
            final float tm00 = determinant3x3(this.m11, this.m12, this.m13, this.m21, this.m22, this.m23, this.m31, this.m32, this.m33);
            final float tm2 = -determinant3x3(this.m10, this.m12, this.m13, this.m20, this.m22, this.m23, this.m30, this.m32, this.m33);
            final float tm3 = determinant3x3(this.m10, this.m11, this.m13, this.m20, this.m21, this.m23, this.m30, this.m31, this.m33);
            final float tm4 = -determinant3x3(this.m10, this.m11, this.m12, this.m20, this.m21, this.m22, this.m30, this.m31, this.m32);
            final float tm5 = -determinant3x3(this.m01, this.m02, this.m03, this.m21, this.m22, this.m23, this.m31, this.m32, this.m33);
            final float tm6 = determinant3x3(this.m00, this.m02, this.m03, this.m20, this.m22, this.m23, this.m30, this.m32, this.m33);
            final float tm7 = -determinant3x3(this.m00, this.m01, this.m03, this.m20, this.m21, this.m23, this.m30, this.m31, this.m33);
            final float tm8 = determinant3x3(this.m00, this.m01, this.m02, this.m20, this.m21, this.m22, this.m30, this.m31, this.m32);
            final float tm9 = determinant3x3(this.m01, this.m02, this.m03, this.m11, this.m12, this.m13, this.m31, this.m32, this.m33);
            final float tm10 = -determinant3x3(this.m00, this.m02, this.m03, this.m10, this.m12, this.m13, this.m30, this.m32, this.m33);
            final float tm11 = determinant3x3(this.m00, this.m01, this.m03, this.m10, this.m11, this.m13, this.m30, this.m31, this.m33);
            final float tm12 = -determinant3x3(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m30, this.m31, this.m32);
            final float tm13 = -determinant3x3(this.m01, this.m02, this.m03, this.m11, this.m12, this.m13, this.m21, this.m22, this.m23);
            final float tm14 = determinant3x3(this.m00, this.m02, this.m03, this.m10, this.m12, this.m13, this.m20, this.m22, this.m23);
            final float tm15 = -determinant3x3(this.m00, this.m01, this.m03, this.m10, this.m11, this.m13, this.m20, this.m21, this.m23);
            final float tm16 = determinant3x3(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22);
            this.m00 = tm00 * determinant_inv;
            this.m11 = tm6 * determinant_inv;
            this.m22 = tm11 * determinant_inv;
            this.m33 = tm16 * determinant_inv;
            this.m01 = tm5 * determinant_inv;
            this.m10 = tm2 * determinant_inv;
            this.m20 = tm3 * determinant_inv;
            this.m02 = tm9 * determinant_inv;
            this.m12 = tm10 * determinant_inv;
            this.m21 = tm7 * determinant_inv;
            this.m03 = tm13 * determinant_inv;
            this.m30 = tm4 * determinant_inv;
            this.m13 = tm14 * determinant_inv;
            this.m31 = tm8 * determinant_inv;
            this.m32 = tm12 * determinant_inv;
            this.m23 = tm15 * determinant_inv;
            return this;
        }
        return null;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(this.m00).append(' ').append(this.m10).append(' ').append(this.m20).append(' ').append(this.m30).append('\n');
        buf.append(this.m01).append(' ').append(this.m11).append(' ').append(this.m21).append(' ').append(this.m31).append('\n');
        buf.append(this.m02).append(' ').append(this.m12).append(' ').append(this.m22).append(' ').append(this.m32).append('\n');
        buf.append(this.m03).append(' ').append(this.m13).append(' ').append(this.m23).append(' ').append(this.m33).append('\n');
        return buf.toString();
    }
    
    @Override
    public Matrix negate() {
        this.m00 = -this.m00;
        this.m01 = -this.m01;
        this.m02 = -this.m02;
        this.m03 = -this.m03;
        this.m10 = -this.m10;
        this.m11 = -this.m11;
        this.m12 = -this.m12;
        this.m13 = -this.m13;
        this.m20 = -this.m20;
        this.m21 = -this.m21;
        this.m22 = -this.m22;
        this.m23 = -this.m23;
        this.m30 = -this.m30;
        this.m31 = -this.m31;
        this.m32 = -this.m32;
        this.m33 = -this.m33;
        return this;
    }
    
    @Override
    public Matrix setIdentity() {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
        return this;
    }
    
    @Override
    public Matrix setZero() {
        this.m00 = 0.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 0.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 0.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 0.0f;
        return this;
    }
    
    @Override
    public double determinant() {
        double d = this.m00 * (this.m11 * this.m22 * this.m33 + this.m12 * this.m23 * this.m31 + this.m13 * this.m21 * this.m32 - this.m13 * this.m22 * this.m31 - this.m11 * this.m23 * this.m32 - this.m12 * this.m21 * this.m33);
        d -= this.m01 * (this.m10 * this.m22 * this.m33 + this.m12 * this.m23 * this.m30 + this.m13 * this.m20 * this.m32 - this.m13 * this.m22 * this.m30 - this.m10 * this.m23 * this.m32 - this.m12 * this.m20 * this.m33);
        d += this.m02 * (this.m10 * this.m21 * this.m33 + this.m11 * this.m23 * this.m30 + this.m13 * this.m20 * this.m31 - this.m13 * this.m21 * this.m30 - this.m10 * this.m23 * this.m31 - this.m11 * this.m20 * this.m33);
        d -= this.m03 * (this.m10 * this.m21 * this.m32 + this.m11 * this.m22 * this.m30 + this.m12 * this.m20 * this.m31 - this.m12 * this.m21 * this.m30 - this.m10 * this.m22 * this.m31 - this.m11 * this.m20 * this.m32);
        return d;
    }
    
    public mat4 scale(final vec3 v) {
        this.m00 *= v.x;
        this.m01 *= v.x;
        this.m02 *= v.x;
        this.m03 *= v.x;
        this.m10 *= v.y;
        this.m11 *= v.y;
        this.m12 *= v.y;
        this.m13 *= v.y;
        this.m20 *= v.z;
        this.m21 *= v.z;
        this.m22 *= v.z;
        this.m23 *= v.z;
        return this;
    }
    
    public mat4 rotate(final double a, final vec3 v) {
        final float c = (float)Math.cos(a);
        final float s = (float)Math.sin(a);
        final float oneminusc = 1.0f - c;
        final float xy = v.x * v.y;
        final float yz = v.y * v.z;
        final float xz = v.x * v.z;
        final float xs = v.x * s;
        final float ys = v.y * s;
        final float zs = v.z * s;
        final float f00 = v.x * v.x * oneminusc + c;
        final float f2 = xy * oneminusc + zs;
        final float f3 = xz * oneminusc - ys;
        final float f4 = xy * oneminusc - zs;
        final float f5 = v.y * v.y * oneminusc + c;
        final float f6 = yz * oneminusc + xs;
        final float f7 = xz * oneminusc + ys;
        final float f8 = yz * oneminusc - xs;
        final float f9 = v.z * v.z * oneminusc + c;
        final float tm00 = this.m00 * f00 + this.m10 * f2 + this.m20 * f3;
        final float tm2 = this.m01 * f00 + this.m11 * f2 + this.m21 * f3;
        final float tm3 = this.m02 * f00 + this.m12 * f2 + this.m22 * f3;
        final float tm4 = this.m03 * f00 + this.m13 * f2 + this.m23 * f3;
        final float tm5 = this.m00 * f4 + this.m10 * f5 + this.m20 * f6;
        final float tm6 = this.m01 * f4 + this.m11 * f5 + this.m21 * f6;
        final float tm7 = this.m02 * f4 + this.m12 * f5 + this.m22 * f6;
        final float tm8 = this.m03 * f4 + this.m13 * f5 + this.m23 * f6;
        final float tm9 = this.m00 * f7 + this.m10 * f8 + this.m20 * f9;
        final float tm10 = this.m01 * f7 + this.m11 * f8 + this.m21 * f9;
        final float tm11 = this.m02 * f7 + this.m12 * f8 + this.m22 * f9;
        final float tm12 = this.m03 * f7 + this.m13 * f8 + this.m23 * f9;
        this.m00 = tm00;
        this.m01 = tm2;
        this.m02 = tm3;
        this.m03 = tm4;
        this.m10 = tm5;
        this.m11 = tm6;
        this.m12 = tm7;
        this.m13 = tm8;
        this.m20 = tm9;
        this.m21 = tm10;
        this.m22 = tm11;
        this.m23 = tm12;
        return this;
    }
    
    public mat4 translate(final vec3 v) {
        final float tm30 = this.m30 + this.m00 * v.x + this.m10 * v.y + this.m20 * v.z;
        final float tm31 = this.m31 + this.m01 * v.x + this.m11 * v.y + this.m21 * v.z;
        final float tm32 = this.m32 + this.m02 * v.x + this.m12 * v.y + this.m22 * v.z;
        final float tm33 = this.m33 + this.m03 * v.x + this.m13 * v.y + this.m23 * v.z;
        this.m30 = tm30;
        this.m31 = tm31;
        this.m32 = tm32;
        this.m33 = tm33;
        return this;
    }
    
    public mat4 translate(final vec2 v) {
        final float tm30 = this.m30 + this.m00 * v.x + this.m10 * v.y;
        final float tm31 = this.m31 + this.m01 * v.x + this.m11 * v.y;
        final float tm32 = this.m32 + this.m02 * v.x + this.m12 * v.y;
        final float tm33 = this.m33 + this.m03 * v.x + this.m13 * v.y;
        this.m30 = tm30;
        this.m31 = tm31;
        this.m32 = tm32;
        this.m33 = tm33;
        return this;
    }
}
