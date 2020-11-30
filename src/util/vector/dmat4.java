// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import org.lwjgl.BufferUtils;
import java.nio.DoubleBuffer;
import java.nio.Buffer;
import java.io.Serializable;

public class dmat4 extends Matrix implements Serializable
{
    private static final long serialVersionUID = 1L;
    public double m00;
    public double m01;
    public double m02;
    public double m03;
    public double m10;
    public double m11;
    public double m12;
    public double m13;
    public double m20;
    public double m21;
    public double m22;
    public double m23;
    public double m30;
    public double m31;
    public double m32;
    public double m33;
    
    private dmat4(final boolean a) {
        this.SIZE = 4;
        this.PREC = 2;
    }
    
    public dmat4() {
        this(true);
        this.setIdentity();
    }
    
    public dmat4(final dmat4 src) {
        this(true);
        load(src, this);
    }
    
    public mat4 asFloat() {
        final mat4 m = new mat4();
        m.m00 = (float)this.m00;
        m.m01 = (float)this.m01;
        m.m02 = (float)this.m02;
        m.m03 = (float)this.m03;
        m.m10 = (float)this.m10;
        m.m11 = (float)this.m11;
        m.m12 = (float)this.m12;
        m.m13 = (float)this.m13;
        m.m20 = (float)this.m20;
        m.m21 = (float)this.m21;
        m.m22 = (float)this.m22;
        m.m23 = (float)this.m23;
        m.m30 = (float)this.m30;
        m.m31 = (float)this.m31;
        m.m32 = (float)this.m32;
        m.m33 = (float)this.m33;
        return m;
    }
    
    public dmat4 load(final dmat4 src) {
        return load(src, this);
    }
    
