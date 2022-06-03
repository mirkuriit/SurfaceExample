package com.example.test3;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawThread extends Thread{

    MysSurface mysSurface;
    SurfaceHolder surfaceHolder; // штука для связи с канвасом
    boolean isRun = false;
    long nowTime, prevTime, ellapsedTime;


    public DrawThread(MysSurface mysSurface, SurfaceHolder surfaceHolder) {
        this.mysSurface = mysSurface;
        this.surfaceHolder = surfaceHolder;
        prevTime = System.currentTimeMillis();
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (isRun){
            nowTime = System.currentTimeMillis();
            ellapsedTime = nowTime - prevTime;
            if (ellapsedTime > 30){
                prevTime = nowTime;
                canvas = null;
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    mysSurface.draw(canvas);
                }
                if (canvas != null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

            }
        }
    }
}
