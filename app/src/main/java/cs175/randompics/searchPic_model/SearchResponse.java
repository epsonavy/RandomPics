package cs175.randompics.searchPic_model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pei Liu on 16/10/5.
 */
public class SearchResponse {

    List<ResultModel> results;

    public ArrayList<String> getThumbSet() {

        Log.i("****SIZE***", String.valueOf(results.size()));
        ArrayList<String> res = new ArrayList<>(20);
        for (ResultModel e: results) {
            res.add(e.urls.thumb);
        }
        return res;
    }
}
