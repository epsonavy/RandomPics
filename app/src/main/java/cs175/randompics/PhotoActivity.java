package cs175.randompics;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.OnClick;
import cs175.randompics.randomPic_model.PicObject;

public class PhotoActivity extends AppCompatActivity implements GridView.OnItemClickListener {

    private GridView picGrid;
    BlockAdapter adapter;
    private ArrayList<PicObject> thumbList = new ArrayList<>();
    SQLiteDatabase db;
    Cursor resultSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        db = openOrCreateDatabase("pics.db", MODE_PRIVATE, null);
        createNewScene();
    }

    protected void createNewScene() {
        fetchData();
        picGrid = (GridView) findViewById(R.id.saveGrid);
        adapter = new BlockAdapter(this, thumbList);
        picGrid.setAdapter(adapter);
        picGrid.setOnItemClickListener(this);
    }

    private void fetchData() {
        // Check database if exist
        Cursor mcursor = db.rawQuery("SELECT count(*) FROM PicsTable", null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (!mcursor.isClosed()) {
            mcursor.close();
        }
        if(icount > 0) {
            resultSet = db.rawQuery("Select * from PicsTable", null);
            resultSet.moveToFirst();
            do {
                String url = resultSet.getString(
                        resultSet.getColumnIndex("url"));
                Log.i("*****DataHave*****", url);
                thumbList.add(new PicObject(url, 0));

            } while (resultSet.moveToNext());
            if (!resultSet.isClosed()) {
                resultSet.close();
            }

        } else {
            Log.i("*****SHOW*****", "EMPTY!!");
            Toast.makeText(this, "My Photo is empty!", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (thumbList.get(position).getState() == 0)
            thumbList.get(position).setState(1);
        else
            thumbList.get(position).setState(0);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.clear_btn)
    public void clearClicked(View view) {
        db.execSQL("DROP TABLE IF EXISTS PicsTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS PicsTable(url VARCHAR);");
        if (thumbList.size() > 0)
            thumbList.clear();
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.back_btn)
    public void backClicked(View view) {
        Intent main = new Intent(PhotoActivity.this, MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(main);
    }

    @Override
    public void onBackPressed() {
        Intent main = new Intent(PhotoActivity.this, MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(main);
        super.onBackPressed();
    }
}
