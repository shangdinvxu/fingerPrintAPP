package com.fgtit.multilevelTreeList;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Node {

    private int res_id;
    private String res_model;
    private String fg1;
    private String fg2;
    private String fg3;
    private String wx_open_id;

    public String getWorker_code() {
        return worker_code;
    }

    public void setWorker_code(String worker_code) {
        this.worker_code = worker_code;
    }

    private String worker_code;

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

    public int getRes_id() {
        return res_id;
    }

    public void setRes_id(int res_id) {
        this.res_id = res_id;
    }

    public String getRes_model() {
        return res_model;
    }

    public void setRes_model(String res_model) {
        this.res_model = res_model;
    }
    /**
     * 传入的实体对象
     */
//    public B bean;
    /**
     * 设置开启 关闭的图片
     */
    public int iconExpand=-1, iconNoExpand = -1;

    private int id;
    /**
     * 根节点pId为0
     */
    private int pId ;

    private String name;

    private String employee_avatar;

    public String getEmployee_avatar() {
        return employee_avatar;
    }

    public void setEmployee_avatar(String employee_avatar) {
        this.employee_avatar = employee_avatar;
    }

    /**
     * 当前的级别
     */
    private int level;

    /**
     * 是否展开
     */
    private boolean isExpand = false;

    private int icon = -1;

    /**
     * 下一级的子Node
     */
    private List<Node> children = new ArrayList<>();

    /**
     * 父Node
     */
    private Node parent;
//    /**
//     * 是否被checked选中
//     */
//    private boolean isChecked;
//    /**
//     * 是否为新添加的
//     */
//    public boolean isNewAdd = true;

//    public boolean isChecked() {
//        return isChecked;
//    }
//
//    public void setChecked(boolean isChecked) {
//        this.isChecked = isChecked;
//    }


    @Override
    public String toString() {
        return name;
    }

    public Node() {}

    public Node(int id, int pId, String name, int res_id, String res_model, String fg1, String fg2, String fg3,String worker_code,
                String wx_open_id, String employee_avatar) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.res_id = res_id;
        this.res_model = res_model;
        this.fg1 = fg1;
        this.fg2 = fg2;
        this.fg3 = fg3;
        this.worker_code = worker_code;
        this.wx_open_id = wx_open_id;
        this.employee_avatar = employee_avatar;
    }
//    public Node(T id, T pId, String name,B bean) {
//        super();
//        this.id = id;
//        this.pId = pId;
//        this.name = name;
//        this.bean = bean;
//    }


    public int getIcon()
    {
        return icon;
    }

    public void setIcon(int icon)
    {
        this.icon = icon;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getpId()
    {
        return pId;
    }

    public void setpId(int pId)
    {
        this.pId = pId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * 是否为跟节点
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     *
     * @return
     */
    public boolean isParentExpand() {
        if (parent == null)
            return false;
        return parent.isExpand();
    }

    /**
     * 是否是叶子界点
     *
     * @return
     */
    public boolean isLeaf()
    {
        return children.size() == 0;
    }

    /**
     * 获取level
     */
    public int getLevel() {

        return parent == null ? 0 : parent.getLevel() + 1;
    }

    /**
     * 设置展开
     *
     * @param isExpand
     */
    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (!isExpand) {

            for (Node node : children) {
                node.setExpand(isExpand);
            }
        }
    }

}
