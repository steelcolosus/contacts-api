package models.db.historty;

import models.db.base.AbstractHistoryEntity;
import models.db.contacts.Address;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

/**
 * Created by eduardo on 15/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class AddressHistory extends AbstractHistoryEntity {

    private String address;

    private long addressId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }
}
