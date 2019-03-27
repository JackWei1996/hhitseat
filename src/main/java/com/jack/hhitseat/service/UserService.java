/**
 * 
 */
package com.jack.hhitseat.service;

import com.jack.hhitseat.model.ResultMap;

/**
 * @author 19604
 *
 */
public interface UserService {
	public ResultMap login(String username, String password);
}
