package com.fgtit.http;

import com.fgtit.CanteenBean;
import com.fgtit.CanteenTotalBean;
import com.fgtit.ClearMessageBean;
import com.fgtit.EatLocationBean;
import com.fgtit.EmployeeBean;
import com.fgtit.EmployeeDetailBean;
import com.fgtit.ErrorBean;
import com.fgtit.FingerSuccessBean;
import com.fgtit.FingerTimeBean;
import com.fgtit.LoginResponse;
import com.fgtit.ServerTimeBean;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PosApi {
    /**
     * 获取员工列表
     * */
    @POST("get_all_department_tree")
    Call<EmployeeBean> getAllDepartmentTree(@Body HashMap hashMap);


    /**
     * 获取员工列表
     * */
    @POST("get_all_department_tree_simple")
    Call<EmployeeBean> getAllDepartmentTreeSimple(@Body HashMap hashMap);




    /**
     * 上传用户信息
     * */
    @POST("update_employee_fg")
    Call<FingerSuccessBean> update_employee_fg(@Body HashMap hashMap);


    /**
     * 获取某个员工详细的信息
     * */
    @POST("get_fg_employee_detail")
    Call<EmployeeDetailBean> get_fg_employee_detail(@Body HashMap hashMap);


    /**
     * 打卡
     * */
    @POST("new_fg_attendance")
    Call<FingerTimeBean> new_fg_attendance(@Body HashMap hashMap);

    //登录
    @POST("login_pos")
    Call<LoginResponse> login_pos(@Body HashMap hashMap);


    /**
     * 根据imei获取相对应的食堂
     * */
    @POST("get_canteen_by_imei")
    Call<EatLocationBean> get_canteen_by_imei(@Body HashMap hashMap);


    /**
     * 上传食堂打卡
     * */
    @POST("new_fg_canteen")
    Call<CanteenBean> new_fg_canteen(@Body HashMap hashMap);


    /**
     * 获取当前时间
     * */
    @POST("get_now_server_time")
    Call<ServerTimeBean> get_now_server_time(@Body HashMap hashMap);


    /**
     * 清空用户信息
     * */
    @POST("clean_employee_fg")
    Call<ClearMessageBean> clean_employee_fg(@Body HashMap hashMap);

    /**
     * 同步食堂打卡记录
     * */
    @POST("new_synchronize_canteen_data")
    Call<CanteenBean> synchronize_canteen_data(@Body HashMap hashMap);

    /**
     * 同步食堂打卡记录
     * */
    @POST("get_user_is_canteen_manager_pos")
    Call<CanteenTotalBean> get_user_is_canteen_manager_pos(@Body HashMap hashMap);


    /**
     * fir 请求
     * */
    @GET("5c9493ffca87a819945c70c1")
    Call<FirBean> getFirVersion(@Query("api_token") String token );


}
