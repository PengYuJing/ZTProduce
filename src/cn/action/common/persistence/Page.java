package cn.action.common.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.action.common.config.Global;
import cn.action.common.utils.CookieUtils;

public class Page<T> {
	
	private int pageNo = 1; // ��ǰҳ��
	private int pageSize = Integer.valueOf(Global.getConfig("page.pageSize")); // ҳ���С������Ϊ��-1����ʾ�����з�ҳ����ҳ��Ч��
	
	private long count;// �ܼ�¼��������Ϊ��-1����ʾ����ѯ����
	
	private int first;// ��ҳ����
	private int last;// βҳ����
	private int prev;// ��һҳ����
	private int next;// ��һҳ����
	
	private boolean firstPage;//�Ƿ��ǵ�һҳ
	private boolean lastPage;//�Ƿ������һҳ

	private int length = 8;// ��ʾҳ�泤��
	private int slider = 1;// ǰ����ʾҳ�泤��
	
	private List<T> list = new ArrayList<T>();
	
	private String orderBy = ""; // ��׼��ѯ��Ч�� ʵ���� updatedate desc, name asc

	private String funcName = "page"; // ���õ��ҳ����õ�js�������ƣ�Ĭ��Ϊpage����һҳ�ж����ҳ����ʱʹ�á�
	
	private String funcParam = ""; // �����ĸ��Ӳ���������������ֵ��
	
	private String message = ""; // ������ʾ��Ϣ����ʾ�ڡ���n����֮��

	public Page() {
		this.pageSize = -1;
	}
	
	/**
	 * ���췽��
	 * @param request ���� repage ����������סҳ��
	 * @param response �������� Cookie����סҳ��
	 */
	public Page(HttpServletRequest request, HttpServletResponse response){
		this(request, response, -2);
	}

	/**
	 * ���췽��
	 * @param request ���� repage ����������סҳ��
	 * @param response �������� Cookie����סҳ��
	 * @param defaultPageSize Ĭ�Ϸ�ҳ��С��������� -1 ��Ϊ����ҳ��������������
	 */
	public Page(HttpServletRequest request, HttpServletResponse response, int defaultPageSize){
		// ����ҳ�����������repage����������סҳ�룩
		String no = request.getParameter("pageNo");
		if (StringUtils.isNumeric(no)){
			CookieUtils.setCookie(response, "pageNo", no);
			this.setPageNo(Integer.parseInt(no));
		}else if (request.getParameter("repage")!=null){
			no = CookieUtils.getCookie(request, "pageNo");
			if (StringUtils.isNumeric(no)){
				this.setPageNo(Integer.parseInt(no));
			}
		}
		// ����ҳ���С����������repage����������סҳ���С��
		String size = request.getParameter("pageSize");
		if (StringUtils.isNumeric(size)){
			CookieUtils.setCookie(response, "pageSize", size);
			this.setPageSize(Integer.parseInt(size));
		}else if (request.getParameter("repage")!=null){
			size = CookieUtils.getCookie(request, "pageSize");
			if (StringUtils.isNumeric(size)){
				this.setPageSize(Integer.parseInt(size));
			}
		}else if (defaultPageSize != -2){
			this.pageSize = defaultPageSize;
		}
		// ����ҳ���ҳ����
        String funcName = request.getParameter("funcName");
        if (StringUtils.isNotBlank(funcName)){
            CookieUtils.setCookie(response, "funcName", funcName);
            this.setFuncName(funcName);
        }else if (request.getParameter("repage")!=null){
            funcName = CookieUtils.getCookie(request, "funcName");
            if (StringUtils.isNotBlank(funcName)){
                this.setFuncName(funcName);
            }
        }
		// �����������
		String orderBy = request.getParameter("orderBy");
		if (StringUtils.isNotBlank(orderBy)){
			this.setOrderBy(orderBy);
		}
	}
	
	/**
	 * ���췽��
	 * @param pageNo ��ǰҳ��
	 * @param pageSize ��ҳ��С
	 */
	public Page(int pageNo, int pageSize) {
		this(pageNo, pageSize, 0);
	}
	
	/**
	 * ���췽��
	 * @param pageNo ��ǰҳ��
	 * @param pageSize ��ҳ��С
	 * @param count ��������
	 */
	public Page(int pageNo, int pageSize, long count) {
		this(pageNo, pageSize, count, new ArrayList<T>());
	}
	
