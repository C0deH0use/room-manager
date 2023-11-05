package pl.codehouse.kata.roommanager.api.room;

import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.codehouse.kata.roommanager.api.room.dto.RoomOccupancyDto;
import pl.codehouse.kata.roommanager.api.room.dto.RoomUsageDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

@Service
class OccupancyServiceImpl implements OccupancyService {

    private static final BigDecimal PREMIUM_PRIZE_THRESHOLD = BigDecimal.valueOf(100);
    private static final BigDecimal ZERO_WITH_SCALE = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    private final List<BigDecimal> guests;
    private final Predicate<? super BigDecimal> premiumGuestPredicate = (t) -> t.compareTo(PREMIUM_PRIZE_THRESHOLD) >= 0;

    public OccupancyServiceImpl(@Qualifier("guests") List<BigDecimal> guests) {
        this.guests = guests;
    }

    @Override
    public RoomOccupancyDto checkInGuests(RoomsAvailabilityDto roomsAvailabilityDto) {
        var availablePremiumRooms = new AtomicInteger(roomsAvailabilityDto.premium());
        var premiumUsage = MutablePair.of(0, ZERO_WITH_SCALE);
        this.guests
                .stream()
                .filter(premiumGuestPredicate)
                .sorted(Comparator.reverseOrder())
                .filter(guest -> availablePremiumRooms.getAndDecrement() > 0)
                .forEach(guest -> {
                    premiumUsage.left++;
                    premiumUsage.right = premiumUsage.right.add(guest);
                });

        var availableEconomyRooms = new AtomicInteger(roomsAvailabilityDto.economy());
        var economyUsage = MutablePair.of(0, ZERO_WITH_SCALE);


        long economyGuests = this.guests
                .stream()
                .filter(not(premiumGuestPredicate))
                .count();
        this.guests
                .stream()
                .filter(not(premiumGuestPredicate))
                .sorted(Comparator.reverseOrder())
                .forEach(guest -> {
                    if (availablePremiumRooms.get() > 0 && economyGuests > roomsAvailabilityDto.economy()) {
                        premiumUsage.left++;
                        premiumUsage.right = premiumUsage.right.add(guest);
                        availablePremiumRooms.decrementAndGet();
                        return;
                    }

                    if (availableEconomyRooms.get() > 0) {
                        economyUsage.left++;
                        economyUsage.right = economyUsage.right.add(guest);
                        availableEconomyRooms.decrementAndGet();
                    }
                });

        RoomUsageDto premiumRoomsUsage = new RoomUsageDto(premiumUsage.left, premiumUsage.right.doubleValue());
        RoomUsageDto economyRoomsUsage = new RoomUsageDto(economyUsage.left, economyUsage.right.doubleValue());
        return new RoomOccupancyDto(premiumRoomsUsage, economyRoomsUsage);
    }
}
