package com.sourav.getaway_drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Random;

public class ParticleSystem {
    private float mDuration;//this will be initialized to the number of seconds we want the effect to run for
    private ArrayList<Particle> mParticles;// it will hold all the particle object we instantiate
    private Random random = new Random();
    boolean mIsRunning = false;
    void inIt(int numParticles){
        mParticles = new ArrayList<>();//creating particles
        for(int i=0; i<numParticles; i++){
            float angle = (random.nextInt(360));
            angle = angle*3.14f/180.f;//multiplying it will turn the angle in degree to radian measurement

            //float speed =  (random.nextFloat()/10);//slow particles
            //option 2 - fast particles
            float speed = (random.nextInt(10)+1);
            PointF direction;
            direction = new PointF((float)Math.cos(angle)*speed,(float)Math.sin(angle)*speed);
            mParticles.add(new Particle(direction));
        }
    }
    void update(long fps){
        mDuration -= (1f/fps);
        for(Particle p : mParticles){
            p.update(fps);
        }
        if(mDuration<0){
            mIsRunning = false;
        }
    }
    void emitParticle(PointF startPosition){
        mIsRunning = true;
        mDuration = 30f;
        for(Particle p : mParticles){
            p.setPosition(startPosition);
        }
    }
void draw (Canvas canvas, Paint paint){
        for(Particle p : mParticles) {
            paint.setARGB(255, 255,255, 255);
            float sizeX = 6;
            float sizeY = 6;
            canvas.drawCircle(p.getPosition().x,p.getPosition().y,sizeX,paint);
        }

}
}
