package com.efida.sports.util.weibo;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 
 * @author lijiaqi
 * @date 2015年1月15日
 * @version 1.0.0
 * @description desc
 */
public class SSLClient extends DefaultHttpClient{

	public SSLClient() throws Exception{  
        super();  
        SSLContext ctx = SSLContext.getInstance("TLS");  
        X509TrustManager tm = new X509TrustManager() {  
                @Override  
                public void checkClientTrusted(X509Certificate[] chain,  
                        String authType) throws CertificateException {  
                }  
                @Override  
                public void checkServerTrusted(X509Certificate[] chain,  
                        String authType) throws CertificateException {  
                }  
                @Override  
                public X509Certificate[] getAcceptedIssuers() {  
                    return null;  
                }  
        };  
        ctx.init(null, new TrustManager[]{tm}, null);  
        SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
        ClientConnectionManager ccm = this.getConnectionManager();  
        SchemeRegistry sr = ccm.getSchemeRegistry();  
        sr.register(new Scheme("https", 443, ssf));  
    }  
	
	public SSLClient(String keyStorePath,String keyStorePassword,String trustStorePath,String trustStorePassword) throws Exception{  
		super();  
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(new FileInputStream(new File(trustStorePath)), trustStorePassword.toCharArray());
		keyStore.load(new FileInputStream(keyStorePath), keyStorePassword.toCharArray());
		SSLSocketFactory ssf = new SSLSocketFactory(keyStore,keyStorePassword,trustStore);  
		ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		ClientConnectionManager ccm = this.getConnectionManager();  
		SchemeRegistry sr = ccm.getSchemeRegistry();  
		sr.register(new Scheme("https", 443, ssf));  
	}  
	
	
	public SSLClient(String pkcs12Path,String keyStorePassword) throws Exception{  
		super();  
        SSLContext ctx = SSLContext.getInstance("TLS");  
        X509TrustManager tm = new X509TrustManager() {  
                @Override  
                public void checkClientTrusted(X509Certificate[] chain,  
                        String authType) throws CertificateException {  
                }  
                @Override  
                public void checkServerTrusted(X509Certificate[] chain,  
                        String authType) throws CertificateException {  
                }  
                @Override  
                public X509Certificate[] getAcceptedIssuers() {  
                    return null;  
                }  
        };  
        ctx.init(null, new TrustManager[]{tm}, null);  
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream instream = new FileInputStream(new File(pkcs12Path));
		trustStore.load(instream, keyStorePassword.toCharArray());
		
		SSLSocketFactory ssf = new SSLSocketFactory(trustStore,keyStorePassword,trustStore);
		ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		ClientConnectionManager ccm = this.getConnectionManager();  
		SchemeRegistry sr = ccm.getSchemeRegistry();  
		sr.register(new Scheme("https", 443, ssf));  
	}  
	
	
	
	
}
