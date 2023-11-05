package pl.codehouse.kata.roommanager.api.room;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Map;

@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomResourceApiTest {

    @Autowired
    private WebTestClient client;

    @Nested
    @DisplayName("Run Test Case examples")
    class TestCases {

        @Test
        @DisplayName("should pass test case number one")
        void shouldPassTestCaseNumberOne() {
            //given
            var bodyMap = Map.of("premium", 3, "economy", 3);

            // when && then
            client.post()
                    .uri("/api/v1/rooms")
                    .body(BodyInserters.fromValue(bodyMap))
                    .accept(MediaType.APPLICATION_JSON)

                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("premiumRooms.usage").isEqualTo(3)
                    .jsonPath("premiumRooms.cost").isEqualTo(738.0)
                    .jsonPath("economyRooms.usage").isEqualTo(3)
                    .jsonPath("economyRooms.cost").isEqualTo(167.99);
        }

        @Test
        @DisplayName("should pass test case number two")
        void shouldPassTestCaseNumberTwo() {
            //given
            var bodyMap = Map.of("premium", 7, "economy", 5);

            // when && then
            client.post()
                    .uri("/api/v1/rooms")
                    .body(BodyInserters.fromValue(bodyMap))
                    .accept(MediaType.APPLICATION_JSON)

                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("premiumRooms.usage").isEqualTo(6)
                    .jsonPath("premiumRooms.cost").isEqualTo(1054)
                    .jsonPath("economyRooms.usage").isEqualTo(4)
                    .jsonPath("economyRooms.cost").isEqualTo(189.99);
        }

        @Test
        @DisplayName("should pass test case number three")
        void shouldPassTestCaseNumberThree() {
            //given
            var bodyMap = Map.of("premium", 2, "economy", 7);

            // when && then
            client.post()
                    .uri("/api/v1/rooms")
                    .body(BodyInserters.fromValue(bodyMap))
                    .accept(MediaType.APPLICATION_JSON)

                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("premiumRooms.usage").isEqualTo(2)
                    .jsonPath("premiumRooms.cost").isEqualTo(583)
                    .jsonPath("economyRooms.usage").isEqualTo(4)
                    .jsonPath("economyRooms.cost").isEqualTo(189.99);
        }

        @Test
        @DisplayName("should pass test case number four")
        void shouldPassTestCaseNumberFour() {
            //given
            var bodyMap = Map.of("premium", 7, "economy", 1);

            // when && then
            client.post()
                    .uri("/api/v1/rooms")
                    .body(BodyInserters.fromValue(bodyMap))
                    .accept(MediaType.APPLICATION_JSON)

                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody()
                    .jsonPath("premiumRooms.usage").isEqualTo(7)
                    .jsonPath("premiumRooms.cost").isEqualTo(1153)
                    .jsonPath("economyRooms.usage").isEqualTo(1)
                    .jsonPath("economyRooms.cost").isEqualTo(45);
        }
    }
}