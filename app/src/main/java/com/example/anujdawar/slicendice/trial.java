package com.example.anujdawar.slicendice;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class trial extends Activity {

    protected static TextView myPopUpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);

        myPopUpView = (TextView) findViewById(R.id.theGreatPopUp);
        myPopUpView.setText("cool");

        finish();
    }
}


