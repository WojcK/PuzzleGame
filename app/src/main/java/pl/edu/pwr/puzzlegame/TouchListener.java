package pl.edu.pwr.puzzlegame;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;

public class TouchListener implements View.OnTouchListener {
    private float xDelta;
    private float yDelta;
    private  MainActivity root_act;

    public TouchListener(MainActivity my_act){
        this.root_act = my_act;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        float x = motionEvent.getRawX();
        float y = motionEvent.getRawY();
        final double tolerance = sqrt(pow(view.getWidth(), 2) + pow(view.getHeight(), 2)) / 10;
        PuzzlePiece piece = (PuzzlePiece) view;
        if (!piece.canMove) {
            return true;
        }


        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xDelta = x - lParams.leftMargin;
                yDelta = y - lParams.topMargin;
                piece.bringToFront();
                break;
            case MotionEvent.ACTION_MOVE:
                lParams.leftMargin = (int) (x - xDelta);
                lParams.topMargin = (int) (y - yDelta);
                view.setLayoutParams(lParams);
                break;
            case MotionEvent.ACTION_UP:
                int xDiff = abs(piece.xCoord - lParams.leftMargin);
                int yDiff = abs(piece.yCoord - lParams.topMargin);
                if (xDiff <= tolerance && yDiff <= tolerance) {
                    lParams.leftMargin = piece.xCoord;
                    lParams.topMargin = piece.yCoord;
                    piece.setLayoutParams(lParams);
                    piece.canMove = false;
                    if(root_act.checkForEnd(root_act.pieces)){
                        root_act.displayTime(root_act.counter());
                    }

                } else {
                    lParams.leftMargin = piece.xScatter;
                    lParams.topMargin = piece.yScatter;
                    piece.setLayoutParams(lParams);
                    root_act.count();
                }

                view.performClick();
                break;
        }

        return true;
    }
}
