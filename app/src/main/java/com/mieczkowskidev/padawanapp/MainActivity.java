package com.mieczkowskidev.padawanapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

//    List<Person> rxFullList = new ArrayList<>();
    public static final String URL = "http://www.swapi.co/api/";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        prepareRetrofit();

    }

    private void prepareRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        SwapiService swapiService = retrofit.create(SwapiService.class);

        swapiService.getPersonList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MainResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull MainResponse mainResponse) {
                        prepareRecyclerView(mainResponse.results);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onResponse() list is empty or null :(");

                    }
                });

    }

    private void prepareRecyclerView(List<Person> personList) {
        Log.d(TAG, "prepareRecyclerView()");

        PersonAdapter personAdapter = new PersonAdapter(personList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(personAdapter);

    }



    // RX CHAIN TO DOWNLOAD IT ALL
//
//    private void prepareFullDownload() {
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//
//        final SwapiService swapiService = retrofit.create(SwapiService.class);
//
//        String[] myItems = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
//
//        Observable
//                .from(Arrays.asList(myItems))
//                .flatMap(new Func1<String, Observable<ResultsResponse>>() {
//                    @Override
//                    public Observable<ResultsResponse> call(String s) {
//                        Log.d(TAG, "call for " + s);
//                        return swapiService.getFullPeople(s);
//                    }
//                })
//                .subscribeOn(Schedulers.newThread())
////                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ResultsResponse>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d(TAG, "onCompleted()");
//
//                        if (rxFullList != null && !rxFullList.isEmpty()) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    prepareRecyclerView(rxFullList);
//                                }
//                            });
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, "onError() " + e.getMessage(), e);
//
//                    }
//
//                    @Override
//                    public void onNext(ResultsResponse resultsResponse) {
//                        Log.d(TAG, "onNext response");
//
//                        if (resultsResponse.results != null && !resultsResponse.results.isEmpty()) {
//                            rxFullList.addAll(resultsResponse.results);
//                        }
//
//                    }
//                });
//
//    }
}
