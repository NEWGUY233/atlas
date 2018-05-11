package com.atlas.crmapp.network;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jason on 2017/5/8.
 */

public class RowsResponse<T> {

    public int recordsFiltered;
    public int draw;
    public int recordsTotal;
    public List<T> rows;

    private static ParameterizedType getParameterizedType(final Class<?> raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    public static ParameterizedType getType(final Class<?> cls) {
        return getParameterizedType(RowsResponse.class, cls);
    }
}
