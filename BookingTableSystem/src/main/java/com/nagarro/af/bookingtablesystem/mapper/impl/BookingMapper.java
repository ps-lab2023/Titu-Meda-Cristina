package com.nagarro.af.bookingtablesystem.mapper.impl;

import com.nagarro.af.bookingtablesystem.dto.BookingDTO;
import com.nagarro.af.bookingtablesystem.mapper.EntityDTOMapper;
import com.nagarro.af.bookingtablesystem.model.Booking;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.repository.CustomerRepository;
import com.nagarro.af.bookingtablesystem.repository.RestaurantRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookingMapper implements EntityDTOMapper<Booking, BookingDTO> {

    private final ModelMapper modelMapper;

    private final CustomerRepository customerRepository;

    private final RestaurantRepository restaurantRepository;

    public BookingMapper(CustomerRepository customerRepository, RestaurantRepository restaurantRepository) {
        this.modelMapper = new ModelMapper();
        configureMapper();
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
    }

    private void configureMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Converter<Customer, UUID> customerUUIDConverter =
                ctx -> ctx.getSource() == null ? null : ctx.getSource().getId();

        Converter<Restaurant, UUID> restaurantUUIDConverter =
                ctx -> ctx.getSource() == null ? null : ctx.getSource().getId();

        Converter<UUID, Customer> uuidCustomerConverter =
                ctx -> ctx == null ? null : customerRepository.findById(ctx.getSource()).orElse(null);

        Converter<UUID, Restaurant> uuidRestaurantConverter =
                ctx -> ctx.getSource() == null ? null :
                        restaurantRepository.findById(ctx.getSource()).orElse(null);

        TypeMap<Booking, BookingDTO> bookingDTOTypeMap = modelMapper.createTypeMap(Booking.class, BookingDTO.class);
        bookingDTOTypeMap.addMappings(mapper -> {
            mapper.using(customerUUIDConverter)
                    .map(
                            Booking::getCustomer,
                            BookingDTO::setCustomerId
                    );
            mapper.using(restaurantUUIDConverter)
                    .map(
                            Booking::getRestaurant,
                            BookingDTO::setRestaurantId
                    );
        });

        TypeMap<BookingDTO, Booking> dtoBookingTypeMap = modelMapper.createTypeMap(BookingDTO.class, Booking.class);
        dtoBookingTypeMap.addMappings(mapper -> {
            mapper.using(uuidCustomerConverter)
                    .map(
                            BookingDTO::getCustomerId,
                            Booking::setCustomer
                    );
            mapper.using(uuidRestaurantConverter)
                    .map(
                            BookingDTO::getRestaurantId,
                            Booking::setRestaurant
                    );
        });
    }

    @Override
    public BookingDTO mapEntityToDTO(Booking booking) {
        return modelMapper.map(booking, BookingDTO.class);
    }

    @Override
    public Booking mapDTOtoEntity(BookingDTO bookingDTO) {
        return modelMapper.map(bookingDTO, Booking.class);
    }
}
