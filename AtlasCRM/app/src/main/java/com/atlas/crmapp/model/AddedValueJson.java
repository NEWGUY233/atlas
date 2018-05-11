package com.atlas.crmapp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by hoda on 2017/12/25.
 */

public class AddedValueJson  implements Serializable{

    private static final long serialVersionUID = 4433970983784373835L;
    private long id;
    private String name;
    private List<Combos> combos;
    private boolean isSelect;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Combos> getCombos() {
        return combos;
    }

    public void setCombos(List<Combos> combos) {
        this.combos = combos;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public class Combos implements  Serializable{
        private long id;
        private String name;
        private String type;
        private long value;
        private BigDecimal price;
        private String peroid;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getPeroid() {
            return peroid;
        }

        public void setPeroid(String peroid) {
            this.peroid = peroid;
        }
    }
}
