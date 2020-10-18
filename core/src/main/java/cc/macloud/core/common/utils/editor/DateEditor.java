/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.utils.editor.DateEditor
   Module Description   :

   Date Created      : 2012/6/6
   Original Author   : jeffma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.utils.editor;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

/**
 * @author jeffma
 * 
 */
public class DateEditor extends PropertyEditorSupport {

	private final DateFormat dateFormat19 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final DateFormat dateFormat16 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final DateFormat dateFormat10 = new SimpleDateFormat("yyyy-MM-dd");

	private final boolean allowEmpty = true;

	/**
	 * Parse the Date from the given text, using the specified DateFormat.
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		} else {
			try {
				if (text.length() == 10) {
					setValue(this.dateFormat10.parse(text));
				} else if (text.length() == 16) {
					setValue(this.dateFormat16.parse(text));
				} else if (text.length() == 19) {
					setValue(this.dateFormat19.parse(text));
				} else {
					throw new IllegalArgumentException("Could not parse date: it is not exactly" + text.length()
							+ "characters long");
				}
			} catch (ParseException ex) {
				throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
			}
		}
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		return (value != null ? this.dateFormat19.format(value) : "");
	}
}
