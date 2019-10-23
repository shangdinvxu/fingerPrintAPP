package com.fgtit;

public class CanteenBean {
    /**
     * jsonrpc : 2.0
     * id : null
     * result : {"res_msg":"","erp_time":null,"res_code":1}
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
         * res_msg :
         * erp_time : null
         * res_code : 1
         */

        private String res_msg;
        private Object erp_time;
        private int res_code;

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
    }
}
