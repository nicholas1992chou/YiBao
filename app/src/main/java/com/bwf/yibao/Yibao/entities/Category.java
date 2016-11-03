package com.bwf.yibao.Yibao.entities;

import java.util.List;

public class Category {
	//一级类别数量
	private int sortcount;
	//一级类别名称
	private String sortkey;
	//二级分类
	private List<Secondary> sortval;
	
	public Category(int sortcount, String sortkey, List<Secondary> sortval) {
		super();
		this.sortcount = sortcount;
		this.sortkey = sortkey;
		this.sortval = sortval;
	}

	public int getSortcount() {
		return sortcount;
	}

	public void setSortcount(int sortcount) {
		this.sortcount = sortcount;
	}

	public String getSortkey() {
		return sortkey;
	}

	public void setSortkey(String sortkey) {
		this.sortkey = sortkey;
	}

	public List<Secondary> getSortval() {
		return sortval;
	}

	public void setSortval(List<Secondary> sortval) {
		this.sortval = sortval;
	}

	@Override
	public String toString() {
		return "Category [sortcount=" + sortcount + ", sortkey=" + sortkey
				+ ", sortval=" + sortval + "]";
	}

	public static class Secondary{
		//二级分类名
		private String sortname;
		//二级分类数量
		private int sortnum;
		
		public Secondary(String sortname, int sortnum) {
			super();
			this.sortname = sortname;
			this.sortnum = sortnum;
		}
		public String getSortname() {
			return sortname;
		}
		public void setSortname(String sortname) {
			this.sortname = sortname;
		}
		public int getSortnum() {
			return sortnum;
		}
		public void setSortnum(int sortnum) {
			this.sortnum = sortnum;
		}
		@Override
		public String toString() {
			return "Secondary [sortname=" + sortname + ", sortnum=" + sortnum
					+ "]";
		}
	}
}
