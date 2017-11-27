package com.distribution.data.collector.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CustomLittleEndian {

	public static void putInt(int value, OutputStream outputStream) throws IOException {
		outputStream.write((byte) ((value >>> 0) & 0xFF));
		outputStream.write((byte) ((value >>> 8) & 0xFF));
		outputStream.write((byte) ((value >>> 16) & 0xFF));
		outputStream.write((byte) ((value >>> 24) & 0xFF));
	}

	public static void putFloat(float value, OutputStream outputStream) throws IOException {
		putInt(Float.floatToIntBits(value), outputStream);
	}

	public static void putDouble(double value, OutputStream outputStream) throws IOException {
		putLong(Double.doubleToLongBits(value), outputStream);
	}

	public static void putLong(long value, OutputStream outputStream) throws IOException {
		outputStream.write((byte) ((value >>> 0) & 0xFF));
		outputStream.write((byte) ((value >>> 8) & 0xFF));
		outputStream.write((byte) ((value >>> 16) & 0xFF));
		outputStream.write((byte) ((value >>> 24) & 0xFF));
		outputStream.write((byte) ((value >>> 32) & 0xFF));
		outputStream.write((byte) ((value >>> 40) & 0xFF));
		outputStream.write((byte) ((value >>> 48) & 0xFF));
		outputStream.write((byte) ((value >>> 56) & 0xFF));
	}

	public static void putShort(short value, OutputStream outputStream) throws IOException {
		outputStream.write((byte) ((value >>> 0) & 0xFF));
		outputStream.write((byte) ((value >>> 8) & 0xFF));
	}

	public static int readInt(InputStream stream) throws IOException {
		int ch1 = stream.read();
		int ch2 = stream.read();
		int ch3 = stream.read();
		int ch4 = stream.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0) {
			throw new IOException();
		}
		return (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
//		return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0);
	}

	public static long readLong(InputStream stream) throws IOException {
		int ch1 = stream.read();
		int ch2 = stream.read();
		int ch3 = stream.read();
		int ch4 = stream.read();
		int ch5 = stream.read();
		int ch6 = stream.read();
		int ch7 = stream.read();
		int ch8 = stream.read();
		if ((ch1 | ch2 | ch3 | ch4 | ch5 | ch6 | ch7 | ch8) < 0) {
			throw new IOException();
		}

		return ((long) ch8 << 56) + ((long) ch7 << 48) + ((long) ch6 << 40) + ((long) ch5 << 32) + ((long) ch4 << 24) +
		(ch3 << 16) + (ch2 << 8) + (ch1 << 0);
	}

	public static float readFloat(InputStream stream) throws IOException {
		return Float.intBitsToFloat(readInt(stream));
	}

	public static short readShort(InputStream stream) throws IOException {
		int ch1 = stream.read();
		int ch2 = stream.read();
		if ((ch1 | ch2) < 0) {
			throw new IOException();
		}
		return (short)((ch2 << 8) + (ch1 << 0));
	}

    public static void putUShort( int value, OutputStream outputStream )
            throws IOException
    {
        outputStream.write( (byte) ( ( value >>> 0 ) & 0xFF ) );
        outputStream.write( (byte) ( ( value >>> 8 ) & 0xFF ) );
    }

	public static int readUShort(InputStream stream) throws IOException {
		int ch1 = stream.read();
		int ch2 = stream.read();
		if ((ch1 | ch2) < 0) {
			throw new IOException();
		}
		return (ch2 << 8) + (ch1 << 0);
	}
	public static void putUInt(long value, OutputStream outputStream) throws IOException {
		outputStream.write((byte) ((value >>> 0) & 0xFF));
		outputStream.write((byte) ((value >>> 8) & 0xFF));
		outputStream.write((byte) ((value >>> 16) & 0xFF));
		outputStream.write((byte) ((value >>> 24) & 0xFF));
	}

	public static byte[] uIntToBytes(long value) throws IOException {
		byte[] b = new byte[4];
		b[0] = (byte) ((value >>> 0) & 0xFF);
		b[1] = (byte) ((value >>> 8) & 0xFF);
		b[2] = ((byte) ((value >>> 16) & 0xFF));
		b[3] = ((byte) ((value >>> 24) & 0xFF));
		return b;
	}

	public static long readUInt(InputStream stream) throws IOException {
		long retNum = readInt(stream);
		return retNum & 0x00FFFFFFFFl;
	}

	public static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}
//
//	public static void main(String[] args) throws Exception {
////		ByteBuffer.allocate(4).i
//		Date st = new Date();
//		long y = st.getTime()/1000;
//		System.out.println(y > Integer.MAX_VALUE);
//	}
}
