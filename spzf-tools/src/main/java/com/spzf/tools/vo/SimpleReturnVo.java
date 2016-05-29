package com.spzf.tools.vo;

import java.io.Serializable;

/**
 * @Title:
 * @Description:返回vo
 * @Author:
 * @Since:
 * @Version:1.1.0
 * @Copyright:
 */
public class SimpleReturnVo implements Serializable{
	private static final long serialVersionUID = -5799849092170512473L;
	// 返回编码
	private Integer responseCode;
	// 返回的信息
	private String responseMsg;

	public SimpleReturnVo() {

	}

	public SimpleReturnVo(Integer responseCode, String responseMsg) {
		this.responseCode = responseCode;
		this.responseMsg = responseMsg;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

}
