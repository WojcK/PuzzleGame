package pl.edu.pwr.puzzlegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class MenuActivity extends AppCompatActivity {
    private int photos_number;
    int cols;
    int rows;
    float alpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        photos_number = 14;
        GridView menu = findViewById(R.id.puzzle_menu);

        cols = getIntent().getIntExtra("ROWS",3);
        rows = getIntent().getIntExtra("COLS", 4);
        alpha = getIntent().getFloatExtra("ALPHA", 0.4f);

        String temp = "photo";
        final int[] drawable_ID = new int[photos_number];
        for(int i = 0; i < photos_number; ++i) {
            drawable_ID[i] = getResources().getIdentifier(temp+i,"drawable",getPackageName());
        }
        ThumbnailAdapter myAdapter = new ThumbnailAdapter(this, drawable_ID);
        menu.setAdapter(myAdapter);
        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent puzzleIntent = new Intent(MenuActivity.this, MainActivity.class);
                puzzleIntent.putExtra("drawable_id", drawable_ID[position]);
                puzzleIntent.putExtra("ROWS", rows);
                puzzleIntent.putExtra("COLS", cols);
                puzzleIntent.putExtra("ALPHA", alpha);
                startActivity(puzzleIntent);
            }
        });
    }
}
