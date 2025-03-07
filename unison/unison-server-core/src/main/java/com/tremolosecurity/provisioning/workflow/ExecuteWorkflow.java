/*
Copyright 2015, 2016 Tremolo Security, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.tremolosecurity.provisioning.workflow;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.Session;

import com.tremolosecurity.config.util.ConfigManager;
import com.tremolosecurity.config.xml.WorkflowType;
import com.tremolosecurity.lastmile.LastMile;
import com.tremolosecurity.provisioning.core.ProvisioningEngine;
import com.tremolosecurity.provisioning.core.ProvisioningException;
import com.tremolosecurity.provisioning.core.Workflow;
import com.tremolosecurity.provisioning.objects.Users;
import com.tremolosecurity.provisioning.objects.Workflows;
import com.tremolosecurity.provisioning.service.util.WFCall;
import com.tremolosecurity.saml.Attribute;
import com.tremolosecurity.server.GlobalEntries;

public class ExecuteWorkflow {

	public int execute(WFCall wfcall, ConfigManager cfgMgr)
			throws Exception {
		Workflow wf = cfgMgr.getProvisioningEngine().getWorkFlow(wfcall.getName());
		
		if (wfcall.getEncryptedParams() != null) {
			LastMile lm = new LastMile();
			lm.loadLastMielToken(wfcall.getEncryptedParams(), GlobalEntries.getGlobalEntries().getConfigManager().getSecretKey(GlobalEntries.getGlobalEntries().getConfigManager().getCfg().getProvisioning().getApprovalDB().getEncryptionKey()));
			StringBuffer b = new StringBuffer();
			b.append('/').append(URLEncoder.encode(wfcall.getName(), "UTF-8"));
			if (! lm.isValid(b.toString())) {
				throw new ProvisioningException("Invalid parameters");
			} else {
				for (Attribute attr : lm.getAttributes()) {
					wfcall.getRequestParams().put(attr.getName(), attr.getValues().get(0));
				}
			}
		} else {
			boolean resultSet = false;
			for (WorkflowType wft : GlobalEntries.getGlobalEntries().getConfigManager().getCfg().getProvisioning().getWorkflows().getWorkflow()) {
				if (wft.getName().equalsIgnoreCase(wfcall.getName())) {
					if (wft.getDynamicConfiguration() != null && wft.getDynamicConfiguration().isDynamic()) {
						throw new ProvisioningException("Encrypted parameters not supplied");
					}
				}
			}
			
			
		}
		
		
		Map<String,Object> req = wf.executeWorkflow(wfcall);
		
		if (req.containsKey("WORKFLOW_ID")) {
			return (Integer) req.get("WORKFLOW_ID");
		} else {
			return -1;
		}

		

	}

	private boolean executeApprovals(WFCall wfcall, Workflow wf, ProvisioningEngine prov, String approvalReason,
			String approver) throws SQLException, ProvisioningException {
		Session session = GlobalEntries.getGlobalEntries().getConfigManager().getProvisioningEngine()
				.getHibernateSessionFactory().openSession();
		if (session != null) {
			Workflows workflow = wf.getFromDB(session);

			int approvalID = -1;

			Query query = session.createQuery("FROM Approvals WHERE workflow = :wfid");
			query.setParameter("wfid", workflow);
			List<com.tremolosecurity.provisioning.objects.Approvals> approvals = query.list();

			for (com.tremolosecurity.provisioning.objects.Approvals approval : approvals) {
				if (approval.getWorkflowObj() != null) {
					approvalID = approval.getId();
				}
			}

			session.close();

			if (approvalID == -1) {
				return false;
			} else {

				prov.doApproval(approvalID, approver, true, approvalReason);

				return true;
			}

		} else {
			return false;
		}
	}

}
