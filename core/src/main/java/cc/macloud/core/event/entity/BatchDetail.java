/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.core.event.entity.BatchDetail
   Module Description   :

   Date Created      : 2011/2/9
   Original Author   : jeffma
   Team              : yaodian100
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   MODIFICATION HISTORY
   ------------------------------------------------------------------------------
   Date Modified       Modified by       Comments
   ------------------------------------------------------------------------------
   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
package cc.macloud.core.event.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.json.JSONObject;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import cc.macloud.core.common.utils.StringUtils;

/**
 * @author jeffma
 * 
 */
@Entity
@Table(name = "CORE_EVENT_DETAIL")
public class BatchDetail implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 3570902641823249449L;

	/** uuid, PK 由 hibernate 自行定义 */
	@Id
	@GeneratedValue(generator = "gen_uuid")
	@GenericGenerator(name = "gen_uuid", strategy = "uuid")
	@Column(name = "OBJ_UUID", length = 32)
	private String uuid;

	/** batchEvent, 关联批次主档, lazy */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_UUID")
	@NotFound(action = NotFoundAction.IGNORE)
	private BatchEvent batchEvent;

	/** dataKey, 批次资料的 domain object id(PK), CREATE job 请丢 uuid */
	@Column(name = "DATA_KEY", length = 100)
	private String dataKey;
	/** dataOld, 原始资料资料, 以 json 格式存放 */
	@Column(name = "DATA_OLD", length = 2000)
	private String dataOld;
	/** dataNew, 修改或新增资料, 以 json 格式存放 */
	@Column(name = "DATA_NEW", length = 2000)
	private String dataNew;
	/** dataOldMap, 暂存用, 实际存放于 dataOld */
	@Transient
	private Map<String, String> dataOldMap;
	/** dataNewMap, 暂存用, 实际存放于 dataNew */
	@Transient
	private Map<String, String> dataNewMap;

	/**
	 * executeStatus 执行状态<br/>
	 * null 表示未執行, True 為成功, False 為失敗
	 */
	@Column(name = "RUN_STATUS")
	private Boolean executeStatus = null;
	/**
	 * failMessage 错误讯息<br/>
	 * 格式为 错误型态,错误明细<br/>
	 * 错误型态目前分 "原值不符", "系统错误"
	 */
	@Column(name = "FAIL_MSG", length = 200)
	private String failMessage = null;

	/** default constructor */
	public BatchDetail() {
		super();
	}

	/** default constructor */
	public BatchDetail(BatchEvent batchEvent) {
		super();
		this.batchEvent = batchEvent;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid
	 *           the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the batchEvent
	 */
	public BatchEvent getBatchEvent() {
		return batchEvent;
	}

	/**
	 * @param batchEvent
	 *           the batchEvent to set
	 */
	public void setBatchEvent(BatchEvent batchEvent) {
		this.batchEvent = batchEvent;
	}

	/**
	 * @return the dataKey
	 */
	public String getDataKey() {
		return dataKey;
	}

	/**
	 * @param dataKey
	 *           the dataKey to set
	 */
	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	/**
	 * @return the dataOld
	 */
	public String getDataOld() {
		dataOld = JSONObject.fromObject(getDataOldMap()).toString();
		return dataOld;
	}

	/**
	 * @param dataOld
	 *           the dataOld to set
	 */
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	private void setDataOld(String dataOld) {
		this.dataOld = dataOld;
		dataOldMap = (Map) JSONObject.toBean(JSONObject.fromObject(dataOld), Map.class);
	}

	public void addDataOld(String key, String value) {
		getDataOldMap().put(key, value);
	}

	/**
	 * @return the dataNew
	 */
	public String getDataNew() {
		dataNew = JSONObject.fromObject(getDataNewMap()).toString();
		return dataNew;
	}

	/**
	 * @param dataNew
	 *           the dataNew to set
	 */
	@SuppressWarnings("unused")
	private void setDataNew(String dataNew) {
		this.dataNew = dataNew;
	}

	public void addDataNew(String key, String value) {
		getDataNewMap().put(key, value);
	}

	/**
	 * @return the executeStatus
	 */
	public Boolean getExecuteStatus() {
		return executeStatus;
	}

	/**
	 * @param executeStatus
	 *           the executeStatus to set
	 */
	public void setExecuteStatus(Boolean executeStatus) {
		this.executeStatus = executeStatus;
	}

	/**
	 * @return the failMessage
	 */
	public String getFailMessage() {
		return failMessage;
	}

	@Transient
	public String getFailTitle() {
		if (StringUtils.isNotBlank(failMessage) && (failMessage.indexOf(",") != -1)) {
			return StringUtils.split(failMessage, ",")[0];
		}
		return "";
	}

	/**
	 * @param failMessage
	 *           the failMessage to set
	 */
	public void setFailMessage(String failMessage) {
		this.failMessage = failMessage;
	}

	/**
	 * @return the dataOldMap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getDataOldMap() {
		if ((dataOld != null) && (dataOldMap == null)) {
			dataOldMap = (Map) JSONObject.toBean(JSONObject.fromObject(dataOld), Map.class);
		}
		if (dataOldMap == null) {
			dataOldMap = new HashMap();
		}
		return dataOldMap;
	}

	/**
	 * @return the dataNewMap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String> getDataNewMap() {
		if ((dataNew != null) && (dataNewMap == null)) {
			dataNewMap = (Map) JSONObject.toBean(JSONObject.fromObject(dataNew), Map.class);
		}
		if (dataNewMap == null) {
			dataNewMap = new HashMap();
		}
		return dataNewMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BatchDetail [uuid=" + uuid + ", dataKey=" + dataKey + ", executeStatus=" + executeStatus
				+ ", failMessage=" + failMessage + "]";
	}
}
