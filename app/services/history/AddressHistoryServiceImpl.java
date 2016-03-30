package services.history;

import models.db.contacts.Address;
import models.db.historty.AddressHistory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import repositories.history.AddressHistoryRepository;
import services.base.AbstractService;
import services.base.interfaces.history.AddressHistoryService;
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by eduardo on 22/03/16.
 */
@Named
@Singleton
@Transactional
public class AddressHistoryServiceImpl extends AbstractService<AddressHistory,Long> implements AddressHistoryService{

    AddressHistoryRepository addressHistoryRepository;

    @Autowired
    public AddressHistoryServiceImpl(AddressHistoryRepository addressHistoryRepository) {
        super(addressHistoryRepository);
        this.addressHistoryRepository = addressHistoryRepository;
    }

    @Override
    public UUID newVersion(Address address, UUID version, boolean markAsDeleted) {
        AddressHistory addressHistory  = new AddressHistory();
        addressHistory.setAddress(address.getAddress());
        addressHistory.setAddressId(address.getId());
        addressHistory.setVersion(version);
        if(markAsDeleted)
            addressHistory.setDeletedAt(DateTime.now().toDate());
        save(addressHistory);
        return addressHistory.getVersion();
    }

    @Override
    public Optional<AddressHistory> getPreviousVersion(Long addressId) {
        return addressHistoryRepository.findAddressHistory(addressId,new PageRequest(0,1)).getContent().stream().findFirst();
    }

    @Override
    public List<AddressHistory> getAddressHistory(long addressId) throws NotFoundException {
        return addressHistoryRepository.findAddressHistory(addressId);
    }

    @Override
    public List<Address> getAddress(UUID addressVersion) {
        List<AddressHistory> addressHistoryList = addressHistoryRepository.findByVersion(addressVersion);
        List<Address> addresses = new ArrayList<>();
        for(AddressHistory addressHistory : addressHistoryList) {
            Address address = new Address();
            address.setAddress(addressHistory.getAddress());
            address.setId(addressHistory.getAddressId());
            addresses.add(address);
        }
        return addresses;
    }


}
