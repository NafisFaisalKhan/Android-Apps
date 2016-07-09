package com.navyas.android.tagimage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    static final String ClientId = "clientid";
    static final String ClientSecret = "clientsecret";
    static Uri uri;
    static String[] tagName = new String[20];
    static List<String> string = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<File> files = new ArrayList<>();

        cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        string.add(cursor.getString(columnIndex));
        //id = cursor.getInt(columnIndex);
               // while (cursor.moveToNext()) {
       /* id = cursor.getInt(columnIndex);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageURI(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id));*/
        while (cursor.moveToNext()){
            string.add(cursor.getString(columnIndex));
        }
        int i = 0;
        for (String attr: string) {
            uri = Uri.fromFile(new File(attr));
            File file = new File(string.get(i));
            files.add(file);
            Log.e("Tag", uri.toString());
            i++;
        }
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        uri = Uri.fromFile(new File(string.get(3)));
        imageView.setImageURI(uri);

        File[] fileArray = new File[files.size()];
        fileArray = files.toArray(fileArray);

        new ClarifaiRecognize(this).execute(fileArray);
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

        private Context mContext;
        String projection[] = {ClarifaiContract.DataEntry._ID,
                ClarifaiContract.DataEntry.COLUMN_IMAGE_LOCATION,
                ClarifaiContract.DataEntry.COLUMN_TAG1};
        Cursor c;
        String sortOrder =
                ClarifaiContract.DataEntry._ID + " DESC";

        private ClarifaiRecognize(Context context){
            mContext = context;
        }

        protected Void doInBackground(File ... files){
            ClarifaiClient client = new ClarifaiClient(ClientId, ClientSecret);
            ClarifaiDbHelper mDbHelper = new ClarifaiDbHelper(mContext);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            int i;
            for (File file: files) {
                i = 0;
                List<RecognitionResult> results = client.recognize(new RecognitionRequest(file));
                for (Tag tag : results.get(0).getTags()) {
                    System.out.println(i + ": " + tag.getName() + ": " + tag.getProbability());
                    tagName[i] = tag.getName();
                    i++;
                }

                Uri uri = Uri.fromFile(file);
                values.put(ClarifaiContract.DataEntry.COLUMN_IMAGE_LOCATION, uri.toString());
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG1, tagName[0]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG2, tagName[1]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG3, tagName[2]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG4, tagName[3]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG5, tagName[4]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG6, tagName[5]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG7, tagName[6]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG8, tagName[7]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG9, tagName[8]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG10, tagName[9]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG11, tagName[10]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG12, tagName[11]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG13, tagName[12]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG14, tagName[13]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG15, tagName[14]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG16, tagName[15]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG17, tagName[16]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG18, tagName[17]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG19, tagName[18]);
                values.put(ClarifaiContract.DataEntry.COLUMN_TAG20, tagName[19]);

                long newRowId;
                newRowId = db.insert(ClarifaiContract.DataEntry.TABLE_NAME, null, values);
//                if (newRowId < 0) throw new android.database.SQLException("Failed to insert row into ");
            }

            testDb(db);

            return null;
        }

        private void testDb(SQLiteDatabase db){
            c = db.query(ClarifaiContract.DataEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);
            c.moveToFirst();
            System.out.println(c.getString(c.getColumnIndex(ClarifaiContract.DataEntry.COLUMN_IMAGE_LOCATION)));
            while (c.moveToNext()) System.out.println(c.getString(c.getColumnIndex(ClarifaiContract.DataEntry.COLUMN_IMAGE_LOCATION)));
        }

    }

}
