package contracts.room

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name "should return room occupancy when specifying the available rooms in the hotel"
    request {
        method 'POST'
        urlPath $(consumer(regex('/api/v1/rooms')))
        body([
                premium: $(consumer(anyPositiveInt())),
                economy: $(consumer(anyPositiveInt())),
        ])
        headers {
            accept 'application/json'
            contentType 'application/json'
        }
    }
    response {
        status OK()
        body([
                "premiumRooms": [
                        "usage": $(producer(anyNumber())),
                        "cost" : $(producer(anyDouble())),
                ],
                "economyRooms": [
                        "usage": $(producer(anyNumber())),
                        "cost" : $(producer(anyDouble())),
                ],
        ])
        headers {
            contentType('application/json')
        }
    }
}