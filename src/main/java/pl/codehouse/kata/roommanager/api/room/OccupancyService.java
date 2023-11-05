package pl.codehouse.kata.roommanager.api.room;

import pl.codehouse.kata.roommanager.api.room.dto.RoomOccupancyDto;
import pl.codehouse.kata.roommanager.api.room.dto.RoomsAvailabilityDto;

public interface OccupancyService {
    RoomOccupancyDto checkInGuests(RoomsAvailabilityDto roomsAvailabilityDto);
}
