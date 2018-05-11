package com.atlas.crmapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/30.
 */

public class Test {
    /**
     * respCode : 0
     * respMsg : 成功
     * illegalJudgment : 1
     * illegalBook : 1
     * illegalReasonLists : [{"illegalDesc":"您预订的航班折扣为10.0折，违反了机票正常须预订9.0折以下航班的规定","illegalReasons":[]},{"illegalDesc":"您预订的航班日期为2018-03-30，违反了机票预定正常须至少提前5天预订的规定","illegalReasons":[]}]
     */

    private int respCode;
    private String respMsg;
    private String illegalJudgment;
    private String illegalBook;
    private List<IllegalReasonListsBean> illegalReasonLists;

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getIllegalJudgment() {
        return illegalJudgment;
    }

    public void setIllegalJudgment(String illegalJudgment) {
        this.illegalJudgment = illegalJudgment;
    }

    public String getIllegalBook() {
        return illegalBook;
    }

    public void setIllegalBook(String illegalBook) {
        this.illegalBook = illegalBook;
    }

    public List<IllegalReasonListsBean> getIllegalReasonLists() {
        return illegalReasonLists;
    }

    public void setIllegalReasonLists(List<IllegalReasonListsBean> illegalReasonLists) {
        this.illegalReasonLists = illegalReasonLists;
    }

    public static class IllegalReasonListsBean {
        /**
         * illegalDesc : 您预订的航班折扣为10.0折，违反了机票正常须预订9.0折以下航班的规定
         * illegalReasons : []
         */

        private String illegalDesc;
        private List<?> illegalReasons;

        public String getIllegalDesc() {
            return illegalDesc;
        }

        public void setIllegalDesc(String illegalDesc) {
            this.illegalDesc = illegalDesc;
        }

        public List<?> getIllegalReasons() {
            return illegalReasons;
        }

        public void setIllegalReasons(List<?> illegalReasons) {
            this.illegalReasons = illegalReasons;
        }
    }
}
