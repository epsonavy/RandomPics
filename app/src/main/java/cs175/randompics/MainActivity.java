package cs175.randompics;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cs175.randompics.randomPic_model.PicObject;
import cs175.randompics.RetrofitForUnsplash.RetrofitHelper;
import cs175.randompics.RetrofitForUnsplash.UnsplashService;
import cs175.randompics.randomPic_model.PicResponse;
import cs175.randompics.searchPic_model.SearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements GridView.OnItemClickListener {

    private GridView picGrid;
    private final int MAX_SIZE = 10;
    BlockAdapter adapter;
    private ArrayList<PicObject> thumbList;
    private UnsplashService apiService;
    EditText input;
    SQLiteDatabase db;
    Cursor resultSet;
    boolean isInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        db = openOrCreateDatabase("pics.db", MODE_PRIVATE, null);
        // db.execSQL("DROP TABLE IF EXISTS PicsTable");
        db.execSQL("CREATE TABLE IF NOT EXISTS PicsTable(url VARCHAR);");
        createNewScene();

        input = (EditText) findViewById(R.id.editText);
        input.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);

        input.setOnKeyListener(new TextView.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    emptyPics();
                    addSearchPics(input.getText().toString());
                    adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });



    }

    protected void createNewScene() {
        RetrofitHelper.init();
        apiService =  RetrofitHelper.getService();
        thumbList = new ArrayList<>(MAX_SIZE);
        picGrid = (GridView) findViewById(R.id.picGrid);
        adapter = new BlockAdapter(this, thumbList);
        picGrid.setAdapter(adapter);
        picGrid.setOnItemClickListener(this);
        addRandomPics();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (thumbList.get(position).getState() == 0)
            thumbList.get(position).setState(1);
        else
            thumbList.get(position).setState(0);
        adapter.notifyDataSetChanged();
        Log.i("*****Clicked*****",thumbList.get(position).getName());
    }

    @OnClick(R.id.save_btn)
    public void saveClicked(View view) {
        for (PicObject e: thumbList) {
            if(e.getState() == 1) {
                db.execSQL("INSERT INTO PicsTable VALUES('"+ e.getName() + "') ;");
                Log.i("*****Saved*****", e.getName());
            }
        }
        Toast.makeText(MainActivity.this, "Photos Saved", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.myphotos_btn)
    public void myPhotoClicked(View view) {
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
                Log.i("*****SHOW*****", url);
            } while (resultSet.moveToNext());
            if (!resultSet.isClosed()) {
                resultSet.close();
            }

            Intent myPhoto = new Intent(MainActivity.this, PhotoActivity.class);
            startActivity(myPhoto);

        } else {
            Log.i("*****SHOW*****", "EMPTY!!");
            Toast.makeText(MainActivity.this, "My Photo is empty!", Toast.LENGTH_LONG).show();
            return;
        }

        // Remove clicked state
        for (PicObject e: thumbList) {
            e.setState(0);
        }
        adapter.notifyDataSetChanged();
    }

    private void addRandomPics() {
        Call<List<PicResponse>> picResponses = apiService.getPic();
        picResponses.enqueue(new Callback<List<PicResponse>>() {
            @Override
            public void onResponse(Call<List<PicResponse>> call, Response<List<PicResponse>> response) {
                //Log.i("Log:********", String.valueOf(response.body().size()));
                List<PicResponse> responses = response.body();
                for (PicResponse e: responses) {
                    thumbList.add(new PicObject(e.getThumb(), 0));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<PicResponse>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API Failure or out of Limit", Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.random_btn)
    public void randomClicked(View view) {
        emptyPics();
        addRandomPics();
        adapter.notifyDataSetChanged();
    }


    public void addSearchPics(String keyword) {
        Call<SearchResponse> searchResponse = apiService.getSearchResult(keyword);
        searchResponse.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                ArrayList<String> set = response.body().getThumbSet();
                Toast.makeText(MainActivity.this, "Found "+ set.size() +" images" , Toast.LENGTH_LONG).show();
                emptyPics();
                for (String e: set) {
                    thumbList.add(new PicObject(e, 0));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API Failure or out of Limit", Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.search_btn)
    public void searchClicked(View view) {
        emptyPics();
        addSearchPics(input.getText().toString());
        adapter.notifyDataSetChanged();
        hideKeyboard(this);
    }

    public void emptyPics() {
        if (thumbList.size() > 0)
            thumbList.clear();
    }

    // Hide soft keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        // Find the currently focused view
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
