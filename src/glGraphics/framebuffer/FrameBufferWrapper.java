// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics.framebuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import glGraphics.OpenGLException;
import util.SimpleLogger;
import glGraphics.texture.Texture2D;
import org.lwjgl.opengl.GL30;
import util.Ref;
import glGraphics.texture.Texture;
import util.vector.ivec2;

public class FrameBufferWrapper
{
    protected int fboID;
    protected int rboID;
    public ivec2 dim;
    protected boolean ready;
    protected Texture tex;
    
    protected FrameBufferWrapper() {
        this.rboID = -1;
        this.ready = false;
        this.dim = new ivec2(Ref.res);
    }
    
    public FrameBufferWrapper(final ivec2 d) {
        this.rboID = -1;
        this.ready = false;
        this.dim = new ivec2(d);
        this.generateFBO();
    }
    
    protected void generateFBO() {
        
        this.tex = new Texture2D(34842, this.dim, true);
        GL30.glGenerateMipmap(3553);
        GL30.glBindFramebuffer(36160, this.fboID = GL30.glGenFramebuffers());
        GL30.glFramebufferTexture2D(36160, 36064, 3553, this.tex.getID(), 0);
        GL30.glBindRenderbuffer(36161, this.rboID = GL30.glGenRenderbuffers());
        int s = GL30.glCheckFramebufferStatus(36160);
        if (s == 36053) {
            SimpleLogger.log("Framebuffer complete", 10, FrameBufferWrapper.class, "generateFBO");
            this.ready = true;
            return;
        }
        throw new OpenGLException("Framebuffer incomplete: " + s + " " + this.tex.getSize());
    }
    
    public void bind() {
        this.bind(true);
    }
    
    public void bind(final boolean clear) {
        if (!this.ready) {
            throw new IllegalStateException("Framebuffer creation was unsuccessfull! Can't bind");
        }
        GL13.glActiveTexture(33994);
        GL11.glBindTexture(3553, this.tex.getID());
        GL30.glBindFramebuffer(36160, this.fboID);
        GL11.glViewport(0, 0, this.dim.x, this.dim.y);
        if (clear) {
            GL11.glClear(17664);
        }
    }
    
    public void unbind() {
        GL13.glActiveTexture(33994);
        GL11.glBindTexture(3553, this.tex.getID());
        GL30.glGenerateMipmap(3553);
    }
    
    public Texture getWriteTexture() {
        return this.tex;
    }
    
    public void releaseFBO() {
        GL11.glDeleteTextures(this.tex.getID());
        GL30.glDeleteFramebuffers(this.fboID);
    }
}
