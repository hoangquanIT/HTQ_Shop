package com.quanht.service;

import com.quanht.entities.ShippingAddress;
import com.quanht.repositories.ShippingAddressRepository;
import com.quanht.request.ShippingAddressRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShippingAddressService {

    private ShippingAddressRepository shippingAddressRepository;

    @Autowired
    public ShippingAddressService(ShippingAddressRepository shippingAddressRepository) {
        this.shippingAddressRepository = shippingAddressRepository;
    }

    @Transactional
    public ShippingAddress updateShippingAddress(Long id, ShippingAddressRequest request) {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(id).get();
        shippingAddress.setName(request.getName());
        shippingAddress.setPhone(request.getPhone());
        shippingAddress.setEmail(request.getEmail());
        shippingAddress.setAddress(request.getAddress());
        shippingAddress.setCity(request.getCity());

        return shippingAddressRepository.save(shippingAddress);
    }
}
