package com.fgtit;

public class ServerTimeBean {
    /**
     * jsonrpc : 2.0
     * id : null
     * result : {"res_data":{"server_time":"2019-03-25 01:28:49"},"res_msg":"","erp_time":null,"res_code":1}
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
         * res_data : {"server_time":"2019-03-25 01:28:49"}
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
             * server_time : 2019-03-25 01:28:49
             */

            private String server_time;

            public String getServer_time() {
                return server_time;
            }

            public void setServer_time(String server_time) {
                this.server_time = server_time;
            }
        }
    }
}
