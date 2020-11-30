// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics.construct;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import util.vector.dvec2;
import util.vector.Vector;
import java.nio.IntBuffer;
import util.SimpleLogger;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL41;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import java.util.LinkedList;
import util.NotImplementedMethodException;
import util.vector.dvec3;
import java.nio.ByteBuffer;

public class Construct
{
    protected ByteBuffer data;
    protected Vertex word;
    protected int[] elems;
    protected int storageMode;
    protected int geoMode;
    protected boolean connected;
    public dvec3 scale;
    public dvec3 rotation;
    public dvec3 position;
    private long lastRendered;
    protected int vaPointer;
    protected int vbPointer;
    protected int ebPointer;
    protected int ebLength;
    
    protected Construct(final int mode, final int geo, final boolean c) {
        this.lastRendered = System.currentTimeMillis();
        this.vaPointer = -10;
        this.vbPointer = -10;
        this.ebPointer = -10;
        this.ebLength = -10;
        this.scale = new dvec3(1.0, 1.0, 1.0);
        this.rotation = new dvec3(0.0, 0.0, 0.0);
        this.position = new dvec3(0.0, 0.0, 0.0);
        this.storageMode = mode;
        this.geoMode = geo;
        this.connected = c;
    }
    
    public Construct(final String fn, final int geo) {
        this(35044, geo, true);
        if (this.geoMode == 4) {
            this.readData(fn);
            return;
        }
        throw new NotImplementedMethodException("lol nope");
    }
    
    public Construct(final LinkedList<Vertex> ll, final int geo, final boolean c) {
        this(35044, geo, c);
        this.assembleData(ll);
    }
    
    public Construct(final LinkedList<Vertex> ll, final int geo) {
        this(ll, geo, true);
    }
    
    public void draw(final long now) {
        if (this.vaPointer == -10) {
            throw new IllegalStateException("No valid VertexArrayObject assigned.");
        }
        GL30.glBindVertexArray(this.vaPointer);
        GL11.glDrawElements(this.geoMode, this.ebLength, 5125, 0L);
        this.lastRendered = System.currentTimeMillis();
    }
    
    public void bake() {
        GL30.glBindVertexArray(this.vaPointer = GL30.glGenVertexArrays());
        GL15.glBindBuffer(34962, this.vbPointer = GL15.glGenBuffers());
        GL15.glBufferData(34962, this.data, this.storageMode);
        GL15.glBindBuffer(34963, this.ebPointer = GL15.glGenBuffers());
        this.ebLength = this.elems.length;
        final IntBuffer buffi = BufferUtils.createIntBuffer(this.elems.length);
        buffi.put(this.elems);
        buffi.flip();
        GL15.glBufferData(34963, buffi, this.storageMode);
        int byteStride = 0;
        int attrib = 0;
        for (final String n : this.word.getAttributes()) {
            final Vector v = this.word.getVectorByName(n);
            if (v.getPrecision() != 2) {
                throw new UnsupportedOperationException("only GL_DOUBLE allowed at the moment");
            }
            if (this.storageMode == 35044 || this.storageMode == 35040) {
                GL41.glVertexAttribLPointer(attrib, v.getSize(), 5130, this.word.getByteSize(), byteStride);
            }
            else {
                GL41.glVertexAttribLPointer(attrib, v.getSize(), 5130, 0, byteStride * this.ebLength);
            }
            GL20.glEnableVertexAttribArray(attrib);
            byteStride += v.getByteSize();
            ++attrib;
        }
        final int err = GL11.glGetError();
        if (err != 0) {
            SimpleLogger.log("error code " + err, -1, this.getClass(), "bakeFixed");
        }
    }
    
    public long getLastRendered() {
        return this.lastRendered;
    }
    
    public void releaseVAO() {
        SimpleLogger.log("deleting construct", 10, Construct.class, "releaseVBO");
        GL15.glDeleteBuffers(this.vbPointer);
        GL15.glDeleteBuffers(this.ebPointer);
        GL30.glDeleteVertexArrays(this.vaPointer);
    }
    
    @Override
    public void finalize() {
        this.releaseVAO();
    }
    
