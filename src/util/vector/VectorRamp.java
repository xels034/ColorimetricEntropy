// 
// Decompiled by Procyon v0.5.36
// 

package util.vector;

import glGraphics.construct.Construct;
import glGraphics.OpenGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import glGraphics.construct.StreamConstruct;
import glGraphics.construct.Vertex;
import java.util.LinkedList;
import glGraphics.ShaderWrapper;
import java.util.TreeMap;

public class VectorRamp<T extends Vector>
{
    private TreeMap<Double, T> handles;
    private boolean ready;
    
    public VectorRamp() {
        this.handles = new TreeMap<>();
        this.ready = false;
    }
    
    public void setCaps(final T start, final T end) {
        this.handles.put(0.0, start);
        this.handles.put(1.0, end);
        this.ready = true;
    }
    
    public void addHandle(final Double key, final T value) {
        this.handles.put(key, value);
    }
    
    public T getValue(final Double key) {
        if (!this.ready) {
            throw new IllegalStateException("call setCaps() first!");
        }
        final Double dk = this.handles.floorKey(key);
        final Double tk = this.handles.ceilingKey(key);
        if (dk == null) {
            return this.handles.get(tk);
        }
        if (tk == null) {
            return this.handles.get(dk);
        }
        final double span = tk - dk;
        final double dist = key - dk;
        if (span == 0.0) {
            return this.handles.get(tk);
        }
        final double fac = dist / span;
        final Vector tv = this.handles.get(tk).copy();
        final Vector dv = this.handles.get(dk).copy();
        dv.scale(fac);
        tv.scale(1.0 - fac);
        return (T)dv.add(tv);
    }
    
    public Double[] getHandles() {
        return this.handles.keySet().toArray(new Double[0]);
    }
    
    public void render() {
        final String vsh = "data/shader/ramp/ramp.vsh";
        final String gsh = "data/shader/ramp/ramp.gsh";
        final String fsh = "data/shader/ramp/ramp.fsh";
        final ShaderWrapper passThrough = new ShaderWrapper(vsh, gsh, fsh);
        final LinkedList<Vertex> ll = new LinkedList<>();
        final double start = this.handles.firstKey();
        final double end = this.handles.lastKey();
        if (start != end) {
            final double shift = -start - 0.5;
            final double mult = 2.0 / (end - start);
            for (final double x : this.handles.keySet()) {
                ll.add(this.makeVertex((x + shift) * mult, x));
            }
        }
        else {
            ll.add(this.makeVertex(-1.0, 0.0));
            ll.add(this.makeVertex(1.0, 0.0));
        }
        final Construct c = new StreamConstruct(ll, 1, true);
        c.bake();
        GL20.glUseProgram(passThrough.getShaderID());
        final int err = GL11.glGetError();
        if (err != 0) {
            throw new OpenGLException(new StringBuilder().append(err).toString());
        }
        c.draw(0L);
        c.releaseVAO();
        passThrough.releaseShader();
    }
    
    private Vertex makeVertex(final double x, final double key) {
        final Vertex v = new Vertex();
        v.addField("vPos", new dvec3((float)x, 0.0, 0.0));
        v.addField("vCol", this.handles.get(key));
        return v;
    }
}
