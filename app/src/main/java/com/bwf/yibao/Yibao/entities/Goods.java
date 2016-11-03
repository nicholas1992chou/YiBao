package com.bwf.yibao.Yibao.entities;

import java.io.Serializable;
import java.util.List;
public class Goods implements Serializable{
	private String calssify;
	private String gName;
	private int goodsId;
	private int gooldsId;
	private long imageId;
	private String goodsName;
	private String goodsinfo;
	private List<String> imagePath;
	private List<String> imagepath;
	private float originalprice;
	private float Originalprice;
	private String phone;
	private float price;
	private String qq;
	private int state;
	private String time;
	public int vote;
	
	public Goods(String calssify, String gName, int goodsId, String goodsName,
			String goodsinfo, List<String> imagePath, float originalprice,
			String phone, float price, String qq, int state, String time) {
		super();
		this.calssify = calssify;
		this.gName = gName;
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.goodsinfo = goodsinfo;
		this.imagePath = imagePath;
		this.originalprice = originalprice;
		this.phone = phone;
		this.price = price;
		this.qq = qq;
		this.state = state;
		this.time = time;

	}
	
	public Goods(String calssify, String gName, long imageId, int goodsId, String goodsName,
			String goodsinfo, List<String> imagePath, float originalprice,
			String phone, float price, String qq, int state, String time) {
		this(calssify, gName, goodsId, goodsName, goodsinfo, imagePath, originalprice, phone, price, qq, state, time);
		this.imageId = imageId;
	}
	
	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public String getCalssify() {
		return calssify;
	}
	public void setCalssify(String calssify) {
		this.calssify = calssify;
	}
	public String getgName() {
		return gName;
	}
	public void setgName(String gName) {
		this.gName = gName;
	}
	public int getGoodsId() {
		return (goodsId == 0)?gooldsId:goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsinfo() {
		return goodsinfo;
	}
	public void setGoodsinfo(String goodsinfo) {
		this.goodsinfo = goodsinfo;
	}
	public List<String> getImagePath() {
		return (imagePath == null)?imagepath:imagePath;
	}
	public void setImagePath(List<String> imagePath) {
		this.imagePath = imagePath;
	}
	public float getOriginalprice() {
		return (originalprice == 0)?Originalprice:originalprice;
	}
	public void setOriginalprice(float originalprice) {
		this.originalprice = originalprice;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Goods [calssify=" + calssify + ", gName=" + gName
				+ ", goodsId=" + goodsId + ", goodsName=" + goodsName
				+ ", goodsinfo=" + goodsinfo + ", imagePath=" + imagePath
				+ ", originalprice=" + originalprice + ", phone=" + phone
				+ ", price=" + price + ", qq=" + qq + ", state=" + state
				+ ", time=" + time + "]";
	}

}
