/*******************************************************************************
 * Copyright 2018 Tremolo Security, Inc.
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
// Copyright 2014 Google Inc. All rights reserved.
//
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file or at
// https://developers.google.com/open-source/licenses/bsd

package com.google.u2f.server;

import java.util.List;

import com.google.u2f.U2FException;
import com.google.u2f.server.data.SecurityKeyData;
import com.google.u2f.server.messages.RegistrationRequest;
import com.google.u2f.server.messages.RegistrationResponse;
import com.google.u2f.server.messages.SignResponse;
import com.google.u2f.server.messages.U2fSignRequest;

public interface U2FServer {

  // registration //
  public RegistrationRequest getRegistrationRequest(String accountName, String appId) throws U2FException;

  public SecurityKeyData processRegistrationResponse(RegistrationResponse registrationResponse,
      long currentTimeInMillis) throws U2FException;

  // authentication //
  public U2fSignRequest getSignRequest(String accountName, String appId) throws U2FException;

  public SecurityKeyData processSignResponse(SignResponse signResponse) throws U2FException;

  // token management //
  public List<SecurityKeyData> getAllSecurityKeys(String accountName);

  public void removeSecurityKey(String accountName, byte[] publicKey) throws U2FException;
}
