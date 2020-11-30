// 
// Decompiled by Procyon v0.5.36
// 

package util;

import util.vector.vec2;
import ddf.minim.analysis.FFT;
import ddf.minim.AudioListener;

public class FrequencySampler implements AudioListener
{
    private FFT fftL;
    private FFT fftR;

    private float freq;
    private float sprd;

    //private float sampleRate;
    //private int timeSize;

    private int CHAN;
    private int fSIZE;
    private float[] filter;
    private float[][] buffers;
    private double[] amplitude;
    
    public FrequencySampler(final int ts, final float sr, final float fq, final float spread, final int length) {
        this.CHAN = 2;
        this.fSIZE = 25;
        this.fSIZE = length;

        //this.sampleRate = sr;
        //this.timeSize = ts;

        this.fftL = new FFT(ts, sr);
        this.fftR = new FFT(ts, sr);
        this.freq = fq;
        this.sprd = spread;
        this.buildFilter();
    }
    
    public void adjustFreq(final float f, final float s) {
        this.freq = f;
        this.sprd = s;
    }
    
    public void adjustFreq(final float x) {
        this.adjustFreq(x, x / 6.0f);
    }
    
    public vec2 getFreq() {
        return new vec2(this.freq, this.sprd);
    }
    
    private void buildFilter() {
        this.filter = new float[this.fSIZE];
        this.buffers = new float[this.CHAN][this.fSIZE];
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
    
    private void addSamples(final float l, final float r) {
        for (int i = this.filter.length - 2; i >= 0; --i) {
            for (int j = 0; j < this.CHAN; ++j) {
                this.buffers[j][i + 1] = this.buffers[j][i];
            }
        }
        this.buffers[0][0] = l;
        this.buffers[1][0] = r;
    }
    
    private double[] getFiltered() {
        final double[] accum = new double[this.CHAN];
        for (int j = 0; j < this.CHAN; ++j) {
            for (int i = 0; i < this.filter.length; ++i) {
                final double[] array = accum;
                final int n = j;
                array[n] += this.buffers[j][i] * this.filter[i];
            }
            final double[] array2 = accum;
            final int n2 = j;
            array2[n2] /= this.fSIZE;
        }
        return accum;
    }
    
    @Override
    public void samples(final float[] sampL, final float[] sampR) {
        this.fftL.forward(sampL);
        this.fftR.forward(sampR);
        final float fL = this.fftL.calcAvg(this.freq - this.sprd / 2.0f, this.freq + this.sprd / 2.0f);
        final float fR = this.fftR.calcAvg(this.freq - this.sprd / 2.0f, this.freq + this.sprd / 2.0f);
        this.addSamples(fL, fR);
        this.amplitude = this.getFiltered();
    }
    
    public double[] getAmplitude() {
        return this.amplitude;
    }
}
