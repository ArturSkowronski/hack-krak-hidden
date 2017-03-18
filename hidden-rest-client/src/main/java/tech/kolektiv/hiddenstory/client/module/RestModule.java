package tech.kolektiv.hiddenstory.client.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RestModule {

  @Provides
  @Singleton
  Retrofit retrofitProvider() {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://hiddenstory.kolektiv.tech:3000/")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    return retrofit;
  }

}