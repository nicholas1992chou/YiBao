package com.bwf.yibao.framwork.tools;

public class UtilsURLPath {
	//	public static String path = "http://169.254.75.71:8080/Market_Flea/";
	//	public static String path = "http://192.168.4.101:8080/Market_Flea/";
	//  阿里云地址
	public static String path = "http://123.56.145.151:8080/Market_Flea/";//总路径
	//  本地服务器地址
	//	public static String path = "http://192.168.10.148:8080/Market_Flea/";//总路径
	// type为0，代表分页请求，conditions应该为String类型的数字
	// type为1，代表分类请求，conditions为二级分类名称
	// type为2，代表查询个人用户发布商品，conditions为登陆用户名 
	// 参数拼接方式:  http://123.56.145.151:8080/Market_Flea/queryGoodsServlet?type=0&conditions=1
	public static String getGoodsPath = path+"queryGoodsServlet";//获取商品 
	public static String getSortPath = path+"goodsSortServlet";//获取全部分类
	public static String downloadPic = path+"Image/";//获取图片
	// 要封装的参数key使用info，value为goods对象转成json数据，calssify属性取二级分类名称，imageId用当前时间毫秒值
	public static String publishgGoodsPath = path+"addGoodsServlet";//上传商品信息 
	public static String uploadPic = path+"sortsServlet";//上传产品图片
	public static String updateGoods = path+"updateStateServlet";
	public static String uploadFeed = path+"feedbackServlet";
	
	// 以下三个Servlet均使用POST方法请求，将数据转成json封装到一个NameValuePair中，其key统一为user
	// http://192.168.10.148:8080/Market_Flea/LoginServlet?user=json
	public static String userLogin = path+"LoginServlet";// 用户登录
	public static String userRegister = path+"RegisterServlet";// 用户注册
	public static String uploadLocation = path+"LocationServlet";// 上传商品位置
	// 检查手机号能否注册，传参的key为phoneNum，返回json数据，type为0表示可以注册
	public static String checkPhoneNum = path+"CheckPhoneServlet";// 检查手机号能否注册
	
	// 添加用户收藏商品、移除用户收藏商品、查询指定商品是否被该用户收藏
	// Servlet都需要分别封装userName、goodsId和type的参数
	// type为字符串，0表示添加收藏，为1代表移除收藏，为2代表查询该商品是否被当前用户收藏
	public static String collectGoods = path+"CollectServlet";// 网络收藏商品
	// Servlet都需要分别封装userName、goodsId的参数
	public static String uploadUserRecord = path+"UserPublicServlet";// 网络记录发布商品
	// 封装参数userName
	public static String queryCollectList = path+"QueryCollect";// 用户收藏商品查询
}
