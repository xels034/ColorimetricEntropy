// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import java.nio.Buffer;
import java.io.Serializable;

public abstract class Matrix implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected int SIZE;
    protected int PREC;
    
    protected Matrix() {
    }
    
    public final int getSize() {
        return this.SIZE;
    }
    
    public final int getPrecision() {
        return this.PREC;
    }
    
    public abstract Matrix setIdentity();
    
    public abstract Matrix invert();
    
    public abstract void load(final Buffer p0);
    
    public abstract Matrix negate();
    
    public abstract void store(final Buffer p0);
    
    public abstract Buffer store();
    
    public abstract Matrix transpose();
    
    public abstract Matrix setZero();
    
    public abstract double determinant();
}