    public static dmat4 load(final dmat4 src, dmat4 dest) {
        if (dest == null) {
            dest = new dmat4();
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
        if (!(buf instanceof DoubleBuffer)) {
            throw new IllegalArgumentException("Provide a siutable DoubleBuffer!");
        }
        if (buf.capacity() != 16) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final DoubleBuffer db = (DoubleBuffer)buf;
        this.m00 = db.get();
        this.m01 = db.get();
        this.m02 = db.get();
        this.m03 = db.get();
        this.m10 = db.get();
        this.m11 = db.get();
        this.m12 = db.get();
        this.m13 = db.get();
        this.m20 = db.get();
        this.m21 = db.get();
        this.m22 = db.get();
        this.m23 = db.get();
        this.m30 = db.get();
        this.m31 = db.get();
        this.m32 = db.get();
        this.m33 = db.get();
        db.flip();
    }
    
    @Override
    public void store(final Buffer buf) {
        if (!(buf instanceof DoubleBuffer)) {
            throw new IllegalArgumentException("Provide a siutable DoubleBuffer!");
        }
        if (buf.position() + 16 > buf.capacity()) {
            throw new IllegalArgumentException("Not Enough space to store this Matrix!");
        }
        final DoubleBuffer db = (DoubleBuffer)buf;
        db.put(this.m00);
        db.put(this.m01);
        db.put(this.m02);
        db.put(this.m03);
        db.put(this.m10);
        db.put(this.m11);
        db.put(this.m12);
        db.put(this.m13);
        db.put(this.m20);
        db.put(this.m21);
        db.put(this.m22);
        db.put(this.m23);
        db.put(this.m30);
        db.put(this.m31);
        db.put(this.m32);
        db.put(this.m33);
    }
    
    @Override
    public DoubleBuffer store() {
        final DoubleBuffer db = BufferUtils.createDoubleBuffer(16);
        db.put(this.m00);
        db.put(this.m01);
        db.put(this.m02);
        db.put(this.m03);
        db.put(this.m10);
        db.put(this.m11);
        db.put(this.m12);
        db.put(this.m13);
        db.put(this.m20);
        db.put(this.m21);
        db.put(this.m22);
        db.put(this.m23);
        db.put(this.m30);
        db.put(this.m31);
        db.put(this.m32);
        db.put(this.m33);
        db.flip();
        return db;
    }
    
    public dmat4 add(final dmat4 right) {
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
    
    public dmat4 subtract(final dmat4 right) {
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
    
    public dmat4 mul(final dmat4 right) {
        final double tm00 = this.m00 * right.m00 + this.m10 * right.m01 + this.m20 * right.m02 + this.m30 * right.m03;
        final double tm2 = this.m01 * right.m00 + this.m11 * right.m01 + this.m21 * right.m02 + this.m31 * right.m03;
        final double tm3 = this.m02 * right.m00 + this.m12 * right.m01 + this.m22 * right.m02 + this.m32 * right.m03;
        final double tm4 = this.m03 * right.m00 + this.m13 * right.m01 + this.m23 * right.m02 + this.m33 * right.m03;
        final double tm5 = this.m00 * right.m10 + this.m10 * right.m11 + this.m20 * right.m12 + this.m30 * right.m13;
        final double tm6 = this.m01 * right.m10 + this.m11 * right.m11 + this.m21 * right.m12 + this.m31 * right.m13;
        final double tm7 = this.m02 * right.m10 + this.m12 * right.m11 + this.m22 * right.m12 + this.m32 * right.m13;
        final double tm8 = this.m03 * right.m10 + this.m13 * right.m11 + this.m23 * right.m12 + this.m33 * right.m13;
        final double tm9 = this.m00 * right.m20 + this.m10 * right.m21 + this.m20 * right.m22 + this.m30 * right.m23;
        final double tm10 = this.m01 * right.m20 + this.m11 * right.m21 + this.m21 * right.m22 + this.m31 * right.m23;
        final double tm11 = this.m02 * right.m20 + this.m12 * right.m21 + this.m22 * right.m22 + this.m32 * right.m23;
        final double tm12 = this.m03 * right.m20 + this.m13 * right.m21 + this.m23 * right.m22 + this.m33 * right.m23;
        final double tm13 = this.m00 * right.m30 + this.m10 * right.m31 + this.m20 * right.m32 + this.m30 * right.m33;
        final double tm14 = this.m01 * right.m30 + this.m11 * right.m31 + this.m21 * right.m32 + this.m31 * right.m33;
        final double tm15 = this.m02 * right.m30 + this.m12 * right.m31 + this.m22 * right.m32 + this.m32 * right.m33;
        final double tm16 = this.m03 * right.m30 + this.m13 * right.m31 + this.m23 * right.m32 + this.m33 * right.m33;
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
    
    public dvec4 transform(final dvec4 v) {
        final double x = this.m00 * v.x + this.m10 * v.y + this.m20 * v.z + this.m30 * v.w;
        final double y = this.m01 * v.x + this.m11 * v.y + this.m21 * v.z + this.m31 * v.w;
        final double z = this.m02 * v.x + this.m12 * v.y + this.m22 * v.z + this.m32 * v.w;
        final double w = this.m03 * v.x + this.m13 * v.y + this.m23 * v.z + this.m33 * v.w;
        v.x = x;
        v.y = y;
        v.z = z;
        v.w = w;
        return v;
    }
    
    @Override
    public Matrix transpose() {
        final double tm00 = this.m00;
        final double tm2 = this.m10;
        final double tm3 = this.m20;
        final double tm4 = this.m30;
        final double tm5 = this.m01;
        final double tm6 = this.m11;
        final double tm7 = this.m21;
        final double tm8 = this.m31;
        final double tm9 = this.m02;
        final double tm10 = this.m12;
        final double tm11 = this.m22;
        final double tm12 = this.m32;
        final double tm13 = this.m03;
        final double tm14 = this.m13;
        final double tm15 = this.m23;
        final double tm16 = this.m33;
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
    
    private static double determinant3x3(final double t00, final double t01, final double t02, final double t10, final double t11, final double t12, final double t20, final double t21, final double t22) {
        return t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22) + t02 * (t10 * t21 - t11 * t20);
    }
    
    @Override
    public Matrix invert() {
        final double determinant = this.determinant();
        if (determinant != 0.0) {
            final double determinant_inv = 1.0 / determinant;
            final double tm00 = determinant3x3(this.m11, this.m12, this.m13, this.m21, this.m22, this.m23, this.m31, this.m32, this.m33);
            final double tm2 = -determinant3x3(this.m10, this.m12, this.m13, this.m20, this.m22, this.m23, this.m30, this.m32, this.m33);
            final double tm3 = determinant3x3(this.m10, this.m11, this.m13, this.m20, this.m21, this.m23, this.m30, this.m31, this.m33);
            final double tm4 = -determinant3x3(this.m10, this.m11, this.m12, this.m20, this.m21, this.m22, this.m30, this.m31, this.m32);
            final double tm5 = -determinant3x3(this.m01, this.m02, this.m03, this.m21, this.m22, this.m23, this.m31, this.m32, this.m33);
            final double tm6 = determinant3x3(this.m00, this.m02, this.m03, this.m20, this.m22, this.m23, this.m30, this.m32, this.m33);
            final double tm7 = -determinant3x3(this.m00, this.m01, this.m03, this.m20, this.m21, this.m23, this.m30, this.m31, this.m33);
            final double tm8 = determinant3x3(this.m00, this.m01, this.m02, this.m20, this.m21, this.m22, this.m30, this.m31, this.m32);
            final double tm9 = determinant3x3(this.m01, this.m02, this.m03, this.m11, this.m12, this.m13, this.m31, this.m32, this.m33);
            final double tm10 = -determinant3x3(this.m00, this.m02, this.m03, this.m10, this.m12, this.m13, this.m30, this.m32, this.m33);
            final double tm11 = determinant3x3(this.m00, this.m01, this.m03, this.m10, this.m11, this.m13, this.m30, this.m31, this.m33);
            final double tm12 = -determinant3x3(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m30, this.m31, this.m32);
            final double tm13 = -determinant3x3(this.m01, this.m02, this.m03, this.m11, this.m12, this.m13, this.m21, this.m22, this.m23);
            final double tm14 = determinant3x3(this.m00, this.m02, this.m03, this.m10, this.m12, this.m13, this.m20, this.m22, this.m23);
            final double tm15 = -determinant3x3(this.m00, this.m01, this.m03, this.m10, this.m11, this.m13, this.m20, this.m21, this.m23);
            final double tm16 = determinant3x3(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22);
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
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
        return this;
    }
    
    @Override
    public Matrix setZero() {
        this.m00 = 0.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;
        this.m10 = 0.0;
        this.m11 = 0.0;
        this.m12 = 0.0;
        this.m13 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 0.0;
        this.m23 = 0.0;
        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 0.0;
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
    
    public dmat4 scale(final dvec3 v) {
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
    
    public dmat4 rotate(final double a, final dvec3 v) {
        final double c = Math.cos(a);
        final double s = Math.sin(a);
        final double oneminusc = 1.0 - c;
        final double xy = v.x * v.y;
        final double yz = v.y * v.z;
        final double xz = v.x * v.z;
        final double xs = v.x * s;
        final double ys = v.y * s;
        final double zs = v.z * s;
        final double f00 = v.x * v.x * oneminusc + c;
        final double f2 = xy * oneminusc + zs;
        final double f3 = xz * oneminusc - ys;
        final double f4 = xy * oneminusc - zs;
        final double f5 = v.y * v.y * oneminusc + c;
        final double f6 = yz * oneminusc + xs;
        final double f7 = xz * oneminusc + ys;
        final double f8 = yz * oneminusc - xs;
        final double f9 = v.z * v.z * oneminusc + c;
        final double tm00 = this.m00 * f00 + this.m10 * f2 + this.m20 * f3;
        final double tm2 = this.m01 * f00 + this.m11 * f2 + this.m21 * f3;
        final double tm3 = this.m02 * f00 + this.m12 * f2 + this.m22 * f3;
        final double tm4 = this.m03 * f00 + this.m13 * f2 + this.m23 * f3;
        final double tm5 = this.m00 * f4 + this.m10 * f5 + this.m20 * f6;
        final double tm6 = this.m01 * f4 + this.m11 * f5 + this.m21 * f6;
        final double tm7 = this.m02 * f4 + this.m12 * f5 + this.m22 * f6;
        final double tm8 = this.m03 * f4 + this.m13 * f5 + this.m23 * f6;
        final double tm9 = this.m00 * f7 + this.m10 * f8 + this.m20 * f9;
        final double tm10 = this.m01 * f7 + this.m11 * f8 + this.m21 * f9;
        final double tm11 = this.m02 * f7 + this.m12 * f8 + this.m22 * f9;
        final double tm12 = this.m03 * f7 + this.m13 * f8 + this.m23 * f9;
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
    
    public dmat4 translate(final dvec3 v) {
        final double tm30 = this.m30 + this.m00 * v.x + this.m10 * v.y + this.m20 * v.z;
        final double tm31 = this.m31 + this.m01 * v.x + this.m11 * v.y + this.m21 * v.z;
        final double tm32 = this.m32 + this.m02 * v.x + this.m12 * v.y + this.m22 * v.z;
        final double tm33 = this.m33 + this.m03 * v.x + this.m13 * v.y + this.m23 * v.z;
        this.m30 = tm30;
        this.m31 = tm31;
        this.m32 = tm32;
        this.m33 = tm33;
        return this;
    }
    
    public dmat4 translate(final dvec2 v) {
        final double tm30 = this.m30 + this.m00 * v.x + this.m10 * v.y;
        final double tm31 = this.m31 + this.m01 * v.x + this.m11 * v.y;
        final double tm32 = this.m32 + this.m02 * v.x + this.m12 * v.y;
        final double tm33 = this.m33 + this.m03 * v.x + this.m13 * v.y;
        this.m30 = tm30;
        this.m31 = tm31;
        this.m32 = tm32;
        this.m33 = tm33;
        return this;
    }
}