    public void readData(final String fn) {
        try {
            (this.word = new Vertex()).addField("vPos", new dvec3());
            this.word.addField("vNor", new dvec3());
            this.word.addField("vUV", new dvec2());
            final BufferedReader br = new BufferedReader(new FileReader(fn));
            String line = br.readLine();
            final LinkedList<dvec3> verts = new LinkedList<>();
            final LinkedList<dvec3> norms = new LinkedList<>();
            final LinkedList<dvec2> texts = new LinkedList<>();
            final LinkedList<Vertex> vertexList = new LinkedList<>();
            while (line != null) {
                final String[] lineElems = line.split(" ");
                if (lineElems[0].equals("v")) {
                    final double v1 = Double.parseDouble(lineElems[1]);
                    final double v2 = Double.parseDouble(lineElems[2]);
                    final double v3 = Double.parseDouble(lineElems[3]);
                    verts.add(new dvec3(v1, v2, v3));
                }
                else if (lineElems[0].equals("vn")) {
                    final double v1 = Double.parseDouble(lineElems[1]);
                    final double v2 = Double.parseDouble(lineElems[2]);
                    final double v3 = Double.parseDouble(lineElems[3]);
                    norms.add(new dvec3(v1, v2, v3));
                }
                else if (lineElems[0].equals("vt")) {
                    final double v1 = Double.parseDouble(lineElems[1]);
                    final double v2 = Double.parseDouble(lineElems[2]);
                    texts.add(new dvec2(v1, v2));
                }
                else if (lineElems[0].equals("f")) {
                    for (int i = 1; i < 4; ++i) {
                        final String[] faceLine = lineElems[i].split("/");
                        final int i2 = Integer.parseInt(faceLine[0]);
                        final int i3 = Integer.parseInt(faceLine[2]);
                        final int i4 = Integer.parseInt(faceLine[1]);
                        final Vector p = new dvec3(verts.get(i2 - 1).x, verts.get(i2 - 1).y, verts.get(i2 - 1).z);
                        final Vector n = new dvec3(norms.get(i3 - 1).x, norms.get(i3 - 1).y, norms.get(i3 - 1).z);
                        final Vector t = new dvec2(texts.get(i4 - 1).x, texts.get(i4 - 1).y);
                        final Vertex v4 = new Vertex();
                        v4.addField("vPos", p);
                        v4.addField("vNor", n);
                        v4.addField("vUV", t);
                        vertexList.add(v4);
                    }
                }
                line = br.readLine();
            }
            br.close();
            this.assembleData(vertexList);
            vertexList.clear();
        }
        catch (IOException x) {
            x.printStackTrace();
        }
    }
    
    public void assembleData(final LinkedList<Vertex> ll) {
        if (ll == null || ll.isEmpty()) {
            throw new IllegalArgumentException("Deliver a filled LinkedList! Size calculation depends on this");
        }
        this.word = ll.getFirst();
        final int dataLength = this.word.getByteSize() * ll.size();
        this.data = BufferUtils.createByteBuffer(dataLength);
        if (this.storageMode == 35044 || this.storageMode == 35040) {
            for (final Vertex vx : ll) {
                for (final Vector v : vx) {
                    v.insertStore(this.data);
                }
            }
        }
        else {
            for (int attCnt = this.word.getAttributeCount(), i = 0; i < attCnt; ++i) {
                for (final Vertex vx2 : ll) {
                    final Vector v2 = vx2.getVectorByIndex(i);
                    v2.insertStore(this.data);
                }
            }
        }
        this.data.flip();
        if (this.geoMode == 4 || !this.connected) {
            this.elems = new int[ll.size()];
            for (int j = 0; j < this.elems.length; ++j) {
                this.elems[j] = j;
            }
        }
        else {
            final int size = (ll.size() - 1) * 2;
            int eleIdx = 0;
            this.elems = new int[size];
            for (int k = 0; k < ll.size() - 1; ++k) {
                this.elems[eleIdx] = k;
                this.elems[eleIdx + 1] = k + 1;
                eleIdx += 2;
            }
        }
    }
}
