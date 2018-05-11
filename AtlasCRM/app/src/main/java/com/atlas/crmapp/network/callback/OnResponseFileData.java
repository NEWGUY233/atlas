package com.atlas.crmapp.network.callback;

import android.support.annotation.Nullable;

import com.lzy.okgo.request.BaseRequest;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by hoda_fang on 2017/6/2.
 */

public  abstract class  OnResponseFileData{
    public abstract void onBefore(BaseRequest request);

    public abstract void onSuccess(File file, Call call, Response response);

    public abstract void onError(Call call, @Nullable Response response, @Nullable Exception e);

    public  abstract  void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed);
}