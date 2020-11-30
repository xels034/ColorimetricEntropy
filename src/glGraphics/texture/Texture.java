// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics.texture;

import org.lwjgl.opengl.GL11;
import util.vector.ivec2;

public abstract class Texture
{
    protected int type;
    protected int format;
    protected int texID;
    protected int edgeBehaviour;
    protected ivec2 dimension;
    
    protected Texture(final int f, final int t, final boolean clamp) {
        this.type = t;
        this.format = f;
        this.edgeBehaviour = (clamp ? 33071 : 10497);
    }
    
    public int getID() {
        return this.texID;
    }
    
    public ivec2 getSize() {
        return new ivec2(this.dimension.x, this.dimension.y);
    }
    
    public int getType() {
        return this.type;
    }
    
    public boolean clamps() {
        return this.edgeBehaviour == 33071;
    }
    
    @Override
    public void finalize() {
        GL11.glDeleteTextures(this.texID);
    }
}
