package pl.codehouse.kata.roommanager.api.room;

import org.springframework.stereotype.Service;
import pl.codehouse.kata.roommanager.api.room.dto.RoomOccupancyDto;

@Service
class OccupancyServiceImpl implements OccupancyService {
    @Override
    public RoomOccupancyDto checkInGuestsToAvailableRooms(RoomsAvailabilityDto roomsAvailabilityDto) {
        return null;
    }
}
