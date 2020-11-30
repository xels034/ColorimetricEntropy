// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics;

import util.SimpleLogger;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL11;
import util.vector.dvec4;
import glGraphics.texture.Texture;
import util.vector.dvec2;
import util.Ref;
import util.vector.dvec3;
import java.util.LinkedList;
import java.util.ArrayList;
import glGraphics.construct.Construct;
import java.util.UUID;
import java.util.HashMap;
import util.vector.dmat4;

public class glGraphics
{
    public static final int M_WORLD = 0;
    public static final int M_SCREEN = 1;
    private dmat4 viewMat;
    private dmat4 projMat;
    private dmat4 screenMat;
    private HashMap<UUID, Construct> assets;
    private HashMap<Integer, Boolean> depthMask;
    private ArrayList<UUID> gpuLoaded;
    private LinkedList<UUID> toRelease;
    private LinkedList<ConstructEntry> workOrders;
    private LinkedList<TextEntry> textOrders;
    private TrueTypeFontWrapper font;
    private boolean wait;
    float perc;
    
    public glGraphics() {
        this.wait = false;
        this.assets = new HashMap<>();
        this.depthMask = new HashMap<>();
        this.gpuLoaded = new ArrayList<>();
        this.toRelease = new LinkedList<>();
        this.workOrders = new LinkedList<>();
        this.textOrders = new LinkedList<>();
        this.font = new TrueTypeFontWrapper("data/font/trench100free.otf", 24.0, new char[] { '\u00f6', '\u00d6', '\u00e4', '\u00c4', '\u00fc', '\u00dc', '\u00df' });
        (this.viewMat = new dmat4()).translate(new dvec3(0.0, 0.0, -5.0));
        this.viewMat.rotate(-1.1780972450961724, new dvec3(1.0, 0.0, 0.0));
        this.viewMat.rotate(45.0, new dvec3(0.0, 0.0, 1.0));
        this.setupProjMat();
        this.setupScreenMat();
    }
    
    public void setupProjMat() {
        this.projMat = new dmat4();
        final double fov = 90.0;
        final double aspectRatio = Ref.res.x / (double)Ref.res.y;
        final double near_plane = 0.1;
        final double far_plane = 100.0;
        final double asd = 180.0 / (fov / 2.0 * 2.0 * 3.141592653589793);
        final double y_scale = 1.0 / Math.tan(asd);
        final double x_scale = y_scale / aspectRatio;
        final double frustum_length = far_plane - near_plane;
        this.projMat.m00 = x_scale;
        this.projMat.m11 = y_scale;
        this.projMat.m22 = -((far_plane + near_plane) / frustum_length);
        this.projMat.m23 = -1.0;
        this.projMat.m32 = -(2.0 * near_plane * far_plane / frustum_length);
        this.projMat.m33 = 0.0;
    }
    
    public void setupScreenMat() {
        (this.screenMat = new dmat4()).translate(new dvec2(-1.0, 1.0));
        this.screenMat.scale(new dvec3(1.0f / (Ref.res.x / 2), -1.0f / (Ref.res.y / 2), 1.0));
    }
    
    public UUID registerConstruct(final Construct c) {
        final UUID u = UUID.randomUUID();
        this.assets.put(u, c);
        return u;
    }
    
    public Construct getConstruct(final UUID u) {
        final Construct c = this.assets.get(u);
        if (c == null) {
            throw new OpenGLException("no such construct registered");
        }
        return this.assets.get(u);
    }
    
    public void releaseConstruct(final UUID u) {
        this.toRelease.add(u);
    }
    
    private void gpuUpload(final UUID u) {
        this.assets.get(u).bake();
        this.gpuLoaded.add(u);
    }
    
    public void drawConstruct(final UUID idx, final ShaderWrapper sw, final Texture[] t) {
        this.drawConstruct(idx, sw, t, 0);
    }
    
    public void drawConstruct(final UUID idx, final ShaderWrapper sw, final Texture[] t, final int mode) {
        if (idx == null) {
            throw new IllegalArgumentException("Error: idx is null!");
        }
        if (!this.assets.containsKey(idx)) {
            throw new IllegalArgumentException("No construct with idx=" + idx + " registered");
        }
        if (!this.gpuLoaded.contains(idx)) {
            this.gpuUpload(idx);
        }
        final dmat4 m = new dmat4();
        final Construct c = this.assets.get(idx);
        m.translate(c.position);
        m.mul(new dmat4().rotate(c.rotation.x, new dvec3(1.0, 0.0, 0.0)));
        m.mul(new dmat4().rotate(c.rotation.y, new dvec3(0.0, 1.0, 0.0)));
        m.mul(new dmat4().rotate(c.rotation.z, new dvec3(0.0, 0.0, 1.0)));
        m.scale(c.scale);
        this.workOrders.add(new ConstructEntry(idx, m, sw, t, mode));
    }
    
