package com.adsys.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public class TXLiveUrl {

		public static void main(String[] args) throws ParseException {
			System.out.println(DateUtil.currDateAddDay(5).getTime()/1000);
				System.out.println(getSafeUrl(Const.TXLIVE_URL_KEY, "26168_dd451e9ce6", 1543247999L));
		}

		private static final char[] DIGITS_LOWER =
				{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

		/*
		 * KEY+ stream_id + txTime
		 */
		public static String getSafeUrl(String key, String streamId, long txTime) {
				String input = new StringBuilder().
								append(key).
								append(streamId).
								append(Long.toHexString(txTime).toUpperCase()).toString();

				String txSecret = null;
				try {
						MessageDigest messageDigest = MessageDigest.getInstance("MD5");
						txSecret  = byteArrayToHexString(
										messageDigest.digest(input.getBytes("UTF-8")));
				} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
				}

				String secret =  txSecret == null ? "" :
						new StringBuilder().
								append("txSecret=").
								append(txSecret).
								append("&").
								append("txTime=").
								append(Long.toHexString(txTime).toUpperCase()).
								toString();
				
				return new StringBuilder().append("rtmp://").
									append(Const.TXLIVE_BIZID).
									append(".livepush.myqcloud.com/live/").
									append(streamId).
									append("?").
									append("bizid=").
									append(Const.TXLIVE_BIZID).append("&").append(secret).toString();
		}

		public static String getPlayUrl(String did) {
			
			return new StringBuilder().append("rtmp://").
								append("play.gogogovision.com/live/").
								append(Const.TXLIVE_BIZID).
								append("_").
								append(did).toString();
		}
		
		private static String byteArrayToHexString(byte[] data) {
				char[] out = new char[data.length << 1];

				for (int i = 0, j = 0; i < data.length; i++) {
						out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
						out[j++] = DIGITS_LOWER[0x0F & data[i]];
				}
				return new String(out);
		}
}
