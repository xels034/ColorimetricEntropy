// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import org.lwjgl.BufferUtils;
import java.nio.DoubleBuffer;
import java.nio.Buffer;
import java.io.Serializable;

public class dmat2 extends Matrix implements Serializable
{
    private static final long serialVersionUID = 1L;
    public double m00;
    public double m01;
    public double m10;
    public double m11;
    
    private dmat2(final boolean a) {
        this.SIZE = 2;
        this.PREC = 2;
    }
    
    public dmat2() {
        this(true);
        this.setIdentity();
    }
    
    public dmat2(final dmat2 src) {
        this(true);
        this.load(src);
    }
    
    public mat2 asFloat() {
        final mat2 m = new mat2();
        m.m00 = (float)this.m00;
        m.m01 = (float)this.m01;
        m.m10 = (float)this.m10;
        m.m11 = (float)this.m11;
        return m;
    }
    
    public dmat2 load(final dmat2 src) {
        return load(src, this);
    }
    
    public static dmat2 load(final dmat2 src, dmat2 dest) {
        if (dest == null) {
            dest = new dmat2();
        }
        dest.m00 = src.m00;
        dest.m01 = src.m01;
        dest.m10 = src.m10;
        dest.m11 = src.m11;
        return dest;
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
        this.m00 = db.get();
        this.m01 = db.get();
        this.m10 = db.get();
        this.m11 = db.get();
        db.flip();
    }
    
    @Override
    public void store(final Buffer buf) {
        if (!(buf instanceof DoubleBuffer)) {
            throw new IllegalArgumentException("Provide a siutable DoubleBuffer!");
        }
        if (buf.position() + 4 > buf.capacity()) {
            throw new IllegalArgumentException("Not Enough space to store this Matrix!");
        }
        final DoubleBuffer db = (DoubleBuffer)buf;
        db.put(this.m00);
        db.put(this.m01);
        db.put(this.m10);
        db.put(this.m11);
    }
    
    @Override
    public DoubleBuffer store() {
        final DoubleBuffer db = BufferUtils.createDoubleBuffer(4);
        db.put(this.m00);
        db.put(this.m01);
        db.put(this.m10);
        db.put(this.m11);
        db.flip();
        return db;
    }
    
    public dmat2 add(final dmat2 right) {
        this.m00 += right.m00;
        this.m01 += right.m01;
        this.m10 += right.m10;
        this.m11 += right.m11;
        return this;
    }
    
    public dmat2 subtract(final dmat2 right) {
        this.m00 -= right.m00;
        this.m01 -= right.m01;
        this.m10 -= right.m10;
        this.m11 -= right.m11;
        return this;
    }
    
    public dmat2 mul(final dmat2 right) {
        final double tm00 = this.m00 * right.m00 + this.m10 * right.m01;
        final double tm2 = this.m01 * right.m00 + this.m11 * right.m01;
        final double tm3 = this.m00 * right.m10 + this.m10 * right.m11;
        final double tm4 = this.m01 * right.m10 + this.m11 * right.m11;
        this.m00 = tm00;
        this.m01 = tm2;
        this.m10 = tm3;
        this.m11 = tm4;
        return this;
    }
    
    public dvec2 transform(final dvec2 v) {
        final double x = this.m00 * v.x + this.m10 * v.y;
        final double y = this.m01 * v.x + this.m11 * v.y;
        v.x = x;
        v.y = y;
        return v;
    }
    
    @Override
    public Matrix transpose() {
        final double tm01 = this.m10;
        final double tm2 = this.m01;
        this.m01 = tm01;
        this.m10 = tm2;
        return this;
    }
    
    @Override
    public Matrix invert() {
        final double determinant_inv = 1.0 / this.determinant();
        final double tm00 = this.m11 * determinant_inv;
        final double tm2 = -this.m01 * determinant_inv;
        final double tm3 = this.m00 * determinant_inv;
        final double tm4 = -this.m10 * determinant_inv;
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
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m10 = 0.0;
        this.m11 = 1.0;
        return this;
    }
    
    @Override
    public Matrix setZero() {
        this.m00 = 0.0;
        this.m01 = 0.0;
        this.m10 = 0.0;
        this.m11 = 0.0;
        return this;
    }
    
    @Override
    public double determinant() {
        return this.m00 * this.m11 - this.m01 * this.m10;
    }
}
