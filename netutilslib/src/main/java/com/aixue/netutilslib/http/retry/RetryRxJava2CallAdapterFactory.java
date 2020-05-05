package com.aixue.netutilslib.http.retry;


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetryRxJava2CallAdapterFactory extends CallAdapter.Factory {
    private RxJava2CallAdapterFactory factory;

    public static RetryRxJava2CallAdapterFactory create(RxJava2CallAdapterFactory factory) {
        return new RetryRxJava2CallAdapterFactory(factory);
    }

    private RetryRxJava2CallAdapterFactory(RxJava2CallAdapterFactory factory) {
        this.factory = factory;
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        CallAdapter callAdapter = factory.get(returnType, annotations, retrofit);
        if (callAdapter != null) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof Retry) {
                    return new RetryRxJava2CallAdapter<>(callAdapter, ((Retry) annotation).retryCount());
                }
            }
        }
        return callAdapter;
    }

    private class RetryRxJava2CallAdapter<R> implements CallAdapter<R, Object> {
        private CallAdapter callAdapter;
        private int maxRetryCount;

        private RetryRxJava2CallAdapter(CallAdapter callAdapter, int maxRetryCount) {
            this.callAdapter = callAdapter;
            this.maxRetryCount = maxRetryCount;
        }

        @Override
        public Type responseType() {
            return callAdapter.responseType();
        }

        @Override
        public Object adapt(Call call) {
            Object original = callAdapter.adapt(call);


            if (original instanceof Observable) {
                if (isBodyObservable()) {
                    return ((Observable<?>) original).retryWhen(new HttpRetryHandler(maxRetryCount));
                } else {
                    return original;
                }
            } else {
                return original;
            }
        }

        private boolean isBodyObservable() {
            Class<?> rawObservableType = getRawType(callAdapter.responseType());
            if (rawObservableType == Response.class
                    || rawObservableType == ResponseBody.class
                    ) {
                return false;
            } else {
                return true;
            }
        }
    }
}
