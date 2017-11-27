package com.distribution.data.collector.type;

import java.io.IOException;

public class NumberUtils {

	public static short readShort(byte[] by, int offset) throws IOException {
		int ch1 = by[offset];
		int ch2 = by[offset + 1];
		if ((ch1 | ch2) < 0) {
			throw new IOException();
		}
		return (short)((ch1 << 8) + (ch2 << 0));
	}

	public static short readChar(byte[] by, int offset) throws IOException {
		int ch1 = by[offset];
		if (ch1 < 0) {
			throw new IOException();
		}
		return (short)(ch1 << 0);
	}

	public static float readFloat(byte[] by, int offset) throws IOException {
		return Float.intBitsToFloat(readInt(by, offset));
	}

	public static int readInt(byte[] by, int offset) throws IOException {
		int ch1 = by[offset] & 0xFF;
		int ch2 = by[offset + 1] & 0xFF;
		int ch3 = by[offset + 2] & 0xFF;
		int ch4 = by[offset + 3] & 0xFF;
		if ((ch1 | ch2 | ch3 | ch4) < 0) {
			throw new IOException();
		}
		return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0);
	}

}
