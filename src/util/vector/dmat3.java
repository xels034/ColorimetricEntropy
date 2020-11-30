// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import org.lwjgl.BufferUtils;
import java.nio.DoubleBuffer;
import java.nio.Buffer;
import java.io.Serializable;

public class dmat3 extends Matrix implements Serializable
{
    private static final long serialVersionUID = 1L;
    public double m00;
    public double m01;
    public double m02;
    public double m10;
    public double m11;
    public double m12;
    public double m20;
    public double m21;
    public double m22;
    
    private dmat3(final boolean a) {
        this.SIZE = 3;
        this.PREC = 2;
    }
    
    public dmat3() {
        this(true);
        this.setIdentity();
    }
    
    public dmat3(final dmat3 src) {
        this(true);
        load(src, this);
    }
    
    public mat3 asFloat() {
        final mat3 m = new mat3();
        m.m00 = (float)this.m00;
        m.m01 = (float)this.m01;
        m.m02 = (float)this.m02;
        m.m10 = (float)this.m10;
        m.m11 = (float)this.m11;
        m.m12 = (float)this.m12;
        m.m20 = (float)this.m20;
        m.m21 = (float)this.m21;
        m.m22 = (float)this.m22;
        return m;
    }
    
    public dmat3 load(final dmat3 src) {
        return load(src, this);
    }
    
    public static dmat3 load(final dmat3 src, dmat3 dest) {
        if (dest == null) {
            dest = new dmat3();
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
        if (!(buf instanceof DoubleBuffer)) {
            throw new IllegalArgumentException("Provide a siutable DoubleBuffer!");
        }
        if (buf.capacity() != 9) {
            throw new IllegalArgumentException("Buffer has invalid size (" + buf.capacity() + ")");
        }
        final DoubleBuffer db = (DoubleBuffer)buf;
        this.m00 = db.get();
        this.m01 = db.get();
        this.m02 = db.get();
        this.m10 = db.get();
        this.m11 = db.get();
        this.m12 = db.get();
        this.m20 = db.get();
        this.m21 = db.get();
        this.m22 = db.get();
        db.flip();
    }
    
    @Override
    public void store(final Buffer buf) {
        if (!(buf instanceof DoubleBuffer)) {
            throw new IllegalArgumentException("Provide a siutable DoubleBuffer!");
        }
        if (buf.position() + 9 > buf.capacity()) {
            throw new IllegalArgumentException("Not Enough space to store this Matrix!");
        }
        final DoubleBuffer db = (DoubleBuffer)buf;
        db.put(this.m00);
        db.put(this.m01);
        db.put(this.m02);
        db.put(this.m10);
        db.put(this.m11);
        db.put(this.m12);
        db.put(this.m20);
        db.put(this.m21);
        db.put(this.m22);
    }
    
    @Override
    public DoubleBuffer store() {
        final DoubleBuffer db = BufferUtils.createDoubleBuffer(9);
        db.put(this.m00);
        db.put(this.m01);
        db.put(this.m02);
        db.put(this.m10);
        db.put(this.m11);
        db.put(this.m12);
        db.put(this.m20);
        db.put(this.m21);
        db.put(this.m22);
        db.flip();
        return db;
    }
    
    public dmat3 add(final dmat3 right) {
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
    
    public dmat3 subtract(final dmat3 right) {
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
    
    public dmat3 mul(final dmat3 right) {
        final double tm00 = this.m00 * right.m00 + this.m10 * right.m01 + this.m20 * right.m02;
        final double tm2 = this.m01 * right.m00 + this.m11 * right.m01 + this.m21 * right.m02;
        final double tm3 = this.m02 * right.m00 + this.m12 * right.m01 + this.m22 * right.m02;
        final double tm4 = this.m00 * right.m10 + this.m10 * right.m11 + this.m20 * right.m12;
        final double tm5 = this.m01 * right.m10 + this.m11 * right.m11 + this.m21 * right.m12;
        final double tm6 = this.m02 * right.m10 + this.m12 * right.m11 + this.m22 * right.m12;
        final double tm7 = this.m00 * right.m20 + this.m10 * right.m21 + this.m20 * right.m22;
        final double tm8 = this.m01 * right.m20 + this.m11 * right.m21 + this.m21 * right.m22;
        final double tm9 = this.m02 * right.m20 + this.m12 * right.m21 + this.m22 * right.m22;
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
    
    public dvec3 transform(final dvec3 v) {
        final double x = this.m00 * v.x + this.m10 * v.y + this.m20 * v.z;
        final double y = this.m01 * v.x + this.m11 * v.y + this.m21 * v.z;
        final double z = this.m02 * v.x + this.m12 * v.y + this.m22 * v.z;
        v.x = x;
        v.y = y;
        v.z = z;
        return v;
    }
    
    @Override
    public Matrix transpose() {
        final double tm00 = this.m00;
        final double tm2 = this.m10;
        final double tm3 = this.m20;
        final double tm4 = this.m01;
        final double tm5 = this.m11;
        final double tm6 = this.m21;
        final double tm7 = this.m02;
        final double tm8 = this.m12;
        final double tm9 = this.m22;
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
            final double determinant_inv = 1.0 / this.determinant();
            final double tm00 = this.m11 * this.m22 - this.m12 * this.m21;
            final double tm2 = -this.m10 * this.m22 + this.m12 * this.m20;
            final double tm3 = this.m10 * this.m21 - this.m11 * this.m20;
            final double tm4 = -this.m01 * this.m22 + this.m02 * this.m21;
            final double tm5 = this.m00 * this.m22 - this.m02 * this.m20;
            final double tm6 = -this.m00 * this.m21 + this.m01 * this.m20;
            final double tm7 = this.m01 * this.m12 - this.m02 * this.m11;
            final double tm8 = -this.m00 * this.m12 + this.m02 * this.m10;
            final double tm9 = this.m00 * this.m11 - this.m01 * this.m10;
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
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        return this;
    }
    
    @Override
    public Matrix setZero() {
        this.m00 = 0.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m10 = 0.0;
        this.m11 = 0.0;
        this.m12 = 0.0;
        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 0.0;
        return this;
    }
    
    @Override
    public double determinant() {
        final double d = this.m00 * (this.m11 * this.m22 - this.m12 * this.m21) + this.m01 * (this.m12 * this.m20 - this.m10 * this.m22) + this.m02 * (this.m10 * this.m21 - this.m11 * this.m20);
        return d;
    }
}
