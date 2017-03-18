package tech.kolektiv.hiddengame.audio;


import rx.functions.Action0;

public interface AudioCallback {

     void onStart(long duration);
     void onProgress(long position, long positionSound, long duration);
     void onBufferChange(int percent);
     void onFinished(Action0 end);
}
