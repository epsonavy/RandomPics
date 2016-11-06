package cs175.randompics.RetrofitForUnsplash;

import java.util.List;

import cs175.randompics.randomPic_model.PicResponse;
import cs175.randompics.searchPic_model.SearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Pei Liu on 16/10/1.
 */

public interface UnsplashService {
    // Backup API Key
    // @GET("photos/random/?client_id=4657bce0528447a3d3b313bf013f8daefcf025547c4e0e13ed80e726e4db8d09")
    @GET("photos/random/?client_id=c2eec0aeffbc275617287837249c98cb80a9c5a22d87a8999e10541f3dc603f7&count=10")
    Call<List<PicResponse>> getPic();

    @GET("search/photos?access_token=ec56791e9d92b4e987eb300e7b33942d72d71a303b75495bec25b4788de83393")
    Call<SearchResponse> getSearchResult(@Query("query") String keyword);
}

