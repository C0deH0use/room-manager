package pl.codehouse.kata.roommanager.api.room

import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import pl.codehouse.kata.roommanager.api.room.dto.RoomOccupancyDto
import pl.codehouse.kata.roommanager.api.room.dto.RoomUsageDto
import pl.codehouse.kata.roommanager.api.room.dto.RoomsAvailabilityDto
import pl.codehouse.kata.roommanager.middleware.GlobalExceptionHandler
import spock.lang.Specification

@WebMvcTest(controllers = RoomResource)
abstract class BaseRoomContractTestSpec extends Specification {

    @SpringBean
    OccupancyService occupancyService = Mock(OccupancyService)

    def setup() {
        occupancyService.checkInGuests(_ as RoomsAvailabilityDto) >> new RoomOccupancyDto(new RoomUsageDto(3, 3.23), new RoomUsageDto(4, 7.54))

        RestAssuredMockMvc.standaloneSetup(new RoomResource(occupancyService), GlobalExceptionHandler)
    }
}
