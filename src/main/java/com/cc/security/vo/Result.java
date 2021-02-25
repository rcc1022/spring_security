/*
 * Copyright 2016 juor & Co., Ltd.
 */
package com.cc.security.vo;


import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * 执行结果
 * 
 * @author Lyn
 *
 */
public class Result<T> extends HashMap<String, Object> {

	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer code;

	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String message;

	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	public Result() {
		put("code", 200);
		put("msg", "success");
	}

    public Result(int i, String s) {
    }

	public Result(int i, String errorMsg, Object errorList) {
	}

	public Result setData(Object data) {
		put("data", data);
		return this;
	}

	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	@JsonIgnore
	public boolean isOk() {
		return code == 0;
	}

	public static <T> Result<T> ok() {
		Result<T> ret = new Result<T>();
		ret.code = 200;
		ret.message = "ok";
		return ret;
	}

	public static <T> Result<T> ok(T data) {
		Result<T> ret = ok();
		ret.data = data;
		return ret;
	}

	public static <T> Result<T> ok(T data, String message) {
		Result<T> ret = ok();
		ret.data = data;
		ret.message = message;
		return ret;
	}


	public static <T> Result<T> error(int code, String message) {
		Result<T> ret = new Result<T>();
		ret.code = code;
		ret.message = message;
		return ret;
	}

	public static <T> Result<T> error(int code, String message, T data) {
		Result<T> ret = new Result<T>();
		ret.code = code;
		ret.message = message;
		ret.data = data;
		return ret;
	}

	public static <T> Result<T> error(int code, String message, Callable<T> callable) {
		Result<T> ret = new Result<T>();
		ret.code = code;
		ret.message = message;
		try {
			ret.data = callable.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return ret;
	}


	public static <T> Result<T> error(Result<?> result) {
		return error(result.code, result.message);
	}

}
