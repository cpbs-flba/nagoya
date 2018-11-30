/**
 * 
 */
package com.nagoya.blockchain.api;

/**
 * @author flba
 *
 */
public interface BlockchainDriver {

	/**
	 * Generates credentials for a specified user: a public key and a private key.
	 * 
	 * @return
	 */
	public Credentials createCredentials();
	
	/**
	 * Creates an asset.
	 * 
	 * @param credentials
	 * @param asset
	 * @return transaction ID
	 */
	public String createAsset(Credentials credentials, Asset asset);
	
	/**
	 * 
	 * @param sender
	 * @param receiver
	 * @param asset
	 * @return
	 */
	public String transferAsset(Credentials sender, Credentials receiver, Asset asset);
	
}
