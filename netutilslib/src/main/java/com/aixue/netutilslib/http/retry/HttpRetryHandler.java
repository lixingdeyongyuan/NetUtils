package com.aixue.netutilslib.http.retry;


import com.aixue.netutilslib.log.NetLogProxy;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.HttpException;

public class HttpRetryHandler implements Function<Observable<Throwable>, ObservableSource<?>> {
    private int maxRetryCount;
    private int retryCount = 0;

    public HttpRetryHandler(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    @Override
    public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                boolean needRetry = false;
                if (retryCount < maxRetryCount) {
                    if (throwable instanceof IOException) {
                        needRetry = true;
                    } else if (throwable instanceof HttpException) {
                        if (((HttpException) throwable).code() != 200) {
                            needRetry = true;
                        }
                    }
                }

                if (needRetry) {
                    retryCount++;
                    NetLogProxy.getInstance().info("HttpRetryHandler", "retry " + retryCount + "/" + maxRetryCount + ", " + throwable);
                    return Observable.timer((long) Math.pow(2, retryCount), TimeUnit.SECONDS);
                } else {
                    return Observable.error(throwable);
                }
            }
        });
    }
}
