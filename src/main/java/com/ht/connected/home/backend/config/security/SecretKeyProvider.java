package com.ht.connected.home.backend.config.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Based on
 * http://www.java2s.com/Code/Java/Security/RetrievingaKeyPairfromaKeyStore.htm
 */
@Component
public class SecretKeyProvider {
	private static final Logger logger = LoggerFactory.getLogger(SecretKeyProvider.class);

    public String getKey() throws URISyntaxException,
            KeyStoreException, IOException,
            NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException{
        return new String( getKeyPair().getPublic().getEncoded(), "UTF-8" );
    }

    private KeyPair getKeyPair() throws
            KeyStoreException, IOException,
            NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
    	 logger.info("path::::"+this.getClass().getResource("").getPath());
    	 logger.info("path::::"+this.getClass().getResource("mykeys.jks").getPath());
        FileInputStream is = new FileInputStream("mykeys.jks");

        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, "mypass".toCharArray());

        String alias = "mykeys";

        Key key = keystore.getKey(alias, "mypass".toCharArray());
        if (key instanceof PrivateKey) {
            // Get certificate of public key
            Certificate cert = keystore.getCertificate(alias);

            // Get public key
            PublicKey publicKey = cert.getPublicKey();

            // Return a key pair
            return new KeyPair(publicKey, (PrivateKey) key);
        } else throw new UnrecoverableKeyException();
    }

}
