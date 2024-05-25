package ru.mirea.solovevave.mireaproject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PostIndexsApiService {
    @GET("{country}/{postalCode}")
    Call<PostIndexResponse> getPostIndexInfo(@Path("country") String country, @Path("postalCode") String postalCode);
}
