// 
// Decompiled by Procyon v0.5.36
// 

package glGraphics;

import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWvidmode;
import glGraphics.framebuffer.FrameBufferWrapper;
import util.vector.vec2;
import util.vector.dvec4;
import util.vector.VectorRamp;
import util.vector.vec3;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.Callbacks;
import util.AudioHandler;
import util.vector.dvec2;
import glGraphics.construct.Vertex;
import java.util.LinkedList;
import util.vector.dvec3;
import glGraphics.construct.Construct;
import messaging.Message;
import java.util.Locale;
import util.vector.ivec2;
import messaging.Messenger;
import util.SimpleLogger;
import util.Ref;
import java.util.UUID;
import glGraphics.framebuffer.FrameBufferManager;
import org.lwjgl.system.Retainable;
import java.util.HashSet;
import glGraphics.texture.Texture;
import java.util.HashMap;
import util.SpectrumSampler;
import util.FrequencySampler;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import messaging.Handler;

public class AppWindow implements Handler
{
    private Minim m;
    private AudioPlayer ap;
    private FrequencySampler mainSampler;
    private FrequencySampler rotaSampler;
    private SpectrumSampler specSampler;
    private String nowPlaying;
    private long window;
    private HashMap<String, ShaderWrapper> shaders;
    private HashSet<Retainable> cbc;
    public static glGraphics glx;
    private FrameBufferManager fbm;
    private boolean ready;
    private boolean closeRequest;
    private boolean scaleFlag;
    private long stamp;
    private long frames;
    private float exposure;
    private float heat;
    private float heatLimit;
    private double rotSpeed;
    Texture texInput;
    Texture texOutput;
    Texture texFull1;
    Texture texFull2;
    Texture texHalf1;
    Texture texHalf2;
    Texture texTone1;
    UUID cid;
    HashMap<String, UUID> icons;
    UUID circleID;
    UUID lineID;
    private int fps;
    private double rotation;
    Texture rgbCurves;
    boolean pressing;
    boolean frameDrag;
    boolean scaleDrag;
    double dRefX;
    double dRefY;
    double sRefX;
    double sRefY;
    double oldx;
    double oldy;
    boolean pause;
    int breakT;
    
    public static void main(final String[] args) {
        Ref.setup();
        final AppWindow w = new AppWindow();
        if (w.ready) {
            w.run();
        }
        else {
            SimpleLogger.log("AppWindow Setup failed :( can't start application.", -2, AppWindow.class, "main");
        }
        Messenger.unsubscribe(w);
    }
    
    public AppWindow() {
        this.nowPlaying = "drag'n'drop a file";
        this.exposure = 3.0f;

        this.heat = 600.0f;
        this.heatLimit = 600.0f;

        this.rotSpeed = 1.0;
        this.ready = false;
        this.closeRequest = false;

            this.setupGL();
        

        if (this.ready) {
            this.setupJAVA();
            this.setupAudio();
        }
    }
    
    private void setupGL() {
        this.setupDisplay();
        this.setupShader();
        this.fbm = new FrameBufferManager();

        this.texFull1 = this.fbm.addBuffer("full1", new ivec2(Ref.res));
        this.texFull2 = this.fbm.addBuffer("full2", new ivec2(Ref.res));
        this.texHalf1 = this.fbm.addBuffer("half1", new ivec2(Ref.res).scale(1.0 / Ref.downRes));
        this.texHalf2 = this.fbm.addBuffer("half2", new ivec2(Ref.res).scale(1.0 / Ref.downRes));
        this.texTone1 = this.fbm.addBuffer("tonemap", new ivec2(256, 1));
        this.texInput = this.fbm.getTexture("Input");
        AppWindow.glx = new glGraphics();
        this.ready = true;
    }
    
    private void setupJAVA() {
        this.icons = new HashMap<>();
        Locale.setDefault(Locale.ENGLISH);
        Messenger.subscribe(this, Message.M_TYPE.RAW_KB);
        Messenger.subscribe(this, Message.M_TYPE.RAW_MS);
        Construct c = new Construct("data/models/icosphere.obj", 4);
        this.cid = AppWindow.glx.registerConstruct(c);
        final double x = 50000.0;
        c.scale = new dvec3(x, x, x);
        this.constructCircle();
        this.constructLine();
        this.constructIcons();
    }
    
