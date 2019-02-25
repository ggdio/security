package br.com.ggdio.security.session;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class EasyX509TrustManager implements X509TrustManager {

	private X509TrustManager standardTrustManager = null;

	/**
	 * Constructor for EasyX509TrustManager.
	 */
	public EasyX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
		super();
		TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		factory.init(keystore);
		TrustManager[] trustmanagers = factory.getTrustManagers();
		if (trustmanagers.length == 0) {
			throw new NoSuchAlgorithmException("no trust manager found");
		}
		this.standardTrustManager = (X509TrustManager) trustmanagers[0];
	}

	@Override
	public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
			throws java.security.cert.CertificateException {
		standardTrustManager.checkClientTrusted(chain, authType);

	}

	@Override
	public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
			throws java.security.cert.CertificateException {
		if ((chain != null) && (chain.length == 1)) {
			chain[0].checkValidity();
		} else {
			standardTrustManager.checkServerTrusted(chain, authType);
		}
	}

	@Override
	public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		return this.standardTrustManager.getAcceptedIssuers();
	}

}