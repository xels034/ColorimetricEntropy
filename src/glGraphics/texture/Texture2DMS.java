// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics.texture;

import org.lwjgl.opengl.GL32;
import util.Ref;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL11;
import util.vector.ivec2;

public class Texture2DMS extends Texture
{
    public Texture2DMS(final int f, final int xr, final int yr) {
        super(f, 37120, true);
        this.dimension = new ivec2(xr, yr);
        this.texID = GL11.glGenTextures();
        GL13.glActiveTexture(33984);
        GL11.glBindTexture(37120, this.texID);
        GL32.glTexImage2DMultisample(37120, Ref.msaa, this.format, xr, yr, true);
    }
}
