package com.example.bcmb.top100;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity {
    private RadioButton radio_all;
    private RadioButton radio_favs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        radio_all = (RadioButton) findViewById(R.id.radio_all);
        radio_favs = (RadioButton) findViewById(R.id.radio_favs_only);
        if (getIntent().getBooleanExtra(MainActivity.SHOW_ALL, true)) {
            radio_all.setChecked(true);
        } else {
            radio_favs.setChecked(true);
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_all:
                if (checked)
                   this.setResult(true);
                    break;
            case R.id.radio_favs_only:
                if (checked)
                   this.setResult(false);
                    break;
        }
    }

    private void setResult(boolean b) {
        Intent i = new Intent();
        i.putExtra(MainActivity.SHOW_ALL, b);
        setResult(Activity.RESULT_OK, i);
    }
}
