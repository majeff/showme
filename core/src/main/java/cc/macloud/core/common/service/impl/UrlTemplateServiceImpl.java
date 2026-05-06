/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.service.impl.UrlTemplateServiceImpl
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
package cc.macloud.core.common.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.TemplateService;
import cc.macloud.core.common.utils.StringUtils;
import cc.macloud.core.message.entity.Mail;

/**
 * @author jeffma
 *
 */
public class UrlTemplateServiceImpl implements TemplateService, Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 4557788151654954161L;
	/** logger */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	/** encoding, default: big5 */
	private String encoding = "big5";
	/** templateMapping, for (template name, url) */
	private Map<String, String> templateMapping = new HashMap<String, String>();
	// /** PROXY_HOST, defaut: null (none use) */
	// private String proxyHost = null;
	// /** PROXY_PORT, default: 8080 */
	// private int proxyPort = 8080;
	/** timeOut (milliseconds) */
	private int timeout = 1000;

	/** default constructors */
	public UrlTemplateServiceImpl() {
		super();
	}

	/**
	 * @param encoding
	 *           the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @param templateMapping
	 *           the templateMapping to set
	 */
	public void setTemplateMapping(Map<String, String> templateMapping) {
		this.templateMapping = templateMapping;
	}

	/**
	 * @param timeout
	 *           the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.template.service.TemplateService#format(java.lang.String, java.util.Map)
	 */

	public String format(String templateName, Map<String, Object> objs) throws CoreException {
		String result = "";
		HttpGet req = null;
		try {
			String uri = "";
			if (templateMapping.get(templateName) != null) {
				uri = templateMapping.get(templateName);
			} else {
				uri = templateName;
			}
			if (objs != null) {
				Iterator<String> it = objs.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					if (uri.indexOf("?") != -1) {
						uri += ("&" + key + "=" + URLEncoder.encode(objs.get(key).toString(), encoding));
					} else {
						uri += ("?" + key + "=" + URLEncoder.encode(objs.get(key).toString(), encoding));
					}
				}
			}
			req = new HttpGet(uri);
			if (encoding != null) {
				req.addHeader("Content-Type", "text/html; charset=" + encoding);
			}

			CloseableHttpClient client = HttpClientBuilder.create().build();
			if (timeout > 0) {
				RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
						.setSocketTimeout(timeout).build();
				req.setConfig(config);
			}

			// if (StringUtils.isNotEmpty(proxyHost)) {
			// client.getHostConfiguration().setProxy(proxyHost, proxyPort);
			// }
			logger.info("In:{}", uri);
			HttpResponse res = client.execute(req);
			if (res == null) {
				throw new CoreException("errors.common.template.url", uri);
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			res.getEntity().writeTo(baos);
			baos.flush();

			if (encoding == null) {
				result = baos.toString();
			} else {
				result = baos.toString(encoding);
			}
			baos.close();

			logger.debug("Out[{}]:{}", res, result);

		} catch (IOException e) {
			throw new CoreException("errors.common.template.url", e, templateName);
		} finally {

		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.template.service.TemplateService#formatByString(java.lang.String, java.lang.String[],
	 * java.lang.Object[])
	 */

	public String formatByString(String template, String[] name, Object[] values) throws CoreException {
		throw new CoreException("errors.common.template.nosupport", "URL");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see cc.macloud.core.template.service.TemplateService#formatByStringTemplate(java.lang.String, java.util.Map)
	 */

	public String formatByStringTemplate(String template, Map<String, Object> objs) throws CoreException {
		throw new CoreException("errors.common.template.nosupport", "URL");
	}

	public Mail formatToMail(String templateName, Map<String, Object> objs) throws CoreException {
		String body = format(templateName, objs);
		String subject = StringUtils.parseTitle(body);
		Mail mail = new Mail(subject, body, null, null);
		return mail;
	}

}
