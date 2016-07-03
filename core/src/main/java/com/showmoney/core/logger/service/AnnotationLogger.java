/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.logger.service.AnnotationLogger
   Module Description   :

   Date Created      : 2012/5/25
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.logger.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jeffma
 * 
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AnnotationLogger {
	public enum Type {
		XML, JSON;
	}

	boolean log() default true;

	/**
	 * 僅為監控使用, 預設 false
	 * 
	 * @return
	 */
	boolean monitor() default false;

	/**
	 * log 格式為何, 預設 XML
	 * 
	 * @return
	 */
	Type type() default Type.XML;

	/**
	 * 提供傳入參數的可搜尋欄位 property
	 * 
	 * @return
	 */
	String key() default "";

	/**
	 * 若 entity 有欄位為 lazy (意味不會進行修改), 請將對應欄位設於此, 對應欄位將不記錄
	 * 
	 * @return
	 */
	String[] ignoreProperties() default {};
}
