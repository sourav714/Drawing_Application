package com.sourav.getaway_drawing;



import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends Activity {
    private LiveDrawingView mLiveDrawingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size); //now we are having the width and height of the display

        mLiveDrawingView = new LiveDrawingView(this,size.x,size.y);
        setContentView(mLiveDrawingView);
    }
    @Override
    protected void onResume(){
        super.onResume();
        mLiveDrawingView.resume();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mLiveDrawingView.pause();
    }
}