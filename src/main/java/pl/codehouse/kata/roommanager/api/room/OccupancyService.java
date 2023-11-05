package pl.codehouse.kata.roommanager.api.room;

import pl.codehouse.kata.roommanager.api.room.dto.RoomOccupancyDto;

public interface OccupancyService {
    RoomOccupancyDto checkInGuests(RoomsAvailabilityDto roomsAvailabilityDto);
}
