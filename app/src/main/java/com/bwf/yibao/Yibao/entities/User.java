package com.bwf.yibao.Yibao.entities;

import java.io.Serializable;

public class User implements Serializable{
	private int id;
	private String userName;
	private String pwd;
	private String country;
	private String province;
	private String city;
	private String district;
	private String street;
	private String streetNumber;
	private String phone;
	private String qq;
	public String profile_image;
	public User(){
		
	}
	
	public User(String userName, String pwd){
		this.userName = userName;
		this.pwd = pwd;
	}
	
	public User(String userName, String pwd, String country, String province,
			String city, String district, String street, String streetNumber,
			String phone, String qq) {
		this(userName, pwd);
		this.country = country;
		this.province = province;
		this.city = city;
		this.district = district;
		this.street = street;
		this.streetNumber = streetNumber;
		this.phone = phone;
		this.qq = qq;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreetNumber() {
		return streetNumber;
	}
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	@Override
	public String toString() {
		return "User [userName=" + userName + ", pwd=" + pwd + ", country="
				+ country + ", province=" + province + ", city=" + city
				+ ", district=" + district + ", street=" + street
				+ ", streetNumber=" + streetNumber + ", phone=" + phone
				+ ", qq=" + qq + "]";
	}
	
}
