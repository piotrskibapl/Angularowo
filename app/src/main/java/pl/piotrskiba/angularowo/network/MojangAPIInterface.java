package pl.piotrskiba.angularowo.network;

import pl.piotrskiba.angularowo.models.MojangProfile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MojangAPIInterface {

    @GET("users/profiles/minecraft/{username}")
    Call<MojangProfile> getProfile(@Path("username") String username);
}