    private void constructCircle() {
        final LinkedList<Vertex> ll = new LinkedList<>();
        double phase = 0.0;
        final double step = 0.0078125;
        for (int i = 0; i < 128; ++i) {
            final Vertex v = new Vertex();
            final dvec3 p = new dvec3();
            p.x = Math.cos(6.283185307179586 * phase);
            p.y = Math.sin(6.283185307179586 * phase);
            phase += step;
            v.addField("vPos", p);
            v.addField("vIdx", new dvec2(i, 0.0));
            ll.add(v);
        }
        final Construct c = new Construct(ll, 1, false);
        this.circleID = AppWindow.glx.registerConstruct(c);
    }
    
    private void constructLine() {
        final LinkedList<Vertex> ll = new LinkedList<>();
        for (int i = 0; i < 128; ++i) {
            final Vertex v = new Vertex();
            v.addField("vPos", new dvec3(5.0, i - 64, 0.0));
            v.addField("vIdx", new dvec2(i, 0.0));
            ll.add(v);
        }
        final Construct c = new Construct(ll, 1, false);
        this.lineID = AppWindow.glx.registerConstruct(c);
    }
    
    private void constructIcons() {
        Construct c = new Construct("data/models/ico_close.obj", 4);
        c.position = new dvec3(Ref.res.x - 3, 12.0, 0.0);
        c.scale = new dvec3(5.0);
        this.icons.put("close", AppWindow.glx.registerConstruct(c));
        c = new Construct("data/models/ico_max.obj", 4);
        c.position = new dvec3(Ref.res.x - 10, -3.0, 0.0);
        c.scale = new dvec3(5.0);
        this.icons.put("max", AppWindow.glx.registerConstruct(c));
        c = new Construct("data/models/ico_min.obj", 4);
        c.position = new dvec3(Ref.res.x - 65.5, 50.0, 0.0);
        c.scale = new dvec3(5.0);
        c.rotation = new dvec3(0.0, 0.0, 1.5707963267948966);
        this.icons.put("min", AppWindow.glx.registerConstruct(c));
        c = new Construct("data/models/ico_min.obj", 4);
        c.position = new dvec3(Ref.res.x - 65.5, Ref.res.y / 2, 0.0);
        c.scale = new dvec3(5.0);
        c.rotation = new dvec3(0.0, 0.0, 1.5707963267948966);
        this.icons.put("min2", AppWindow.glx.registerConstruct(c));
        c = new Construct("data/models/ico_drag.obj", 4);
        c.position = new dvec3(Ref.res.x - 27, Ref.res.y - 2.5, 0.0);
        c.scale = new dvec3(-5.0);
        this.icons.put("drag", AppWindow.glx.registerConstruct(c));
        c = new Construct("data/models/ico_min.obj", 4);
        c.position = new dvec3(Ref.res.x / 2 - 250, 500.0, 0.0);
        c.scale = new dvec3(-500.0, 50.0, 1.0);
        this.icons.put("pan", AppWindow.glx.registerConstruct(c));
    }
    
    private void replaceIcons() {
        Construct c = AppWindow.glx.getConstruct(this.icons.get("close"));
        c.position = new dvec3(Ref.res.x - 3, 12.0, 0.0);
        c.scale = new dvec3(5.0);
        c = AppWindow.glx.getConstruct(this.icons.get("max"));
        c.position = new dvec3(Ref.res.x - 10, -3.0, 0.0);
        c.scale = new dvec3(5.0);
        c = AppWindow.glx.getConstruct(this.icons.get("min"));
        c.position = new dvec3(Ref.res.x - 65.5, 50.0, 0.0);
        c.scale = new dvec3(5.0);
        c.rotation = new dvec3(0.0, 0.0, 1.5707963267948966);
        c = AppWindow.glx.getConstruct(this.icons.get("min2"));
        c.position.x = Ref.res.x - 65.5;
        c = AppWindow.glx.getConstruct(this.icons.get("drag"));
        c.position = new dvec3(Ref.res.x - 27, Ref.res.y - 2.5, 0.0);
        c.scale = new dvec3(-5.0);
        c = AppWindow.glx.getConstruct(this.icons.get("pan"));
        c.position = new dvec3(Ref.res.x / 2 - 250, 500.0, 0.0);
        c.scale = new dvec3(-500.0, 50.0, 1.0);
    }
    
