package com.sourav.getaway_drawing;
import android.graphics.PointF;
public class Particle {
    PointF mVelocity;
    PointF mPosition;

    Particle(PointF direction){
        mVelocity = new PointF();
        mPosition = new PointF();
        mVelocity.x = direction.x;
        mPosition.y = direction.y;
    }
    void update(float fps){
        mPosition.x += mVelocity.x;
        mPosition.y += mVelocity.y;
    }
    void setPosition(PointF position){
        mPosition.x = position.x;
        mPosition.y = position.y;
    }
    PointF getPosition(){
        return mPosition;
    }

}
