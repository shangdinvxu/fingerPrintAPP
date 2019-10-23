package com.fgtit;

import java.util.List;

public class EmployeeBean {
    /**
     * jsonrpc : 2.0
     * id : null
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
{"res_model":"hr.employee","child_ids":[],"show_amount":true,"partner_id":7454,"id":543,"name":"Locus.gao 高玮玮","level":6,113,"id":704,"pId":480},{"res_model":"hr.employee","child_ids":[],"show_amount":true,"partner_id":42651,"id":705,"name":"Chunying.yu 于春影","level":7,"partner_name":"于春影","open":false,"pId":704,"is_manager":true,"res_id":10296}]
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
             * child_ids : []
             * show_amount : true
             * name : 江苏若态科技有限公司
             * open : true
             * level : 1
             * res_model : res.company
             * res_id : 1
             * id : 1
             * pId : 0
             * partner_id : 7421
             * partner_name : 郭华根
             * is_manager : true
             */

            private boolean show_amount;
            private String name;
            private boolean open;
            private int level;
            private String res_model;
            private int res_id;
            private int id;
            private int pId;
            private int partner_id;
            private String partner_name;
            private boolean is_manager;
            private List<?> child_ids;
            private String fg1;
            private String fg2;
            private String fg3;

            public int getpId() {
                return pId;
            }

            public void setpId(int pId) {
                this.pId = pId;
            }

            public String getWorker_code() {
                return worker_code;
            }

            public void setWorker_code(String worker_code) {
                this.worker_code = worker_code;
            }

            private String worker_code;
            private String wx_open_id;
            private String employee_avatar;

            public String getEmployee_avatar() {
                return employee_avatar;
            }

            public void setEmployee_avatar(String employee_avatar) {
                this.employee_avatar = employee_avatar;
            }

            public String getFg1() {
                return fg1;
            }

            public void setFg1(String fg1) {
                this.fg1 = fg1;
            }

            public String getFg2() {
                return fg2;
            }

            public void setFg2(String fg2) {
                this.fg2 = fg2;
            }

            public String getFg3() {
                return fg3;
            }

            public void setFg3(String fg3) {
                this.fg3 = fg3;
            }

            public String getWx_open_id() {
                return wx_open_id;
            }

            public void setWx_open_id(String wx_open_id) {
                this.wx_open_id = wx_open_id;
            }

            public boolean isShow_amount() {
                return show_amount;
            }

            public void setShow_amount(boolean show_amount) {
                this.show_amount = show_amount;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public boolean isOpen() {
                return open;
            }

            public void setOpen(boolean open) {
                this.open = open;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public String getRes_model() {
                return res_model;
            }

            public void setRes_model(String res_model) {
                this.res_model = res_model;
            }

            public int getRes_id() {
                return res_id;
            }

            public void setRes_id(int res_id) {
                this.res_id = res_id;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPId() {
                return pId;
            }

            public void setPId(int pId) {
                this.pId = pId;
            }

            public int getPartner_id() {
                return partner_id;
            }

            public void setPartner_id(int partner_id) {
                this.partner_id = partner_id;
            }

            public String getPartner_name() {
                return partner_name;
            }

            public void setPartner_name(String partner_name) {
                this.partner_name = partner_name;
            }

            public boolean isIs_manager() {
                return is_manager;
            }

            public void setIs_manager(boolean is_manager) {
                this.is_manager = is_manager;
            }

            public List<?> getChild_ids() {
                return child_ids;
            }

            public void setChild_ids(List<?> child_ids) {
                this.child_ids = child_ids;
            }
        }
    }
}
