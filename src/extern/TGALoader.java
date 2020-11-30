// 
// Decompiled by Procyon v0.5.36
// 

package extern;

import org.lwjgl.BufferUtils;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.InputStream;

@SuppressWarnings("all")
public class TGALoader
{
    private static int texWidth;
    private static int texHeight;
    private static int width;
    private static int height;
    private static short pixelDepth;
    
    private TGALoader() {
    }
    
    private static short flipEndian(final short signedShort) {
        final int input = signedShort & 0xFFFF;
        return (short)(input << 8 | (input & 0xFF00) >>> 8);
    }
    
    public static int getLastDepth() {
        return TGALoader.pixelDepth;
    }
    
    public static int getLastWidth() {
        return TGALoader.width;
    }
    
    public static int getLastHeight() {
        return TGALoader.height;
    }
    
    public static int getLastTexWidth() {
        return TGALoader.texWidth;
    }
    
    public static int getLastTexHeight() {
        return TGALoader.texHeight;
    }
    
    public static ByteBuffer loadImage(final InputStream fis) throws IOException {
        return loadImage(fis, true);
    }
    
    public static ByteBuffer loadImage(final InputStream fis, final boolean flipped) throws IOException {
        byte red = 0;
        byte green = 0;
        byte blue = 0;
        byte alpha = 0;
        final BufferedInputStream bis = new BufferedInputStream(fis, 100000);
        final DataInputStream dis = new DataInputStream(bis);
        final short idLength = (short)dis.read();
        final short colorMapType = (short)dis.read();
        final short imageType = (short)dis.read();
        final short cMapStart = flipEndian(dis.readShort());
        final short cMapLength = flipEndian(dis.readShort());
        final short cMapDepth = (short)dis.read();
        final short xOffset = flipEndian(dis.readShort());
        final short yOffset = flipEndian(dis.readShort());
        TGALoader.width = flipEndian(dis.readShort());
        TGALoader.height = flipEndian(dis.readShort());
        TGALoader.pixelDepth = (short)dis.read();
        TGALoader.texWidth = get2Fold(TGALoader.width);
        TGALoader.texHeight = get2Fold(TGALoader.height);
        final short imageDescriptor = (short)dis.read();
        if (idLength > 0) {
            bis.skip(idLength);
        }
        byte[] rawData = null;
        if (TGALoader.pixelDepth == 32) {
            rawData = new byte[TGALoader.texWidth * TGALoader.texHeight * 4];
        }
        else {
            rawData = new byte[TGALoader.texWidth * TGALoader.texHeight * 3];
        }
        if (TGALoader.pixelDepth == 24) {
            for (int i = TGALoader.height - 1; i >= 0; --i) {
                for (int j = 0; j < TGALoader.width; ++j) {
                    blue = dis.readByte();
                    green = dis.readByte();
                    red = dis.readByte();
                    final int ofs = (j + i * TGALoader.texWidth) * 3;
                    rawData[ofs] = red;
                    rawData[ofs + 1] = green;
                    rawData[ofs + 2] = blue;
                }
            }
        }
        else if (TGALoader.pixelDepth == 32) {
            if (flipped) {
                for (int i = TGALoader.height - 1; i >= 0; --i) {
                    for (int j = 0; j < TGALoader.width; ++j) {
                        blue = dis.readByte();
                        green = dis.readByte();
                        red = dis.readByte();
                        alpha = dis.readByte();
                        final int ofs = (j + i * TGALoader.texWidth) * 4;
                        rawData[ofs] = red;
                        rawData[ofs + 1] = green;
                        rawData[ofs + 2] = blue;
                        if ((rawData[ofs + 3] = alpha) == 0) {
                            rawData[ofs + 2] = 0;
                            rawData[ofs] = (rawData[ofs + 1] = 0);
                        }
                    }
                }
            }
            else {
                for (int i = 0; i < TGALoader.height; ++i) {
                    for (int j = 0; j < TGALoader.width; ++j) {
                        blue = dis.readByte();
                        green = dis.readByte();
                        red = dis.readByte();
                        alpha = dis.readByte();
                        final int ofs = (j + i * TGALoader.texWidth) * 4;
                        rawData[ofs + 2] = red;
                        rawData[ofs + 1] = green;
                        rawData[ofs] = blue;
                        if ((rawData[ofs + 3] = alpha) == 0) {
                            rawData[ofs + 2] = 0;
                            rawData[ofs] = (rawData[ofs + 1] = 0);
                        }
                    }
                }
            }
        }
        fis.close();
        final ByteBuffer scratch = BufferUtils.createByteBuffer(rawData.length);
        scratch.put(rawData);
        scratch.flip();
        return scratch;
    }
    
    private static int get2Fold(final int fold) {
        int ret;
        for (ret = 2; ret < fold; ret *= 2) {}
        return ret;
    }
}
