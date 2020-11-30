// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics.texture;

import org.lwjgl.BufferUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import extern.PNGDecoder;
import java.io.FileInputStream;
import util.Ref;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL11;
import util.vector.ivec2;
import java.nio.ByteBuffer;

public class Texture2D extends Texture
{
    private Texture2D(final int f, final boolean c) {
        super(f, 3553, c);
    }
    
    private void setup(final ByteBuffer bb, final int w, final int h) {
        this.dimension = new ivec2(w, h);
        this.texID = GL11.glGenTextures();
        GL13.glActiveTexture(33984);
        GL11.glBindTexture(3553, this.texID);
        GL11.glPixelStorei(3317, 4);
        GL11.glTexImage2D(3553, 0, this.format, w, h, 0, 6408, 5121, bb);
        GL30.glGenerateMipmap(3553);
        GL11.glTexParameteri(3553, 10242, this.edgeBehaviour);
        GL11.glTexParameteri(3553, 10243, this.edgeBehaviour);
        GL11.glTexParameteri(3553, 10241, 9987);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 34046, Ref.aniso);
    }
    
    public Texture2D(final int f, final ivec2 dim, final boolean c) {
        this(f, c);
        this.setup(null, dim.x, dim.y);
    }
    
    public Texture2D(final int f, final String fn, final boolean c) {
        this(f, c);
        try {
            final FileInputStream fis = new FileInputStream(fn);
            final PNGDecoder peng = new PNGDecoder(fis);
            this.dimension = new ivec2(peng.getWidth(), peng.getHeight());
            final ByteBuffer beebee = ByteBuffer.allocateDirect(4 * peng.getWidth() * peng.getHeight());
            peng.decode(beebee, peng.getWidth() * 4, PNGDecoder.Format.RGBA);
            beebee.flip();
            fis.close();
            this.setup(beebee, this.dimension.x, this.dimension.y);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Texture2D(final int f, final BufferedImage bi, final boolean c) {
        this(f, c);
        final int capacity = bi.getWidth() * bi.getHeight() * 4;
        final ByteBuffer bb = BufferUtils.createByteBuffer(capacity);
        for (int y = 0; y < bi.getHeight(); ++y) {
            for (int x = 0; x < bi.getWidth(); ++x) {
                bb.putInt(bi.getRGB(x, y));
            }
        }
        bb.flip();
        this.setup(bb, bi.getWidth(), bi.getHeight());
    }
}
