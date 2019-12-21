package cn.action.common.mapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class JsonMapper extends ObjectMapper {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

	private static JsonMapper mapper;

	public JsonMapper() {
		this(Include.NON_EMPTY);
	}

	public JsonMapper(Include include) {
		// �������ʱ�������Եķ��
		if (include != null) {
			this.setSerializationInclusion(include);
		}
		// �������š����������ŵ��ֶ�����
		this.enableSimple();
		// ��������ʱ������JSON�ַ����д��ڵ�Java����ʵ��û�е�����
		this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // ��ֵ����Ϊ�մ�
		this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
			@Override
			public void serialize(Object value, JsonGenerator jgen,
					SerializerProvider provider) throws IOException,
					JsonProcessingException {
				jgen.writeString("");
			}
        });
		// ����HTML���롣
		this.registerModule(new SimpleModule().addSerializer(String.class, new JsonSerializer<String>(){
			@Override
			public void serialize(String value, JsonGenerator jgen,
					SerializerProvider provider) throws IOException,
					JsonProcessingException {
				jgen.writeString(StringEscapeUtils.unescapeHtml4(value));
			}
        }));
		// ����ʱ��
		this.setTimeZone(TimeZone.getDefault());//getTimeZone("GMT+8:00")
	}

	/**
	 * ����ֻ�����Null�ҷ�Empty(��List.isEmpty)�����Ե�Json�ַ�����Mapper,�������ⲿ�ӿ���ʹ��.
	 */
	public static JsonMapper getInstance() {
		if (mapper == null){
			mapper = new JsonMapper().enableSimple();
		}
		return mapper;
	}

	/**
	 * ����ֻ�����ʼֵ���ı�����Ե�Json�ַ�����Mapper, ���Լ�Ĵ洢��ʽ���������ڲ��ӿ���ʹ�á�
	 */
	public static JsonMapper nonDefaultMapper() {
		if (mapper == null){
			mapper = new JsonMapper(Include.NON_DEFAULT);
		}
		return mapper;
	}
	
	/**
	 * Object������POJO��Ҳ������Collection�����顣
	 * �������ΪNull, ����"null".
	 * �������Ϊ�ռ���, ����"[]".
	 */
	public String toJson(Object object) {
		try {
			return this.writeValueAsString(object);
		} catch (IOException e) {
			logger.warn("write to json string error:" + object, e);
			return null;
		}
	}

	/**
	 * �����л�POJO���Collection��List<String>.
	 * 
	 * ���JSON�ַ���ΪNull��"null"�ַ���, ����Null.
	 * ���JSON�ַ���Ϊ"[]", ���ؿռ���.
	 * 
	 * ���跴���л�����Collection��List<MyBean>, ��ʹ��fromJson(String,JavaType)
	 * @see #fromJson(String, JavaType)
	 */
	public <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		try {
			return this.readValue(jsonString, clazz);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * �����л�����Collection��List<Bean>, ��ʹ�ú���createCollectionType��������,Ȼ����ñ�����.
	 * @see #createCollectionType(Class, Class...)
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromJson(String jsonString, JavaType javaType) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		try {
			return (T) this.readValue(jsonString, javaType);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * ���췺�͵�Collection Type��:
	 * ArrayList<MyBean>, �����constructCollectionType(ArrayList.class,MyBean.class)
	 * HashMap<String,MyBean>, �����(HashMap.class,String.class, MyBean.class)
	 */
	public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return this.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	/**
	 * ��JSON�eֻ����Bean�Ĳ��֌��ԕr������һ���Ѵ���Bean��ֻ���wԓ���ֵČ���.
	 */
	@SuppressWarnings("unchecked")
	public <T> T update(String jsonString, T object) {
		try {
			return (T) this.readerForUpdating(object).readValue(jsonString);
		} catch (JsonProcessingException e) {
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		} catch (IOException e) {
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		}
		return null;
	}

	/**
	 * ݔ��JSONP��ʽ����.
	 */
	public String toJsonP(String functionName, Object object) {
		return toJson(new JSONPObject(functionName, object));
	}
	
	

	/**
	 * �O���Ƿ�ʹ��Enum��toString�������x��Enum,
	 * ��False�r�rʹ��Enum��name()�������x��Enum, Ĭ�J��False.
	 * ע�Ȿ����һ��Ҫ��Mapper������, ���е��x������֮ǰ�{��.
	 */
	public JsonMapper enableEnumUseToString() {
		this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		return this;
	}



	/**
	 * ��������
	 * ���������ŵ��ֶ�����
	 */
	public JsonMapper enableSimple() {
		this.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		return this;
	}
	
	/**
	 * ȡ��Mapper����һ�������û�ʹ���������л�API.
	 */
	public ObjectMapper getMapper() {
		return this;
	}

	/**
	 * ����ת��ΪJSON�ַ���
	 * @param object
	 * @return
	 */
	public static String toJsonString(Object object){
		return JsonMapper.getInstance().toJson(object);
	}
	
	/**
	 * JSON�ַ���ת��Ϊ����
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static Object fromJsonString(String jsonString, Class<?> clazz){
		return JsonMapper.getInstance().fromJson(jsonString, clazz);
	}
	
	/**
	 * ����
	 */
	public static void main(String[] args) {
		List<Map<String, Object>> list = Lists.newArrayList();
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", 1);
		map.put("pId", -1);
		map.put("name", "���ڵ�");
		list.add(map);
		map = Maps.newHashMap();
		map.put("id", 2);
		map.put("pId", 1);
		map.put("name", "���");
		map.put("open", true);
		list.add(map);
		String json = JsonMapper.getInstance().toJson(list);
		System.out.println(json);
	}
	
}
