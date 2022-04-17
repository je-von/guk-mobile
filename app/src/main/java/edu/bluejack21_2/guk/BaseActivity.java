package edu.bluejack21_2.guk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("edu.bluejack21_2.guk", Context.MODE_PRIVATE);
        float scale = 1;
        if(prefs.contains("font_size")){
            if(prefs.getString("font_size", null).equals("Small")){
                scale = 0.75f;
            } else if(prefs.getString("font_size", null).equals("Large")){
                scale = 1.25f;
            }
        }
        adjustFontScale(getResources().getConfiguration(), scale);
    }

    public void adjustFontScale(Configuration configuration, float scale) {
        configuration.fontScale = scale;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }
}
