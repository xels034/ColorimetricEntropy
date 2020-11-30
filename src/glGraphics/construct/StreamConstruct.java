// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics.construct;

import util.NotImplementedMethodException;
import java.util.LinkedList;

public class StreamConstruct extends Construct
{
    public StreamConstruct(final LinkedList<Vertex> ll, final int geo, final boolean c) {
        super(35040, geo, c);
        this.assembleData(ll);
    }
    
    public StreamConstruct(final LinkedList<Vertex> ll, final int geo) {
        this(ll, geo, true);
    }
    
    public StreamConstruct(final String fn, final int geo) {
        super(35040, geo, true);
        if (this.geoMode == 4) {
            this.readData(fn);
            return;
        }
        throw new NotImplementedMethodException("lol nope");
    }
}
