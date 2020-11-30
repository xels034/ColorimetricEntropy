// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics.framebuffer;

import util.SimpleLogger;
import glGraphics.texture.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class ScreenBufferWrapper extends FrameBufferWrapper
{
    public ScreenBufferWrapper() {
        this.ready = true;
    }
    
    @Override
    public void bind(final boolean clear) {
        if (!this.ready) {
            throw new IllegalStateException("Screenbuffer creation was unsuccessfull! Can't bind");
        }
        GL30.glBindFramebuffer(36160, 0);
        GL11.glViewport(0, 0, this.dim.x, this.dim.y);
        if (clear) {
            GL11.glClear(17664);
        }
    }
    
    @Override
    public void unbind() {
    }
    
    @Override
    public Texture getWriteTexture() {
        SimpleLogger.log("Warning: attempting to get texture ID of screenbuffer. Its always 0 and can't be read!", 0, this.getClass(), "getWriteTextureID");
        return null;
    }
    
    @Override
    public void releaseFBO() {
    }
}
