package com.atlas.crmapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hoda on 2017/6/21.
 */

public class CoachsListJson implements Serializable{

    public int recordsFiltered;
    public int draw;
    public int recordsTotal;
    public List<CoachJson> rows;
    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public List<CoachJson> getRows() {
        return rows;
    }

    public void setRows(List<CoachJson> rows) {
        this.rows = rows;
    }


}
