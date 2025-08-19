package com.ing.hubs.loan.api.service;


import com.ing.hubs.loan.api.dto.CustomerDto;
import com.ing.hubs.loan.api.exception.ResourceNotFoundException;
import com.ing.hubs.loan.api.mapper.CustomerMapper;
import com.ing.hubs.loan.api.model.Customer;
import com.ing.hubs.loan.api.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toDto)
                .collect(Collectors.toList());
    }

    public CustomerDto findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        return CustomerMapper.toDto(customer);
    }

    public CustomerDto addCustomer(CustomerDto customerDto) {
        Customer customer = CustomerMapper.toEntity(customerDto);
        return CustomerMapper.toDto(customerRepository.save(customer));
    }

    public void deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Customer with id " + id + " not found");
        }
    }
}
