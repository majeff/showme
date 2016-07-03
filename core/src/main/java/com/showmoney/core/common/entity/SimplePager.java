/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.common.entity.SimplePager
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
package com.showmoney.core.common.entity;

import java.io.Serializable;

/**
 * @author jeffma
 */
public class SimplePager implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 7151721763327843272L;

	public final static int DISPLAY_NUM = 9;

	/** pageRecord, 每頁筆数 */
	private int pageRecord = 10;
	/** currentPage, 目前頁數 */
	private int currentPage = 0;
	/** totalSize, 全部筆數 */
	private long totalSize = 0;

	// cache
	/** pageSize, 全部頁數 */
	private int pageSize = 1;
	private int startPage = 1;
	private int endPage = 1;

	/**
	 * <pre>
	 * pagerType: 0 (default) 
	 * 最多顯示 10頁, 優先置中, 左右最多各 5 
	 * 
	 * pagerType: 1 
	 * 固定顯示 1-10, 11-20, 21-30 
	 * 
	 * pagerType: 2 
	 * 優先顯示10頁, 其次優先至中
	 * </pre>
	 */
	private int pagerType = 2;

	/** default constructor */
	public SimplePager() {
	}

	public SimplePager(int pagerType) {
		this.pagerType = pagerType;
	}

	public SimplePager(int pagerType, int pageRecord) {
		this.pagerType = pagerType;
		this.pageRecord = pageRecord;
	}

	private void calculate() {
		pageSize = (int) (totalSize / pageRecord);
		if (((totalSize % pageRecord) != 0) || (totalSize == 0)) {
			pageSize++;
		}

		// currentPage 最小為 0, 最大為 pageSize-1
		if (currentPage > (pageSize - 1)) {
			currentPage = pageSize - 1;
		} else if (currentPage < 0) {
			currentPage = 0;
		}

		if (pagerType == 1) {
			// 固定顯示 1-10, 11-20, 21-30
			startPage = ((currentPage / 10) * 10) + 1;
			endPage = (startPage + 9) > pageSize ? pageSize : startPage + 9;
		} else if (pagerType == 2) {
			// 顯示 10頁優先,
			// if (currentPage > (pageSize - (DISPLAY_NUM - 1))) {
			// startPage = pageSize - DISPLAY_NUM - 1;
			// } else
			if (currentPage > ((DISPLAY_NUM / 2) - 1)) {
				startPage = currentPage - ((DISPLAY_NUM / 2) - 1);
			}
			if (startPage < 1) {
				startPage = 1;
			}

			if ((startPage + DISPLAY_NUM) > pageSize) {
				endPage = pageSize;
			} else {
				endPage = (startPage + DISPLAY_NUM);
			}
		} else {
			// 最多顯示 10頁, 優先置中, 左右最多各 5
			if (currentPage > ((DISPLAY_NUM / 2) - 1)) {
				startPage = currentPage - ((DISPLAY_NUM / 2) - 1);
			} else {
				startPage = 1;
			}
			if ((currentPage + ((DISPLAY_NUM / 2) + 1)) > pageSize) {
				endPage = pageSize;
			} else {
				endPage = currentPage + ((DISPLAY_NUM / 2) + 1);
			}
		}
	}

	/**
	 * @return the totalSize
	 */
	public long getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize
	 *           the totalSize to set
	 */
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
		calculate();
	}

	/**
	 * @return the pageRecord
	 */
	public int getPageRecord() {
		return pageRecord;
	}

	/**
	 * @param pageRecord
	 *           the pageRecord to set
	 */
	public void setPageRecord(int pageRecord) {
		this.pageRecord = pageRecord;
	}

	/**
	 * 最小為 0, 最大為 pageSize-1
	 * 
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage
	 *           the currentPage to set
	 */
	public void setCurrentPage(int currentPage) { // NOPMD
		this.currentPage = currentPage;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return the startPage
	 */
	public int getStartPage() {
		return startPage;
	}

	/**
	 * @return the endPage
	 */
	public int getEndPage() {
		return endPage;
	}

	public int getStartRecord() {
		return currentPage == 0 ? 0 : (currentPage * pageRecord) + 1;
	}

	public int getEndRecord() {
		return ((currentPage + 1) * pageRecord) > totalSize ? (int) totalSize : ((currentPage + 1) * pageRecord);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SimplePager [currentPage=" + currentPage + ", pageRecord=" + pageRecord + ", pageSize=" + pageSize
				+ ", startPage=" + startPage + ", endPage=" + endPage + ", totalSize=" + totalSize + "]";
	}

}
