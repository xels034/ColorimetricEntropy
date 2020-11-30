// 
// Decompiled by Procyon v0.5.36
// 

package util;

public class SimpleLogger
{
    public static boolean log;
    public static int level;
    
    static {
        SimpleLogger.log = true;
        SimpleLogger.level = -1;
    }
    
    public static void log(final Object msg, final int lvl, final Class<?> c, final String methodName) {
        if (SimpleLogger.log) {
            if (lvl < 0) {
                System.err.println("[" + lvl + "][" + c.getName() + ":" + methodName + "]: " + msg);
            }
            else if (lvl <= SimpleLogger.level) {
                System.out.println("[" + lvl + "][" + c.getName() + ":" + methodName + "]: " + msg);
            }
        }
    }
}
