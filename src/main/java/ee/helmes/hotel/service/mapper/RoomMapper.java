package ee.helmes.hotel.service.mapper;

import ee.helmes.hotel.domain.Room;
import ee.helmes.hotel.service.dto.RoomDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", disableSubMappingMethodsGeneration = true)
public abstract class RoomMapper implements DtoAndEntityMapper<RoomDto, Room> {}
