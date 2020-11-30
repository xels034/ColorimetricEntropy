// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics.framebuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import java.util.Map;
import glGraphics.ShaderWrapper;
import glGraphics.glGraphics;
import glGraphics.texture.Texture;
import util.vector.ivec2;
import util.Ref;
import glGraphics.construct.Construct;
import java.util.HashMap;

public class FrameBufferManager
{
    private HashMap<String, FrameBufferWrapper> fbos;
    private FrameBufferWrapper activeFBO;
    private Construct saq;
    
    public FrameBufferManager() {
        (this.fbos = new HashMap<>()).put("Input", new FrameBufferWrapperMS(Ref.res));
        this.fbos.put("Output", new ScreenBufferWrapper());
        (this.saq = new Construct("data/models/sa_quad.obj", 4)).bake();
    }
    
    public Texture addBuffer(final String name, final ivec2 dim) {
        if (name.equals("Input") || name.equals("Output")) {
            throw new IllegalArgumentException("Names \"Input\" and \"Output\" are reserved.");
        }
        this.fbos.put(name, new FrameBufferWrapper(new ivec2(dim)));
        return this.fbos.get(name).getWriteTexture();
    }
    
    public FrameBufferWrapper removeAndGetBuffer(final String name) {
        if (name.equals("Input") || name.equals("output")) {
            throw new IllegalArgumentException("Cannot remove Input or Output Framebuffer");
        }
        return this.fbos.remove(name);
    }
    
    public void insertBuffer(final String name, final FrameBufferWrapper fbw) {
        if (name.equals("Input") || name.equals("Output")) {
            throw new IllegalArgumentException("Names \"Input\" and \"Output\" are reserved.");
        }
        this.fbos.put(name, fbw);
    }
    
    public Texture getTexture(final String n) {
        return this.fbos.get(n).getWriteTexture();
    }
    
    private Texture setNextFBO(final String n) {
        this.activeFBO = this.fbos.get(n);
        return this.activeFBO.getWriteTexture();
    }
    
    public void executeEffect() {
        this.activeFBO.bind();
        this.saq.draw(0L);
        this.activeFBO.unbind();
    }
    
    public void executeEffect(final glGraphics glx) {
        this.executeEffect(glx, "Input");
    }
    
    public void executeEffect(final glGraphics glx, final String n) {
        this.executeEffect(glx, n, true);
    }
    
    public void executeEffect(final glGraphics glx, final String n, final boolean clear) {
        this.setNextFBO(n);
        this.activeFBO.bind(clear);
        glx.execute();
        this.activeFBO.unbind();
    }
    
    public Texture executeEffect(final String into, final ShaderWrapper sw, final Map<String, Object> attributes, final Texture[] textures) {
        this.setNextFBO(into);
        for (final Map.Entry<String, Object> e : attributes.entrySet()) {
            sw.storeUniform(e.getKey(), e.getValue());
        }
        for (int i = 0; i < textures.length; ++i) {
            final Texture t = textures[i];
            GL13.glActiveTexture(33984 + i);
            GL11.glBindTexture(t.getType(), t.getID());
        }
        this.executeEffect();
        return this.activeFBO.tex;
    }
    
    public Texture bind(final String n) {
        this.setNextFBO(n);
        this.activeFBO.bind();
        return this.activeFBO.tex;
    }
    
    public void unbind() {
        this.activeFBO.unbind();
    }
    
    public int getFrameBufferCount() {
        return this.fbos.size();
    }
    
    public void releaseAll() {
        this.saq.releaseVAO();
        for (final FrameBufferWrapper fbw : this.fbos.values()) {
            fbw.releaseFBO();
        }
        this.fbos.clear();
    }
}