	/**
	 * ���췽��
	 * @param pageNo ��ǰҳ��
	 * @param pageSize ��ҳ��С
	 * @param count ��������
	 * @param list ��ҳ���ݶ����б�
	 */
	public Page(int pageNo, int pageSize, long count, List<T> list) {
		this.setCount(count);
		this.setPageNo(pageNo);
		this.pageSize = pageSize;
		this.list = list;
	}
	
	/**
	 * ��ʼ������
	 */
	public void initialize(){
				
		//1
		this.first = 1;
		
		this.last = (int)(count / (this.pageSize < 1 ? 20 : this.pageSize) + first - 1);
		
		if (this.count % this.pageSize != 0 || this.last == 0) {
			this.last++;
		}

		if (this.last < this.first) {
			this.last = this.first;
		}
		
		if (this.pageNo <= 1) {
			this.pageNo = this.first;
			this.firstPage=true;
		}

		if (this.pageNo >= this.last) {
			this.pageNo = this.last;
			this.lastPage=true;
		}

		if (this.pageNo < this.last - 1) {
			this.next = this.pageNo + 1;
		} else {
			this.next = this.last;
		}

		if (this.pageNo > 1) {
			this.prev = this.pageNo - 1;
		} else {
			this.prev = this.first;
		}
		
		//2
		if (this.pageNo < this.first) {// �����ǰҳС����ҳ
			this.pageNo = this.first;
		}

		if (this.pageNo > this.last) {// �����ǰҳ����βҳ
			this.pageNo = this.last;
		}
		
	}
	
	/**
	 * Ĭ�������ǰ��ҳ��ǩ 
	 * <div class="page">${page}</div>
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		
		if (pageNo == first) {// �������ҳ
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">&#171; ��һҳ</a></li>\n");
		} else {
			sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+prev+","+pageSize+",'"+funcParam+"');\">&#171; ��һҳ</a></li>\n");
		}

		int begin = pageNo - (length / 2);

		if (begin < first) {
			begin = first;
		}

		int end = begin + length - 1;

		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}

		if (begin > first) {
			int i = 0;
			for (i = first; i < first + slider && i < begin; i++) {
				sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+",'"+funcParam+"');\">"
						+ (i + 1 - first) + "</a></li>\n");
			}
			if (i < begin) {
				sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
			}
		}

		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first)
						+ "</a></li>\n");
			} else {
				sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+",'"+funcParam+"');\">"
						+ (i + 1 - first) + "</a></li>\n");
			}
		}

		if (last - end > slider) {
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
			end = last - slider;
		}

		for (int i = end + 1; i <= last; i++) {
			sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+i+","+pageSize+",'"+funcParam+"');\">"
					+ (i + 1 - first) + "</a></li>\n");
		}

		if (pageNo == last) {
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">��һҳ &#187;</a></li>\n");
		} else {
			sb.append("<li><a href=\"javascript:\" onclick=\""+funcName+"("+next+","+pageSize+",'"+funcParam+"');\">"
					+ "��һҳ &#187;</a></li>\n");
		}

		sb.append("<li class=\"disabled controls\"><a href=\"javascript:\">��ǰ ");
		sb.append("<input type=\"text\" value=\""+pageNo+"\" onkeypress=\"var e=window.event||event;var c=e.keyCode||e.which;if(c==13)");
		sb.append(funcName+"(this.value,"+pageSize+",'"+funcParam+"');\" onclick=\"this.select();\"/> / ");
		sb.append("<input type=\"text\" value=\""+pageSize+"\" onkeypress=\"var e=window.event||event;var c=e.keyCode||e.which;if(c==13)");
		sb.append(funcName+"("+pageNo+",this.value,'"+funcParam+"');\" onclick=\"this.select();\"/> ����");
		sb.append("�� " + count + " ��"+(message!=null?message:"")+"</a></li>\n");

		sb.insert(0,"<ul>\n").append("</ul>\n");
		
		sb.append("<div style=\"clear:both;\"></div>");

//		sb.insert(0,"<div class=\"page\">\n").append("</div>\n");
		
		return sb.toString();
	}
	
	/**
	 * ��ȡ��ҳHTML����
	 * @return
	 */
	public String getHtml(){
		return toString();
	}
	
	public static void main(String[] args) {
		Page<String> p = new Page<String>(3, 3);
		System.out.println(p);
		System.out.println("��ҳ��"+p.getFirst());
		System.out.println("βҳ��"+p.getLast());
		System.out.println("��ҳ��"+p.getPrev());
		System.out.println("��ҳ��"+p.getNext());
	}

