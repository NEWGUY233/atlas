package com.atlas.crmapp.network;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * Created by Harry on 2017-03-22.
 */

public class JsonResponse<T> implements Serializable {

    private static final long serialVersionUID = 3013230836175987851L;
    public int errorCode;
    public String message;
    public T data;

}
