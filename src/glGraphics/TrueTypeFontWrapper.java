// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics;

import util.vector.ivec2;
import util.vector.dvec2;
import util.Ref;
import util.vector.dvec3;
import util.vector.dmat4;
import util.vector.vec4;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.awt.FontFormatException;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Font;
import java.io.File;
import java.awt.image.BufferedImage;
import glGraphics.construct.Construct;
import glGraphics.texture.Texture2D;
import util.vector.dvec4;
import java.util.HashMap;

public class TrueTypeFontWrapper
{
    private HashMap<Character, dvec4> charMap;
    private double size;
    private Texture2D fontTex;
    private ShaderWrapper fontShader;
    private Construct saq;
    
    public TrueTypeFontWrapper(final String fn, final double s) {
        this(fn, s, new char[0]);
    }
    
    public TrueTypeFontWrapper(final String fn, final double s, final char[] ac) {
        this.size = s;
        this.fontTex = new Texture2D(35906, this.setupBI(fn, ac), true);
        this.setupQuad();
        this.setupShader();
    }
    
    private BufferedImage setupBI(final String fn, final char[] ac) {
        try {
            final int SPACING = (int)(this.size * 0.35);
            this.charMap = new HashMap<>();
            Font awtFont = Font.createFont(0, new File(fn));
            awtFont = awtFont.deriveFont(1, (float)this.size);
            final double cpd = Math.sqrt(256 + ac.length);
            BufferedImage bi = new BufferedImage(1, 1, 2);
            Graphics2D g2 = bi.createGraphics();
            g2.setFont(awtFont);
            final FontMetrics fm = g2.getFontMetrics(awtFont);
            final int charH = fm.getHeight();
            final int texDim = (int)(cpd * (charH + SPACING));
            bi = new BufferedImage(texDim, texDim, 3);
            g2 = bi.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.black);
            g2.fillRect(0, 0, texDim, texDim);
            int w = 0;
            int h = charH;
            g2.setColor(Color.white);
            g2.setFont(awtFont);
            for (int i = 0; i < 256 + ac.length; ++i) {
                char c;
                if (i < 256) {
                    c = (char)i;
                }
                else {
                    c = ac[i - 256];
                }
                final int charW = fm.charWidth(c);
                if (w + charW > texDim) {
                    w = 0;
                    h += charH + SPACING;
                }
                g2.drawString(new StringBuilder(String.valueOf(c)).toString(), w, h);
                this.charMap.put(c, new dvec4(w, h - charH, charW, charH));
                w += charW + SPACING;
            }
            return bi;
        }
        catch (FontFormatException | IOException ex2) {
            ex2.printStackTrace();
            return null;
        }
    }
    
    public void drawText(final double x, final double y, final String text, final dvec4 color) {
        this.drawText(x, y, 1.0, text, color);
    }
    
    public void drawText(final double x, final double y, final double scale, final String text, final dvec4 color) {
        GL20.glUseProgram(this.fontShader.getShaderID());
        GL13.glActiveTexture(33984);
        GL11.glBindTexture(3553, this.fontTex.getID());
        final ivec2 ts = this.fontTex.getSize();
        long tis = System.currentTimeMillis();
        tis &= 0xFFFFFFFL;
        final float f = tis;
        this.fontShader.storeUniform("time", f);
        this.fontShader.storeUniform("color", new vec4(color));
        final dmat4 screenProj = new dmat4();
        screenProj.translate(new dvec3(-1.0, 1.0, 0.0));
        screenProj.scale(new dvec3(2.0 / Ref.res.x, -2.0 / Ref.res.y, 1.0));
        double xShift = 0.0;
        char[] charArray;
        for (int length = (charArray = text.toCharArray()).length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            final dvec4 metrics = this.charMap.get(c);
            final dmat4 pos = new dmat4(screenProj);
            pos.translate(new dvec3(x + xShift, y, 0.0));
            pos.scale(new dvec3(scale));
            pos.scale(new dvec3(metrics.z, metrics.w, 1.0));
            final dmat4 uv = new dmat4();
            final double tx = metrics.x / ts.x;
            final double ty = metrics.y / ts.y;
            final double tw = metrics.z / ts.x;
            final double th = metrics.w / ts.y;
            uv.translate(new dvec2(tx, ty));
            uv.scale(new dvec3(tw, th, 1.0));
            this.fontShader.storeUniform("MVP", pos);
            this.fontShader.storeUniform("FONT", uv);
            this.saq.draw(0L);
            xShift += metrics.z * scale;
        }
    }
    
    public int getTextWidth(final String t) {
        int w = 0;
        char[] charArray;
        for (int length = (charArray = t.toCharArray()).length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            w += (int)this.charMap.get(c).z;
        }
        return w;
    }
    
    public int getTextHeight() {
        return (int)this.charMap.values().iterator().next().w;
    }
    
    private void setupQuad() {
        (this.saq = new Construct("data/models/font_quad.obj", 4)).bake();
    }
    
    private void setupShader() {
        String fsh = null;
        final String vsh = "data/shader/font/font.vsh";
        final String gsh = "data/shader/font/font.gsh";
        fsh = "data/shader/font/font.fsh";
        this.fontShader = new ShaderWrapper(vsh, gsh, fsh);
        final dmat4 m = new dmat4();
        this.fontShader.storeUniform("MVP", m);
        this.fontShader.storeUniform("FONT", m);
        this.fontShader.storeUniform("color", new vec4(1.0f, 1.0f, 1.0f, 1.0f));
    }
    
    public void release() {
        GL11.glDeleteTextures(this.fontTex.getID());
        this.saq.releaseVAO();
    }
}
