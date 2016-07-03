/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.common.utils.FtpUtils
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
package com.showmoney.core.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.showmoney.core.common.exception.CoreException;

/**
 * @author jeffma
 * 
 */
public final class FtpUtils {

	/** logger */
	private static final Logger logger = LoggerFactory.getLogger(FtpUtils.class); // NOPMD

	/** default constructors */
	private FtpUtils() {
	}

	/**
	 * write file (check file exist, if exist rename with date)
	 * 
	 * @param sb file content
	 * @param fileName file location and filename
	 * @return file
	 * @throws CoreException
	 */
	public static File writeFile(StringBuffer sb, String fileName, String encoding) throws CoreException {
		// write file
		File file = null;
		file = new File(fileName);
		FileOutputStream fos = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			} else {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddkkmmss");
				file.renameTo(new File(fileName.replaceAll(".txt", "") + "." + format.format(Calendar.getInstance().getTime())
						+ ".txt"));
				logger.warn("Rename old file");
				file.createNewFile();
			}

			fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes(encoding));
			fos.flush();
		} catch (IOException e) {
			throw new CoreException("errors.system.file", e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * check rempte ftp server's file size
	 * 
	 * @param host ftp hostname
	 * @param port ftp port
	 * @param remoteLocation file location
	 * @param remoteFileName file name
	 * @param userid account id
	 * @param password account password
	 * @param size file size
	 * @return result
	 * @throws IOException
	 */
	public static boolean checkRemoteFileSize(String host, int port, String remoteLocation, String remoteFileName,
			String userid, String password, int size) throws IOException {
		boolean result = false;

		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(host, port);
		ftpClient.login(userid, password);
		ftpClient.sendSiteCommand("cd " + remoteLocation);
		FTPFile[] files = ftpClient.listFiles(remoteLocation);

		for (FTPFile file : files) {
			if (file.getSize() == size) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * upload file by FTP protocol (ASCII)
	 * 
	 * @param host ftp host name
	 * @param port ftp port
	 * @param remoteLocation remote location
	 * @param remoteFileName remote file name
	 * @param userid ftp account id
	 * @param password ftp account password
	 * @param is for upload file
	 */
	public static void uploadFile(String host, int port, String remoteLocation, String remoteFileName, String userid,
			String password, InputStream is) {
		uploadFile(host, port, remoteLocation, remoteFileName, userid, password, is, true);
	}

	/**
	 * upload file by FTP protocol
	 * 
	 * @param host ftp host name
	 * @param port ftp port
	 * @param remoteLocation remote location
	 * @param remoteFileName remote file name
	 * @param userid ftp account id
	 * @param password ftp account password
	 * @param is for upload file
	 * @param flag true:ASCII, false:BINARY
	 */
	public static void uploadFile(String host, int port, String remoteLocation, String remoteFileName, String userid,
			String password, InputStream is, boolean flag) {
		boolean successFlag = false;
		FTPClient ftpClient = null;
		int retry = 0;
		while (!(retry > 3 || (retry < 3 && successFlag))) {
			logger.debug("host:{}, file:{},retry={}", new Object[] { host, remoteLocation + remoteFileName, retry });
			try {
				ftpClient = new FTPClient();
				ftpClient.connect(host, port);
				ftpClient.login(userid, password);
				if (flag) {
					ftpClient.sendSiteCommand("ascii");
				} else {
					ftpClient.sendSiteCommand("binary");
				}
				ftpClient.sendSiteCommand("cd " + remoteLocation);

				successFlag = ftpClient.storeFile(remoteFileName, is);

				ftpClient.disconnect();
			} catch (IOException e1) {
				retry++;
				try {
					Thread.sleep(10000); // sleep 10 second
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				logger.error("Error", e1);
			}
		}
	}
}
