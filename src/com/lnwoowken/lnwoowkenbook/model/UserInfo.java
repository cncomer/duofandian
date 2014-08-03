package com.lnwoowken.lnwoowkenbook.model;

public class UserInfo {
	private int id;
	private String sid;//--无用
	private String userName;//--用户名
	private String pwd;//--密码
	private String name;//--昵称
	private String phoneNum;//--电话
	private String eMail;//--E-Mail
	private String login;//--登录状态
	private String createDate;
	private String state;//--当前状态
	private String rights;//无用
	private String version;//--版本号
	private String cu;
	private String errorconn;//--非法次数
	private String errorlogin;//--登录错误数
	private String lastlogin;
	private String vcode;//--登录验证号
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRights() {
		return rights;
	}
	public void setRights(String rights) {
		this.rights = rights;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCu() {
		return cu;
	}
	public void setCu(String cu) {
		this.cu = cu;
	}
	public String getErrorconn() {
		return errorconn;
	}
	public void setErrorconn(String errorconn) {
		this.errorconn = errorconn;
	}
	public String getErrorlogin() {
		return errorlogin;
	}
	public void setErrorlogin(String errorlogin) {
		this.errorlogin = errorlogin;
	}
	public String getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(String lastlogin) {
		this.lastlogin = lastlogin;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	

}
