/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.service.impl.VelocityServiceImpl
   Module Description   :

   Date Created      : 2008/3/14
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.service.impl;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;

import cc.macloud.core.account.utils.AdminHelper;
import cc.macloud.core.common.exception.CoreException;
import cc.macloud.core.common.service.TemplateService;
import cc.macloud.core.common.utils.DateUtils;
import cc.macloud.core.common.utils.StringUtils;
import cc.macloud.core.message.entity.Mail;

/**
 * @author jeffma
 * 
 */
public class VelocityServiceImpl implements TemplateService {

	/** logger */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private String pathPrefix = "velocity/";
	@Autowired(required = false)
	private AbstractMessageSource messageSource;

	/** default constructors */
	public VelocityServiceImpl() {
		try {
			Properties properties = System.getProperties();

			if (properties.get(VelocityEngine.RESOURCE_LOADER) == null) {
				properties.put(VelocityEngine.RESOURCE_LOADER, "classpath");
			}
			if (properties.get("classpath.resource.loader.class") == null) {
				properties.put("classpath.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			}
			if (properties.get(VelocityEngine.INPUT_ENCODING) == null) {
				properties.put(VelocityEngine.INPUT_ENCODING, "UTF-8");
			}
//			if (properties.get(VelocityEngine.OUTPUT_ENCODING) == null) {
//				properties.put(VelocityEngine.OUTPUT_ENCODING, "UTF-8");
//			}
//			if (properties.get(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS) == null) {
//				properties.put(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogSystem");
//			}
			Velocity.init(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** default constructors */
	public VelocityServiceImpl(Properties properties) {
		try {
			if (properties.get(VelocityEngine.RESOURCE_LOADER) == null) {
				properties.put(VelocityEngine.RESOURCE_LOADER, "classpath");
			}
			if (properties.get("classpath.resource.loader.class") == null) {
				properties.put("classpath.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			}
			if (properties.get(VelocityEngine.INPUT_ENCODING) == null) {
				properties.put(VelocityEngine.INPUT_ENCODING, "UTF-8");
			}
//			if (properties.get(VelocityEngine.OUTPUT_ENCODING) == null) {
//				properties.put(VelocityEngine.OUTPUT_ENCODING, "UTF-8");
//			}
//			if (properties.get(VelocityEngine.VM_LIBRARY) == null) {
//				properties.put(VelocityEngine.VM_LIBRARY, "org.apache.velocity.runtime.log.NullLogSystem");
//			}
			Velocity.init(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param pathPrefix
	 */
	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.template.service.TemplateService#format(java.lang.String, java.util.Map)
	 */

	public String format(String templateName, Map<String, Object> objs) throws CoreException {
		String result = "";
		VelocityContext context = new VelocityContext();
		if (objs != null) {
			Iterator<String> it = objs.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				context.put(key, objs.get(key));
			}
		}
		prepareDefault(context);

		try {
			StringWriter writer = new StringWriter();
			Template template = null;

			template = Velocity.getTemplate(pathPrefix + templateName + ".vm");
			if (template == null) {
				throw new CoreException("errors.common.template.empty", templateName);
			}
			template.merge(context, writer);
			writer.flush();
			result = writer.toString();
			writer.close();
		} catch (ResourceNotFoundException e) {
			throw new CoreException("errors.common.template.empty", e, templateName);
		} catch (Throwable e) {
			throw new CoreException("errors.common.template.unknow", e);
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
		Map<String, Object> m = new HashMap<String, Object>();
		if ((name != null) && (values != null)) {
			if (name.length == values.length) {
				for (int i = 0; i < values.length; i++) {
					m.put(name[i], values[i]);
					logger.debug("put key:" + name[i] + ",value:" + values[i]);
				}
			} else {
				throw new CoreException("errors.common.template.input", String.valueOf(name.length),
						String.valueOf(values.length));
			}
		}
		return formatByStringTemplate(template, m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cc.macloud.core.template.service.TemplateService#formatByStringTemplate(java.lang.String, java.util.Map)
	 */

	public String formatByStringTemplate(String template, Map<String, Object> objs) throws CoreException {
		String result = "";
		try {
			logger.debug("template:{},input:{}", template, objs);
			if (template == null) {
				return result;
			}

			VelocityContext context = new VelocityContext();
			if (objs != null) {
				Iterator<String> it = objs.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					context.put(key, objs.get(key));
				}
			}
			prepareDefault(context);

			StringWriter writer = new StringWriter();
			Velocity.evaluate(context, writer, "customize", template);
			writer.flush();
			result = writer.toString();
			writer.close();
		} catch (ResourceNotFoundException e) {
			throw new CoreException("errors.common.template.empty", e);
		} catch (Throwable e) {
			throw new CoreException("errors.common.template.unknow", e);
		}
		return result;
	}

	public Mail formatToMail(String templateName, Map<String, Object> objs) throws CoreException {
		String body = format(templateName, objs);
		String subject = StringUtils.parseTitle(body);
		Mail mail = new Mail(subject, body, null, null);
		return mail;
	}

	private void prepareDefault(VelocityContext context) {
		if (context.get("now") == null) {
			context.put("now", DateUtils.getCurrentTime());
		}
		if (context.get("format") == null) {
			context.put("format", new SimpleDateFormat());
		}
		if (context.get("numberTool") == null) {
			context.put("numberTool", new NumberTool());
		}
		if (context.get("dateTool") == null) {
			context.put("dateTool", new DateTool());
		}
		if (messageSource != null) {
			context.put("messageSource", messageSource);
		}
		context.put("adminHelper", AdminHelper.getInstance());
	}
}
