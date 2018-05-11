package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 * Created by Harry on 2017-03-28.
 */

public class CompnayInfoJson implements Serializable {
    private static final long serialVersionUID = 7418650304025042538L;
    public long id;
    public String name;
    public String contact;
    public String phone;
    public String fax;
    public String city;
    public String address;
    public String thumbnail;
    public String description;
    public long contractId;
}
