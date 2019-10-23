package com.fgtit;

public class EmployeeDetailBean {

    /**
     * jsonrpc : 2.0
     * id : null
     * result : {"res_data":{"rt_employee_fingerprint1":"","rt_employee_fingerprint2":"","rt_employee_fingerprint3":"","name":"赵耀耀","wx_open_id":"","employee_ava":"http://192.168.2.10:8081/linkloving_app_api/get_worker_image?worker_id=7842&model=hr.employee&field=image_medium&time=1552985724.0"},"res_msg":"","erp_time":null,"res_code":1}
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
         * res_data : {"rt_employee_fingerprint1":"","rt_employee_fingerprint2":"","rt_employee_fingerprint3":"","name":"赵耀耀","wx_open_id":"","employee_ava":"http://192.168.2.10:8081/linkloving_app_api/get_worker_image?worker_id=7842&model=hr.employee&field=image_medium&time=1552985724.0"}
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
             * rt_employee_fingerprint1 :
             * rt_employee_fingerprint2 :
             * rt_employee_fingerprint3 :
             * name : 赵耀耀
             * wx_open_id :
             * employee_ava : http://192.168.2.10:8081/linkloving_app_api/get_worker_image?worker_id=7842&model=hr.employee&field=image_medium&time=1552985724.0
             */

            private String rt_employee_fingerprint1;
            private String rt_employee_fingerprint2;
            private String rt_employee_fingerprint3;
            private String name;
            private String wx_open_id;
            private String employee_ava;

            public String getWorker_code() {
                return worker_code;
            }

            public void setWorker_code(String worker_code) {
                this.worker_code = worker_code;
            }

            private String worker_code;
            private int employee_id;

            public int getEmployee_id() {
                return employee_id;
            }

            public void setEmployee_id(int employee_id) {
                this.employee_id = employee_id;
            }

            public String getRt_employee_fingerprint1() {
                return rt_employee_fingerprint1;
            }

            public void setRt_employee_fingerprint1(String rt_employee_fingerprint1) {
                this.rt_employee_fingerprint1 = rt_employee_fingerprint1;
            }

            public String getRt_employee_fingerprint2() {
                return rt_employee_fingerprint2;
            }

            public void setRt_employee_fingerprint2(String rt_employee_fingerprint2) {
                this.rt_employee_fingerprint2 = rt_employee_fingerprint2;
            }

            public String getRt_employee_fingerprint3() {
                return rt_employee_fingerprint3;
            }

            public void setRt_employee_fingerprint3(String rt_employee_fingerprint3) {
                this.rt_employee_fingerprint3 = rt_employee_fingerprint3;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getWx_open_id() {
                return wx_open_id;
            }

            public void setWx_open_id(String wx_open_id) {
                this.wx_open_id = wx_open_id;
            }

            public String getEmployee_ava() {
                return employee_ava;
            }

            public void setEmployee_ava(String employee_ava) {
                this.employee_ava = employee_ava;
            }
        }
    }
}
