package com.sourav.getaway_drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class LiveDrawingView extends SurfaceView implements Runnable {
    private final boolean DEBUGGING = true;
    private SurfaceHolder mOurHolder;//SurfaceHolder object is used to hold the drawing object
    private Canvas mCanvas;//
    private Paint mPaint;//
    private long mFPS;
    private final int MILLIS_IN_SECOND=1000;
    private int mScreenX;
    private int mScreenY;
    private int mFontSize;
    private int mFontMargin;
    private ArrayList<ParticleSystem> mParticleSystem = new ArrayList<>();
    private int mNextSystem = 0;
    private final int MAX_SYSTEMS = 1000;
    private int mParticlesPerSystem = 100;
    private Thread mThread=null;
    private volatile boolean mDrawing;
    private boolean mPaused=true;

    private RectF mResetButton;
    private RectF mTogglePauseButton;
    public LiveDrawingView(Context context, int x, int y){
        super(context);
        mScreenX = x;
        mScreenY = y;
        mFontSize = mScreenX/20;
        mFontMargin = mScreenX/75;
        mOurHolder = getHolder();
        mPaint = new Paint();
        mResetButton = new RectF(0,0,100,100);
        mTogglePauseButton = new RectF(0,150,100,250);
        for(int i=0; i<MAX_SYSTEMS; i++){
            mParticleSystem.add(new ParticleSystem());
            mParticleSystem.get(i).inIt(mParticlesPerSystem);
        }

    }

    private void draw(){
        if(mOurHolder.getSurface().isValid()){
            mCanvas = mOurHolder.lockCanvas();
            mCanvas.drawColor(Color.argb(255,0,0,0));
            mPaint.setColor(Color.argb(255,255,255,255));
            mPaint.setTextSize(mFontSize);
            for(int i=0; i<mNextSystem; i++){
                mParticleSystem.get(i).draw(mCanvas,mPaint);
            }

            mCanvas.drawRect(mResetButton,mPaint);
            mCanvas.drawRect(mTogglePauseButton,mPaint);
            if(DEBUGGING){
                printDebuggingText();
            }
            mOurHolder.unlockCanvasAndPost(mCanvas);




        }
    }
    @Override
    public void run() {
        while (mDrawing){
            long frameStartTime = System.currentTimeMillis();
            if(!mPaused){
                update();
            }
            draw();
            long timeThisFrame = System.currentTimeMillis() - frameStartTime;
            if(timeThisFrame>0){
                mFPS = MILLIS_IN_SECOND/timeThisFrame;
            }
        }
    }
    private void printDebuggingText(){
        int debugSize = mFontSize/2;
        int debugStart = 300;
        mPaint.setTextSize(debugSize);
        mCanvas.drawText("FPS: "+mFPS,10,debugStart+debugSize,mPaint);
        mCanvas.drawText("Systems: "+mNextSystem,10, mFontMargin+debugStart+debugSize*2,mPaint);
        mCanvas.drawText("Particles: "+mNextSystem*mParticlesPerSystem,10,mFontMargin+debugStart+debugSize*3,mPaint);
    }


    private void update(){
for(int i=0; i<mParticleSystem.size(); i++){
    if(mParticleSystem.get(i).mIsRunning){
        mParticleSystem.get(i).update(mFPS);
    }
}
    }
    public void pause(){
        mDrawing = false;
        try{
            mThread.join();
        }catch (InterruptedException e){
            Log.e("Error:","joining thread");

        }
    }
    public void resume(){
        mDrawing=true;
        mThread = new Thread(this);
        mThread.start();
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
       if((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE){
           mParticleSystem.get(mNextSystem).emitParticle(new PointF(motionEvent.getX(),motionEvent.getY()));
           mNextSystem++;
           if(mNextSystem == MAX_SYSTEMS){
               mNextSystem = 0;
           }

       }
       if((motionEvent.getAction()& MotionEvent.ACTION_MASK)==MotionEvent.ACTION_DOWN){
           if(mResetButton.contains(motionEvent.getX(),motionEvent.getY())){
               mNextSystem = 0;
           }
           if(mTogglePauseButton.contains(motionEvent.getX(),motionEvent.getY())){
               mPaused = !mPaused;
           }
       }
        return true;
    }

}
