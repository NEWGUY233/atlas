package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 * Created by Alex on 2017/3/16.
 */

public class ZPResponse<T> implements Serializable {
    public int errorCode;
    public String message;
    public T data;
}
