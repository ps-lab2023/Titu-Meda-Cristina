package com.nagarro.af.bookingtablesystem.mapper;

import com.nagarro.af.bookingtablesystem.dto.BookingDTO;
import com.nagarro.af.bookingtablesystem.dto.MenuDTO;
import com.nagarro.af.bookingtablesystem.dto.RestaurantDTO;
import com.nagarro.af.bookingtablesystem.dto.UserDTO;
import com.nagarro.af.bookingtablesystem.model.Booking;
import com.nagarro.af.bookingtablesystem.model.Menu;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.model.User;
import com.nagarro.af.bookingtablesystem.utils.PostgresDbContainer;
import com.nagarro.af.bookingtablesystem.utils.TestComparators;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Comparator;

@Testcontainers
@Transactional
@SpringBootTest
public abstract class MapperTest {

    @Container
    private static final PostgreSQLContainer<PostgresDbContainer> postgreSQLContainer = PostgresDbContainer.getInstance();

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    protected final Comparator<Menu> menuComparator = TestComparators.MENU_COMPARATOR;

    protected final Comparator<MenuDTO> menuDTOComparator = TestComparators.MENU_DTO_COMPARATOR;

    protected final Comparator<User> userComparator = TestComparators.USER_COMPARATOR;

    protected final Comparator<UserDTO> userDtoComparator = TestComparators.USER_DTO_COMPARATOR;

    protected final Comparator<Restaurant> restaurantComparator = TestComparators.RESTAURANT_COMPARATOR;

    protected final Comparator<RestaurantDTO> restaurantDTOComparator = TestComparators.RESTAURANT_DTO_COMPARATOR;

    protected final Comparator<Booking> bookingComparator = TestComparators.BOOKING_COMPARATOR;

    protected final Comparator<BookingDTO> bookingDTOComparator = TestComparators.BOOKING_DTO_COMPARATOR;
}
