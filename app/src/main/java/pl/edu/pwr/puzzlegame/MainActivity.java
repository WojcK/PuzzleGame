package pl.edu.pwr.puzzlegame;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static java.lang.StrictMath.abs;

public class MainActivity extends AppCompatActivity {
    long startTime = 0;
    ArrayList<PuzzlePiece> pieces;
    int count;

    int cols;
    int rows;
    float alpha;
    int mistakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.startTime = System.currentTimeMillis();
        int drawable_id = getIntent().getIntExtra("drawable_id", 0);
        final RelativeLayout layout = findViewById(R.id.layout);
        Drawable d = getResources().getDrawable(drawable_id);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageDrawable(d);
        count = 0;

        cols = getIntent().getIntExtra("ROWS",4);
        rows = getIntent().getIntExtra("COLS",4);
        alpha = getIntent().getFloatExtra("ALPHA", 0.4f);
        if(alpha > 1 || alpha < 0) {
            alpha = 0.4f;
        }

        imageView.setAlpha(alpha);
        mistakes = 0;

        imageView.post(new Runnable() {
            @Override
            public void run() {
                pieces = splitImage();
                int increment_width = pieces.get(0).pieceWidth + 5;
                int increment_height = pieces.get(0).pieceHeight + 5;
                TouchListener listener = new TouchListener(MainActivity.this);
                int cnt = 0;
                for(int i = 0 ; i < cols; ++i){
                    for(int k = 0; k < rows; ++k, cnt++){
                        int pieces_scatter_left = layout.getWidth() / 2 + i * increment_width;
                        int pieces_scatter_top = increment_height * k;
                        PuzzlePiece piece = pieces.get(cnt);
                        piece.xScatter = pieces_scatter_left;
                        piece.yScatter = pieces_scatter_top;
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(piece.pieceWidth, piece.pieceHeight);
                        params.setMargins(layout.getWidth() / 2 + i * increment_width,increment_height * k,0, 0);
                        piece.setOnTouchListener(listener);
                        layout.addView(piece, params);
                    }
                }
            }
        });
    }

    private int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];
        if (imageView == null || imageView.getDrawable() == null)
            return ret;


        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);


        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();


        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;


        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH)/2;
        int left = (int) (imgViewW - actW)/2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }

    private ArrayList<PuzzlePiece> splitImage() {

        ImageView imageView = findViewById(R.id.imageView);
        ArrayList<PuzzlePiece> pieces = new ArrayList<>(25);


        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimensions = getBitmapPositionInsideImageView(imageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        int croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);


        int pieceWidth = croppedImageWidth/cols;
        int pieceHeight = croppedImageHeight/rows;


        int yCoord = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord, yCoord, pieceWidth, pieceHeight);
                PuzzlePiece piece = new PuzzlePiece(getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
                piece.xCoord = xCoord;
                piece.yCoord = yCoord;
                piece.pieceWidth = pieceWidth;
                piece.pieceHeight = pieceHeight;
                pieces.add(piece);
                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }
        Collections.shuffle(pieces);
        return pieces;
    }

    static public boolean checkForEnd(ArrayList<PuzzlePiece> puzzles){
        for(PuzzlePiece p: puzzles){
            if(p.canMove){
                return false;
            }
        }
        return true;
    }


    public void displayTime(int tries){
        long endTime = System.currentTimeMillis();
        long difference = endTime - startTime;
        String txt_diff = Long.toString(difference / 1000);
        Toast.makeText(this, "It took u: " + txt_diff + " secs, failed attempts : "+tries, Toast.LENGTH_SHORT).show();
    }
    public void count(){
        count++;
    }

    public int counter(){
        return count;
    }


}