    private void setupAudio() {
      if(this.m == null){
        (this.m = new Minim(new AudioHandler())).debugOff();

        //buffer size and sample rate found out by loading a random mp3 and printing the values
        //TODO create new samplers each time a file is loaded
        int bs   = 1024;
        float sr = 11025.0f;

        this.mainSampler = new FrequencySampler(bs, sr, 80.0f, 0.0f, 25);
        this.rotaSampler = new FrequencySampler(bs, sr, 2000.0f, 0.0f, 10);
        this.specSampler = new SpectrumSampler (bs, sr, 10L);
      } else{
        if (this.ap != null) this.ap.close();

        (this.ap = this.m.loadFile(this.nowPlaying)).loop();

        this.ap.addListener(this.mainSampler);
        this.ap.addListener(this.rotaSampler);
        this.ap.addListener(this.specSampler);
      }
    }
    
    private void setupDisplay() {
        System.setProperty("org.lwjgl.util.Debug", "false");
        System.setProperty("org.lwjgl.util.NoChecks", "true");
        this.cbc = new HashSet<>();
        Retainable r = Callbacks.errorCallbackPrint(System.err);
        this.cbc.add(r);
        GLFW.glfwSetErrorCallback((GLFWErrorCallback)r);
        if (GLFW.glfwInit() != 1) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(131075, 1);
        GLFW.glfwWindowHint(131077, 0);
        GLFW.glfwWindowHint(135169, 8);
        GLFW.glfwWindowHint(135170, 8);
        GLFW.glfwWindowHint(135171, 8);
        GLFW.glfwWindowHint(135172, 0);
        GLFW.glfwWindowHint(135173, 0);
        GLFW.glfwWindowHint(135174, 0);
        GLFW.glfwWindowHint(135181, 0);
        GLFW.glfwWindowHint(135183, 120);
        GLFW.glfwWindowHint(139265, 196609);
        GLFW.glfwWindowHint(139266, 4);
        GLFW.glfwWindowHint(139267, 4);
        GLFW.glfwWindowHint(139270, 1);
        this.window = GLFW.glfwCreateWindow(Ref.res.x, Ref.res.y, "3ntropy", 0L, 0L);
        if (this.window == 0L) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        r = new GLFWFramebufferSizeCallback() {
          @Override
          public void invoke(final long w, final int width, final int height) {
            AppWindow.access$0(AppWindow.this, true);
            Ref.res.x = width;
            Ref.res.y = height;
          }
        };
        this.cbc.add(r);
        GLFW.glfwSetFramebufferSizeCallback(this.window, (GLFWFramebufferSizeCallback)r);
        r = new GLFWKeyCallback() {
          @Override
          public void invoke(final long w, final int key, final int scancode, final int action, final int mods) {
            if (key == 256 && action == 0) {
              GLFW.glfwSetWindowShouldClose(w, 1);
            }
            final boolean b = action == 1;
            Messenger.send(new Message(Message.M_TYPE.RAW_KB, new Message.RW_KB_Param(key, b)));
          }
        };
        this.cbc.add(r);
        GLFW.glfwSetKeyCallback(this.window, (GLFWKeyCallback)r);
        r = new GLFWMouseButtonCallback() {
          @Override
          public void invoke(final long w, final int button, final int action, final int mods) {
            Messenger.send(new Message(Message.M_TYPE.RAW_MS,
                new Message.RW_MS_Param(button, action == 1, AppWindow.this.oldx, AppWindow.this.oldy, 0.0, 0.0, 0)));
          }
        };
        this.cbc.add(r);
        GLFW.glfwSetMouseButtonCallback(this.window, (GLFWMouseButtonCallback)r);
        r = new GLFWCursorPosCallback() {
          boolean Minit = false;

          @Override
          public void invoke(final long w, final double xpos, final double ypos) {
            if (!this.Minit) {
              AppWindow.this.oldx = xpos;
              AppWindow.this.oldy = ypos;
              this.Minit = true;
            }
            Messenger.send(new Message(Message.M_TYPE.RAW_MS, new Message.RW_MS_Param(-1, false, (int) xpos, (int) ypos,
                (int) (xpos - AppWindow.this.oldx), (int) (ypos - AppWindow.this.oldy), 0)));
            AppWindow.this.oldx = xpos;
            AppWindow.this.oldy = ypos;
          }
        };
        this.cbc.add(r);
        GLFW.glfwSetCursorPosCallback(this.window, (GLFWCursorPosCallback)r);
        r = new GLFWScrollCallback() {
          @Override
          public void invoke(final long w, final double xoffset, final double yoffset) {
            Messenger.send(new Message(Message.M_TYPE.RAW_MS,
                new Message.RW_MS_Param(-1, false, 0.0, 0.0, 0.0, 0.0, (int) yoffset)));
          }
        };
        this.cbc.add(r);
        GLFW.glfwSetScrollCallback(this.window, (GLFWScrollCallback)r);
        r = new GLFWDropCallback() {
          @Override
          public void invoke(final long w, final int count, final long names) {
            String[] s = Callbacks.dropCallbackNamesString(count, names);
            if (s.length == 1) {
              AppWindow.access$1(AppWindow.this, s[0]);
              AppWindow.this.setupAudio();
              s = s[0].split("\\\\");
              AppWindow.access$1(AppWindow.this, s[s.length - 1]);
            }
          }
        };
        this.cbc.add(r);
        GLFW.glfwSetDropCallback(this.window, (GLFWDropCallback)r);
        GLFW.glfwMakeContextCurrent(this.window);
        GLFW.glfwSwapInterval(1);
        GLContext.createFromCurrent();
        GL11.glEnable(32925);
        GL11.glEnable(3024);
        GL11.glEnable(36281);
        GL11.glEnable(34895);
        GL11.glEnable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL14.glBlendEquation(32774);
    }
    
