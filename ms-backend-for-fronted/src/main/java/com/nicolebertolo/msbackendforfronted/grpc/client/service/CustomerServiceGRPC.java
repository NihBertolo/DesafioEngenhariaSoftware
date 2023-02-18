package com.nicolebertolo.msbackendforfronted.grpc.client.service;


import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.customer.CustomerRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.stream.Collectors;

@Service
public class CustomerServiceGRPC {

    @Value("${grpc.clients.customer.address}")
    private String address;

    @Value("${grpc.clients.customer.port}")
    private int port;

    private ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public FindCustomerByIdResponse findCustomerById(String customerId, String tracing) {
        LOGGER.info("[CustomerServiceGRPC.findCustomerById] - Init GRPC Communication");
        val findCustomerByIdRequest = FindCustomerByIdRequest.newBuilder()
                .setId(customerId)
                .setTracing(tracing)
                .build();

        return CustomerServiceAPIGrpc.newBlockingStub(this.getChannel()).findCustomerById(findCustomerByIdRequest);
    }

    public CreateCustomerResponse createCustomer(CustomerRequest customerRequest, String tracing) {
        LOGGER.info("[CustomerServiceGRPC.createCustomer] - Init GRPC Communication");

        val createCustomerRequest = CreateCustomerRequest.newBuilder()
                .setName(customerRequest.getName())
                .setLastname(customerRequest.getName())
                .setTracing(tracing)
                .addAllCustomerDocumentDto(customerRequest.getDocuments().stream().map(document ->
                        CustomerDocumentDto.newBuilder()
                                .setDocumentNumber(document.getDocumentNumber())
                                .setDocumentType(document.getDocumentType())
                                .build()
                ).collect(Collectors.toList()))
                .addAllCustomerAddressDto(customerRequest.getAddresses().stream().map(address ->
                        CustomerAddressDto.newBuilder()
                                .setAddressName(address.getAddressName())
                                .setCountryName(address.getCountryName())
                                .setZipCode(address.getZipCode())
                                .setIsPrincipal(address.getIsPrincipal())
                                .build()
                ).collect(Collectors.toList()))
                .build();

        return CustomerServiceAPIGrpc.newBlockingStub(this.getChannel()).createCustomer(createCustomerRequest);
    }

    public FindAllCustomersResponse findAllCustomers(String tracing) {
        LOGGER.info("[CustomerServiceGRPC.findAllCustomers] - Init GRPC Communication");

        val findAllCustomersRequest = FindAllCustomersRequest.newBuilder()
                .setTracing(tracing)
                .build();

        return CustomerServiceAPIGrpc.newBlockingStub(this.getChannel()).findAllCustomers(findAllCustomersRequest);
    }

}
