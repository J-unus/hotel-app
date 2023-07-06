package ee.helmes.hotel.service.mapper;

import ee.helmes.hotel.domain.Booking;
import ee.helmes.hotel.service.dto.BookingDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", disableSubMappingMethodsGeneration = true)
public abstract class BookingMapper implements DtoAndEntityMapper<BookingDto, Booking> {}
