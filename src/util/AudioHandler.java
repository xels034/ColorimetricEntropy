// 
// Decompiled by Procyon v0.5.36
// 

package util;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;

public class AudioHandler
{
    public String sketchPath(final String fileName) {
        final File f = new File(fileName);
        return f.getAbsolutePath();
    }
    
    public InputStream createInput(final String fileName) {
        try {
            return new FileInputStream(new File(fileName));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
