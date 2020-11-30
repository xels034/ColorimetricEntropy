// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics.construct;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import util.vector.Vector;
import util.NotImplementedMethodException;
import java.util.LinkedList;

public class DynamicConstruct extends Construct
{
    public DynamicConstruct(final LinkedList<Vertex> ll, final int geo, final boolean c) {
        super(35048, geo, c);
        this.assembleData(ll);
    }
    
    public DynamicConstruct(final LinkedList<Vertex> ll, final int geo) {
        this(ll, geo, true);
    }
    
    public DynamicConstruct(final String fn, final int geo) {
        super(35048, geo, true);
        if (this.geoMode == 4) {
            this.readData(fn);
            return;
        }
        throw new NotImplementedMethodException("lol nope");
    }
    
    public void replaceAttribute(final int loc, final LinkedList<Vector> attrib) {
        if (attrib == null || attrib.isEmpty()) {
            throw new IllegalArgumentException("Provide a filled LinkedList!");
        }
        final Vector v1 = attrib.getFirst();
        final ByteBuffer replacer = BufferUtils.createByteBuffer(v1.getByteSize() * attrib.size());
        for (final Vector v2 : attrib) {
            v2.insertStore(replacer);
        }
        replacer.flip();
        this.replaceAttribute(loc, replacer);
    }
    
    public void replaceAttribute(final int loc, final ByteBuffer attrib) {
        GL30.glBindVertexArray(this.vaPointer);
        GL15.glBindBuffer(34962, this.vbPointer);
        long offset = 0L;
        for (int i = 0; i < loc; ++i) {
            offset += this.word.getVectorByIndex(i).getByteSize() * this.ebLength;
        }
        System.out.println(offset);
        GL15.glBufferSubData(34962, offset, attrib);
    }
}
