package com.mieczkowskidev.padawanapp;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Patryk Mieczkowski on 14.05.16.
 */
public interface SwapiService {

    @GET("people/")
    Call<ResultsResponse> getPeople();

    @GET("people/")
    rx.Observable<ResultsResponse> getFullPeople(@Query("page") String pageNumber);

}
