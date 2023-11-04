package contracts.room

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    name "should return room occupancy when specifying the available rooms in the hotel"
    request {
        method 'POST'
        urlPath $(consumer(regex('/api/v1/rooms')))
        body([
                premium: $(consumer(anyNumber()), producer(-10)),
                economy: $(consumer(anyNumber()), producer(-1)),
        ])
        headers {
            accept 'application/json'
            contentType 'application/json'
        }
    }
    response {
        status BAD_REQUEST()
        body([
                "type"   : "about:blank",
                "title"  : "Bad Request",
                "status" : 400,
                "detail" : "Invalid request content.",
                "premium": "available premium rooms must be zero or higher",
                "economy": "available economy rooms must be zero or higher"
        ])
        headers {
            contentType('application/problem+json')
        }
    }
}