    private void setupShader() {
        this.shaders = new HashMap<>();
        final String vsh = "data/shader/geo/standard.vsh";
        final String gsh = "data/shader/geo/standard.gsh";
        final String gsh2 = "data/shader/geo/sphere.gsh";
        final String fshg = "data/shader/geo/sphere.fsh";
        final String fshi = "data/shader/geo/plane.fsh";
        final String fshp = "data/shader/post/msaa_to_tex.fsh";
        final String fshVB = "data/shader/post/vectorBlur.fsh";
        final String fshCB = "data/shader/post/combine.fsh";

        final String fshTN = "data/shader/post/tone.fsh";
        final String cvsh = "data/shader/circle/circle.vsh";
        final String cgsh = "data/shader/circle/circle.gsh";
        final String cfsh = "data/shader/circle/circle.fsh";
        this.shaders.put("sphere", new ShaderWrapper(vsh, gsh2, fshg));
        this.shaders.put("plane", new ShaderWrapper(vsh, gsh, fshi));
        this.shaders.put("msaa_to_tex", new ShaderWrapper(vsh, gsh, fshp));
        this.shaders.put("vectorBlur", new ShaderWrapper(vsh, gsh, fshVB));
        this.shaders.put("combine", new ShaderWrapper(vsh, gsh, fshCB));
        this.shaders.put("tone", new ShaderWrapper(vsh, gsh, fshTN));

        this.shaders.put("circle", new ShaderWrapper(cvsh, cgsh, cfsh));
        this.setupUniforms();
    }
    
