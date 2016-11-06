package cs175.randompics.RetrofitForUnsplash;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pei Liu on 16/10/1.
 */
public class RetrofitHelper {

    public static final String BASE_URL = "https://api.unsplash.com/";
    private static Retrofit retrofit;

    public static void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static UnsplashService getService() {
        return retrofit.create(UnsplashService.class);
    }
}
