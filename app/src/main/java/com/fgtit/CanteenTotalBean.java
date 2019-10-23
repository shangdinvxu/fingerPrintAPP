package com.fgtit;

import java.util.List;

/**
 * Created by Daniel.Xu on 2019-10-22.
 */
public class CanteenTotalBean {

    /**
     * jsonrpc : 2.0
     * id : null
     * result : {"res_data":[{"name":"沭阳","detail_lines":[{"rt_real_total":0,"rt_need_total":0,"rt_start_time":"09:30","name":"午餐","rt_end_time":"16:30"},{"rt_real_total":0,"rt_need_total":0,"rt_start_time":"17:00","name":"晚餐","rt_end_time":"17:30"},{"rt_real_total":0,"rt_need_total":0,"rt_start_time":"23:00","name":"加班餐","rt_end_time":"23:59"}]},{"name":"庙头","detail_lines":[{"rt_real_total":0,"rt_need_total":0,"rt_start_time":"11:30","name":"午餐","rt_end_time":"17:30"},{"rt_real_total":0,"rt_need_total":0,"rt_start_time":"18:00","name":"晚餐","rt_end_time":"19:00"}]}],"res_msg":"","erp_time":null,"res_code":1}
     */

    private String jsonrpc;
    private Object id;
    private ResultBean result;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * res_data : [{"name":"沭阳","detail_lines":[{"rt_real_total":0,"rt_need_total":0,"rt_start_time":"09:30","name":"午餐","rt_end_time":"16:30"},{"rt_real_total":0,"rt_need_total":0,"rt_start_time":"17:00","name":"晚餐","rt_end_time":"17:30"},{"rt_real_total":0,"rt_need_total":0,"rt_start_time":"23:00","name":"加班餐","rt_end_time":"23:59"}]},{"name":"庙头","detail_lines":[{"rt_real_total":0,"rt_need_total":0,"rt_start_time":"11:30","name":"午餐","rt_end_time":"17:30"},{"rt_real_total":0,"rt_need_total":0,"rt_start_time":"18:00","name":"晚餐","rt_end_time":"19:00"}]}]
         * res_msg :
         * erp_time : null
         * res_code : 1
         */

        private String res_msg;
        private Object erp_time;
        private int res_code;
        private List<ResDataBean> res_data;

        public String getRes_msg() {
            return res_msg;
        }

        public void setRes_msg(String res_msg) {
            this.res_msg = res_msg;
        }

        public Object getErp_time() {
            return erp_time;
        }

        public void setErp_time(Object erp_time) {
            this.erp_time = erp_time;
        }

        public int getRes_code() {
            return res_code;
        }

        public void setRes_code(int res_code) {
            this.res_code = res_code;
        }

        public List<ResDataBean> getRes_data() {
            return res_data;
        }

        public void setRes_data(List<ResDataBean> res_data) {
            this.res_data = res_data;
        }

        public static class ResDataBean {
            /**
             * name : 沭阳
             * detail_lines : [{"rt_real_total":0,"rt_need_total":0,"rt_start_time":"09:30","name":"午餐","rt_end_time":"16:30"},{"rt_real_total":0,"rt_need_total":0,"rt_start_time":"17:00","name":"晚餐","rt_end_time":"17:30"},{"rt_real_total":0,"rt_need_total":0,"rt_start_time":"23:00","name":"加班餐","rt_end_time":"23:59"}]
             */

            private String name;
            private List<DetailLinesBean> detail_lines;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<DetailLinesBean> getDetail_lines() {
                return detail_lines;
            }

            public void setDetail_lines(List<DetailLinesBean> detail_lines) {
                this.detail_lines = detail_lines;
            }

            public static class DetailLinesBean {
                /**
                 * rt_real_total : 0
                 * rt_need_total : 0
                 * rt_start_time : 09:30
                 * name : 午餐
                 * rt_end_time : 16:30
                 */

                private int rt_real_total;
                private int rt_need_total;
                private String rt_start_time;
                private String name;
                private String rt_end_time;

                public int getRt_real_total() {
                    return rt_real_total;
                }

                public void setRt_real_total(int rt_real_total) {
                    this.rt_real_total = rt_real_total;
                }

                public int getRt_need_total() {
                    return rt_need_total;
                }

                public void setRt_need_total(int rt_need_total) {
                    this.rt_need_total = rt_need_total;
                }

                public String getRt_start_time() {
                    return rt_start_time;
                }

                public void setRt_start_time(String rt_start_time) {
                    this.rt_start_time = rt_start_time;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getRt_end_time() {
                    return rt_end_time;
                }

                public void setRt_end_time(String rt_end_time) {
                    this.rt_end_time = rt_end_time;
                }
            }
        }
    }
}
