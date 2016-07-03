/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : com.showmoney.core.common.dao.hibernate.AutoNoGenerator
   Module Description   :
	 1. 由指定Table,自動產生特定格式的編號. 
	 2. 傳回的編號格式: 流水號分類(如果有參數) + 日期(如果有參數) + 最新的編號.
	 3. 傳回的編號長度 = sn_catg_value 長度 + date_pattern 長度 + num_pattern 長度.
	 4. entity 如何使用此 generator:
 **
 * @return the tnsSeq
 * @hibernate.id column="TNS_SEQ" type="string" length="18" generator-class="com.showmoney.core.common.dao.hibernate.AutoNoGenerator" unsaved-value="null"
 * @hibernate.generator-param name="sn_catg_value" value="TM" (無預設值: 請依各 entity/UC 需求決定) 
 * @hibernate.generator-param name="date_pattern" value = "yyyyMMdd" (無預設值: 請依各 entity/UC 需求決定) 
 * @hibernate.generator-param name="num_pattern" value = "00000000"  (預設值如範例所示)
 * @hibernate.generator-param name="table" value = "AUTONO"         (預設值如範例所示)
 * @hibernate.generator-param name="pk_sn_catg" value = "sn_catg"    (預設值如範例所示)
 * @hibernate.generator-param name="pk_num_date" value = "num_date"  (預設值如範例所示)
 * @hibernate.generator-param name="column_num" value = "num"         (預設值如範例所示)
 **
	 5. 參數說明:
 **
 * @return the tnsSeq
 * @hibernate.id column="TNS_SEQ" type="string:資料型態是string" length="欄位長度" generator-class="自動編號class名稱" unsaved-value="要是null"
 * @hibernate.generator-param name="sn_catg_value:流水號分類的值" value="若不給流水號分類值,則回傳的編號不會串流水號分類"  
 * @hibernate.generator-param name="date_pattern:日期格式" value = "若不給日期格式,則回傳的編號不會串日期"
 * @hibernate.generator-param name="num_pattern:編號格式" value = "若不給編號格式,則系統預設為00000000"
 * @hibernate.generator-param name="table:紀錄最新可用的自動編號之檔名" value = "若不給自動編號檔名,則系統預設為 AUTONO" 
 * @hibernate.generator-param name="pk_sn_catg:流水號分類的欄位名稱" value = "若不給[流水號分類]欄位名稱,則系統預設為 sn_catg" 
 * @hibernate.generator-param name="pk_num_date:編號日期的欄位名稱" value = "若不給[編號日期]欄位名稱,則系統預設為 num_date" 
 * @hibernate.generator-param name="column_num:編號的欄位名稱" value = "若不給[編號]欄位名稱,則系統預設為 num" 
 **
   Date Created      : 2008/10/23
   Original Author   : jeff.ma
   Team              : 
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package com.showmoney.core.common.dao.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.engine.TransactionHelper;
import org.hibernate.id.Configurable;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.type.Type;
import org.hibernate.util.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoNoGenerator extends TransactionHelper implements PersistentIdentifierGenerator, Configurable {

	/** 參數: table, 自動編號檔名 */
	public static final String DEFAULT_TABLE_NAME = "COMM_AUTONO";

	/** 參數: value_sn_catg : 流水號分類的值 */
	public static final String SN_CATG_VALUE = "sn_catg_value";
	/** 預設流水號分類值: sn_catg */
	public static final String DEFAULT_SN_CATG_VALUE = "#@#@";

	/** 參數:date_pattern : 日期格式: ex: yyyyMMdd */
	public static final String DATE_PATTERN = "date_pattern";
	public static final String DEFAULT_DATE_PATTERN = "'#@#@'";
	public static final String DEFAULT_DATE_VALUE = "#@#@";

	/** 參數: pk_sn_catg : 主鍵的欄位名稱 */
	public static final String PK_SN_CATG = "pk_sn_catg";
	/** 預設主鍵欄位名稱: sn_catg 流水號分類分類 */
	public static final String DEFAULT_PK_SN_CATG_NAME = "sn_catg";

	/** 參數: pk_num_date : 主鍵的欄位名稱 */
	public static final String PK_NUM_DATE = "pk_num_date";
	/** 預設主鍵欄位名稱: num_date 編號日期 */
	public static final String DEFAULT_PK_NUM_DATE_NAME = "num_date";

	/** 參數: column_num : 編號的欄位名稱 */
	public static final String COLUMN_NUM = "column_num";
	/** 預設編號欄位名稱: num 編號, 用來記錄最新可用編號 */
	public static final String DEFAULT_COLUMN_NUM_NAME = "num";

	/** 參數: num_pattern : 編號格式 */
	public static final String NUM_PATTERN = "num_pattern";
	/** 預設編號輸出格式: 00000000 */
	public static final String DEFAULT_NUM_PATTERN = "00000000";

	/** 編號輸出格式: numPattern: 00000000 */
	private NumberFormat numFormatter = null;

	private static final Logger logger = LoggerFactory.getLogger(AutoNoGenerator.class); // NOPMD
	private static final String CLASS_NAME = "AutoNoGenerator";
	private static final SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS"); // NOPMD

	/** table name */
	private String tableName = DEFAULT_TABLE_NAME;
	/** 流水號分類的欄位名稱 */
	private String pkSnCatg = DEFAULT_PK_SN_CATG_NAME;
	/** 編號日期的欄位名稱 */
	private String pkNumDate = DEFAULT_PK_NUM_DATE_NAME;
	/** 編號的欄位名稱 */
	private String columnNum = DEFAULT_COLUMN_NUM_NAME;
	/** 日期格式 */
	private String datePattern;
	/** 編號輸出格式 */
	private String numPattern;
	/** 流水號分類的值 */
	private String snCatg;
	/** 編號日期的值 */
	private String numDate;
	private String query;
	private String insert;
	private String update;
	/**
	 * buildFlag, 0x01 for category+num, 0x02 for date+num, 0x03
	 * category+date+num
	 */
	private int buildFlag = -1;

	/** default constructors */
	public AutoNoGenerator() {
	}

	public void configure(Type type, Properties params, Dialect dialect) {

		// tableName = PropertiesHelper.getString(TABLE, params,
		// DEFAULT_TABLE_NAME);
		// pkSnCatg = PropertiesHelper.getString(PK_SN_CATG, params,
		// DEFAULT_PK_SN_CATG_NAME);
		// pkNumDate = PropertiesHelper.getString(PK_NUM_DATE, params,
		// DEFAULT_PK_NUM_DATE_NAME);
		// columnNum = PropertiesHelper.getString(COLUMN_NUM, params,
		// DEFAULT_COLUMN_NUM_NAME);
		tableName = DEFAULT_TABLE_NAME;
		pkSnCatg = DEFAULT_PK_SN_CATG_NAME;
		pkNumDate = DEFAULT_PK_NUM_DATE_NAME;
		columnNum = DEFAULT_COLUMN_NUM_NAME;

		snCatg = PropertiesHelper.getString(SN_CATG_VALUE, params, DEFAULT_SN_CATG_VALUE);
		datePattern = PropertiesHelper.getString(DATE_PATTERN, params, DEFAULT_DATE_PATTERN);
		numPattern = PropertiesHelper.getString(NUM_PATTERN, params, DEFAULT_NUM_PATTERN);
		buildFlag = PropertiesHelper.getInt("build_flag", params, -1);
		if (buildFlag == -1) {
			buildFlag = 0x00;
			if (!DEFAULT_SN_CATG_VALUE.equals(snCatg)) {
				buildFlag = buildFlag | 0x01;
			}
			if (!DEFAULT_DATE_PATTERN.equals(datePattern)) {
				buildFlag = buildFlag | 0x02;
			}
		}

		// 數字的 format
		this.numFormatter = new DecimalFormat(numPattern);

		query = "select " + columnNum + " from " + dialect.appendLockHint(LockMode.PESSIMISTIC_WRITE, tableName)
				+ " where " + pkSnCatg + " = ? " + " and " + pkNumDate + " = ? " + dialect.getForUpdateString();

		insert = "insert into " + tableName + " (" + pkSnCatg + ", " + pkNumDate + ", " + columnNum + ", "
				+ "crt_usr, crt_ts, upd_usr, upd_ts, " + "usr_ip, upd_prg, ver)"
				+ " values (  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		update = "update " + tableName + " set 	" + columnNum + " = ?  , " + "   upd_ts = ? " + " where " + pkSnCatg
				+ " = ? " + " and " + pkNumDate + " = ? ";
	}

	public synchronized Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		String result = (String) doWorkInNewTransaction(session);
		return result;
	}

	public String[] sqlCreateStrings(Dialect dialect) {
		return new String[] {
				"create table " + tableName + " ( " + pkSnCatg + " " + dialect.getTypeName(Types.VARCHAR, 30, 0, 0) + " , "
						+ pkNumDate + " " + dialect.getTypeName(Types.VARCHAR, 30, 0, 0) + " , " + columnNum + " "
						+ dialect.getTypeName(Types.BIGINT) + " , " + " crt_usr  "
						+ dialect.getTypeName(Types.VARCHAR, 30, 0, 0) + " , " + " crt_ts "
						+ dialect.getTypeName(Types.VARCHAR, 30, 0, 0) + " , " + " upd_usr  "
						+ dialect.getTypeName(Types.VARCHAR, 30, 0, 0) + " , " + " upd_ts  "
						+ dialect.getTypeName(Types.VARCHAR, 30, 0, 0) + " , " + " usr_ip  "
						+ dialect.getTypeName(Types.VARCHAR, 30, 0, 0) + " , " + " upd_prg  "
						+ dialect.getTypeName(Types.VARCHAR, 30, 0, 0) + " , " + " ver  "
						+ dialect.getTypeName(Types.TIMESTAMP) + " , " + " tx_apv_mgr1  "
						+ dialect.getTypeName(Types.VARCHAR, 30, 0, 0) + " , " + " tx_apv_mgr2  "
						+ dialect.getTypeName(Types.VARCHAR, 30, 0, 0) + ")",
				"ALTER TABLE " + tableName + " ADD CONSTRAINT AUTONO_PK PRIMARY KEY(SN_CATG, NUM_DATE, NUM)" };
	}

	public String[] sqlDropStrings(Dialect dialect) {
		StringBuffer sqlDropString = new StringBuffer("drop table ");
		if (dialect.supportsIfExistsBeforeTableName()) {
			sqlDropString.append(" if exists ");
		}
		sqlDropString.append(tableName).append(dialect.getCascadeConstraintsString());
		if (dialect.supportsIfExistsAfterTableName()) {
			sqlDropString.append(" if exists ");
		}
		return new String[] { sqlDropString.toString() };
	}

	public Object generatorKey() {
		return tableName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.hibernate.engine.TransactionHelper#doWorkInCurrentTransaction(java
	 * .sql.Connection, java.lang.String)
	 */
	@Override
	public Serializable doWorkInCurrentTransaction(final Connection conn, final String sql) throws SQLException {
		String result = null;
		int rows;
		long num;
		DateFormat df = null;
		@SuppressWarnings("unused")
		int count = 0; // 更新 0 笔为错误. 未实作

		// 依 datePattern 格式化日期
		df = new SimpleDateFormat(datePattern);
		Date now = Calendar.getInstance().getTime();

		do {
			// The loop ensures atomicity of the
			// select + update even for no transaction
			// or read committed isolation level

			// 只產生今天日期內的編號
			numDate = df.format(now);

			// Query編號
			// sql = query;
			// SQL.debug(query);
			PreparedStatement qps = conn.prepareStatement(query);
			try {
				qps.setString(1, snCatg);
				qps.setString(2, numDate);
				ResultSet rs = qps.executeQuery();

				if (!rs.next()) {
					// 查無資料,則 Insert 一筆新的
					rs.close();
					qps.close();

					// sql = insert;
					// SQL.debug(insert);
					PreparedStatement ips = conn.prepareStatement(insert);
					try {
						ips.setString(1, snCatg);
						ips.setString(2, numDate);
						ips.setInt(3, 1);
						ips.setString(4, "system");
						ips.setString(5, defaultDateFormat.format(now));
						ips.setString(6, "system");
						ips.setString(7, defaultDateFormat.format(now));
						ips.setString(8, "127.0.0.1");
						ips.setString(9, CLASS_NAME);
						ips.setTimestamp(10, (new Timestamp(now.getTime())));
						count = ips.executeUpdate();
						num = 1;
					} catch (SQLException sqle) {
						logger.error("could not update hi value sql: {}, msg: {}", insert, sqle.getMessage());
						throw sqle;
					} finally {
						ips.close();
					} // end of insert
				} else {
					// 查出資料,則取出最新的編號
					num = rs.getLong(1);
					rs.close();
					qps.close();
				}
			} catch (SQLException sqle) {
				logger.error("could not read a hi value, sql: {}, msg: {}", query, sqle);
				qps.close();
				throw sqle;
			} // end of query

			// Update 自動編號檔,紀錄下一筆可用編號(已使用編號 + 1).
			// sql = update;
			// SQL.debug(update);
			PreparedStatement ups = conn.prepareStatement(update);
			try {
				ups.setLong(1, (num + 1));
				ups.setString(2, defaultDateFormat.format(now));
				ups.setString(3, snCatg);
				ups.setString(4, numDate);
				rows = ups.executeUpdate();
			} catch (SQLException sqle) {
				logger.error("could not update hi value sql: {}, msg: {}", update, sqle.getMessage());
				throw sqle;
			} finally {
				ups.close();
			} // end of update
		} while (rows == 0);

		// 回傳值: 分類值(若有sn_catg_value參數值) +指定的日期格式(若有date_pattern) +字串化的數字
		// 若同時無參數值sn_catg_value, date_pattern,則只傳回 字串化的數字
		switch (buildFlag) {
		case 0x00:
			result = numFormatter.format(num);
			break;
		case 0x01: //
			result = snCatg + numFormatter.format(num);
			break;
		case 0x02:
			result = numDate + numFormatter.format(num);
			break;
		case 0x03:
			result = snCatg + numDate + numFormatter.format(num);
			break;
		default:
			result = numFormatter.format(num);
		}

		return result;

	}
}