    private void setupUniforms() {
        final ShaderWrapper blanks = this.shaders.get("sphere");

        final ShaderWrapper blankmtt = this.shaders.get("msaa_to_tex");
        final ShaderWrapper vb = this.shaders.get("vectorBlur");


        final vec3 lightDir = new vec3(-1.0f, -1.0f, -1.0f);

        blanks.storeUniform("lightDir", lightDir);
        blanks.storeUniform("heat", this.heat);
        blankmtt.storeUniform("fboTex", 0);
        blankmtt.storeUniform("dim", new ivec2(Ref.res));
        blankmtt.storeUniform("exposure", this.exposure);
        vb.storeUniform("weights", this.gauss(32));
        vb.storeUniform("fboTex", 0);
        vb.storeUniform("quality", Ref.gq);

    }
    
    private Float[] gauss(final int l) {
        final Float[] g = new Float[l];
        final float s = 0.35f;
        for (int i = 0; i < l; ++i) {
            final float fac = i / (float)l;
            g[i] = (float)Math.exp(-(fac * fac) / (2.0f * s * s));
        }
        return g;
    }
    
    private void run() {
        this.preparedRender();
        while (GLFW.glfwWindowShouldClose(this.window) == 0 && !this.closeRequest) {
            this.update();
            this.render();
            GLFW.glfwSwapBuffers(this.window);
            GLFW.glfwPollEvents();
        }
        this.cleanUp();
        this.m.stop();
    }
    
    private void update() {
        this.updateFPSCounter();
        this.updateShapes();
        Messenger.update();
    }
    
    private void updateFPSCounter() {
        if (System.currentTimeMillis() - this.stamp > 1000L) {
            GLFW.glfwSetWindowTitle(this.window, "3ntropy  ||  FPS: " + this.frames);
            this.fps = (int)this.frames;
            this.frames = 0L;
            this.stamp = System.currentTimeMillis();
        }
        ++this.frames;
    }
    
    private void updateShapes() {
        final double[] lData = this.mainSampler.getAmplitude();
        final double[] hData = this.rotaSampler.getAmplitude();
        final double[] bData = this.specSampler.getBeats();
        final Float[] sData = this.specSampler.getSpectrumPow();
        final Construct cc = AppWindow.glx.getConstruct(this.cid);
        final Construct cr = AppWindow.glx.getConstruct(this.circleID);
        final Construct lc = AppWindow.glx.getConstruct(this.lineID);
        if (lData != null) {
            float powerAdjuster = 1.0f + this.mainSampler.getFreq().x / 600.0f;
            powerAdjuster = (float)Math.pow(powerAdjuster, 1.25);
            this.heat = 300.0f + (float)lData[0] * 12.0f * powerAdjuster;
            cc.scale = new dvec3(1.0 + lData[0] * 0.01);
            cr.scale = new dvec3(1.4);
        }
        this.rotation += this.rotSpeed;
        if (hData != null) {
            AppWindow.glx.getConstruct(this.icons.get("min2")).rotation.z = hData[0] * 0.04 + 1.5707963267948966;
            cc.rotation = new dvec3(hData[0] * 0.1, 0.0, 0.0);
            cc.position = (dvec3)new dvec3(0.0, 0.0, bData[2] * 2.0 - 0.3).add(new dvec3(0.0, 0.0, hData[0] * 0.05));
            cr.position = (dvec3)new dvec3(0.0, 0.0, bData[2] * 2.0 - 0.3).add(new dvec3(0.0, 0.0, hData[0] * 0.05));
            cr.rotation = new dvec3(1.5707963267948966 - hData[0] * 0.03, this.rotation / 450.0, 0.0);
            lc.position = new dvec3(980 + Ref.res.x, Ref.res.y / 2 + hData[0] * 13.0, 0.0);
            lc.rotation = new dvec3(0.0, 0.0, hData[0] * 0.01);
        }
        cc.rotation.add(new dvec3(0.0, 0.0, this.rotation / 65.0));
        this.shaders.get("circle").storeUniform("freq", sData);
        final double ratio = Ref.res.y / 720.0;
        lc.scale = new dvec3(-200.0, -3.0 * ratio, 1.0);
    }
    
