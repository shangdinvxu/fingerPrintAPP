package com.fgtit;

import java.util.List;

public class EatLocationBean {

    /**
     * jsonrpc : 2.0
     * id : null
     * result : {"res_data":{"name":"沭阳1号指纹机","time_data":[{"start_time":"11:30","name":"午餐","end_time":"12:30"},{"start_time":"17:30","name":"晚餐","end_time":"17:20"}]},"res_msg":"","erp_time":null,"res_code":1}
     */

    private String jsonrpc;
    private Object id;
    private ResultBean result;
    private ErrorBean error;

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

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
         * res_data : {"name":"沭阳1号指纹机","time_data":[{"start_time":"11:30","name":"午餐","end_time":"12:30"},{"start_time":"17:30","name":"晚餐","end_time":"17:20"}]}
         * res_msg :
         * erp_time : null
         * res_code : 1
         */

        private ResDataBean res_data;
        private String res_msg;
        private Object erp_time;
        private int res_code;

        public ResDataBean getRes_data() {
            return res_data;
        }

        public void setRes_data(ResDataBean res_data) {
            this.res_data = res_data;
        }

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

        public static class ResDataBean {
            /**
             * name : 沭阳1号指纹机
             * time_data : [{"start_time":"11:30","name":"午餐","end_time":"12:30"},{"start_time":"17:30","name":"晚餐","end_time":"17:20"}]
             */

            private String name;
            private List<TimeDataBean> time_data;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<TimeDataBean> getTime_data() {
                return time_data;
            }

            public void setTime_data(List<TimeDataBean> time_data) {
                this.time_data = time_data;
            }

            public static class TimeDataBean {
                /**
                 * start_time : 11:30
                 * name : 午餐
                 * end_time : 12:30
                 */

                private String start_time;
                private String name;
                private String end_time;

                public String getStart_time() {
                    return start_time;
                }

                public void setStart_time(String start_time) {
                    this.start_time = start_time;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getEnd_time() {
                    return end_time;
                }

                public void setEnd_time(String end_time) {
                    this.end_time = end_time;
                }
            }
        }
    }
}
