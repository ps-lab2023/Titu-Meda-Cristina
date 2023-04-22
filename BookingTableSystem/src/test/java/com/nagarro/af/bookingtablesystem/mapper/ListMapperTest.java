package com.nagarro.af.bookingtablesystem.mapper;

import com.nagarro.af.bookingtablesystem.config.ModelMapperConfig;
import com.nagarro.af.bookingtablesystem.dto.AdminDTO;
import com.nagarro.af.bookingtablesystem.dto.CustomerDTO;
import com.nagarro.af.bookingtablesystem.model.Admin;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ListMapper.class, ModelMapperConfig.class})
public class ListMapperTest {

    private static final UUID ADMIN_UUID = UUID.fromString(TestDataBuilder.ADMIN_ID);

    private static final UUID CUSTOMER_ID = UUID.fromString(TestDataBuilder.CUSTOMER_ID);

    @Autowired
    private ListMapper listMapper;

    @Test
    public void testListMapper_fromAdminList_toAdminDTOList() {
        Admin admin = buildAdmin();
        List<Admin> admins = new ArrayList<>();
        admins.add(admin);

        AdminDTO adminDTO = buildAdminDTO();
        List<AdminDTO> adminDTOList = new ArrayList<>();
        adminDTOList.add(adminDTO);

        List<AdminDTO> mappedAdminList = listMapper.mapList(admins, AdminDTO.class);

        assertNotNull(mappedAdminList);
        assertTrue(mappedAdminList.containsAll(adminDTOList));
    }

    @Test
    public void testListMapper_fromCustomerList_toCustomerDTOList() {
        Customer customer = buildCustomer();
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);

        CustomerDTO customerDTO = buildCustomerDTO();
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        customerDTOList.add(customerDTO);

        List<CustomerDTO> mappedCustomers = listMapper.mapList(customers, CustomerDTO.class);

        assertNotNull(mappedCustomers);
        assertTrue(mappedCustomers.containsAll(customerDTOList));
    }

    @NotNull
    private Admin buildAdmin() {
        Admin admin = TestDataBuilder.buildAdmin();
        admin.setId(ADMIN_UUID);
        return admin;
    }

    @NotNull
    private AdminDTO buildAdminDTO() {
        AdminDTO adminDTO = TestDataBuilder.buildAdminDTO();
        adminDTO.setId(ADMIN_UUID);
        return adminDTO;
    }

    @NotNull
    private Customer buildCustomer() {
        Customer customer = TestDataBuilder.buildCustomer();
        customer.setId(CUSTOMER_ID);
        return customer;
    }

    @NotNull
    private CustomerDTO buildCustomerDTO() {
        CustomerDTO customerDTO = TestDataBuilder.buildCustomerDTO();
        customerDTO.setId(CUSTOMER_ID);
        return customerDTO;
    }
}
