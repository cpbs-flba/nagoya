
package com.nagoya.model.dbo.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.nagoya.model.dbo.DBO;
import com.nagoya.model.dbo.person.PersonDBO;

@Entity(name = "tonline_user")
public class OnlineUserDBO extends DBO {

    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "person_id")
    private PersonDBO         person;

    @Column(name = "session_token", nullable = false)
    private String            sessionToken;

    @Column(name = "private_key", nullable = false)
    private String            privateKey;

    @Column(name = "expiration_date", nullable = false)
    private Date              expirationDate;

    @Column(name = "blockchain_key")
    private String            blockchainKey;

    @Transient
    private String            jsonWebToken;

    /**
     * @return the person
     */
    public PersonDBO getPerson() {
        return person;
    }

    /**
     * @param person the person to set
     */
    public void setPerson(PersonDBO person) {
        this.person = person;
    }

    /**
     * @return the sessionToken
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * @param sessionToken the sessionToken to set
     */
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    /**
     * @return the privateKey
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * @param privateKey the privateKey to set
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * @return the expirationDate
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return the blockchainKey
     */
    public String getBlockchainKey() {
        return blockchainKey;
    }

    /**
     * @param blockchainKey the blockchainKey to set
     */
    public void setBlockchainKey(String blockchainKey) {
        this.blockchainKey = blockchainKey;
    }

    /**
     * @return the jsonWebToken
     */
    public String getJsonWebToken() {
        return jsonWebToken;
    }

    /**
     * @param jsonWebToken the jsonWebToken to set
     */
    public void setJsonWebToken(String jsonWebToken) {
        this.jsonWebToken = jsonWebToken;
    }

}
