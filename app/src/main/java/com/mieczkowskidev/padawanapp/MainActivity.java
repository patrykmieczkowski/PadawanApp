package com.mieczkowskidev.padawanapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    List<Person> rxFullList = new ArrayList<>();

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String URL = "http://www.swapi.co/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

//        prepareRetrofit();
        prepareFullDownload();

    }

    private void prepareRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SwapiService swapiService = retrofit.create(SwapiService.class);

        swapiService.getPeople().enqueue(new Callback<ResultsResponse>() {
            @Override
            public void onResponse(Call<ResultsResponse> call, final Response<ResultsResponse> response) {

                if (response.body().results != null && !response.body().results.isEmpty()) {

                    Log.d(TAG, "onResponse() " + response.body().results.size());
                    Log.d(TAG, "onResponse() INFO " + call.request().url());

                    for (Person person : response.body().results) {
                        Log.d(TAG, "onResponse person: " + person.name);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            prepareRecyclerView(response.body().results);
                        }
                    });
                } else {
                    Log.e(TAG, "onResponse() list is empty or null :(");
                }

            }

            @Override
            public void onFailure(Call<ResultsResponse> call, Throwable t) {
                Log.e(TAG, "onFailure()", t);
            }
        });
    }

    private void prepareFullDownload() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        final SwapiService swapiService = retrofit.create(SwapiService.class);

        String[] myItems = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};

        Observable
                .from(Arrays.asList(myItems))
                .flatMap(new Func1<String, Observable<ResultsResponse>>() {
                    @Override
                    public Observable<ResultsResponse> call(String s) {
                        Log.d(TAG, "call for " + s);
                        return swapiService.getFullPeople(s);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultsResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");

                        if (rxFullList != null && !rxFullList.isEmpty()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    prepareRecyclerView(rxFullList);
                                }
                            });
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError() " + e.getMessage(), e);

                    }

                    @Override
                    public void onNext(ResultsResponse resultsResponse) {
                        Log.d(TAG, "onNext response");

                        if (resultsResponse.results != null && !resultsResponse.results.isEmpty()) {
                            rxFullList.addAll(resultsResponse.results);
                        }

                    }
                });

    }

    private void prepareRecyclerView(List<Person> personList) {

        PersonAdapter personAdapter = new PersonAdapter(personList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(personAdapter);

    }
}
