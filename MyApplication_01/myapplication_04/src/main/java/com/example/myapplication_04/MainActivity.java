package com.example.myapplication_04;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView txv;
    Spinner cinema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txv =(TextView)findViewById(R.id.txv);
        cinema=(Spinner) findViewById(R.id.cinema);
    }

    public void order(View view) {
        String[] cinemas = getResources().getStringArray(R.array.cinemas);
        int index=cinema.getSelectedItemPosition();
        txv.setText("订"+cinemas[index]+"的票");
    }
}
