package pl.piotrskiba.angularowo.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerAPIClient {
    // I know that the API key should not be present here, but as the game server has been closed,
    // the API is not usable anymore, and it's set up to return the same data every query
    public static final String API_KEY = "ySrfs3nAqaw28D8W1KJPz3HT8IAydXnY";

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://piotrskiba.pl/angularowo/api/v1/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            UnauthorizedInterceptor unauthorizedInterceptor = new UnauthorizedInterceptor();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(unauthorizedInterceptor)
                    .build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
