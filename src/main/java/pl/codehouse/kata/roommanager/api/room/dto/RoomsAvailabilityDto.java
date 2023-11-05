package pl.codehouse.kata.roommanager.api.room.dto;

import jakarta.validation.constraints.Min;

public record RoomsAvailabilityDto(
        @Min(value = 0, message = "available premium rooms must be zero or higher") Integer premium,
        @Min(value = 0, message = "available economy rooms must be zero or higher") Integer economy
) {
}