    private void preparedRender() {
        final VectorRamp<dvec4> vr = new VectorRamp<>();
        vr.setCaps(new dvec4(1.0, 1.0, 1.0, 1.0), new dvec4(1.0, 1.0, 1.0, 0.98));
        vr.addHandle(0.25, new dvec4(0.9, 0.9, 1.0, 1.0));
        vr.addHandle(0.75, new dvec4(1.0, 1.0, 1.0, 1.0));
        this.rgbCurves = this.fbm.bind("tonemap");
        vr.render();
        this.fbm.unbind();
    }
    
    private void render() {
        this.renderGLX();
        this.renderBlur1(1.0f, 0.75f, 0.05f);
        this.renderBlur2(0.5f, 0.25f, 0.4f);
        this.renderTone();
        this.updateHeat();
        final int err = GL11.glGetError();
        if (err != 0) {
            throw new OpenGLException("error code " + err);
        }
    }
    
    private void renderGLX() {
        AppWindow.glx.changeMask(false);
        AppWindow.glx.changeMask(true);
        AppWindow.glx.drawConstruct(this.cid, this.shaders.get("sphere"), new Texture[] {});
        AppWindow.glx.drawConstruct(this.circleID, this.shaders.get("circle"), new Texture[0]);
        this.fbm.executeEffect(AppWindow.glx, "Input");
        this.renderUI();
        final HashMap<String, Object> at = new HashMap<>();
        at.put("fboTex", 0);
        at.put("exposure", this.exposure);
        this.fbm.executeEffect("full1", this.shaders.get("msaa_to_tex"), at, new Texture[] { this.texInput });
    }
    
    private void renderBlur1(final float str, final float thresh, final float rad) {
        final HashMap<String, Object> at = new HashMap<>();
        at.put("fboTex", 0);
        at.put("strength", str);
        at.put("threshold", thresh);
        at.put("dir", new vec2(rad, 0.0f));
        this.fbm.executeEffect("half1", this.shaders.get("vectorBlur"), at, new Texture[] { this.texFull1 });
        at.clear();
        at.put("strength", 1.0f);
        at.put("threshold", 0.0f);
        at.put("dir", new vec2(0.0f, rad));
        this.fbm.executeEffect("half2", this.shaders.get("vectorBlur"), at, new Texture[] { this.texHalf1 });
        at.clear();
        at.put("original", 0);
        at.put("glare", 1);
        this.fbm.executeEffect("full2", this.shaders.get("combine"), at, new Texture[] { this.texFull1, this.texHalf2 });
    }
    
    private void renderBlur2(final float str, final float thresh, final float rad) {
        final HashMap<String, Object> at = new HashMap<>();
        at.put("fboTex", 0);
        at.put("strength", str);
        at.put("threshold", thresh);
        at.put("dir", new vec2(rad, 0.0f));
        this.fbm.executeEffect("half1", this.shaders.get("vectorBlur"), at, new Texture[] { this.texFull2 });
        at.clear();
        at.put("strength", 1.0f);
        at.put("threshold", 0.0f);
        at.put("dir", new vec2(0.0f, rad));
        this.fbm.executeEffect("half2", this.shaders.get("vectorBlur"), at, new Texture[] { this.texHalf1 });
        at.clear();
        at.put("original", 0);
        at.put("glare", 1);
        this.fbm.executeEffect("full1", this.shaders.get("combine"), at, new Texture[] { this.texFull2, this.texHalf2 });
    }
    
    private void renderTone() {
        final HashMap<String, Object> at = new HashMap<>();
        at.put("screen", 0);
        at.put("colRamp", 1);
        this.fbm.executeEffect("Output", this.shaders.get("tone"), at, new Texture[] { this.texFull1, this.rgbCurves });
    }
    
