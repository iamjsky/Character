package com.character.microblogapp.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.character.microblogapp.BuildConfig;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MError;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Net {

    private static String baseURL = BuildConfig.DEBUG ? Constant.SERVER_URL : Constant.SERVER_URL;

    Retrofit retrofit;
    private static Net net = null;
    public Api api;

    private Net() {
        HttpRequestInterceptor interceptor = new HttpRequestInterceptor();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);
    }

    public static Net instance() {
        if (net == null) {
            net = new Net();
        }

        return net;
    }

    public abstract static class BaseCallBack<T> {
        public abstract void onSuccess(T response);

        public abstract void onFailure(MError response);
    }

    public abstract static class ResponseCallBack<T> extends BaseCallBack<T> implements Callback<T> {

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (response.code() != 200) {
                MError error = new MError();
                error.resultcode = 500;
                onFailure(error);
            } else {
                if (((Response) response).body() != null) {
                    MBase result = (MBase) ((Response) response).body();
                    if (result != null && result.resultcode > 0) {
                        MError error = new MError();
                        error.resultcode = result.resultcode;
                        error.res_msg = result.res_msg;
                        onFailure(error);
                    } else {
                        onSuccess(response.body());
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            MError error = new MError();
            error.resultcode = 500;
            onFailure(error);
        }

        @Override
        public void onSuccess(T response) {

        }

        @Override
        public void onFailure(MError response) {

        }
    }

    public static class EmptyCallback<T> implements Callback<T> {
        @Override
        public void onResponse(Call<T> call, Response<T> response) {
        }

        @Override
        public void onFailure(Call call, Throwable throwable) {
        }
    }

    private class HttpRequestInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            HttpUrl url = request.url().newBuilder().build();
//            Request newRequest = request.newBuilder()
//                    .url(url)
//                    .addHeader("x-" + Constants.HttpServiceName + "-application-id", Constants.HttpApplicationID)
//                    .addHeader("x-" + Constants.HttpServiceName + "-rest-api-key", Constants.HttpRestApiKey)
//                    .addHeader("x-access-token", MyInfo.getInstance().token)
//                    .addHeader("x-timezone", Constants.HttpTimeZone)
//                    .build();
//            return chain.proceed(newRequest);
            return chain.proceed(chain.request());
        }
    }
}
