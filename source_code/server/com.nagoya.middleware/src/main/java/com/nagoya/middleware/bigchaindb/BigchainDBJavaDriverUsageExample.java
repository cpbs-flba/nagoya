
package com.nagoya.middleware.bigchaindb;

import java.io.IOException;
import java.security.KeyPair;
import java.util.Map;
import java.util.TreeMap;

import com.bigchaindb.builders.BigchainDbConfigBuilder;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.Asset;
import com.bigchaindb.model.FulFill;
import com.bigchaindb.model.GenericCallback;
import com.bigchaindb.model.Input;
import com.bigchaindb.model.MetaData;
import com.bigchaindb.model.Output;
import com.bigchaindb.model.Transaction;
import com.bigchaindb.util.Base58;
import com.bigchaindb.util.KeyPairUtils;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import okhttp3.Response;

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

        BigchainDBJavaDriverUsageExample examples = new BigchainDBJavaDriverUsageExample();

        // set configuration
        BigchainDBJavaDriverUsageExample.setConfig();

        // generate Keys
        KeyPair keys = BigchainDBJavaDriverUsageExample.getKeys();

        System.out.println(Base58.encode(keys.getPublic().getEncoded()));
        System.out.println(Base58.encode(keys.getPrivate().getEncoded()));

        // create New asset
        @SuppressWarnings("serial")
        Map<String, String> assetData = new TreeMap<String, String>() {
            {
                put("Genetic Ressource", "Tomato 123");
            }
        };
        System.out.println("(*) Assets Prepared..");

        // create metadata
        MetaData metaData = new MetaData();
        metaData.setMetaData("Origin", "Austria");
        System.out.println("(*) Metadata Prepared..");

        // execute CREATE transaction
        String txId1 = examples.doCreate(assetData, metaData, keys);

        // create transfer metadata
        MetaData transferMetadata = new MetaData();
        transferMetadata.setMetaData("Origin", "Brasil");
        System.out.println("(*) Transfer Metadata Prepared..");

        // let the transaction commit in block
        Thread.sleep(5000);

        // execute TRANSFER transaction on the CREATED asset
        examples.doTransfer(txId1, transferMetadata, keys);

    }

    private void onSuccess(Response response) {
        // TODO : Add your logic here with response from server
        System.out.println("Transaction posted successfully");
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
                System.out.println("malformed " + response.message());
                onFailure();
            }

            @Override
            public void pushedSuccessfully(Response response) {
                System.out.println("pushedSuccessfully");
                onSuccess(response);
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
        BigchainDbConfigBuilder //
            .baseUrl("https://test.bigchaindb.com") // or use http://testnet.bigchaindb.com
            .addToken("app_id", "") //
            .addToken("app_key", "") //
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

        EdDSAPublicKey publicKey = (EdDSAPublicKey) keyPair.getPublic();
        String encode = Base58.encode(publicKey.getAbyte());
        System.out.println("first: " + encode);

        String encodePrivateKeyBase64 = KeyPairUtils.encodePrivateKeyBase64(keyPair);
        System.out.println("base64 private key: " + encodePrivateKeyBase64);

        KeyPair decodedKeyPair = KeyPairUtils.decodeKeyPair(encodePrivateKeyBase64);
        EdDSAPublicKey publicKey2 = (EdDSAPublicKey) decodedKeyPair.getPublic();
        String encode2 = Base58.encode(publicKey2.getAbyte());
        System.out.println("second: " + encode2);

        System.out.println("(*) Keys Generated..");
        return keyPair;

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

            transaction = BigchainDbTransactionBuilder.init()//
                .addOutput("100", (EdDSAPublicKey) keys.getPublic()) //
                .addAssets(assetData, TreeMap.class) //
                .addMetaData(metaData) //
                .operation(Operations.CREATE) //
                .buildAndSign((EdDSAPublicKey) keys.getPublic(), (EdDSAPrivateKey) keys.getPrivate()) //
                .sendTransaction(handleServerResponse());

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
     */
    public void doTransfer(String txId, MetaData metaData, KeyPair keys)
        throws Exception {

        try {

            // which transaction you want to fulfill?
            FulFill fulfill = new FulFill();
            fulfill.setOutputIndex(0);
            fulfill.setTransactionId(txId);

            // build and send TRANSFER transaction
            Transaction transaction2 = BigchainDbTransactionBuilder.init() //

                .addInput(null, fulfill, (EdDSAPublicKey) keys.getPublic()) //

                .addOutput("200", (EdDSAPublicKey) keys.getPublic()) //

                .addAssets(txId, String.class) //

                .addMetaData(metaData) //

                .operation(Operations.TRANSFER) //
                .buildOnly((EdDSAPublicKey) keys.getPublic());

            System.out.println(transaction2.toString());

            Transaction transaction = BigchainDbTransactionBuilder.init().addInput(null, fulfill, (EdDSAPublicKey) keys.getPublic()) //

                .addOutput("100", (EdDSAPublicKey) keys.getPublic()) //

                .addAssets(txId, String.class) //

                .addMetaData(metaData) //

                .operation(Operations.TRANSFER) //

                .buildAndSign((EdDSAPublicKey) keys.getPublic(), (EdDSAPrivateKey) keys.getPrivate()) //

                .sendTransaction(handleServerResponse());

            System.out.println("(*) TRANSFER Transaction sent.. - " + transaction.getId());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Transaction trans(String assetId, MetaData metaData) {
        Transaction result = new Transaction();
        Asset asset = new Asset(assetId);
        result.setAsset(asset);

        result.setMetaData(metaData);
        result.setOperation(Operations.TRANSFER.toString());
        result.setVersion("2.0");

        Input input = new Input();
        // TODO: input
        result.getInputs().add(input);

        Output output = new Output();
        // TODO: output
        // output.setCondition(condition);
        result.getOutputs().add(output);
        return result;
    }
}
