package com.atlas.crmapp.network;

/**
 * Created by Harry on 2017-03-25.
 */

public abstract class OnResponseData {

//    public Class<T>getGenericClass() {
//        return (Class<T>) ((ParameterizedType) getClass()
//                .getGenericSuperclass()).getActualTypeArguments()[0];
//    }

    public abstract void onSuccess(String jsonData, int responseCode);

    public abstract void onError(DcnException error);
}
