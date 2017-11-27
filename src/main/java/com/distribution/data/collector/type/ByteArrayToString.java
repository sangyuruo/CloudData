package com.distribution.data.collector.type;

import org.springframework.core.convert.converter.Converter;

import java.io.UnsupportedEncodingException;

public class ByteArrayToString implements Converter<byte[], String> {

	private String charSet = "GB18030";

	public String convert(byte[] bytes) {
		try {
			return new String(bytes, this.charSet).trim();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new String(bytes);
		}
	}

	/**
	 * @return the charSet
	 */
	public String getCharSet() {
		return charSet;
	}

	/**
	 * @param charSet the charSet to set
	 */
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

}
