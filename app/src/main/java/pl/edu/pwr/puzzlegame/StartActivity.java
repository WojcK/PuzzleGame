package pl.edu.pwr.puzzlegame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StartActivity extends AppCompatActivity {


    private Button play;
    private EditText rows;
    private EditText cols;
    private EditText alpha;
    private int rows_num;
    private int cols_num;
    private float alpha_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        play = findViewById(R.id.play_button);
        rows = findViewById(R.id.rows_edit);
        cols = findViewById(R.id.cols_edit);
        alpha = findViewById(R.id.alpha_edit);

        final Intent save_data = new Intent(getBaseContext(), MenuActivity.class);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rows_num = Integer.parseInt(rows.getText().toString());
                cols_num = Integer.parseInt(cols.getText().toString());
                alpha_num = Float.parseFloat(alpha.getText().toString());
                save_data.putExtra("ROWS",rows_num);
                save_data.putExtra("COLS", cols_num);
                save_data.putExtra("ALPHA",alpha_num);
                startActivity(save_data);
            }
        });


    }
}
