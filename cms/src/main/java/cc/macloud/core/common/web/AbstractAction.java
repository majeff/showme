/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.cms.core.web.AbstractAction
   Module Description   :

   Date Created      : 2012/5/30
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import cc.macloud.core.common.utils.editor.DateEditor;

/**
 * @author jeffma
 * 
 */
public abstract class AbstractAction {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("beanValidator")
	protected Validator beanValidator;

	// @InitBinder
	// public void initBinder(WebDataBinder binder) {
	// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	// binder.registerCustomEditor(Date.class, new DateEditor(dateFormat, false));
	// }

	@InitBinder
	public void prepare(HttpServletRequest request, WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateEditor());
		// logger.debug("prepare url:{} target:{}", request.getServletPath(), binder.getTarget());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.OK)
	public ModelAndView handleAllExceptions(Exception ex) {
		logger.error("action error", ex);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("error", ex.getMessage());
		return new ModelAndView("errors", model);
	}

}
