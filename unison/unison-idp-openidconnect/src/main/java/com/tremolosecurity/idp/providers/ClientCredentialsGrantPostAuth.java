/*******************************************************************************
 * Copyright 2021 Tremolo Security, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.tremolosecurity.idp.providers;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.tremolosecurity.config.util.UrlHolder;
import com.tremolosecurity.config.xml.AuthChainType;
import com.tremolosecurity.idp.providers.oidc.db.StsRequest;
import com.tremolosecurity.idp.providers.oidc.model.OidcSessionState;
import com.tremolosecurity.log.AccessLog;
import com.tremolosecurity.log.AccessLog.AccessEvent;
import com.tremolosecurity.proxy.auth.AuthController;
import com.tremolosecurity.proxy.auth.AuthInfo;
import com.tremolosecurity.proxy.auth.AzSys;
import com.tremolosecurity.proxy.auth.PostAuthSuccess;
import com.tremolosecurity.proxy.auth.RequestHolder;
import com.tremolosecurity.proxy.util.NextSys;
import com.tremolosecurity.proxy.util.ProxyConstants;
import com.tremolosecurity.saml.Attribute;
import com.tremolosecurity.server.GlobalEntries;

public class ClientCredentialsGrantPostAuth implements PostAuthSuccess {
	
	static Logger logger = Logger.getLogger(ClientCredentialsGrantPostAuth.class);
	
	AzSys azSys;
	
	OpenIDConnectTransaction transaction;
	OpenIDConnectTrust trust;
	OpenIDConnectIdP idp;

	
	public ClientCredentialsGrantPostAuth(OpenIDConnectTransaction transaction,OpenIDConnectTrust trust,OpenIDConnectIdP idp) {
		this.azSys = new AzSys();
		
		
		this.transaction = transaction;
		this.trust = trust;
		this.idp = idp;
	}
	
	
	@Override
	public void runAfterSuccessfulAuthentication(HttpServletRequest req, HttpServletResponse resp, UrlHolder holder,
			AuthChainType act, RequestHolder reqHolder, AuthController actl, NextSys next)
			throws IOException, ServletException {
		
		HttpSession session = req.getSession();
		AuthInfo authData = ((AuthController) session.getAttribute(ProxyConstants.AUTH_CTL)).getAuthInfo();
		
		if (! azSys.checkRules(authData, GlobalEntries.getGlobalEntries().getConfigManager(), trust.getClientAzRules(), new HashMap<String,Object>())) {
			AccessLog.log(AccessEvent.AzFail, holder.getApp(), req, authData, new StringBuilder().append("client not authorized for client_credentials grant on trust '").append(trust.getClientID()).append("'").toString());
			resp.sendError(403);
			return;
		} 
		
		JSONObject existingClaims = new JSONObject();
		for (String attrName : authData.getAttribs().keySet()) {
			Attribute attr = authData.getAttribs().get(attrName);
			if (attr.getValues().size() == 1) {
				existingClaims.put(attrName, attr.getValues().get(0));
			} else {
				JSONArray vals = new JSONArray();
				vals.addAll(attr.getValues());
				existingClaims.put(attrName, vals);
			}
		}
		
		OpenIDConnectAccessToken access = new OpenIDConnectAccessToken();
		OidcSessionState oidcSession = idp.createUserSession(req, trust.getClientID(), holder, trust, authData.getUserDN(), GlobalEntries.getGlobalEntries().getConfigManager(), access,UUID.randomUUID().toString(),authData.getAuthChain(),existingClaims,null);
		
		Gson gson = new Gson();
		String json = gson.toJson(access);
		
		resp.setContentType("application/json");
		resp.getOutputStream().write(json.getBytes("UTF-8"));
		resp.getOutputStream().flush();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Token JSON : '" + json + "'");
		}
		
		AccessLog.log(AccessEvent.AzSuccess, holder.getApp(), req, authData,"");

	}

}
