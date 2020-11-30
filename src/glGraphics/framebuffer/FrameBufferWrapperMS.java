// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics.framebuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import glGraphics.OpenGLException;
import util.SimpleLogger;
import util.Ref;
import glGraphics.texture.Texture2DMS;
import org.lwjgl.opengl.GL30;
import util.vector.ivec2;

public class FrameBufferWrapperMS extends FrameBufferWrapper
{
    public FrameBufferWrapperMS(final ivec2 d) {
        super(d);
    }
    
    @Override
    protected void generateFBO() {
        GL30.glBindFramebuffer(36160, this.fboID = GL30.glGenFramebuffers());
        this.tex = new Texture2DMS(34842, this.dim.x, this.dim.y);
        GL30.glFramebufferTexture2D(36160, 36064, 37120, this.tex.getID(), 0);
        GL30.glBindRenderbuffer(36161, this.rboID = GL30.glGenRenderbuffers());
        GL30.glRenderbufferStorageMultisample(36161, Ref.msaa, 36012, this.dim.x, this.dim.y);
        GL30.glFramebufferRenderbuffer(36160, 36096, 36161, this.rboID);
        if (GL30.glCheckFramebufferStatus(36160) == 36053) {
            SimpleLogger.log("Framebuffer complete", 10, FrameBufferWrapperMS.class, "generateFBO");
            this.ready = true;
            return;
        }
        throw new OpenGLException("Framebuffer incomplete");
    }
    
    @Override
    public void bind(final boolean clear) {
        if (!this.ready) {
            throw new IllegalStateException("Framebuffer creation was unsuccessfull! Can't bind");
        }
        GL13.glActiveTexture(33994);
        GL11.glBindTexture(37120, this.tex.getID());
        GL30.glBindFramebuffer(36160, this.fboID);
        GL11.glViewport(0, 0, this.dim.x, this.dim.y);
        if (clear) {
            GL11.glClear(17664);
        }
    }
    
    @Override
    public void unbind() {
    }
}
