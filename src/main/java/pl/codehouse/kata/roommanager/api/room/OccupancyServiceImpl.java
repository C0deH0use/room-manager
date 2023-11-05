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
    private final Predicate<? super BigDecimal> economyGuestPredicate = not(premiumGuestPredicate);

    public OccupancyServiceImpl(@Qualifier("guests") List<BigDecimal> guests) {
        this.guests = guests.stream().sorted(Comparator.reverseOrder()).toList();
    }

    @Override
    public RoomOccupancyDto checkInGuests(RoomsAvailabilityDto roomsAvailabilityDto) {
        var availablePremiumRooms = new AtomicInteger(roomsAvailabilityDto.premium());
        var premiumUsage = MutablePair.of(0, ZERO_WITH_SCALE);
        var availableEconomyRooms = new AtomicInteger(roomsAvailabilityDto.economy());
        var economyUsage = MutablePair.of(0, ZERO_WITH_SCALE);

        long economyGuests = this.guests
                .stream()
                .filter(not(premiumGuestPredicate))
                .count();
        for (var guest : this.guests) {

            if (premiumGuestPredicate.test(guest) && availablePremiumRooms.get() > 0) {
                premiumUsage.left++;
                premiumUsage.right = premiumUsage.right.add(guest);
                availablePremiumRooms.decrementAndGet();
            }

            if (economyGuestPredicate.test(guest)) {
                if (isEconomyGuestAllowedForUpgrade(roomsAvailabilityDto, availablePremiumRooms, economyGuests)) {
                    premiumUsage.left++;
                    premiumUsage.right = premiumUsage.right.add(guest);
                    availablePremiumRooms.decrementAndGet();
                } else if (availableEconomyRooms.get() > 0) {
                    economyUsage.left++;
                    economyUsage.right = economyUsage.right.add(guest);
                    availableEconomyRooms.decrementAndGet();
                }
            }
        }

        RoomUsageDto premiumRoomsUsage = new RoomUsageDto(premiumUsage.left, premiumUsage.right.doubleValue());
        RoomUsageDto economyRoomsUsage = new RoomUsageDto(economyUsage.left, economyUsage.right.doubleValue());
        return new RoomOccupancyDto(premiumRoomsUsage, economyRoomsUsage);
    }

    private static boolean isEconomyGuestAllowedForUpgrade(RoomsAvailabilityDto roomsAvailabilityDto, AtomicInteger availablePremiumRooms, long economyGuests) {
        return availablePremiumRooms.get() > 0 && economyGuests > roomsAvailabilityDto.economy();
    }
}
