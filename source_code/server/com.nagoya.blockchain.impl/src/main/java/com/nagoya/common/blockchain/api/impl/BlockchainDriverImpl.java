
package com.nagoya.common.blockchain.api.impl;

import java.security.KeyPair;

import com.bigchaindb.util.Base58;
import com.bigchaindb.util.KeyPairUtils;
import com.nagoya.blockchain.api.Asset;
import com.nagoya.blockchain.api.BlockchainDriver;
import com.nagoya.blockchain.api.Credentials;
import com.nagoya.common.blockchain.api.impl.deprecated.BigchainDBJavaDriverUsageExample;

import net.i2p.crypto.eddsa.EdDSAPublicKey;

/**
 * 
 * @author flba
 *
 */
public class BlockchainDriverImpl implements BlockchainDriver {

    @Override
    public Credentials createCredentials() {
        KeyPair keyPair = BigchainDBJavaDriverUsageExample.getKeys();

        EdDSAPublicKey publicKey = (EdDSAPublicKey) keyPair.getPublic();
        String publicKeyString = Base58.encode(publicKey.getAbyte());
        String privateKeyString = KeyPairUtils.encodePrivateKeyBase64(keyPair);

        Credentials credentials = new Credentials();
        credentials.setPublicKey(publicKeyString);
        credentials.setPrivateKey(privateKeyString);

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
