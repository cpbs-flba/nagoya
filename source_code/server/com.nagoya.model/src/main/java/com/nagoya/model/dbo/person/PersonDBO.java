
package com.nagoya.model.dbo.person;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.nagoya.model.dbo.DBO;

/**
 * @author Florin Bogdan Balint
 */
@Audited
@Entity(name = "tperson")
@Inheritance(strategy = InheritanceType.JOINED)
public class PersonDBO extends DBO {

    private static final long  serialVersionUID = 1L;

    @Column(name = "email", nullable = false)
    private String             email;

    @Column(name = "email_confirmed", nullable = false)
    private boolean            emailConfirmed;

    @Column(name = "store_private_keys", nullable = false)
    private boolean            storePrivateKey;

    @Column(name = "password", nullable = false)
    private String             password;

    @Enumerated(EnumType.STRING)
    @Column(name = "person_type", nullable = false)
    private PersonType         personType;

    @Audited
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private AddressDBO         address;

    @NotAudited
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    private Set<PersonKeysDBO> keys             = new HashSet<PersonKeysDBO>();

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the emailConfirmed
     */
    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    /**
     * @param emailConfirmed the emailConfirmed to set
     */
    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the personType
     */
    public PersonType getPersonType() {
        return personType;
    }

    /**
     * @param personType the personType to set
     */
    public void setPersonType(PersonType personType) {
        this.personType = personType;
    }

    /**
     * @return the address
     */
    public AddressDBO getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(AddressDBO address) {
        this.address = address;
    }

    /**
     * @return the keys
     */
    public Set<PersonKeysDBO> getKeys() {
        return keys;
    }

    /**
     * @param keys the keys to set
     */
    public void setKeys(Set<PersonKeysDBO> keys) {
        this.keys = keys;
    }

    /**
     * @return the storePrivateKey
     */
    public boolean isStorePrivateKey() {
        return storePrivateKey;
    }

    /**
     * @param storePrivateKey the storePrivateKey to set
     */
    public void setStorePrivateKey(boolean storePrivateKey) {
        this.storePrivateKey = storePrivateKey;
    }

}
