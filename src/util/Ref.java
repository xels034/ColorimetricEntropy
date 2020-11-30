// 
// Decompiled by Procyon v0.5.36
// 

package util;

import util.vector.ivec2;

public class Ref
{
    public static final int LOG_LEVEL = -1;
    public static final long renderPause = 10000L;
    public static final int MAX_FBO = 8;
    public static ivec2 res;
    public static int downRes;
    public static int gq;
    public static int msaa;
    public static int aniso;
    public static final int maxFPS = 120;
    
    static {
        Ref.downRes = 4;
        Ref.gq = 16;
        Ref.msaa = 4;
        Ref.aniso = 16;
    }
    
    public static void setup() {
        Ref.res = new ivec2(1240, 720);
    }
}
