package com.rentpath.motif.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.rentpath.motif.MotifConfig;
import com.rentpath.motif.MotifContextWrapper;

public class MainActivity extends AppCompatActivity {

    private EditText mStatusBarColorEditText;
    private EditText mToolbarColorEditText;
    private EditText mButtonColorEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStatusBarColorEditText = (EditText) findViewById(R.id.status_bar_color_edit_text);
        mToolbarColorEditText = (EditText) findViewById(R.id.toolbar_color_edit_text);
        mButtonColorEditText = (EditText) findViewById(R.id.button_color_edit_text);
        findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mStatusBarColorEditText.getText().toString().length() > 0) {
                    ((SampleStatusBarViewFactory) MotifConfig.get().getStatusBarViewFactory()).setColor(Color.parseColor("#" + mStatusBarColorEditText.getText().toString()));
                }

                if (mToolbarColorEditText.getText().toString().length() > 0) {
                    ((ToolbarViewFactory) MotifConfig.get().getRegisteredViewFactoryForClass(Toolbar.class)).setColor(Color.parseColor("#" + mToolbarColorEditText.getText().toString()));
                }

                if (mButtonColorEditText.getText().toString().length() > 0) {
                    ((ApplyButtonViewFactory) MotifConfig.get().getRegisteredViewFactoryForId(R.id.apply)).setColor(Color.parseColor("#" + mButtonColorEditText.getText().toString()));
                }

                startActivity(new Intent(view.getContext(), MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MotifContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
