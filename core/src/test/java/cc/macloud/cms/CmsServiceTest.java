/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   Module Name          : cc.macloud.cms.CmsServiceTest
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
package cc.macloud.cms;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.velocity.tools.generic.NumberTool;
import org.junit.BeforeClass;
import org.junit.Test;

import cc.macloud.cms.content.entity.Content;
import cc.macloud.cms.content.entity.ContentTemplate;
import cc.macloud.cms.content.entity.ContentTemplateElement;
import cc.macloud.cms.content.entity.ContentTemplateElement.Type;
import cc.macloud.cms.content.service.ContentService;
import cc.macloud.cms.content.service.ContentTemplateService;
import cc.macloud.cms.workflow.entity.FlowAction;
import cc.macloud.cms.workflow.entity.FlowNode;
import cc.macloud.cms.workflow.entity.Workflow;
import cc.macloud.cms.workflow.service.WorkflowService;
import cc.macloud.core.common.utils.SpringCommonTest;

/**
 * @author jeffma
 *
 */
public class CmsServiceTest extends SpringCommonTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	static public void setUpBeforeClass() throws Exception {
		configCtx();
	}

	/**
	 * Test method for
	 * {@link cc.macloud.cms.content.service.ContentService#publish(cc.macloud.cms.content.entity.Content, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCRUDWorkflow() {
		WorkflowService service = ctx.getBean(WorkflowService.class);
		Workflow obj = new Workflow("TEST", "測試");
		FlowNode node1 = new FlowNode(obj, Content.Status.EDITING.getDesc(), Content.Status.EDITING.getCode());
		obj.setStartNode(node1);

		service.save(obj);

		FlowNode node2 = new FlowNode(obj, Content.Status.PUBLISH.getDesc(), Content.Status.PUBLISH.getCode());
		service.saveNode(node2);
		FlowAction action1 = new FlowAction(node2, node2.getStatus(), "TEST2");
		node1.addAction(action1);

		obj.setDescription("測試測試");
		service.save(obj);

		service.delete(obj);
	}

	@Test
	public void test0PrepareWorkflow() {
		WorkflowService service = ctx.getBean(WorkflowService.class);
		Workflow obj = service.get("PUBLISH_DIRECT");
		if (obj == null) {
			obj = new Workflow("PUBLISH_DIRECT", "直接發佈");
		}
		FlowNode node1 = new FlowNode(obj, Content.Status.EDITING.getDesc(), Content.Status.EDITING.getCode());
		FlowNode node2 = null;
		if ((obj.getStartNode() != null) && obj.getStartNode().getStatus().equals(Content.Status.PUBLISH.getCode())) {
			node2 = obj.getStartNode();
		} else {
			node2 = new FlowNode(obj, Content.Status.PUBLISH.getDesc(), Content.Status.PUBLISH.getCode());
		}
		FlowAction action1 = new FlowAction(node2, "publish", Content.Status.PUBLISH.getDesc());
		node1.addAction(action1);
		obj.setStartNode(node1);

		service.save(obj);
	}

	@Test
	public void test1PrepareTemplate() {
		ContentTemplateService service = ctx.getBean(ContentTemplateService.class);
		ContentTemplate template = service.get("TEST01");
		if (template == null) {
			template = new ContentTemplate("TEST01", "測試用樣板(直接發佈, 不控管版本)", false, "PUBLISH_DIRECT");
			template.addElement(new ContentTemplateElement("text01", "測試1", "測試1", Type.TEXT));
			template.addElement(new ContentTemplateElement("text02", "測試2", "測試2", Type.CHECKBOX));
			template.addElement(new ContentTemplateElement("text03", "測試3", "測試3", Type.DATE));
			template.addElement(new ContentTemplateElement("text04", "測試4", "測試4", Type.FILE));
			template.addElement(new ContentTemplateElement("text05", "測試5", "測試5", Type.IMAGE));
			template.addElement(new ContentTemplateElement("text06", "測試6", "測試6", Type.MENU));
			template.addElement(new ContentTemplateElement("text07", "測試7", "測試7", Type.MULTIMENU));
			template.addElement(new ContentTemplateElement("text08", "測試8", "測試8", Type.NUMBER));
			template.addElement(new ContentTemplateElement("text09", "測試9", "測試9", Type.RADIO));
			template.addElement(new ContentTemplateElement("text10", "測試10", "測試10", Type.RICH));
			template.addElement(new ContentTemplateElement("text11", "測試11", "測試11", Type.TEXT));
			template.addElement(new ContentTemplateElement("text12", "測試12", "測試12", Type.TEXTAREA));
			template.addElement(new ContentTemplateElement("text13", "測試13", "測試13", Type.TIME));
			template.addElement(new ContentTemplateElement("text14", "測試14", "測試14", Type.TIMESTAMP));
			service.save(template);
		}
		template = service.get("TEST02");
		if (template == null) {
			template = new ContentTemplate("TEST02", "測試用樣板(直接發佈, 控管版本)", true, "PUBLISH_DIRECT");
			template.addElement(new ContentTemplateElement("text01", "測試1", "測試1", Type.TEXT));
			template.addElement(new ContentTemplateElement("text02", "測試2", "測試2", Type.CHECKBOX));
			template.addElement(new ContentTemplateElement("text03", "測試3", "測試3", Type.DATE));
			template.addElement(new ContentTemplateElement("text04", "測試4", "測試4", Type.FILE));
			template.addElement(new ContentTemplateElement("text05", "測試5", "測試5", Type.IMAGE));
			template.addElement(new ContentTemplateElement("text06", "測試6", "測試6", Type.MENU));
			template.addElement(new ContentTemplateElement("text07", "測試7", "測試7", Type.MULTIMENU));
			template.addElement(new ContentTemplateElement("text08", "測試8", "測試8", Type.NUMBER));
			template.addElement(new ContentTemplateElement("text09", "測試9", "測試9", Type.RADIO));
			template.addElement(new ContentTemplateElement("text10", "測試10", "測試10", Type.RICH));
			template.addElement(new ContentTemplateElement("text11", "測試11", "測試11", Type.TEXT));
			template.addElement(new ContentTemplateElement("text12", "測試12", "測試12", Type.TEXTAREA));
			template.addElement(new ContentTemplateElement("text13", "測試13", "測試13", Type.TIME));
			template.addElement(new ContentTemplateElement("text14", "測試14", "測試14", Type.TIMESTAMP));
			service.save(template);
		}
		template = service.get("COMP_ANNOUNCE");
		if (template == null) {
			template = new ContentTemplate("COMP_ANNOUNCE", "內部公告", true, "PUBLISH_DIRECT");
			template.addElement(new ContentTemplateElement("body", "主文", "主文", Type.RICH));
			service.save(template);
		}
	}

	@Test
	public void test2() {
		ContentService service = ctx.getBean(ContentService.class);
		ContentTemplateService tempService = ctx.getBean(ContentTemplateService.class);
		ContentTemplate template = tempService.get("COMP_ANNOUNCE");
		NumberTool numTool = new NumberTool();
		for (int i = 2; i < 100; i++) {
			Content content = new Content(template);
			content.setTitle("測試" + numTool.format("000", i));
			content.getElements().get("body").setClob("測試" + numTool.format("000", i));
			service.save(content);

			service.publish(content, null, "save & publish");
		}
	}

	/**
	 * Test method for
	 * {@link cc.macloud.cms.content.service.ContentService#createContent(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateContent() {
		JSONObject json = new JSONObject();
		JSONArray labels = new JSONArray();
		JSONArray values = new JSONArray();
		for (int i = 1; i <= 5; i++) {
			labels.add("label" + i);
			values.add("value" + i);
		}
		json.accumulate("labels", labels);
		json.accumulate("values", values);
		// .key("labels").array().object().key("l1").value("lable1").endObject().object().key("l2").value("lable2")
		// .endObject().endArray();
		// build.key("values").array().object().key("v1").value("value1").endObject().object().key("v2").value("value2")
		// .endObject().endArray();
		System.out.println("json:" + json.toString());
	}
}
