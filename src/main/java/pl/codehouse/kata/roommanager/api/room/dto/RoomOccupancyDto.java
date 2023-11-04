package pl.codehouse.kata.roommanager.api.room.dto;

public record RoomOccupancyDto(
        RoomUsageDto premiumRooms,
        RoomUsageDto economyRooms
) {
}
