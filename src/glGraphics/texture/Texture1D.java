// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics.texture;

import java.io.IOException;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL11;
import java.nio.ByteBuffer;
import util.vector.ivec2;
import extern.PNGDecoder;
import java.io.FileInputStream;

public class Texture1D extends Texture
{
    public Texture1D(final int f, final String fn, final boolean clamp) {
        super(f, 3552, clamp);
        try {
            final FileInputStream fis = new FileInputStream(fn);
            final PNGDecoder peng = new PNGDecoder(fis);
            this.dimension = new ivec2(peng.getWidth(), peng.getHeight());
            final ByteBuffer beebee = ByteBuffer.allocateDirect(4 * peng.getWidth());
            peng.decode(beebee, peng.getWidth() * 4, PNGDecoder.Format.RGBA);
            beebee.flip();
            fis.close();
            this.texID = GL11.glGenTextures();
            GL13.glActiveTexture(33984);
            GL11.glBindTexture(3552, this.texID);
            GL11.glPixelStorei(3317, 4);
            GL11.glTexImage1D(3552, 0, this.format, peng.getWidth(), 0, 6408, 5121, beebee);
            GL30.glGenerateMipmap(3552);
            GL11.glTexParameteri(3552, 10242, 33071);
            GL11.glTexParameteri(3552, 10241, 9987);
            GL11.glTexParameteri(3552, 10240, 9729);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