    private void renderUI() {
        this.shaders.get("circle").storeUniform("heat", 620.0f);
        AppWindow.glx.drawConstruct(this.lineID, this.shaders.get("circle"), new Texture[0], 1);
        AppWindow.glx.drawConstruct(this.icons.get("drag"), this.shaders.get("plane"), new Texture[0], 1);
        AppWindow.glx.drawConstruct(this.icons.get("close"), this.shaders.get("plane"), new Texture[0], 1);
        AppWindow.glx.drawConstruct(this.icons.get("max"), this.shaders.get("plane"), new Texture[0], 1);
        AppWindow.glx.drawConstruct(this.icons.get("min"), this.shaders.get("plane"), new Texture[0], 1);
        AppWindow.glx.drawConstruct(this.icons.get("pan"), this.shaders.get("plane"), new Texture[0], 1);
        AppWindow.glx.drawConstruct(this.icons.get("min2"), this.shaders.get("plane"), new Texture[0], 1);
        final dvec4 col = new dvec4(1.0, 1.0, 1.0, 1.0);
        AppWindow.glx.drawText(Ref.res.x - 100, Ref.res.y - 100, String.format("%d Hz", (int)this.mainSampler.getFreq().x), col);
        AppWindow.glx.drawText(10.0, 10.0, "FPS: " + this.fps, col);
        final int asd = AppWindow.glx.getTextWidth(this.nowPlaying);
        AppWindow.glx.drawText((Ref.res.x - asd) / 2, 10.0, this.nowPlaying, col);
        this.fbm.executeEffect(AppWindow.glx, "Input", false);
        this.shaders.get("circle").storeUniform("heat", this.heat);
    }
    
    private void updateHeat() {
        this.shaders.get("sphere").storeUniform("heat", this.heat);
        this.shaders.get("circle").storeUniform("heat", this.heat);
        this.shaders.get("plane").storeUniform("heat", this.heat);
        long ts = System.currentTimeMillis();
        ts &= 0xFFFFFFFL;
        final float f = ts;
        this.shaders.get("tone").storeUniform("time", f);
        this.shaders.get("sphere").storeUniform("time", f);
        this.shaders.get("circle").storeUniform("time", f);
        this.shaders.get("plane").storeUniform("time", f);
    }
    
    private void cleanUp() {
        AppWindow.glx.waitForRelease();
        AppWindow.glx.deconstructAll();
        this.fbm.releaseAll();
        for (final ShaderWrapper sw : this.shaders.values()) {
            sw.releaseShader();
        }
        this.shaders.clear();
        for (final Retainable r : this.cbc) {
            r.release();
        }
        this.cbc.clear();
    }
    
    @Override
    public void handleMessage(final Message msg) {
        if (msg.getMsgType() == Message.M_TYPE.RAW_MS) {
            this.handleMouse(msg);
        }
        else {
            this.handleKeyboard(msg);
        }
    }
    
    private void rebuildFramebuffer() {
        this.replaceIcons();
        GL11.glViewport(0, 0, Ref.res.x, Ref.res.y);
        AppWindow.glx.setupProjMat();
        AppWindow.glx.setupScreenMat();
        final FrameBufferWrapper fbw = this.fbm.removeAndGetBuffer("tonemap");
        this.fbm.releaseAll();
        this.fbm = new FrameBufferManager();
        this.texFull1 = this.fbm.addBuffer("full1", new ivec2(Ref.res));
        this.texFull2 = this.fbm.addBuffer("full2", new ivec2(Ref.res));
        this.texHalf1 = this.fbm.addBuffer("half1", new ivec2(Ref.res).scale(1.0 / Ref.downRes));
        this.texHalf2 = this.fbm.addBuffer("half2", new ivec2(Ref.res).scale(1.0 / Ref.downRes));
        this.fbm.insertBuffer("tonemap", fbw);
        this.texInput = this.fbm.getTexture("Input");
        this.shaders.get("msaa_to_tex").storeUniform("dim", Ref.res);
    }
    
