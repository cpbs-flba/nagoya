
package com.nagoya.common.blockchain.api.impl;

import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeoutException;

import com.bigchaindb.api.TransactionsApi;
import com.bigchaindb.builders.BigchainDbConfigBuilder;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.Connection;
import com.bigchaindb.model.FulFill;
import com.bigchaindb.model.GenericCallback;
import com.bigchaindb.model.MetaData;
import com.bigchaindb.model.Transaction;
import com.bigchaindb.util.Base58;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import okhttp3.Response;

/**
 * simple usage of BigchainDB Java driver (https://github.com/bigchaindb/java-bigchaindb-driver) to create TXs on BigchainDB network
 * 
 * @author innoprenuer
 *
 */
public class BigchainDBJavaDriverUsageExample {

    /**
     * main method
     * 
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String args[])
        throws Exception {
        System.out.println("started");
        BigchainDBJavaDriverUsageExample examples = new BigchainDBJavaDriverUsageExample();

        // set configuration
        BigchainDBJavaDriverUsageExample.configureConnectionManager();

        // generate Keys
        KeyPair keys = BigchainDBJavaDriverUsageExample.getKeys();

        System.out.println(Base58.encode(keys.getPublic().getEncoded()));
        System.out.println(Base58.encode(keys.getPrivate().getEncoded()));

        // create New asset
        Map<String, String> assetData = new TreeMap<String, String>() {
            {
                put("name", "James Bond");
                put("age", "doesn't matter");
                put("purpose", "saving the world");
            }
        };
        System.out.println("(*) Assets Prepared..");

        // create metadata
        MetaData metaData = new MetaData();
        metaData.setMetaData("where is he now?", "Thailand");
        System.out.println("(*) Metadata Prepared..");

        // execute CREATE transaction
        String txId = examples.doCreate(assetData, metaData, keys);
        String assetId = txId;
        Thread.sleep(2000);

        for (int i = 0; i < 50; i++) {
            // create transfer metadata
            MetaData transferMetadata = new MetaData();
            transferMetadata.setMetaData("where is he now?", "Japan - " + Math.random());
            System.out.println("(*) Transfer Metadata Prepared for i - " + i);

            // let the transaction commit in block
            Thread.sleep(2000);

            // execute TRANSFER transaction on the CREATED asset
            txId = examples.doTransfer(assetId, txId, transferMetadata, keys);
            examples.doCreate(assetData, transferMetadata, keys);
        }

    }

    private void onSuccess(Response response)
        throws IOException {
        // TODO : Add your logic here with response from server
        System.out.println("Success : ");
    }

    private void onFailure() {
        // TODO : Add your logic here
        System.out.println("Transaction failed");
    }

    private GenericCallback handleServerResponse() {
        // define callback methods to verify response from BigchainDBServer
        GenericCallback callback = new GenericCallback() {

            @Override
            public void transactionMalformed(Response response) {
                System.out.println("malformed - " + response.message());
                onFailure();
            }

            @Override
            public void pushedSuccessfully(Response response) {
                try {
                    System.out.println("pushedSuccessfully - " + response.body().string());
                    onSuccess(response);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void otherError(Response response) {
                System.out.println("otherError" + response.message());
                onFailure();
            }
        };

        return callback;
    }

    /**
     * configures connection url and credentials
     */
    public static void setConfig() {
        BigchainDbConfigBuilder.baseUrl("http://localhost:9984/") // or use http://testnet.bigchaindb.com
            .addToken("app_id", "").addToken("app_key", "").setTimeout(60000).setup();

    }

    /**
     * configures connections and api endpoints
     * 
     * @throws TimeoutException
     */
    public static void configureConnectionManager()
        throws TimeoutException {

        // define connections
        Map<String, Object> conn1Config = new HashMap<String, Object>(), conn2Config = new HashMap<String, Object>();

        // defien headers for connections
        Map<String, String> headers1 = new HashMap<String, String>();
        Map<String, String> headers2 = new HashMap<String, String>();

        // config header for connection 1
        headers1.put("app_id", "");
        headers1.put("app_key", "");

        // config header for connection 2
        headers2.put("app_id", "8609ee17");
        headers2.put("app_key", "130a79938e5dcc6c7aa1166028f06e20");

        // config connection 1
        conn1Config.put("baseUrl", "https://test.bigchaindb.com");
        conn1Config.put("headers", headers1);
        Connection conn1 = new Connection(conn1Config);

        // config connection 2
        conn2Config.put("baseUrl", "https://test.bigchaindb.com");
        conn2Config.put("headers", headers2);
        Connection conn2 = new Connection(conn2Config);

        // add connections
        List<Connection> connections = new ArrayList<Connection>();
        connections.add(conn1);
        // connections.add(conn2);

        BigchainDbConfigBuilder.addConnections(connections).setTimeout(60000) // override default timeout of 20000 milliseconds
            .setup();

    }

    /**
     * generates EdDSA keypair to sign and verify transactions
     * 
     * @return KeyPair
     */
    public static KeyPair getKeys() {
        // prepare your keys
        net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
        KeyPair keyPair = edDsaKpg.generateKeyPair();
        System.out.println("(*) Keys Generated..");
        return keyPair;

    }

    public void getTransaction(String txId)
        throws IOException, InterruptedException {
        // Thread.sleep(1000);
        Transaction tx = TransactionsApi.getTransactionById(txId);
        System.out.println("********************");
        System.out.println(tx);
    }

    /**
     * performs CREATE transactions on BigchainDB network
     * 
     * @param assetData data to store as asset
     * @param metaData data to store as metadata
     * @param keys keys to sign and verify transaction
     * @return id of CREATED asset
     */
    public String doCreate(Map<String, String> assetData, MetaData metaData, KeyPair keys)
        throws Exception {

        try {
            // build and send CREATE transaction
            Transaction transaction = null;

            transaction = BigchainDbTransactionBuilder.init().addAssets(assetData, TreeMap.class).addMetaData(metaData).operation(Operations.CREATE)
                .buildAndSign((EdDSAPublicKey) keys.getPublic(), (EdDSAPrivateKey) keys.getPrivate()).sendTransaction(handleServerResponse());

            System.out.println("(*) CREATE Transaction sent.. - " + transaction.getId());
            return transaction.getId();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * performs TRANSFER operations on CREATED assets
     * 
     * @param txId id of transaction/asset
     * @param metaData data to append for this transaction
     * @param keys keys to sign and verify transactions
     * @return
     */
    public String doTransfer(String assetId, String txId, MetaData metaData, KeyPair keys)
        throws Exception {

        try {

            // which transaction you want to fulfill?
            FulFill fulfill = new FulFill();
            fulfill.setOutputIndex(0);
            fulfill.setTransactionId(txId);

            // build and send TRANSFER transaction
            Transaction transaction = BigchainDbTransactionBuilder.init().addInput(null, fulfill, (EdDSAPublicKey) keys.getPublic())
                .addOutput("1", (EdDSAPublicKey) keys.getPublic()).addAssets(assetId, String.class).addMetaData(metaData)
                .operation(Operations.TRANSFER).buildAndSign((EdDSAPublicKey) keys.getPublic(), (EdDSAPrivateKey) keys.getPrivate())
                .sendTransaction(handleServerResponse());

            System.out.println("(*) TRANSFER Transaction sent.. - " + transaction.getId());
            return transaction.getId();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}