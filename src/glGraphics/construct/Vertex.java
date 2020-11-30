// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics.construct;

import java.util.Iterator;
import java.util.HashMap;
import java.util.LinkedList;
import util.vector.Vector;

public class Vertex implements Iterable<Vector>
{
    private LinkedList<String> keys;
    private HashMap<String, Vector> fields;
    private int byteSize;
    
    public Vertex() {
        this.keys = new LinkedList<>();
        this.fields = new HashMap<>();
    }
    
    public void addField(final String n, final Vector v) {
        this.keys.add(n);
        this.fields.put(n, v);
        this.byteSize += Vector.getBytePerPrecision(v.getPrecision()) * v.getSize();
    }
    
    @Override
    public Iterator<Vector> iterator() {
        return new VertexIterator();
    }
    
    public int getAttributeCount() {
        return this.keys.size();
    }
    
    public int getByteSize() {
        return this.byteSize;
    }
    
    public LinkedList<String> getAttributes() {
        return (LinkedList<String>)this.keys.clone();
    }
    
    public Vector getVectorByName(final String n) {
        return this.fields.get(n);
    }
    
    public Vector getVectorByIndex(final int i) {
        return this.fields.get(this.keys.get(i));
    }
    
    private class VertexIterator implements Iterator<Vector>
    {
        private int pos;
        
        public VertexIterator() {
            this.pos = 0;
        }
        
        @Override
        public boolean hasNext() {
            return this.pos < Vertex.this.keys.size();
        }
        
        @Override
        public Vector next() {
            final String vn = Vertex.this.keys.get(this.pos);
            final Vector v = Vertex.this.fields.get(vn);
            ++this.pos;
            return v;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("lol nope");
        }
    }
}
