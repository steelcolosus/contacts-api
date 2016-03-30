package services.base.interfaces.history;

import models.db.contacts.Address;
import models.db.contacts.Contact;
import models.db.historty.AddressHistory;
import models.db.historty.ContactHistory;
import services.base.GenericService;
import utils.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by eduardo on 19/03/16.
 */

public interface AddressHistoryService extends GenericService<AddressHistory, Long>{

    public UUID newVersion(Address address, UUID version, boolean markAsDeleted);
    public Optional<AddressHistory> getPreviousVersion(Long addressId);
    public List<AddressHistory> getAddressHistory(long addressId) throws NotFoundException;
    public List<Address> getAddress(UUID addressVersion);
}
