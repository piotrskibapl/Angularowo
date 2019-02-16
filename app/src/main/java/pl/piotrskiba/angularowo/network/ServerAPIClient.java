package pl.piotrskiba.angularowo.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerAPIClient {

    public static final String API_KEY = "ySrfs3nAqaw28D8W1KJPz3HT8IAydXnY";

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://piotrskiba.pl/angularowo/api/v1/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
