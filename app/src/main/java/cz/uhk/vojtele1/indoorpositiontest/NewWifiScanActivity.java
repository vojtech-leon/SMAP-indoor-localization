package cz.uhk.vojtele1.indoorpositiontest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewWifiScanActivity extends AppCompatActivity {
    public static final String EXTRA_X = "cz.uhk.vojtele1.indoorpositiontest.extra_x";
    public static final String EXTRA_Y = "cz.uhk.vojtele1.indoorpositiontest.extra_y";
    public static final String EXTRA_TIME = "cz.uhk.vojtele1.indoorpositiontest.extra_time";

    private EditText editTextX;
    private EditText editTextY;
    private EditText editTextTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wifi_scan);
        editTextX = findViewById(R.id.edit_x);
        editTextY = findViewById(R.id.edit_y);
        editTextTime = findViewById(R.id.edit_time);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(editTextX.getText()) || TextUtils.isEmpty(editTextY.getText())) {
                Toast.makeText(
                        getApplicationContext(),
                        R.string.error_empty,
                        Toast.LENGTH_LONG).show();
            } else {
                replyIntent.putExtra(EXTRA_X, editTextX.getText().toString());
                replyIntent.putExtra(EXTRA_Y, editTextY.getText().toString());
                replyIntent.putExtra(EXTRA_TIME, editTextTime.getText().toString());
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
    }
}