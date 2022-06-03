package com.example.test3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class MysSurface extends SurfaceView implements SurfaceHolder.Callback {
    //переменные для рисования
    float x, y; //текущее положение картинки
    float tx, ty;//координаты точки касния
    float dx, dy; // смещение координат
    float koeff;

    //перемнные для картикни
    Bitmap image;
    Resources res;
    Paint paint;

    //обьект потока
    DrawThread drawThread;

    public MysSurface(Context context) {
        super(context);
        getHolder().addCallback(this);


        x = 100;
        y = 100;
        koeff = 50;
        res = getResources();
        image = BitmapFactory.decodeResource(res, R.drawable.medium);
        Paint paint = new Paint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            tx = event.getX();
            ty = event.getY();
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(image, x, y, paint);
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        //рассчет смещения
        if (tx != 0){
            calculate();
        }
        x += dx;
        y += dy;
    }

    private void calculate(){
        double g = Math.sqrt((tx-x)*(tx-x)+(ty-y)*(ty-y));
        dx = (float) (koeff*(tx-x)/g);
        dy = (float) (koeff*(ty-y)/g);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        drawThread = new DrawThread(this, getHolder());
        drawThread.setRun(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean stop = true;
        drawThread.setRun(false);
        while (stop){
            try {
                drawThread.join();
                stop = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
