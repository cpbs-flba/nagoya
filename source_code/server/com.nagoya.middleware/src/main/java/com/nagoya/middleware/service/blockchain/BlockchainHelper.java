/**
 * 
 */
package com.nagoya.middleware.service.blockchain;

import com.nagoya.blockchain.api.BlockchainDriver;
import com.nagoya.blockchain.api.Credentials;
import com.nagoya.common.blockchain.api.impl.BlockchainDriverImpl;

/**
 * @author flba
 *
 */
public class BlockchainHelper {

	private BlockchainDriver blockchainDriver;
	
	public BlockchainHelper() {
		blockchainDriver = new BlockchainDriverImpl();
	}
	
	public Credentials createCredentials() {
		// what do we do - do we send an e-mail?
		
		Credentials credentials = blockchainDriver.createCredentials();
		return credentials;
	}
}
