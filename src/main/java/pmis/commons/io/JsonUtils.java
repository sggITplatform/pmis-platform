package pmis.commons.io;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.base.Strings;

public class JsonUtils
{

	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	/**
	 * 
	 * @Title: AllJsonUtil
	 * @Description: 所有属性值,无论是null 还是not null * <per> Exp data
	 *               {"tenantId":null,"pageNo":1,"pageSize"
	 *               :15,"totalRecord":0,"totalPage":0,
	 *               "results":null,"id":11,"username":null,"password":"ddd"}
	 * 
	 *               result: String
	 *               json=JsonUtils.NoNullJsonUtil().toJson(user);
	 *               {"tenantId":null,"pageNo":1,"pageSize"
	 *               :15,"totalRecord":0,"totalPage":0,
	 *               "results":null,"id":11,"username":null,"password":"ddd"}
	 *               <per>
	 * @version: V1.0
	 * @return
	 */
	public static JsonUtils AllJsonUtil()
	{
		return new JsonUtils(Include.ALWAYS);
	}

	/**
	 * 创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。
	 * 
	 * @return 只输出初始值被改变的属性到Json字符串的Mapper * <per> Exp data
	 *         {"tenantId":null,"pageNo":1,"pageSize"
	 *         :15,"totalRecord":0,"totalPage":0,
	 *         "results":null,"id":11,"username":null,"password":"ddd"}
	 * 
	 *         result: String json=JsonUtils.nonDefaultJsonUtil().toJson(user);
	 *         {"pageNo":2,"password":"ddd222"} <per>
	 */
	public static JsonUtils nonDefaultJsonUtil()
	{
		return new JsonUtils(Include.NON_DEFAULT);
	}

	/**
	 * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用。
	 * 
	 * @return 只输出非Null且非Empty的属性到Json字符串的Mapper * <per> Exp data
	 *         {"tenantId":null,"pageNo":1,"pageSize"
	 *         :15,"totalRecord":0,"totalPage":0,
	 *         "results":null,"id":11,"username":null,"password":"ddd"}
	 * 
	 *         result: String json=JsonUtils.nonEmptyJsonUtil().toJson(user);
	 *         {"pageNo":1,"pageSize":15,"totalRecord":0,"totalPage":0,"id":11,
	 *         "username":"xxxx","password":"ddd"} <per>
	 * 
	 */
	public static JsonUtils nonEmptyJsonUtil()
	{
		return new JsonUtils(Include.NON_EMPTY);
	}

	/**
	 * 
	 * @Title: NoNullJsonUtil
	 * @Description: 获取非Null的属性值 <per> Exp data
	 *               {"tenantId":null,"pageNo":1,"pageSize"
	 *               :15,"totalRecord":0,"totalPage":0,
	 *               "results":null,"id":11,"username":null,"password":"ddd"}
	 * 
	 *               result: String
	 *               json=JsonUtils.NoNullJsonUtil().toJson(user);
	 *               {"pageNo":1,"pageSize":15,"totalRecord":0,"totalPage":0,
	 *               "id":11,"password":"ddd"} <per>
	 * @version: V1.0
	 * @return
	 */
	public static JsonUtils NoNullJsonUtil()
	{
		return new JsonUtils(Include.NON_NULL);
	}

	private ObjectMapper mapper;

	public JsonUtils(Include include)
	{
		mapper = new ObjectMapper();
		// 设置输出时包含属性的风格
		if (include != null)
		{
			mapper.setSerializationInclusion(include);
		}
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// 设置默认DateFormat yyyy-MM-dd HH:mm:ss
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		this.mapper.registerModule(new GuavaModule());
	}

