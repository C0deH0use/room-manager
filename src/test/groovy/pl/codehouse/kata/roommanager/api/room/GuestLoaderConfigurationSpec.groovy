package pl.codehouse.kata.roommanager.api.room

import spock.lang.Specification

class GuestLoaderConfigurationSpec extends Specification {
    def "should return guests stored in file"() {
        given: "configured file containing guests"
        def fileName = "test-guests.txt"
        def expectedGuests = [23.00, 45.00, 155.00, 374.00, 22.00, 99.99, 100.00, 101.00, 115.00, 209.00]

        when: "read guests from file"
        def sut = new GuestLoaderConfiguration(fileName);
        def result = sut.guests

        then:
        result ==~ expectedGuests
    }

    def "should return empty list when file does not have any records"() {
        given: "configured file containing guests"
        def fileName = "empty-guests.txt"

        when: "read guests from file"
        def sut = new GuestLoaderConfiguration(fileName);
        def result = sut.guests

        then:
        result == []
    }

    def "should return empty list when file is empty"() {
        given: "configured file containing guests"
        def fileName = "empty-guests.txt"

        when: "read guests from file"
        def sut = new GuestLoaderConfiguration(fileName);
        def result = sut.guests

        then:
        result == []
    }

    def "should throw exception when file is missing"() {
        given: "configured file containing guests"
        def fileName = "other-guests.txt"

        when: "read guests from file"
        def sut = new GuestLoaderConfiguration(fileName)
        def _ = sut.guests

        then:
        def exc = thrown(NullPointerException)
        exc.message == "Guests file is missing"
    }
}