	/**
	 * ��ȡ��������
	 * @return
	 */
	public long getCount() {
		return count;
	}

	/**
	 * ������������
	 * @param count
	 */
	public void setCount(long count) {
		this.count = count;
		if (pageSize >= count){
			pageNo = 1;
		}
	}
	
	/**
	 * ��ȡ��ǰҳ��
	 * @return
	 */
	public int getPageNo() {
		return pageNo;
	}
	
	/**
	 * ���õ�ǰҳ��
	 * @param pageNo
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	/**
	 * ��ȡҳ���С
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * ����ҳ���С�����500��
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize <= 0 ? 10 : pageSize;// > 500 ? 500 : pageSize;
	}

	/**
	 * ��ҳ����
	 * @return
	 */
	@JsonIgnore
	public int getFirst() {
		return first;
	}

	/**
	 * βҳ����
	 * @return
	 */
	@JsonIgnore
	public int getLast() {
		return last;
	}
	
	/**
	 * ��ȡҳ������
	 * @return getLast();
	 */
	@JsonIgnore
	public int getTotalPage() {
		return getLast();
	}

	/**
	 * �Ƿ�Ϊ��һҳ
	 * @return
	 */
	@JsonIgnore
	public boolean isFirstPage() {
		return firstPage;
	}

	/**
	 * �Ƿ�Ϊ���һҳ
	 * @return
	 */
	@JsonIgnore
	public boolean isLastPage() {
		return lastPage;
	}
	
	/**
	 * ��һҳ����ֵ
	 * @return
	 */
	@JsonIgnore
	public int getPrev() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}

	/**
	 * ��һҳ����ֵ
	 * @return
	 */
	@JsonIgnore
	public int getNext() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}
	
	/**
	 * ��ȡ��ҳ���ݶ����б�
	 * @return List<T>
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * ���ñ�ҳ���ݶ����б�
	 * @param list
	 */
	public Page<T> setList(List<T> list) {
		this.list = list;
		initialize();
		return this;
	}

	/**
	 * ��ȡ��ѯ�����ַ���
	 * @return
	 */
	@JsonIgnore
	public String getOrderBy() {
		// SQL���ˣ���ֹע�� 
		String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
					+ "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
		Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		if (sqlPattern.matcher(orderBy).find()) {
			return "";
		}
		return orderBy;
	}

	/**
	 * ���ò�ѯ���򣬱�׼��ѯ��Ч�� ʵ���� updatedate desc, name asc
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * ��ȡ���ҳ����õ�js��������
	 * function ${page.funcName}(pageNo){location="${ctx}/list-${category.id}${urlSuffix}?pageNo="+i;}
	 * @return
	 */
	@JsonIgnore
	public String getFuncName() {
		return funcName;
	}

	/**
	 * ���õ��ҳ����õ�js�������ƣ�Ĭ��Ϊpage����һҳ�ж����ҳ����ʱʹ�á�
	 * @param funcName Ĭ��Ϊpage
	 */
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	/**
	 * ��ȡ��ҳ�����ĸ��Ӳ���
	 * @return
	 */
	@JsonIgnore
	public String getFuncParam() {
		return funcParam;
	}

	/**
	 * ���÷�ҳ�����ĸ��Ӳ���
	 * @return
	 */
	public void setFuncParam(String funcParam) {
		this.funcParam = funcParam;
	}

	/**
	 * ������ʾ��Ϣ����ʾ�ڡ���n����֮��
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * ��ҳ�Ƿ���Ч
	 * @return this.pageSize==-1
	 */
	@JsonIgnore
	public boolean isDisabled() {
		return this.pageSize==-1;
	}
	
	/**
	 * �Ƿ��������ͳ��
	 * @return this.count==-1
	 */
	@JsonIgnore
	public boolean isNotCount() {
		return this.count==-1;
	}
	
	/**
	 * ��ȡ Hibernate FirstResult
	 */
	public int getFirstResult(){
		int firstResult = (getPageNo() - 1) * getPageSize();
		if (firstResult >= getCount()) {
			firstResult = 0;
		}
		return firstResult;
	}
	/**
	 * ��ȡ Hibernate MaxResults
	 */
	public int getMaxResults(){
		return getPageSize();
	}


	
}
