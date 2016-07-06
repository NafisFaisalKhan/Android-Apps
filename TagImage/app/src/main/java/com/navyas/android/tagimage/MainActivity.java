package com.navyas.android.tagimage;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Cursor cursor;
    private static int columnIndex;
    private static int id;
    private static int id1;
    private static final String[] proj = {MediaStore.Images.Media.DATA};
    //clientid
    //secretid
    static Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);*/




        List<String> string = new ArrayList<String>();
        cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        //id = cursor.getInt(columnIndex);
               // while (cursor.moveToNext()) {
       /* id = cursor.getInt(columnIndex);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageURI(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id));*/
        while (cursor.moveToNext()){
            string.add(cursor.getString(columnIndex));
        }
        for (String attr: string) {

            uri = Uri.fromFile(new File(attr));
            Log.e("Tag", uri.toString());
        }
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        uri = Uri.fromFile(new File(string.get(128)));
        imageView.setImageURI(uri);
        File file = new File(string.get(128));
        new ClarifaiRecognize().execute(file);
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

    private class ClarifaiRecognize extends AsyncTask<File, Void, Void>{
        protected Void doInBackground(File ... file){
            ClarifaiClient client = new ClarifaiClient(ClientId, ClientSecret);
            List<RecognitionResult> results = client.recognize(new RecognitionRequest(file));
            for (Tag tag : results.get(0).getTags()) {
                System.out.println(tag.getName() + ": " + tag.getProbability());
            }
            return null;
        }
    }

}
