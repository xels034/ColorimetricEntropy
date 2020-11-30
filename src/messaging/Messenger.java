// 
// Decompiled by Procyon v0.5.36
// 

package messaging;

import util.SimpleLogger;
import java.util.Collection;
import java.util.LinkedList;
import java.util.HashMap;

public class Messenger
{
    private static HashMap<Message.M_TYPE, LinkedList<Handler>> handleMap;
    private static LinkedList<Message> mQueue;
    
    static {
        Messenger.handleMap = new HashMap<>();
        Messenger.mQueue = new LinkedList<>();
    }
    
    public static void subscribe(final Handler h, final Message.M_TYPE msgT) {
        if (!Messenger.handleMap.containsKey(msgT)) {
            Messenger.handleMap.put(msgT, new LinkedList<Handler>());
        }
        Messenger.handleMap.get(msgT).add(h);
    }
    
    public static void subscribe(final Handler h, final Collection<Message.M_TYPE> msgT) {
        for (final Message.M_TYPE i : msgT) {
            subscribe(h, i);
        }
    }
    
    //FIXME msgT will never be a valid key
/*     public static void unsubscribe(final Handler h, final int msgT) {
        if (Messenger.handleMap.containsKey(msgT)) {
            Messenger.handleMap.get(msgT).remove(h);
        }
    } */
    
    public static void unsubscribe(final Handler h) {
        SimpleLogger.log("Handler " + h + " unsubscribed completly", 0, Messenger.class, "unsubscribe");
        for (final Message.M_TYPE msgT : Messenger.handleMap.keySet()) {
            Messenger.handleMap.get(msgT).remove(h);
        }
    }
    
    public static void fire(final Message m) {
        if (Messenger.handleMap.containsKey(m.getMsgType())) {
            final LinkedList<Handler> localCopy = (LinkedList<Handler>)Messenger.handleMap.get(m.getMsgType()).clone();
            for (final Handler h : localCopy) {
                h.handleMessage(m);
            }
        }
        else {
            SimpleLogger.log("Note: Message of type " + m.getMsgType() + " is requested by no-one!", 1, Messenger.class, "fire");
        }
    }
    
    public static void send(final Message m) {
        Messenger.mQueue.add(m);
    }
    
    public static void update() {
        final LinkedList<Message> workList = (LinkedList<Message>)Messenger.mQueue.clone();
        Messenger.mQueue.clear();
        for (final Message m : workList) {
            fire(m);
        }
    }
}
