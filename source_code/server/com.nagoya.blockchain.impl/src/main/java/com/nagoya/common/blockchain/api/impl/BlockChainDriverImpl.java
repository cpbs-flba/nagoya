package com.nagoya.common.blockchain.api.impl;

import com.nagoya.blockchain.api.Asset;
import com.nagoya.blockchain.api.BlockchainDriver;
import com.nagoya.blockchain.api.Credentials;

/**
 * 
 * @author flba
 *
 */
public class BlockChainDriverImpl implements BlockchainDriver {

	@Override
	public Credentials createCredentials() {
		Credentials credentials = new Credentials();
		credentials.setPublicKey("public_key_123");
		credentials.setPrivateKey("private_key_123");
		return credentials;
	}

	@Override
	public String createAsset(Credentials credentials, Asset asset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String transferAsset(Credentials sender, Credentials receiver, Asset asset) {
		// TODO Auto-generated method stub
		return null;
	}

}
