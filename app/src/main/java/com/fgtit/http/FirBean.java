package com.fgtit.http;

/**
 * Created by Daniel.Xu on 2019-10-25.
 */
public class FirBean {

    /**
     * name : 若态助手
     * version : 13
     * changelog : 增加食堂打卡统计功能
     * updated_at : 1571794475
     * versionShort : 1.1.3
     * build : 13
     * installUrl : http://download.fir.im/apps/5c9493ffca87a819945c70c1/install?download_token=e48d66ad141e8554f499dedc7d988b54&source=update
     * install_url : http://download.fir.im/apps/5c9493ffca87a819945c70c1/install?download_token=e48d66ad141e8554f499dedc7d988b54&source=update
     * direct_install_url : http://download.fir.im/apps/5c9493ffca87a819945c70c1/install?download_token=e48d66ad141e8554f499dedc7d988b54&source=update
     * update_url : http://fir.im/fcql
     * binary : {"fsize":7206615}
     */

    private String name;
    private String version;
    private String changelog;
    private int updated_at;
    private String versionShort;
    private String build;
    private String installUrl;
    private String install_url;
    private String direct_install_url;
    private String update_url;
    private BinaryBean binary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getDirect_install_url() {
        return direct_install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public BinaryBean getBinary() {
        return binary;
    }

    public void setBinary(BinaryBean binary) {
        this.binary = binary;
    }

    public static class BinaryBean {
        /**
         * fsize : 7206615
         */

        private int fsize;

        public int getFsize() {
            return fsize;
        }

        public void setFsize(int fsize) {
            this.fsize = fsize;
        }
    }
}
