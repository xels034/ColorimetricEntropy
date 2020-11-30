// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.ByteBuffer;
import java.nio.Buffer;
import java.io.Serializable;

public abstract class Vector implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected int SIZE;
    protected int PREC;
    
    protected Vector() {
    }
    
    public final double length() {
        return (float)Math.sqrt(this.lengthSquared());
    }
    
    public abstract double lengthSquared();
    
    public final Vector normalise() {
        final double len = this.length();
        if (len != 0.0) {
            final double l = 1.0 / len;
            this.scale(l);
            return this;
        }
        throw new IllegalStateException("Zero length vector");
    }
    
    public final int getSize() {
        return this.SIZE;
    }
    
    public final int getPrecision() {
        return this.PREC;
    }
    
    public final int getByteSize() {
        return getBytePerPrecision(this.PREC) * this.SIZE;
    }
    
    public static final int getBytePerPrecision(final int p) {
        switch (p) {
            case 0: {
                return 4;
            }
            case 1: {
                return 4;
            }
            case 2: {
                return 8;
            }
            default: {
                return -1;
            }
        }
    }
    
    public abstract void load(final Buffer p0);
    
    public abstract void insertStore(final ByteBuffer p0);
    
    public abstract Buffer store();
    
    public abstract Vector copy();
    
    public abstract Vector negate();
    
    public abstract Vector scale(final double p0);
    
    public abstract Vector add(final Vector p0);
    
    public abstract Vector subtract(final Vector p0);
}
