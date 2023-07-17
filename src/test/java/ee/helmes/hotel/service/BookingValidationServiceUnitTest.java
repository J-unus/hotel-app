package ee.helmes.hotel.service;

import ee.helmes.hotel.service.dto.RoomFilter;
import ee.helmes.hotel.web.rest.errors.ValidationException;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingValidationServiceUnitTest {

    private BookingValidationService bookingValidationService;

    @BeforeEach
    public void setUp() {
        bookingValidationService = new BookingValidationService();
    }

    @Test
    void validateRoomFilter_ValidRoomFilter_NoExceptionThrown() {
        RoomFilter roomFilter = new RoomFilter();
        roomFilter.setStartDate(LocalDate.now().plusDays(1));
        roomFilter.setEndDate(LocalDate.now().plusDays(2));
        roomFilter.setRoomAmount("2");
        roomFilter.setPriceRange("100_200");

        Assertions.assertDoesNotThrow(() -> bookingValidationService.validateRoomFilter(roomFilter));
    }

    @Test
    void validateRoomFilter_InvalidRoomAmount_ThrowsIllegalArgumentException() {
        RoomFilter roomFilter = new RoomFilter();
        roomFilter.setStartDate(LocalDate.now().plusDays(1));
        roomFilter.setEndDate(LocalDate.now().plusDays(2));
        roomFilter.setRoomAmount("4");
        roomFilter.setPriceRange("100_200");

        Assertions.assertThrows(IllegalArgumentException.class, () -> bookingValidationService.validateRoomFilter(roomFilter));
    }

    @Test
    void validateRoomFilter_InvalidPriceRange_ThrowsIllegalArgumentException() {
        RoomFilter roomFilter = new RoomFilter();
        roomFilter.setStartDate(LocalDate.now().plusDays(1));
        roomFilter.setEndDate(LocalDate.now().plusDays(2));
        roomFilter.setRoomAmount("2");
        roomFilter.setPriceRange("400_500");

        Assertions.assertThrows(IllegalArgumentException.class, () -> bookingValidationService.validateRoomFilter(roomFilter));
    }

    @Test
    void validateBookingDateRange_ValidDateRange_NoExceptionThrown() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(2);

        Assertions.assertDoesNotThrow(() -> bookingValidationService.validateBookingDateRange(startDate, endDate));
    }

    @Test
    void validateBookingDateRange_StartDateBeforeMinDate_ThrowsValidationException() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(2);

        Assertions.assertThrows(ValidationException.class, () -> bookingValidationService.validateBookingDateRange(startDate, endDate));
    }

    @Test
    void validateBookingDateRange_EndDateAfterMaxDate_ThrowsValidationException() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusYears(2);

        Assertions.assertThrows(ValidationException.class, () -> bookingValidationService.validateBookingDateRange(startDate, endDate));
    }

    @Test
    void validateBookingDateRange_StartDateEqualToEndDate_ThrowsValidationException() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);

        Assertions.assertThrows(ValidationException.class, () -> bookingValidationService.validateBookingDateRange(startDate, endDate));
    }
}
