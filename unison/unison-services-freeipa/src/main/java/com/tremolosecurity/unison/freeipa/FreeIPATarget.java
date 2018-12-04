/*******************************************************************************
 * Copyright 2015, 2018 Tremolo Security, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.tremolosecurity.unison.freeipa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.tremolosecurity.config.util.ConfigManager;
import com.tremolosecurity.provisioning.core.ProvisioningException;
import com.tremolosecurity.provisioning.core.User;
import com.tremolosecurity.provisioning.core.UserStoreProvider;
import com.tremolosecurity.provisioning.core.UserStoreProviderWithAddGroup;
import com.tremolosecurity.provisioning.core.Workflow;
import com.tremolosecurity.provisioning.core.ProvisioningUtil.ActionType;
import com.tremolosecurity.provisioning.util.HttpCon;
import com.tremolosecurity.saml.Attribute;
import com.tremolosecurity.unison.freeipa.json.IPABatchResponse;
import com.tremolosecurity.unison.freeipa.json.IPACall;
import com.tremolosecurity.unison.freeipa.json.IPAResponse;
import com.tremolosecurity.unison.freeipa.json.IPATopResult;
import com.tremolosecurity.unison.freeipa.util.IPAException;

import net.sourceforge.myvd.util.PBKDF2;




public class FreeIPATarget implements UserStoreProviderWithAddGroup{

	static Logger logger = org.apache.logging.log4j.LogManager.getLogger(FreeIPATarget.class.getName());
	
	SecureRandom random;
	
	String url;
	String userName;
	String password;
	boolean createShadowAccount;
	boolean multiDomain;
	String primaryDomain;

	private ConfigManager cfgMgr;

	private String name;
	
	private String trustViewName;
	
	private void addGroup(UserPrincipal principal, String groupName,
			HttpCon con, int approvalID, Workflow workflow) throws Exception {
		if (principal.isPrimaryDomain()) {
			
			IPACall addGroup = new IPACall();
			addGroup.setId(0);
			addGroup.setMethod("group_add_member");
			ArrayList<String> groupNames = new ArrayList<String>();
			groupNames.add(groupName);
			
			addGroup.getParams().add(groupNames);
			
			
			HashMap<String,Object> nvps = new HashMap<String,Object>();
			ArrayList<String> users = new ArrayList<String>();
			users.add(principal.getUid());
			nvps.put("user", users);
			
			addGroup.getParams().add(nvps);
			
			IPAResponse resp = this.executeIPACall(addGroup, con);
			
			this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Add,  approvalID, workflow, "group", groupName);
		} else {
			IPACall addGroup = new IPACall();
			addGroup.setId(0);
			addGroup.setMethod("group_add_member");
			ArrayList<String> groupNames = new ArrayList<String>();
			groupNames.add(groupName);
			
			addGroup.getParams().add(groupNames);
			
			
			HashMap<String,Object> nvps = new HashMap<String,Object>();
			ArrayList<String> users = new ArrayList<String>();
			users.add(principal.getUid());
			nvps.put("ipaexternalmember", principal.getUPN());
			
			addGroup.getParams().add(nvps);
			
			IPAResponse resp = this.executeIPACall(addGroup, con);
			
			this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Add,  approvalID, workflow, "group", groupName);
		}
	}
	
	private void removeGroup(UserPrincipal principal, String groupName,
			HttpCon con, int approvalID, Workflow workflow) throws Exception {
		if (principal.isPrimaryDomain()) {
			IPACall addGroup = new IPACall();
			addGroup.setId(0);
			addGroup.setMethod("group_remove_member");
			
			ArrayList<String> groupNames = new ArrayList<String>();
			groupNames.add(groupName);
			
			addGroup.getParams().add(groupNames);
			
			
			HashMap<String,Object> nvps = new HashMap<String,Object>();
			ArrayList<String> users = new ArrayList<String>();
			users.add(principal.getUid());
			nvps.put("user", users);
			
			addGroup.getParams().add(nvps);
			
			IPAResponse resp = this.executeIPACall(addGroup, con);
			
			this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Delete,  approvalID, workflow, "group", groupName);
		} else {
			IPACall addGroup = new IPACall();
			addGroup.setId(0);
			addGroup.setMethod("group_remove_member");
			
			ArrayList<String> groupNames = new ArrayList<String>();
			groupNames.add(groupName);
			
			addGroup.getParams().add(groupNames);
			
			
			HashMap<String,Object> nvps = new HashMap<String,Object>();
			ArrayList<String> users = new ArrayList<String>();
			users.add(principal.getUPN());
			nvps.put("ipaexternalmember", users);
			
			addGroup.getParams().add(nvps);
			
			IPAResponse resp = this.executeIPACall(addGroup, con);
			
			this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Delete,  approvalID, workflow, "group", groupName);
		}
	}
	
	
	public void createUser(User user, Set<String> attributes, Map<String, Object> request)
			throws ProvisioningException {
		

		UserPrincipal principal = new UserPrincipal(user.getUserID(), multiDomain, primaryDomain);
		int approvalID = 0;
		if (request.containsKey("APPROVAL_ID")) {
			approvalID = (Integer) request.get("APPROVAL_ID");
		}
		
		Workflow workflow = (Workflow) request.get("WORKFLOW");
		
		try {
			HttpCon con = this.createClient();
			
			try {
				if (principal.isPrimaryDomain()) {
					IPACall createUser = new IPACall();
					createUser.setId(0);
					createUser.setMethod("user_add");
					
					ArrayList<String> userArray = new ArrayList<String>();
					userArray.add(principal.getUid());
					createUser.getParams().add(userArray);
					
					HashMap<String,Object> userAttrs = new HashMap<String,Object>();
					
					for (String attrName : attributes) {
						Attribute attr = user.getAttribs().get(attrName);
						
						if (attr != null && ! attr.getName().equalsIgnoreCase("uid")) {
							if (attr.getValues().size() == 1) {
								userAttrs.put(attr.getName(), attr.getValues().get(0));
							} else {
								ArrayList vals = new ArrayList<String>();
								vals.addAll(attr.getValues());
								userAttrs.put(attr.getName(), vals);
							}
							
							
						}
					}
					
					createUser.getParams().add(userAttrs);
					
					IPAResponse resp = this.executeIPACall(createUser, con);
					
					this.cfgMgr.getProvisioningEngine().logAction(name,true, ActionType.Add,  approvalID, workflow, "uid", user.getUserID());
					this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Add,  approvalID, workflow, "uid", user.getUserID());
					
					for (String attrName : userAttrs.keySet()) {
						Object o = userAttrs.get(attrName);
						if (o instanceof String) {
							this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Add,  approvalID, workflow, attrName, (String) o);
						} else {
							List<String> vals = (List<String>) o;
							for (String val : vals) {
								this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Add,  approvalID, workflow, attrName, val);
							}
						}
					}
					
					
					
					for (String group : user.getGroups()) {
						this.addGroup(principal, group, con, approvalID, workflow);
					}
					
					if (this.createShadowAccount) {
						String password = new BigInteger(130, random).toString(32);
						password = PBKDF2.generateHash(password);
						user.setPassword(password);
						this.setUserPassword(user, request);
					}
				} else {
					IPACall idOveride = new IPACall();
					idOveride.setId(0);
					idOveride.setMethod("idoverrideuser_add");
					List<String> params = new ArrayList<String>();
					params.add(this.trustViewName);
					params.add(principal.getUPN());
					idOveride.getParams().add(params);
					Map<String,Object> param2 = new HashMap<String,Object>();

					
					for (String attrName : attributes) {
						Attribute attr = user.getAttribs().get(attrName);
						if (attr != null) {
							if (attr.getName().equalsIgnoreCase("uid") && ! attr.getValues().get(0).equals(user.getUserID())) {
								param2.put(attr.getName(),attr.getValues().get(0));
							} else if (! attr.getName().equalsIgnoreCase("uid")) {
								param2.put(attr.getName(),attr.getValues().get(0));
							}
							
						}
					}

					idOveride.getParams().add(param2);

					IPAResponse resp = this.executeIPACall(idOveride, con);

					this.cfgMgr.getProvisioningEngine().logAction(name,true, ActionType.Add,  approvalID, workflow, "uid", user.getUserID());
					this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Add,  approvalID, workflow, "uid", user.getUserID());
					
					for (String attrName : attributes) {
						Attribute attr = user.getAttribs().get(attrName);
						if (attr != null) {
							this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Add,  approvalID, workflow, attrName, attr.getValues().get(0));
						}
					}

					
					
					
					
					for (String group : user.getGroups()) {
						this.addGroup(principal, group, con, approvalID, workflow);
					}
				}
				
			} finally {
				if (con != null) {
					con.getBcm().shutdown();
				}
			}
		} catch (Exception e) {
			throw new ProvisioningException("Could not run search",e);
		}
		
	}

	public void deleteUser(User user, Map<String, Object> request)
			throws ProvisioningException {
		
		UserPrincipal principal = new UserPrincipal(user.getUserID(), multiDomain, primaryDomain);
		int approvalID = 0;
		if (request.containsKey("APPROVAL_ID")) {
			approvalID = (Integer) request.get("APPROVAL_ID");
		}
		
		Workflow workflow = (Workflow) request.get("WORKFLOW");
		
		try {
			HttpCon con = this.createClient();
			
			try {
				if (principal.isPrimaryDomain()) {
					IPACall deleteUser = new IPACall();
					deleteUser.setId(0);
					deleteUser.setMethod("user_del");
					
					ArrayList<String> userArray = new ArrayList<String>();
					userArray.add(principal.getUid());
					deleteUser.getParams().add(userArray);
					
					HashMap<String,String> additionalParams = new HashMap<String,String>();
					
					deleteUser.getParams().add(additionalParams);
					
					IPAResponse resp = this.executeIPACall(deleteUser, con);
					
					this.cfgMgr.getProvisioningEngine().logAction(name,true, ActionType.Delete,  approvalID, workflow, "uid", user.getUserID());
				} else {
					IPACall idOveride = new IPACall();
					idOveride.setId(0);
					idOveride.setMethod("idoverrideuser_del");
					List<String> params = new ArrayList<String>();
					params.add(this.trustViewName);
					params.add(principal.getUPN());
					idOveride.getParams().add(params);
					Map<String,Object> param2 = new HashMap<String,Object>();
					idOveride.getParams().add(param2);

					try {
						IPAResponse resp = this.executeIPACall(idOveride, con);
					} catch (IPAException e) {
						if (! e.getMessage().equalsIgnoreCase("no modifications to be performed")) {
							throw e;
						}
					}
				}
			} finally {
				if (con != null) {
					con.getBcm().shutdown();
				}
			}
		} catch (Exception e) {
			throw new ProvisioningException("Could not run search",e);
		}
		
	}

	public User findUser(String userID, Set<String> attributes, Map<String, Object> request)
			throws ProvisioningException {
		try {
			HttpCon con = this.createClient();
			
			try {
				return findUser(userID, attributes, con,request);
				
			} finally {
				if (con != null) {
					con.getBcm().shutdown();
				}
			}
		} catch (IPAException e) {
			throw e;
		} catch (Exception e) {
			throw new ProvisioningException("Could not run search",e);
		}
		
	}

	private User findUser(String userID, Set<String> attributes, HttpCon con,Map<String,Object> request)
			throws IPAException, ClientProtocolException, IOException {
		
		UserPrincipal principal = new UserPrincipal(userID, multiDomain, primaryDomain);
		
		if (principal.isPrimaryDomain()) {
			IPACall userSearch = new IPACall();
			userSearch.setId(0);
			userSearch.setMethod("user_show");
			
			ArrayList<String> userArray = new ArrayList<String>();
			userArray.add(principal.getUid());
			userSearch.getParams().add(userArray);
			
			HashMap<String,String> additionalParams = new HashMap<String,String>();
			additionalParams.put("all", "true");
			additionalParams.put("rights", "true");
			userSearch.getParams().add(additionalParams);
			
			IPAResponse resp = this.executeIPACall(userSearch, con);
			
			User user = new User();
			user.setUserID(userID);
			Map<String,Object> results = (Map<String,Object>) resp.getResult().getResult();
			
			for (String attributeName : attributes) {
				if (attributeName.equalsIgnoreCase("uid")) {
					Attribute a = user.getAttribs().get(attributeName);
					if (a == null) {
						a = new Attribute(attributeName);
						user.getAttribs().put(attributeName, a);
					}
					StringBuilder s = new StringBuilder().append((String) ((List)results.get(attributeName)).get(0));
					if (this.multiDomain) {
						s.append('@').append(principal.getDomain());
					}
					a.getValues().add(s.toString());
				} else {
					if (results.get(attributeName) != null) {
						if (results.get(attributeName) instanceof List) {
							Attribute a = user.getAttribs().get(attributeName);
							if (a == null) {
								a = new Attribute(attributeName);
								user.getAttribs().put(attributeName, a);
							}
							List l = (List) results.get(attributeName);
							for (Object o : l) {
								a.getValues().add((String) o);
							}
						} else {
							Attribute a = user.getAttribs().get(attributeName);
							if (a == null) {
								a = new Attribute(attributeName);
								user.getAttribs().put(attributeName, a);
							}
							a.getValues().add((String) results.get(attributeName));
						}
					}
				}
			}
			
			if (results != null && results.get("memberof_group") != null) {
				for (Object o : ((List) results.get("memberof_group"))) {
					String groupName = (String) o;
					user.getGroups().add(groupName);
				}
			}
			return user;
		} else {
			IPACall listGroups = new IPACall();
			listGroups.setId(0);
			listGroups.setMethod("group_find");

			ArrayList<String> userArray = new ArrayList<String>();
			userArray.add("");
			listGroups.getParams().add(userArray);
			
			HashMap<String,String> additionalParams = new HashMap<String,String>();
			additionalParams.put("pkey_only", "true");
			additionalParams.put("sizelimit", "0");
			listGroups.getParams().add(additionalParams);
			
			IPAResponse resp = this.executeIPACall(listGroups, con);

			List<Map> groups = (List<Map>) resp.getResult().getResult();
			List<IPACall> groupsToFind = new ArrayList<IPACall>();

			for (Map group : groups) {
				IPACall showGroup = new IPACall();
				showGroup.setId(0);
				showGroup.setMethod("group_show");
				ArrayList<String> groupName = new ArrayList<String>();
				groupName.add(((List)group.get("cn")).get(0).toString());
				showGroup.getParams().add(groupName);

				additionalParams = new HashMap<String,String>();
				additionalParams.put("no_members", "true");
				showGroup.getParams().add(additionalParams);

				groupsToFind.add(showGroup);
			}

			IPACall groupDetails = new IPACall();
			groupDetails.setId(0);
			groupDetails.setMethod("batch");
			groupDetails.getParams().add(groupsToFind);

			additionalParams = new HashMap<String,String>();
			
			groupDetails.getParams().add(additionalParams);

			IPABatchResponse batchResp = this.executeIPABatchCall(groupDetails, con);

			User user = new User();
			user.setUserID(userID);
			user.getAttribs().put("uid",new Attribute("uid",userID));

			if (batchResp.getResult() != null) {
				for (IPATopResult res : batchResp.getResult().getResults()) {
					String groupName = (String) res.getValue();
					if (((Map)res.getResult()).containsKey("ipaexternalmember")) {
						List<String> vals = (List<String>) ((Map)res.getResult()).get("ipaexternalmember");
						for (String val : vals) {
							if (val.equalsIgnoreCase(userID)) {
								user.getGroups().add(groupName);
								break;
							}
						}
					}
				}
			}

			//call id_override
			IPACall idOveride = new IPACall();
			idOveride.setId(0);
			idOveride.setMethod("idoverrideuser_show");
			List<String> params = new ArrayList<String>();
			params.add(this.trustViewName);
			params.add(userID);
			idOveride.getParams().add(params);
			Map<String,Object> param2 = new HashMap<String,Object>();
			param2.put("all", true);
			param2.put("rights",false);
			idOveride.getParams().add(param2);

			resp = null;
			
			try {
				resp = this.executeIPACall(idOveride, con);

				Map<String,List<String>> attrFromIpa = (Map<String, List<String>>) resp.getResult().getResult();
				
				for (String attrName : attrFromIpa.keySet()) {
					if (attributes.contains(attrName)) {
						Attribute attrToAdd = new Attribute(attrName);
						attrToAdd.getValues().addAll(attrFromIpa.get(attrName));
						user.getAttribs().put(attrName,attrToAdd);
					}
				}
			} catch (IPAException e) {
				if (! e.getMessage().contains("User ID override not found")) {
					throw e;
				} else {
					request.put("freeipa.exists", false);
				}
			}



			return user;
		}
	}
	
	public IPAResponse executeIPACall(IPACall ipaCall) throws Exception {
		HttpCon con = null;
		try {
			con = this.createClient();
			return this.executeIPACall(ipaCall, con);
		} finally {
			if (con != null) {
				con.getBcm().shutdown();
			}
		}
	} 

	private IPAResponse executeIPACall(IPACall ipaCall,HttpCon con) throws IPAException, ClientProtocolException, IOException {
		
		Gson gson = new Gson();
		String json = gson.toJson(ipaCall);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Outbound JSON : '" + json + "'");
		}
		
		HttpClient http = con.getHttp();
		
		StringEntity str = new StringEntity(json,ContentType.APPLICATION_JSON);
		HttpPost httppost = new HttpPost(this.url + "/ipa/session/json");
		httppost.addHeader("Referer", this.url + "/ipa/ui/");
		httppost.setEntity(str);
		HttpResponse resp = http.execute(httppost);
		
		
		
		
		
		
		BufferedReader in = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
		StringBuffer b = new StringBuffer();
		String line = null;
		while ((line = in.readLine()) != null) {
			b.append(line);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Inbound JSON : " + b.toString());
		}
		
		EntityUtils.consumeQuietly(resp.getEntity());
		httppost.completed();
		
		IPAResponse ipaResponse = gson.fromJson(b.toString(), IPAResponse.class);
		
		if (ipaResponse.getError() != null) {
			IPAException ipaException = new IPAException(ipaResponse.getError().getMessage());
			ipaException.setCode(ipaResponse.getError().getCode());
			ipaException.setName(ipaResponse.getError().getName());
			throw ipaException;
		} else {
			return ipaResponse;
		}
		
	}

	private IPABatchResponse executeIPABatchCall(IPACall ipaCall,HttpCon con) throws IPAException, ClientProtocolException, IOException {
		
		Gson gson = new Gson();
		String json = gson.toJson(ipaCall);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Outbound JSON : '" + json + "'");
		}
		
		HttpClient http = con.getHttp();
		
		StringEntity str = new StringEntity(json,ContentType.APPLICATION_JSON);
		HttpPost httppost = new HttpPost(this.url + "/ipa/session/json");
		httppost.addHeader("Referer", this.url + "/ipa/ui/");
		httppost.setEntity(str);
		HttpResponse resp = http.execute(httppost);
		
		
		
		
		
		
		BufferedReader in = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
		StringBuffer b = new StringBuffer();
		String line = null;
		while ((line = in.readLine()) != null) {
			b.append(line);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Inbound JSON : " + b.toString());
		}
		
		EntityUtils.consumeQuietly(resp.getEntity());
		httppost.completed();
		
		IPABatchResponse ipaResponse = gson.fromJson(b.toString(), IPABatchResponse.class);
		
		if (ipaResponse.getError() != null) {
			IPAException ipaException = new IPAException(ipaResponse.getError().getMessage());
			ipaException.setCode(ipaResponse.getError().getCode());
			ipaException.setName(ipaResponse.getError().getName());
			throw ipaException;
		} else {
			return ipaResponse;
		}
		
	}

	public void init(Map<String, Attribute> cfg, ConfigManager cfgMgr,
			String name) throws ProvisioningException {
		this.url = this.loadOption("url", cfg, false);
		this.userName = this.loadOption("userName", cfg, false);
		this.password = this.loadOption("password", cfg, true);
		this.createShadowAccount = Boolean.parseBoolean(this.loadOption("createShadowAccounts", cfg, false));
		this.cfgMgr = cfgMgr;
		this.name = name;
		
		this.random = new SecureRandom();

		if (cfg.get("multiDomain") != null) {
			this.multiDomain = this.loadOption("multiDomain", cfg, false).equalsIgnoreCase("true");
		} else {
			this.multiDomain = false;
		}

		if (this.multiDomain) {
			this.primaryDomain = this.loadOption("primaryDomain", cfg, false);
			if (cfg.get("trustView") != null) {
				this.trustViewName = this.loadOption("trustView", cfg, false);
			} else {
				this.trustViewName = "Default Trust View";
			}
		}
		
		
	}
	
	private String loadOption(String name,Map<String,Attribute> cfg,boolean mask) throws ProvisioningException{
		if (! cfg.containsKey(name)) {
			throw new ProvisioningException(name + " is required");
		} else {
			String val = cfg.get(name).getValues().get(0); 
			if (! mask) {
				logger.info("Config " + name + "='" + val + "'");
			} else {
				logger.info("Config " + name + "='*****'");
			}
			
			return val;
		}
	}
	
	private HttpCon createClient() throws Exception {
		return this.createClient(this.userName, this.password);
	}
	
	private HttpCon createClient(String lusername,String lpassword) throws Exception {
		
		BasicHttpClientConnectionManager bhcm = new BasicHttpClientConnectionManager(cfgMgr.getHttpClientSocketRegistry());
		
		
		RequestConfig rc = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
		
		    CloseableHttpClient http = HttpClients.custom().setConnectionManager(bhcm).setDefaultRequestConfig(rc).build();
		    
		    http.execute(new HttpGet(this.url + "/ipa/session/login_kerberos")).close();
		    
		    
		doLogin(lusername, lpassword, http);
		
		HttpCon con = new HttpCon();
		con.setBcm(bhcm);
		con.setHttp(http);
		
		return con;
		
	}

	private void doLogin(String lusername, String lpassword,
			CloseableHttpClient http) throws UnsupportedEncodingException,
			IOException, ClientProtocolException {
		HttpPost httppost = new HttpPost(this.url + "/ipa/session/login_password");
		
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("user", lusername));
		formparams.add(new BasicNameValuePair("password", lpassword));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");

		
		httppost.setEntity(entity);
		
		CloseableHttpResponse response = http.execute(httppost);
		if (logger.isDebugEnabled()) {
			logger.debug("Login response : " + response.getStatusLine().getStatusCode());
		}
		
		response.close();
	}

	public void setUserPassword(User user, Map<String, Object> request)
			throws ProvisioningException {

		UserPrincipal principal = new UserPrincipal(user.getUserID(), multiDomain, primaryDomain);

	    if (! principal.isPrimaryDomain()) {
			throw new ProvisioningException("Can not set password on users outside of the primary domain");
		}

		if (user.getPassword() != null && ! user.getPassword().isEmpty()) {
			int approvalID = 0;
			if (request.containsKey("APPROVAL_ID")) {
				approvalID = (Integer) request.get("APPROVAL_ID");
			}
			
			Workflow workflow = (Workflow) request.get("WORKFLOW");
			
			try {
				HttpCon con = this.createClient();
				
				try {
					IPACall setPassword = new IPACall();
					setPassword.setId(0);
					setPassword.setMethod("passwd");
					
					ArrayList<String> userArray = new ArrayList<String>();
					userArray.add(principal.getUid());
					setPassword.getParams().add(userArray);
					
					HashMap<String,String> additionalParams = new HashMap<String,String>();
					additionalParams.put("password", user.getPassword());
					setPassword.getParams().add(additionalParams);
					
					IPAResponse resp = this.executeIPACall(setPassword, con);
					con.getBcm().shutdown();
					
					//no we need to reset the password, this is a hack.  right way is to tell IPA the user doesn't need to reset their password
					HttpPost httppost = new HttpPost(this.url + "/ipa/session/change_password");
					httppost.addHeader("Referer", this.url + "/ipa/ui/");	
					List<NameValuePair> formparams = new ArrayList<NameValuePair>();
					formparams.add(new BasicNameValuePair("user", principal.getUid()));
					formparams.add(new BasicNameValuePair("old_password", user.getPassword()));
					formparams.add(new BasicNameValuePair("new_password", user.getPassword()));
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
	
					
					httppost.setEntity(entity);
					
					
					
					con = this.createClient(principal.getUid(), user.getPassword());
					CloseableHttpClient http = con.getHttp();
					 
					
					CloseableHttpResponse httpResp = http.execute(httppost);
					
					if (logger.isDebugEnabled()) {
						logger.debug("Response of password reset : " + httpResp.getStatusLine().getStatusCode());
					}
					
					
					this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Replace,  approvalID, workflow, "userPassword", "********************************");
				} finally {
					if (con != null) {
						con.getBcm().shutdown();
					}
				}
			} catch (Exception e) {
				throw new ProvisioningException("Could not run search",e);
			}
		}
		
	}
	
	
	private void setAttribute(UserPrincipal principal, Attribute attrNew,
			HttpCon con, int approvalID, Workflow workflow) throws Exception {
		if (principal.isPrimaryDomain()) {
			IPACall modify = new IPACall();
			modify.setId(0);
			modify.setMethod("user_mod");
			
			ArrayList<String> userArray = new ArrayList<String>();
			userArray.add(principal.getUid());
			modify.getParams().add(userArray);
			
			HashMap<String,Object> additionalParams = new HashMap<String,Object>();
			if (attrNew.getValues().size() > 1) {
				additionalParams.put(attrNew.getName(), attrNew.getValues());
			} else {
				additionalParams.put(attrNew.getName(), attrNew.getValues().get(0));
			}
			
			modify.getParams().add(additionalParams);
			
			try {
				IPAResponse resp = this.executeIPACall(modify, con);
			} catch (IPAException e) {
				if (! e.getMessage().equalsIgnoreCase("no modifications to be performed")) {
					throw e;
				}
			}
		} else {

			if (attrNew.getName().equalsIgnoreCase("uid") && attrNew.getValues().get(0).equals(principal.getUPN())) {
				return;
			} 

			IPACall idOveride = new IPACall();
			idOveride.setId(0);
			idOveride.setMethod("idoverrideuser_mod");
			List<String> params = new ArrayList<String>();
			params.add(this.trustViewName);
			params.add(principal.getUPN());
			idOveride.getParams().add(params);
			Map<String,Object> param2 = new HashMap<String,Object>();
			param2.put("all", true);
			param2.put("rights",false);
			param2.put(attrNew.getName(), attrNew.getValues().get(0));
			idOveride.getParams().add(param2);

			try {
				IPAResponse resp = this.executeIPACall(idOveride, con);
			} catch (IPAException e) {
				if (! e.getMessage().equalsIgnoreCase("no modifications to be performed")) {
					throw e;
				}
			}

			
		}
	}
	
	private void deleteAttribute(UserPrincipal principal, String attrName,
			HttpCon con, int approvalID, Workflow workflow) throws Exception {
		
		if (principal.isPrimaryDomain()) {
			IPACall modify = new IPACall();
			modify.setId(0);
			modify.setMethod("user_mod");
			
			ArrayList<String> userArray = new ArrayList<String>();
			userArray.add(principal.getUid());
			modify.getParams().add(userArray);
			
			HashMap<String,Object> additionalParams = new HashMap<String,Object>();
			additionalParams.put(attrName, "");
			
			
			modify.getParams().add(additionalParams);
			
			IPAResponse resp = this.executeIPACall(modify, con);
		} else {
			IPACall idOveride = new IPACall();
			idOveride.setId(0);
			idOveride.setMethod("idoverrideuser_mod");
			List<String> params = new ArrayList<String>();
			params.add(this.trustViewName);
			params.add(principal.getUPN());
			idOveride.getParams().add(params);
			Map<String,Object> param2 = new HashMap<String,Object>();
			param2.put("all", true);
			param2.put("rights",false);
			param2.put(attrName, "");
			idOveride.getParams().add(param2);

			try {
				IPAResponse resp = this.executeIPACall(idOveride, con);
			} catch (IPAException e) {
				if (! e.getMessage().equalsIgnoreCase("no modifications to be performed")) {
					throw e;
				}
			}
		}
	}
	
	
	

	public void syncUser(User user, boolean addOnly, Set<String> attributes,
			Map<String, Object> request) throws ProvisioningException {
		
		UserPrincipal principal = new UserPrincipal(user.getUserID(), multiDomain, primaryDomain);
		
		User fromIPA = null;
		HttpCon con = null;
		try {
		con = this.createClient();
		
			try {
				fromIPA = this.findUser(user.getUserID(), attributes, request); 
			} catch (IPAException ipaException) {
				if (ipaException.getCode() != 4001) {
					throw ipaException;
				}
			}
			
			
			
			int approvalID = 0;
			if (request.containsKey("APPROVAL_ID")) {
				approvalID = (Integer) request.get("APPROVAL_ID");
			}
			
			Workflow workflow = (Workflow) request.get("WORKFLOW");
			
			if (fromIPA == null) {
				if (principal.isPrimaryDomain()) {
					this.createUser(user, attributes, request);
				}
			} else {
				if (! principal.isPrimaryDomain() && request.get("freeipa.exists") != null && ((Boolean)request.get("freeipa.exists")) == false) {
					this.createUser(user, attributes, request);
					return;
				}

				//if (principal.isPrimaryDomain()) {
					//check to see if the attributes from the incoming object match
					for (String attrName : attributes) {
						if (attrName.equalsIgnoreCase("uid")) {
							continue;
						}
						
						Attribute attrNew = checkAttribute(principal,user, fromIPA, con,
								approvalID, workflow, attrName, addOnly);
						
					}
					
					if (! addOnly) {
						for (String attrToDel : fromIPA.getAttribs().keySet()) {
							if (! attrToDel.equalsIgnoreCase("uid")) {
								//These attributes were no longer on the user, delete them
								this.deleteAttribute(principal, attrToDel, con, approvalID, workflow);
								this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Delete,  approvalID, workflow, attrToDel, "");
							}
						}
					}
				//}
				
				//check groups
				HashSet<String> curGroups = new HashSet<String>();
				curGroups.addAll(fromIPA.getGroups());
				for (String group : user.getGroups()) {
					if (curGroups.contains(group)) {
						curGroups.remove(group);
					} else {
						this.addGroup(principal, group, con, approvalID, workflow);
					}
				}
				
				if (! addOnly) {
					for (String group : curGroups) {
						this.removeGroup(principal, group, con, approvalID, workflow);
					}
				}
				
				if (principal.isPrimaryDomain()) {
					if (this.createShadowAccount) {
						String password = new BigInteger(130, random).toString(32);
						password = PBKDF2.generateHash(password);
						user.setPassword(password);
						this.setUserPassword(user, request);
					}
				}
				
			}
		} catch (Exception e) {
			throw new ProvisioningException("Could not sync user",e);
		} finally {
			if (con != null) {
				con.getBcm().shutdown();
			}
		} 
		
	}

	private Attribute checkAttribute(UserPrincipal principal,User user, User fromIPA, HttpCon con,
			int approvalID, Workflow workflow, String attrName,boolean addOnly)
			throws Exception {
		Attribute attrNew = user.getAttribs().get(attrName);
		if (attrNew != null) {
			Attribute attrOld = fromIPA.getAttribs().get(attrName);
			
			if (attrOld != null) {
				fromIPA.getAttribs().remove(attrName);
				if (attrNew.getValues().size() != attrOld.getValues().size()) {
					//attribute changed, update ipa
					setAttribute(principal,attrNew,con,approvalID,workflow);
					
					//determine changes
					
					
					auditAttributeChanges(approvalID, workflow, attrName,
							attrNew, attrOld,addOnly);
					
					
					
					
				} else if (attrOld.getValues().size() == 0 || ! attrOld.getValues().get(0).equals(attrNew.getValues().get(0))) {
					setAttribute(principal,attrNew,con,approvalID,workflow);
					this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Replace,  approvalID, workflow, attrName, attrNew.getValues().get(0));
					
				} else {
					HashSet<String> oldVals = new HashSet<String>();
					oldVals.addAll(attrOld.getValues());
					for (String val : attrNew.getValues()) {
						if (! oldVals.contains(val)) {
							setAttribute(principal,attrNew,con,approvalID,workflow);
							break;
						} else {
							oldVals.remove(val);
						}
					}
					
					if (oldVals.size() > 0) {
						setAttribute(principal,attrNew,con,approvalID,workflow);
					}
					
					//determine changes
					auditAttributeChanges(approvalID, workflow, attrName,
							attrNew, attrOld,addOnly);
				}
			
				
			} else {
				//attribute doesn't exist, update IPA
				setAttribute(principal,attrNew,con,approvalID,workflow);
				this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Add,  approvalID, workflow, attrName, attrNew.getValues().get(0));
			}
		}
		return attrNew;
	}

	private void auditAttributeChanges(int approvalID, Workflow workflow,
			String attrName, Attribute attrNew, Attribute attrOld,boolean addOnly)
			throws ProvisioningException {
		HashSet<String> oldVals = new HashSet<String>();
		oldVals.addAll(attrOld.getValues());
		
		for (String val : attrNew.getValues()) {
			if (! oldVals.contains(val)) {
				this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Add,  approvalID, workflow, attrName, val);
				oldVals.remove(val);
			}
		}
		
		if (! addOnly) {
			HashSet<String> newVals = new HashSet<String>();
			newVals.addAll(attrNew.getValues());
			for (String val : attrOld.getValues() ) {
				if (! newVals.contains(val)) {
					this.cfgMgr.getProvisioningEngine().logAction(name,false, ActionType.Delete,  approvalID, workflow, attrName, val);
				}
			}
		}
	}

	@Override
	public void addGroup(String name, Map<String,String> additionalAttributes,User user, Map<String, Object> request) throws ProvisioningException {
		int approvalID = 0;
		if (request.containsKey("APPROVAL_ID")) {
			approvalID = (Integer) request.get("APPROVAL_ID");
		}
		
		Workflow workflow = (Workflow) request.get("WORKFLOW");
		
		IPACall groupSearch = new IPACall();
		groupSearch.setId(0);
		groupSearch.setMethod("group_add");
		
		ArrayList<String> groupArray = new ArrayList<String>();
		groupArray.add(name);
		groupSearch.getParams().add(groupArray);
		
		HashMap<String,String> additionalParams = new HashMap<String,String>();
		
		for (String key : additionalAttributes.keySet()) {
			additionalParams.put(key, additionalAttributes.get(key));
		}
		
		groupSearch.getParams().add(additionalParams);
		
		HttpCon con = null;
		
		try {
		
			con = this.createClient();
			
			IPAResponse resp = this.executeIPACall(groupSearch, con);
			
			this.cfgMgr.getProvisioningEngine().logAction(name,true, ActionType.Add,  approvalID, workflow, "group-object", name);
		} catch (Exception e) {
			throw new ProvisioningException("Could not find groups",e);
		} finally {
			if (con != null) {
				con.getBcm().close();
			}
		}
		
	}

	@Override
	public void deleteGroup(String name, User user, Map<String, Object> request) throws ProvisioningException {
		int approvalID = 0;
		if (request.containsKey("APPROVAL_ID")) {
			approvalID = (Integer) request.get("APPROVAL_ID");
		}
		
		Workflow workflow = (Workflow) request.get("WORKFLOW");
		
		IPACall groupSearch = new IPACall();
		groupSearch.setId(0);
		groupSearch.setMethod("group_del");
		
		ArrayList<String> groupArray = new ArrayList<String>();
		groupArray.add(name);
		groupSearch.getParams().add(groupArray);
		
		HashMap<String,String> additionalParams = new HashMap<String,String>();
		
		groupSearch.getParams().add(additionalParams);
		
		HttpCon con = null;
		
		try {
		
			con = this.createClient();
			
			IPAResponse resp = this.executeIPACall(groupSearch, con);
			
			this.cfgMgr.getProvisioningEngine().logAction(name,true, ActionType.Delete,  approvalID, workflow, "group-object", name);
		} catch (Exception e) {
			throw new ProvisioningException("Could not find groups",e);
		} finally {
			if (con != null) {
				con.getBcm().close();
			}
		}
		
	}

	@Override
	public boolean isGroupExists(String name, User user, Map<String, Object> request) throws ProvisioningException {
		IPACall groupSearch = new IPACall();
		groupSearch.setId(0);
		groupSearch.setMethod("group_show");
		
		ArrayList<String> groupArray = new ArrayList<String>();
		groupArray.add(name);
		groupSearch.getParams().add(groupArray);
		
		HashMap<String,String> additionalParams = new HashMap<String,String>();
		
		groupSearch.getParams().add(additionalParams);
		
		HttpCon con = null;
		
		try {
		
			con = this.createClient();
			
			IPAResponse resp = this.executeIPACall(groupSearch, con);
			
			return true;
		} catch (IPAException ipae) {
			if (ipae.getCode() == 4001) {
				return false;
			} else {
				throw new ProvisioningException("Could not find groups",ipae);
			}
 		} catch (Exception e) {
			throw new ProvisioningException("Could not find groups",e);
		} finally {
			if (con != null) {
				con.getBcm().close();
			}
		}
	}

}

class UserPrincipal {
	String uid;
	String domain;
	String upn;
	boolean primaryDomain;

	public UserPrincipal(String upn,boolean isMultiDomain,String primaryDomain) {
		if (isMultiDomain) {
			uid = upn.substring(0,upn.indexOf('@'));
			domain = upn.substring(upn.indexOf('@') + 1);
			this.primaryDomain = domain.equalsIgnoreCase(primaryDomain);
			this.upn = upn;
		} else {
			uid = upn;
			domain = primaryDomain;
			this.primaryDomain = true;
			this.upn = upn;
		}
	}

	public String getUPN() {
		return this.upn;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @return the primaryDomain
	 */
	public boolean isPrimaryDomain() {
		return primaryDomain;
	}
}