	/**
	 * 构造泛型的Collection Type如: ArrayList<MyBean>,
	 * 则调用constructCollectionType(ArrayList.class,MyBean.class)
	 * HashMap<String,MyBean>, 则调用(HashMap.class,String.class, MyBean.class)
	 */
	public JavaType createCollectionType(Class<?> collectionClass, Class<?>[] elementClasses)
	{
		return this.mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	/**
	 * 設定是否使用Enum的toString函數來讀寫Enum, 為False時時使用Enum的name()函數來讀寫Enum, 默認為False.
	 * 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
	 */
	public void enableEnumUseToString()
	{
		this.mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		this.mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
	}

	/**
	 * 
	 * 
	 * @Title: fromJson
	 * @Description: <p>
	 *               Json数据转换为对象性,可以转换为Map、Map集合、List集合以及具体的某一个对象
	 *               <p>
	 * 
	 *               <pre>
	 * 		返回map对象
	 * 		Map map=JsonUtils.AllJsonUtil.fromJson(jsonStr,Map.class);
	 * 		将json字符串转换成List<Map>集合
	 * 		List<Map<String, Object>> list = JsonUtils.AllJsonUtil.fromJson(jsonStr,List.class);
	 * 		Json字符串转换成Array数组
	 * 		String [] arr = JsonUtils.AllJsonUtil.fromJson(jsonStr, String [].class);
	 * 		Json字符串转换成Map集合
	 * 		Map<String, Map<String, Object>> maps = JsonUtils.AllJsonUtil.fromJson(jsonStr, Map.class);
	 * 		Json字符串转换为一个具体的对象
	 * 		SysUser user=JsonUtils.AllJsonUtil.fromJson(jsonStr, SysUser.class);
	 * </pre>
	 * @param: <p>
	 * @param jsonString
	 * @param: <p>
	 * @param clazz
	 * @param: <p>
	 * @return <p>
	 * @date: 2014年5月16日
	 * @return: T
	 * @throws
	 * 
	 */
	public <T> T fromJson(String jsonString, Class<T> clazz)
	{
		if (Strings.isNullOrEmpty(jsonString))
		{
			return null;
		}
		try
		{
			return this.mapper.readValue(jsonString, clazz);
		}
		catch (IOException e)
		{
			logger.warn("parse json string error:" + jsonString, e);
		}
		return null;
	}

	/**
	 * 反序列化复杂Collection如List<Bean>, 先使用函數createCollectionType构造类型,然后调用本函数.
	 * 
	 * @param jsonString
	 * @param javaType
	 * @return
	 */
	public <T> T fromJson(String jsonString, JavaType javaType)
	{
		if ((Strings.isNullOrEmpty(jsonString)) || (!(jsonString.contains("{"))))
		{
			return null;

		}
		try
		{
			return this.mapper.readValue(jsonString, javaType);
		}
		catch (Exception e)
		{
			logger.warn("parse json string error:" + jsonString, e);
		}
		return null;
	}

	/**
	 * 取出Mapper做进一步的设置或使用其他序列化API
	 * 
	 * @return
	 */
	public ObjectMapper getMapper()
	{
		return this.mapper;
	}

	/**
	 * 设定格式化Date类型数据格式 在所有读写动作之前调用
	 */
	public void setDateFormat(String dateFormat)
	{
		mapper.setDateFormat(new SimpleDateFormat(dateFormat));
	}

	/**
	 * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".
	 * 
	 * @param object
	 * @return
	 */

	public String toJson(Object object)
	{
		try
		{
			return this.mapper.writeValueAsString(object);
		}
		catch (IOException e)
		{
			logger.warn("write to json string error:" + object, e);
		}
		return null;
	}

	/*
	 * 输出JSONP格式数据
	 */
	public String toJsonP(String functionName, Object object)
	{
		return toJson(new JSONPObject(functionName, object));
	}

	public JsonNode treeFromJson(String jsonString) throws IOException
	{
		return this.mapper.readTree(jsonString);
	}

	public <T> T treeToValue(JsonNode node, Class<T> clazz) throws JsonProcessingException
	{
		return this.mapper.treeToValue(node, clazz);
	}

	/**
	 * 当JSON里只含有Bean的部分属性时，更新一个已存在Bean，只覆盖该部分的屬性.
	 * 
	 * @param jsonString
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T update(String jsonString, T object)
	{
		try
		{
			return this.mapper.readerForUpdating(object).readValue(jsonString);
		}
		catch (JsonProcessingException e)
		{
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		}
		catch (IOException e)
		{
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		}
		return null;
	}
}