    public void drawText(final double x, final double y, final String t, final dvec4 c) {
        this.drawText(x, y, 1.0, t, c);
    }
    
    public void drawText(final double x, final double y, final double s, final String t, final dvec4 color) {
        this.textOrders.add(new TextEntry(new dvec3(x, y, s), color, t));
    }
    
    public void changeMask(final boolean enabled) {
        this.depthMask.put(this.workOrders.size(), enabled);
    }
    
    public void execute() {
        dmat4 mvp = new dmat4();
        final long now = System.currentTimeMillis();
        int ord = 0;
        for (final ConstructEntry ce : this.workOrders) {
            if (this.depthMask.containsKey(ord)) {
                GL11.glDepthMask(this.depthMask.get(ord));
            }
            for (int i = 0; i < ce.tex.length; ++i) {
                GL13.glActiveTexture(33984 + i);
                GL11.glBindTexture(ce.texT[i], ce.tex[i]);
            }
            if (ce.matMode == 0) {
                final dmat4 m = new dmat4(ce.model);
                final dmat4 v = new dmat4(this.viewMat);
                final dmat4 p = new dmat4(this.projMat);
                mvp = p.mul(v).mul(m);
            }
            else if (ce.matMode == 1) {
                final dmat4 m = new dmat4(ce.model);
                final dmat4 v = new dmat4(this.screenMat);
                //final dmat4 p = new dmat4();
                mvp = v.mul(m);
            }
            ce.s.storeUniform("MVP", mvp);
            ce.s.storeUniform("M", ce.model);
            ce.s.storeUniform("V", this.viewMat);
            ce.s.storeUniform("P", this.projMat);
            this.assets.get(ce.pointer).draw(now);
            final int err = GL11.glGetError();
            if (err != 0) {
                SimpleLogger.log(err, -1, this.getClass(), "execute()");
            }
            ++ord;
        }
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        for (final TextEntry te : this.textOrders) {
            this.font.drawText(te.position.x, te.position.y, te.scale, te.text, te.color);
        }
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        this.cleanUp(now);
    }
    
    private void cleanUp(final long now) {
        this.workOrders.clear();
        this.textOrders.clear();
        this.depthMask.clear();
        final LinkedList<UUID> olds = new LinkedList<>();
        for (final UUID u : this.gpuLoaded) {
            final long gap = now - this.assets.get(u).getLastRendered();
            if (gap > 10000L) {
                olds.add(u);
            }
        }
        for (final UUID u : olds) {
            this.assets.get(u).releaseVAO();
            this.gpuLoaded.remove(u);
        }
        for (final UUID u : this.toRelease) {
            this.assets.get(u).releaseVAO();
            this.gpuLoaded.remove(u);
            this.assets.remove(u);
        }
        this.toRelease.clear();
    }
    
    public void rotate(final double d) {
        this.viewMat.rotate(d * 0.003, new dvec3(0.0, 0.0, 1.0));
    }
    
    public int getTextWidth(final String t) {
        return this.font.getTextWidth(t);
    }
    
    public int getTextHeight() {
        return this.font.getTextHeight();
    }
    
    public void deconstructAll() {
        this.gpuLoaded.clear();
        for (final Construct c : this.assets.values()) {
            c.releaseVAO();
        }
        this.assets.clear();
        this.font.release();
    }
    
    public boolean isLocked() {
        return this.wait;
    }
    
    public synchronized void takeLock() {
        this.wait = true;
    }
    
    public synchronized void releaseLock() {
        this.wait = false;
        this.notifyAll();
    }
    
    public synchronized void waitForRelease() {
        if (this.wait) {
            try {
                this.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static class ConstructEntry
    {
        private UUID pointer;
        private dmat4 model;
        private ShaderWrapper s;
        private int[] tex;
        private int[] texT;
        private int matMode;
        
        public ConstructEntry(final UUID uuid, final dmat4 m, final ShaderWrapper sw, final Texture[] t, final int mode) {
            this.pointer = uuid;
            this.model = new dmat4(m);
            this.s = sw;
            this.matMode = mode;
            this.tex = new int[t.length];
            this.texT = new int[t.length];
            for (int i = 0; i < t.length; ++i) {
                this.tex[i] = t[i].getID();
                this.texT[i] = t[i].getType();
            }
        }
    }
    
    private class TextEntry
    {
        private dvec2 position;
        private double scale;
        private dvec4 color;
        private String text;
        
        public TextEntry(final dvec3 p, final dvec4 c, final String t) {
            this.position = new dvec2(p.x, p.y);
            this.scale = p.z;
            this.color = new dvec4(c);
            this.text = t;
        }
    }
}
