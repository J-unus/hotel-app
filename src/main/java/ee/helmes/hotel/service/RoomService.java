package ee.helmes.hotel.service;

import ee.helmes.hotel.domain.Room;
import ee.helmes.hotel.repository.RoomRepository;
import ee.helmes.hotel.service.dto.RoomDto;
import ee.helmes.hotel.service.dto.RoomFilter;
import ee.helmes.hotel.service.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RoomSpecificationService roomSpecificationService;

    public Page<RoomDto> query(Pageable pageable, RoomFilter roomFilter) {
        Specification<Room> spec = roomSpecificationService.getRoomSpecification(roomFilter);

        return roomRepository.findAll(spec, pageable).map(roomMapper::fromEntityToDto);
    }
}
