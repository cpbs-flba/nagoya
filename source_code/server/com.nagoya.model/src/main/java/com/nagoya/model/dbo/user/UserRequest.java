
package com.nagoya.model.dbo.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.nagoya.model.dbo.DBO;
import com.nagoya.model.dbo.contract.Contract;
import com.nagoya.model.dbo.person.Person;

@Entity(name = "tuser_request")
public class UserRequest extends DBO {

    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person            person;

    @OneToOne(optional = true)
    @JoinColumn(name = "contract_id")
    private Contract          contract;

    @Column(name = "token", nullable = false)
    private String            token;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private RequestType       requestType;

    @Column(name = "expiration_date", nullable = false)
    private Date              expirationDate;

    /**
     * @return the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * @param person the person to set
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the requestType
     */
    public RequestType getRequestType() {
        return requestType;
    }

    /**
     * @param requestType the requestType to set
     */
    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
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

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

}
