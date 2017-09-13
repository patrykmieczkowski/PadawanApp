package com.mieczkowskidev.padawanapp;


import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Patryk Mieczkowski on 14.05.16.
 */
public interface SwapiService {

    @GET("people/")
    Call<ResultsResponse> getPeople();

    @GET("people/")
    Single<MainResponse> getPersonList();

}
