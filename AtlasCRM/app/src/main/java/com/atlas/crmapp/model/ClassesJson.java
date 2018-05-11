package com.atlas.crmapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jason on 2017/4/21.
 */

public class ClassesJson implements Serializable {

    private static final long serialVersionUID = -1561768915346979889L;
    public int recordsFiltered;
    public int draw;
    public int recordsTotal;
    public List<ClassJson> rows;
}
