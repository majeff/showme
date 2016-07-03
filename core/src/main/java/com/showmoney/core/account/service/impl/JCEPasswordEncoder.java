/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.account.service.impl.JCEPasswordEncoder
   Module Description   :

   Date Created      : 2008/4/23
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.account.service.impl;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.authentication.encoding.BaseDigestPasswordEncoder;

import com.showmoney.core.common.utils.BytesUtils;

/**
 * @author jeffma
 * 
 */
public class JCEPasswordEncoder extends BaseDigestPasswordEncoder {

	/** logger */
	private final Logger logger = LoggerFactory.getLogger(JCEPasswordEncoder.class);
	/** cryptInstance, default: DESede, allow: "DES", "DESede" or "Blowfish" */
	private String cryptInstance = "DESede";
	/** cryptKey, default: qwert12345 */
	private String cryptKey = "qwert12345";

	/** default constructors */
	public JCEPasswordEncoder() {
		super();
	}

	/** default constructors */
	public JCEPasswordEncoder(String cryptInstance, String cryptKey) {
		this.cryptInstance = cryptInstance;
		this.cryptKey = cryptKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.acegisecurity.providers.encoding.PasswordEncoder#encodePassword(java.lang.String, java.lang.Object)
	 */
	public String encodePassword(final String rawPass, final Object salt) throws DataAccessException {
		String result = "";
		if (salt != null) {
			cryptKey = salt.toString();
		}

		try {
			KeyGenerator generator = KeyGenerator.getInstance(cryptInstance);
			generator.init(new SecureRandom(cryptKey.getBytes()));
			Key key = generator.generateKey();
			Cipher cipher = Cipher.getInstance(cryptInstance);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] cipherByte = cipher.doFinal(rawPass.getBytes());
			result = BytesUtils.bytesToHex(cipherByte);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw new InvalidDataAccessApiUsageException(e.getMessage(), e);
		}
		String saltedPass = mergePasswordAndSalt(result, salt, false);

		return saltedPass;
	}

	public String decodePassword(final String rawPass, final Object salt) throws DataAccessException {
		String result = "";
		if (salt != null) {
			cryptKey = salt.toString();
		}

		try {
			KeyGenerator generator = KeyGenerator.getInstance(cryptInstance);
			generator.init(new SecureRandom(cryptKey.getBytes()));
			Key key = generator.generateKey();
			Cipher cipher = Cipher.getInstance(cryptInstance);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] cipherByte = cipher.doFinal(BytesUtils.hexToBytes(rawPass));
			result = new String(cipherByte);
		} catch (Exception e) {
			throw new InvalidDataAccessApiUsageException(e.getMessage(), e);
		}

		String saltedPass = mergePasswordAndSalt(result, salt, false);

		return saltedPass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.acegisecurity.providers.encoding.PasswordEncoder#isPasswordValid(java.lang.String, java.lang.String,
	 * java.lang.Object)
	 */
	public boolean isPasswordValid(final String encPass, final String rawPass, final Object salt)
			throws DataAccessException {
		String pass1 = "" + encPass;
		String pass2 = encodePassword(rawPass, salt);
		logger.debug("pass1:{}, pass2:{}", pass1, pass2);
		return pass1.equals(pass2);
	}
}
