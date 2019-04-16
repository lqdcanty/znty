package com.efida.sports.util.weibo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * https接口
 * 
 * @author lijiaqi
 * @date 2015年1月15日
 * @version 1.0.0
 * @description desc
 */
public class HttpsClientUtil {

	public static final String charset = "utf-8";

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url) throws Exception {
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	// public static String doGet(String url,String keyStorePath,String
	// keyStorePassword) throws Exception{
	// CloseableHttpClient httpClient = null;
	// HttpGet httpGet = null;
	// String result = null;
	// try{
	// KeyStore keyStore = KeyStore.getInstance("PKCS12");
	// FileInputStream inStream = new FileInputStream(new File(keyStorePath));
	// try {
	// keyStore.load(inStream, keyStorePassword.toCharArray());
	// } catch (Exception e) {
	// throw e;
	// }finally{
	// inStream.close();
	// }
	// SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore,
	// keyStorePassword.toCharArray()).build();
	// SSLConnectionSocketFactory sslsf =
	// new SSLConnectionSocketFactory(sslContext,new
	// String[]{"TLSv1"},null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	// httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
	// httpGet = new HttpGet(url);
	// HttpResponse response = httpClient.execute(httpGet);
	// if(response != null){
	// HttpEntity resEntity = response.getEntity();
	// if(resEntity != null){
	// result = EntityUtils.toString(resEntity,charset);
	// }
	// }
	// }catch(Exception ex){
	// throw ex;
	// }
	// return result;
	// }
	public static String doGet(String url, String keyStorePath, String keyStorePassword) throws Exception {
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;
		try {
			httpClient = new SSLClient(keyStorePath, keyStorePassword);
			httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url, String keyStorePath, String keyStorePassword, String trustStorePath,
			String trustStorePassword) throws Exception {
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;
		try {
			httpClient = new SSLClient(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword);
			httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param map
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, Map<String, String> map, String charset) throws Exception {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.setHeader("charset", "utf-8");
			// 设置参数
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			if (map != null && map.size() > 0) {
				Iterator iterator = map.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, String> elem = (Entry<String, String>) iterator.next();
					list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
				}
			}
			if (list.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
				httpPost.setEntity(entity);
			}
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}

	/**
	 * 执行
	 * 
	 * @param url
	 * @param xmlString
	 * @return
	 * @throws Exception
	 */
	public static String executeXMLHttpMethod(String url, String xmlString) throws Exception {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "text/xml");
			httpPost.setHeader("charset", "utf-8");
			StringEntity entity = new StringEntity(xmlString, "utf-8");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}

	public static String executeJSONHttpMethod(String url, String jsonStr) throws Exception {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "text/json");
			httpPost.setHeader("charset", "utf-8");
			StringEntity entity = new StringEntity(jsonStr, "utf-8");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}

	/**
	 * 执行
	 * 
	 * @param url
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public static String executeJSONHttpMethod(String url, String jsonStr, String keyStorePath, String keyStorePassword,
			String trustStorePath, String trustStorePassword) throws Exception {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = new SSLClient(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword);
			httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "text/json");
			httpPost.setHeader("charset", "utf-8");
			StringEntity entity = new StringEntity(jsonStr, "utf-8");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}

	/**
	 * 执行
	 * 
	 * @param url
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public static String executeGetHttpMethod(String url, String keyStorePath, String keyStorePassword,
			String trustStorePath, String trustStorePassword) throws Exception {
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;
		try {
			httpClient = new SSLClient(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword);
			httpGet = new HttpGet(url);
			httpGet.setHeader("Content-Type", "text/json");
			httpGet.setHeader("charset", "utf-8");
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}

	/**
	 * 执行
	 * 
	 * @param url
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public static String executePostHttpMethod(String url, Map<String, Object> params, String keyStorePath,
			String keyStorePassword, String trustStorePath, String trustStorePassword) throws Exception {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = new SSLClient(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword);
			httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "text/json");
			httpPost.setHeader("charset", "utf-8");

			// 设置参数
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			Iterator iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> elem = (Entry<String, String>) iterator.next();
				list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
			}
			if (list.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
				httpPost.setEntity(entity);
			}

			HttpResponse response = httpClient.execute(httpPost);

			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}

	/**
	 * 上传二进制文件
	 * 
	 * @param url
	 * @param data
	 * @param fileName
	 * @param parameterName
	 * @return
	 * @throws Exception
	 */
	public static String executeByteHttpMethod(String url, byte[] data, String parameterName, String fileName)
			throws Exception {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = wrapClient(new DefaultHttpClient());
			httpPost = new HttpPost(url);
			// httpPost.setHeader("Content-Type","text/xml");
			// httpPost.setHeader("charset","utf-8");
			MultipartEntity reqEntity = new MultipartEntity();
			ByteArrayBody bin = new ByteArrayBody(data, fileName);
			reqEntity.addPart(parameterName, bin);
			httpPost.setEntity(reqEntity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}

	/**
	 * 上传二进制文件
	 * 
	 * @param url
	 * @param data
	 * @param fileName
	 * @param parameterName
	 * @return
	 * @throws Exception
	 */
	public static String executeByteHttpMethod(String url, byte[] data, String parameterName, String fileName,
			String keyStorePath, String keyStorePassword, String trustStorePath, String trustStorePassword)
			throws Exception {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = new SSLClient(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword);
			httpPost = new HttpPost(url);
			// httpPost.setHeader("Content-Type","text/xml");
			// httpPost.setHeader("charset","utf-8");
			MultipartEntity reqEntity = new MultipartEntity();
			ByteArrayBody bin = new ByteArrayBody(data, fileName);
			reqEntity.addPart(parameterName, bin);
			httpPost.setEntity(reqEntity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return result;
	}

	public static HttpClient wrapClient(HttpClient base) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("https", 443, ssf));
			ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
			return new DefaultHttpClient(mgr, base.getParams());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String getCtx(HttpServletRequest request) {
		String port = "";
		if (request.getServerPort() != 80) {
			port = ":" + request.getServerPort();
		}
		StringBuffer ctx = new StringBuffer();
		String protocol = null;
		if (StringUtils.isNotBlank(request.getHeader("X-Forwarded-Proto"))) {
			protocol = request.getHeader("X-Forwarded-Proto");
		} else {
			protocol = request.getScheme();
		}
		ctx.append(protocol).append("://").append(request.getServerName()).append(port)
				.append(request.getContextPath());
		if (ctx.charAt(ctx.length() - 1) != '/') {
			ctx.append("/");
		}
		return ctx.toString();
	}

	/**
	 * 获取Ip地址
	 * 
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.equals("127.0.0.1")) {
			// 根据网卡取本机配置的IP
			InetAddress inet = null;
			try {
				inet = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			ip = inet.getHostAddress();
		}
		if (!ip.contains(",")) {
			return ip;
		}
		String[] ips = ip.split(",");
		return ips[0];
	}

}
