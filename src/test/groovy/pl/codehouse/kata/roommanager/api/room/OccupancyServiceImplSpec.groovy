package pl.codehouse.kata.roommanager.api.room


import spock.lang.Specification

class OccupancyServiceImplSpec extends Specification {

    def "should assign premium room to guest when willing to pay more then 100 EURO"() {
        given: "guest willing to pay 101 EURO"
        def sut = new OccupancyServiceImpl([101.0])

        when: "assigning to available rooms"
        def rooms = new RoomsAvailabilityDto(10, 10)
        def occupancyDto = sut.checkInGuests(rooms)

        then: "premium room should be in usage"
        occupancyDto.premiumRooms().usage() == 1
        occupancyDto.premiumRooms().cost() == 101

        and: "economy room should be free"
        occupancyDto.economyRooms().usage() == 0
        occupancyDto.economyRooms().cost() == 0
    }

    def "should first assign these premium guests that are willing to pay highest prize for premium rooms when limited amount of premium rooms available"() {
        given: "5 premium users available"
        def sut = new OccupancyServiceImpl([101.0, 129.91, 111.34, 199.99])

        when: "assigning to available rooms"
        def rooms = new RoomsAvailabilityDto(2, 10)
        def occupancyDto = sut.checkInGuests(rooms)

        then: "premium rooms should be assign to these guests that are willing the highest prize"
        occupancyDto.premiumRooms().usage() == 2
        occupancyDto.premiumRooms().cost() == 129.91 + 199.99

        and: "economy room should be free"
        occupancyDto.economyRooms().usage() == 0
        occupancyDto.economyRooms().cost() == 0
    }

    def "should assign economy room to guest when willing to pay less then 100 EURO"() {
        given: "guest willing to pay 20 EURO"
        def sut = new OccupancyServiceImpl([20.0])

        when: "assigning to available rooms"
        def rooms = new RoomsAvailabilityDto(0, 10)
        def occupancyDto = sut.checkInGuests(rooms)

        then: "economy room should be in usage"
        occupancyDto.economyRooms().usage() == 1
        occupancyDto.economyRooms().cost() == 20

        and: "premium room should be free"
        occupancyDto.premiumRooms().usage() == 0
        occupancyDto.premiumRooms().cost() == 0
    }

    def "should assign to premium rooms economy guests that are amongst the highest willing payers when limited amount of economy rooms available and premium rooms are free"() {
        given: "5 economy users available"
        def sut = new OccupancyServiceImpl([11.0, 29.91, 61.34, 19.99])

        when: "assigning to available rooms"
        def rooms = new RoomsAvailabilityDto(3, 1)
        def occupancyDto = sut.checkInGuests(rooms)

        then: "all premium rooms should be in usage"
        occupancyDto.premiumRooms().usage() == 3
        occupancyDto.premiumRooms().cost() == 29.91 + 61.34 + 19.99

        and: "economy room should be free"
        occupancyDto.economyRooms().usage() == 1
        occupancyDto.economyRooms().cost() == 11
    }

    def "should assign all guests when limited number of rooms are available"() {
        given: "both premium and economy guests"
        def sut = new OccupancyServiceImpl([11.0, 101.0, 29.91, 19.99, 199.99, 78.62, 61.34, 129.91, 111.34])

        when: "assigning to available rooms"
        def rooms = new RoomsAvailabilityDto(5, 5)
        def occupancyDto = sut.checkInGuests(rooms)

        then: "all premium rooms should be in usage"
        occupancyDto.premiumRooms().usage() == 4
        occupancyDto.premiumRooms().cost() == 199.99 + 129.91 + 111.34 + 101.0

        and: "economy room should be free"
        occupancyDto.economyRooms().usage() == 5
        occupancyDto.economyRooms().cost() == 61.34 + 78.62 + 29.91 + 19.99 + 11.0
    }


    def "should promote the highest payed economy guests when premium rooms are available and economy rooms are all occupied"() {
        given: "both premium and economy guests"
        def sut = new OccupancyServiceImpl([11.0, 101.0, 29.91, 19.99, 199.99, 78.62, 61.34, 129.91, 111.34])

        when: "assigning to available rooms"
        def rooms = new RoomsAvailabilityDto(5, 4)
        def occupancyDto = sut.checkInGuests(rooms)

        then: "all premium rooms should be in usage"
        occupancyDto.premiumRooms().usage() == 5
        occupancyDto.premiumRooms().cost() == 199.99 + 129.91 + 111.34 + 101.0 + 78.62

        and: "economy room should be free"
        occupancyDto.economyRooms().usage() == 4
        occupancyDto.economyRooms().cost() == 61.34 + 29.91 + 19.99 + 11.0
    }
}