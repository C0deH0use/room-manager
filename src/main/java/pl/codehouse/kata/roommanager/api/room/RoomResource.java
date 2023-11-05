package pl.codehouse.kata.roommanager.api.room;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.codehouse.kata.roommanager.api.room.dto.RoomOccupancyDto;

@Validated
@RestController
@RequestMapping(value = "/api/v1/rooms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
class RoomResource {
    private static final Logger log = LoggerFactory.getLogger(RoomResource.class);

    private final OccupancyService service;

    RoomResource(OccupancyService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<RoomOccupancyDto> occupyAvailableRooms(@Valid @RequestBody RoomsAvailabilityDto roomsAvailabilityDto) {
        log.debug("Following {} rooms will be occupied with hotel guests", roomsAvailabilityDto);
        var occupancy = service.checkInGuests(roomsAvailabilityDto);
        return ResponseEntity.ok(occupancy);
    }
}
