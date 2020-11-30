// 
// Decompiled by Procyon v0.5.36
// 

package messaging;

public class Message
{
    private M_TYPE msgType;
    private Object params;
    
    public Message(final M_TYPE m) {
        this.msgType = m;
    }
    
    public Message(final M_TYPE m, final Object o) {
        this.msgType = m;
        this.params = o;
    }
    
    public M_TYPE getMsgType() {
        return this.msgType;
    }
    
    public Object getParam() {
        return this.params;
    }
    
    public void setParams(final Object o) {
        this.params = o;
    }
    
    public Message copy() {
        return new Message(this.msgType, this.params);
    }
    
    public enum M_TYPE
    {
        CHANGE_STATE("CHANGE_STATE", 0), 
        RAW_KB("RAW_KB", 1), 
        RAW_MS("RAW_MS", 2), 
        CONTROL_CMD("CONTROL_CMD", 3);
        
        private M_TYPE(final String name, final int ordinal) {
        }
    }
    
    public static class RW_KB_Param
    {
        public int key;
        public boolean pressed;
        
        public RW_KB_Param(final int k, final boolean p) {
            this.key = k;
            this.pressed = p;
        }
    }
    
    public static class RW_MS_Param
    {
        public int button;
        public boolean pressed;
        public double mx;
        public double my;
        public double dx;
        public double dy;
        public double dw;
        
        public RW_MS_Param(final int b, final boolean p, final double mx, final double my, final double dx, final double dy, final int w) {
            this.button = b;
            this.pressed = p;
            this.mx = mx;
            this.my = my;
            this.dx = dx;
            this.dy = dy;
            this.dw = w;
        }
    }
}