    private void handleMouse(final Message msg) {
        final Message.RW_MS_Param p = (Message.RW_MS_Param)msg.getParam();
        if (!p.pressed && this.scaleFlag) {
            this.scaleFlag = false;
            this.rebuildFramebuffer();
        }
        if (p.pressed && p.button == 0) {
            this.pressing = true;
        }
        else if (!p.pressed && p.button == 0) {
            this.pressing = false;
            if (p.my < 25.0 && p.mx > Ref.res.x - 25) {
                this.closeRequest = true;
            }
            if (p.my < 25.0 && p.mx > Ref.res.x - 50 && p.mx < Ref.res.x - 25) {
                GLFW.glfwSetWindowPos(this.window, 0, 0);
                final long monitor = GLFW.glfwGetPrimaryMonitor();
                final ByteBuffer bb = GLFW.glfwGetVideoMode(monitor);
                final int w = GLFWvidmode.width(bb);
                final int h = GLFWvidmode.height(bb);
                GLFW.glfwSetWindowSize(this.window, w, h - 30);
            }
            if (p.my < 25.0 && p.mx > Ref.res.x - 75 && p.mx < Ref.res.x - 50) {
                GLFW.glfwIconifyWindow(this.window);
            }
        }
        if (p.pressed && p.my < 50.0) {
            this.dRefX = p.mx;
            this.dRefY = p.my;
            this.frameDrag = true;
        }
        if (p.pressed && p.mx > Ref.res.x - 50 && p.my > Ref.res.y - 50) {
            this.sRefX = p.mx;
            this.sRefY = p.my;
            this.scaleDrag = true;
        }
        if (!this.pressing) {
            this.frameDrag = false;
            this.scaleDrag = false;
        }
        if (this.frameDrag) {
            final double dx = p.mx - this.dRefX;
            final double dy = p.my - this.dRefY;
            final IntBuffer x = BufferUtils.createIntBuffer(1);
            final IntBuffer y = BufferUtils.createIntBuffer(1);
            GLFW.glfwGetWindowPos(this.window, x, y);
            final int ix = x.get();
            final int iy = y.get();
            GLFW.glfwSetWindowPos(this.window, (int)(ix + dx), (int)(iy + dy));
        }
        if (this.scaleDrag) {
            final double sx = p.mx - this.sRefX;
            final double sy = p.my - this.sRefY;
            this.sRefX = p.mx;
            this.sRefY = p.my;
            GLFW.glfwSetWindowSize(this.window, (int)(Ref.res.x + sx), (int)(Ref.res.y + sy));
        }
        if (this.pressing) {
            double dist = Math.abs(Ref.res.y / 2 - p.my);
            dist /= Ref.res.y / 4;
            double freq;
            dist = (freq = Math.min(dist, 1.0));
            double h2 = p.my;
            h2 = Math.max(h2, Ref.res.y / 4);
            h2 = Math.min(h2, 3 * Ref.res.y / 4);
            AppWindow.glx.getConstruct(this.icons.get("min2")).position.y = (int)h2 + 35;
            freq = Math.pow(freq, 3.0);
            this.mainSampler.adjustFreq((float)(freq * 20000.0));
        }
    }
    
    private void handleKeyboard(final Message msg) {
        final Message.RW_KB_Param p = (Message.RW_KB_Param)msg.getParam();
        if (!p.pressed) {
            if (p.key == 69) {
                if (Ref.gq > 1) {
                    Ref.gq /= 2;
                }
                this.shaders.get("vectorBlur").storeUniform("quality", Ref.gq);
            }
            if (p.key == 82) {
                Ref.gq *= 2;
                this.shaders.get("vectorBlur").storeUniform("quality", Ref.gq);
            }
            if (p.key == 70) {
                this.heatLimit *= 1.05f;
            }
            if (p.key == 68) {
                this.heatLimit /= 1.05f;
            }
            if (this.heatLimit < 600.0f) {
                this.heatLimit = 600.0f;
            }
            if (p.key == 32) {
              if(this.ap != null){
                this.pause = !this.pause;
                if (this.pause) {
                    this.ap.pause();
                    this.breakT = this.ap.position();
                }
                else {
                    this.ap.loop();
                    this.ap.cue(this.breakT);
                }
              }
            }
        }
    }
    
    static /* synthetic */ void access$0(final AppWindow appWindow, final boolean scaleFlag) {
        appWindow.scaleFlag = scaleFlag;
    }
    
    static /* synthetic */ void access$1(final AppWindow appWindow, final String nowPlaying) {
        appWindow.nowPlaying = nowPlaying;
    }
}
