// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics.texture;

import java.io.IOException;
import util.Ref;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL11;
import util.vector.ivec2;
import extern.PNGDecoder;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class TextureCM extends Texture
{
    public TextureCM(final int f, final String fn) {
        super(f, 34067, true);
        try {
            final ByteBuffer[] faces = new ByteBuffer[6];
            for (int i = 0; i < 6; ++i) {
                final FileInputStream fis = new FileInputStream(String.valueOf(fn) + i + ".png");
                final PNGDecoder peng = new PNGDecoder(fis);
                this.dimension = new ivec2(peng.getWidth(), peng.getHeight());
                peng.decode(faces[i] = ByteBuffer.allocateDirect(4 * peng.getWidth() * peng.getHeight()), peng.getWidth() * 4, PNGDecoder.Format.RGBA);
                faces[i].flip();
                fis.close();
            }
            this.texID = GL11.glGenTextures();
            GL13.glActiveTexture(33984);
            GL11.glBindTexture(34067, this.texID);
            GL11.glPixelStorei(3317, 4);
            for (int i = 0; i < 6; ++i) {
                GL11.glTexImage2D(34069 + i, 0, this.format, this.dimension.x, this.dimension.y, 0, 6408, 5121, faces[i]);
            }
            GL30.glGenerateMipmap(34067);
            GL11.glTexParameteri(34067, 10241, 9987);
            GL11.glTexParameteri(34067, 10240, 9729);
            GL11.glTexParameteri(34067, 34046, Ref.aniso);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
