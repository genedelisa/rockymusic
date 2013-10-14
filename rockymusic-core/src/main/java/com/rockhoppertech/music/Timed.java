package com.rockhoppertech.music;


public interface Timed {

    public abstract double getStartBeat();

    public abstract void setStartBeat(double d);

    public abstract double getDuration();
    
    public abstract double getEndBeat();

    public abstract void setDuration(double d);

} 