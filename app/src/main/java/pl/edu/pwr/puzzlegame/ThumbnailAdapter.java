package pl.edu.pwr.puzzlegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ThumbnailAdapter extends BaseAdapter {
    private int[] imageList;
    private Context mContext;
    private LayoutInflater inflater;
    ThumbnailAdapter(Context srcContext, int[] source){
        this.imageList = source;
        this.mContext = srcContext;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return imageList.length;
    }

    @Override
    public Object getItem(int position) {
        return imageList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View SingleGrid = inflater.inflate(R.layout.single_image, parent, false);
        int drawable_id = imageList[position];
        Bitmap puzzle = BitmapFactory.decodeResource(mContext.getResources(), drawable_id);
        Bitmap generatedThumbnail = ThumbnailUtils.extractThumbnail(puzzle, 100, 300);
        ImageView myImage = (ImageView) SingleGrid.findViewById(R.id.source_image);
        myImage.setImageBitmap(generatedThumbnail);
        return SingleGrid;
    }
}
