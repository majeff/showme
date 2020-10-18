/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.common.utils.FileUtils
   Module Description   :

   Date Created      : 2010/1/20
   Original Author   : jeff.ma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.common.utils;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.oro.io.GlobFilenameFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jeff.ma
 * 
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

	public static int getExistingFileSerialNo(File folder, String prefix) {
		int serialNo = 0;
		FilenameFilter ff = new GlobFilenameFilter(prefix + "*.*");
		String[] fileList = folder.list(ff);

		int psize = prefix.getBytes().length;
		logger.debug("get file size: {}, prefix:{}", fileList.length, prefix);
		for (String filename : fileList) {
			int serialNoOfFile = Integer.parseInt(filename.substring(psize, filename.indexOf(".", psize)));
			logger.warn("match; fileno:{}, file:{}", serialNoOfFile, filename);
			if (serialNoOfFile > serialNo) {
				serialNo = serialNoOfFile;
			}
		}
		return serialNo;
	}
}
