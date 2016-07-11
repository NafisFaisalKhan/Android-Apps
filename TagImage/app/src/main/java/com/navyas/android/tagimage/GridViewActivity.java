package com.navyas.android.tagimage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GridViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        List<String> grid = (ArrayList<String>) getIntent().getSerializableExtra("grid");
        for (String val: grid) {
            System.out.println(val);
        }
    }
}
