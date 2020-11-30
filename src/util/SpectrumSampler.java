// 
// Decompiled by Procyon v0.5.36
// 

package util;

import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;
import ddf.minim.AudioListener;

public class SpectrumSampler implements AudioListener
{
    private FFT fftL;
    private FFT fftR;
    private BeatDetect bd;
    //private float sampleRate;
    //private int timeSize;
    private int SPECLEN;
    private int fSIZE;
    private float[] filter;
    private float[][] beatBuffer;
    private float[][] specBuffer;
    
    public SpectrumSampler(final int ts, final float sr, final long length) {
        this.SPECLEN = 128;
        this.fSIZE = 25;

        //this.timeSize = ts;
        //this.sampleRate = sr;

        this.fftL = new FFT(ts, sr);
        this.fftR = new FFT(ts, sr);
        this.bd = new BeatDetect(ts, sr);
        this.buildFilter();
    }
    
    private void buildFilter() {
        this.filter = new float[this.fSIZE];
        this.specBuffer = new float[this.SPECLEN][this.fSIZE];
        this.beatBuffer = new float[3][this.fSIZE];
        final float peak = 0.1f;
        for (int i = 0; i < this.filter.length; ++i) {
            final float p = i / (float)this.filter.length;
            float lin;
            if (p < peak) {
                lin = p / peak;
            }
            else {
                final float dist = 1.0f - peak;
                final float newPos = p - peak;
                lin = 1.0f - newPos / dist;
            }
            final float sin = (float)Math.sin(1.5707963267948966 * lin * lin);
            this.filter[i] = sin;
        }
    }
    
    @Override
    public void samples(final float[] samp) {
        this.samples(samp, samp);
    }
    
    private void addFrequency(final float[] il, final float[] rl, final float[] ir, final float[] rr) {
        final float[] erg = new float[il.length];
        for (int i = 0; i < il.length; ++i) {
            final float l = (float)Math.hypot(il[i], rl[i]);
            final float r = (float)Math.hypot(ir[i], rr[i]);
            erg[i] = (r + l) / 2.0f;
        }
        for (int i = 0; i < 128; ++i) {
            float sum = 0.0f;
            for (int j = 0; j < 8; ++j) {
                sum += erg[i * 8 + j];
            }
            for (int j = this.filter.length - 2; j >= 0; --j) {
                this.specBuffer[i][j + 1] = this.specBuffer[i][j];
            }
            this.specBuffer[i][0] = sum;
        }
    }
    
    private void addSamples(final boolean h, final boolean k, final boolean s) {
        for (int i = this.filter.length - 2; i >= 0; --i) {
            for (int j = 0; j < 3; ++j) {
                this.beatBuffer[j][i + 1] = this.beatBuffer[j][i];
            }
        }
        this.beatBuffer[0][0] = h ? 1 : 0;
        this.beatBuffer[1][0] = k ? 1 : 0;
        this.beatBuffer[2][0] = s ? 1 : 0;
    }
    
    @Override
    public void samples(final float[] sampL, final float[] sampR) {
        this.fftL.forward(sampL);
        this.fftR.forward(sampR);
        this.bd.detect(sampL);
        this.addSamples(this.bd.isHat(), this.bd.isKick(), this.bd.isSnare());
        this.addFrequency(this.fftL.getSpectrumImaginary(), this.fftL.getSpectrumReal(), this.fftR.getSpectrumImaginary(), this.fftR.getSpectrumReal());
    }
    
    public double[] getBeats() {
        final double[] accum = new double[3];
        for (int j = 0; j < 3; ++j) {
            for (int i = 0; i < this.filter.length; ++i) {
                final double[] array = accum;
                final int n = j;
                array[n] += this.beatBuffer[j][i] * this.filter[i];
            }
            final double[] array2 = accum;
            final int n2 = j;
            array2[n2] /= this.fSIZE;
        }
        return accum;
    }
    
    public Float[] getSpectrumPow() {
        final Float[] accum = new Float[128];
        for (int j = 0; j < 128; ++j) {
            accum[j] = 0.0f;
            for (int i = 0; i < this.fSIZE; ++i) {
                accum[j] = Math.max(accum[j], this.specBuffer[j][i] * this.filter[i]);
            }
        }
        return accum;
    }
}
