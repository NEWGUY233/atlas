package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 * Created by Jason on 2017/4/23.
 */

public class ContractJson implements Serializable{

    private static final long serialVersionUID = 7207750413582978375L;
    public long id;
    public String code;
    public String type;
    public long startTime;
    public long endTime;
    public String state;
    public long companyId;
    public String companyName;
}
