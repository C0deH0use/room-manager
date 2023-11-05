package pl.codehouse.kata.roommanager.api.room;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
class GuestLoaderConfiguration {
    public static final int MAX_GUESTS_DISPLAY = 100;
    private final Logger log = LoggerFactory.getLogger(GuestLoaderConfiguration.class);

    private final String fileName;

    GuestLoaderConfiguration(@Value("${room.manager.guests-file}") String fileName) {
        this.fileName = fileName;
    }

    @Bean("guests")
    @Scope(value = WebApplicationContext.SCOPE_APPLICATION)
    public List<BigDecimal> getGuests() throws IOException {
        return loadGuests();
    }

    private List<BigDecimal> loadGuests() throws IOException {
        log.info("Reading Guests from file... {}", fileName);
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            Objects.requireNonNull(inputStream, "Guests file is missing");
            String line = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            log.info("Parsing content of the file....");

            List<BigDecimal> loadedGuests = Arrays.stream(line.replaceAll("\\[|\\]", "")
                            .split(", "))
                    .filter(StringUtils::isNotBlank)
                    .map(BigDecimal::new)
                    .map(bd -> bd.setScale(2, RoundingMode.HALF_DOWN))
                    .toList();

            log.info("Loaded {} guests from configuration file", loadedGuests.size());
            log.info("First {} guests loaded {}....",
                    MAX_GUESTS_DISPLAY,
                    loadedGuests.stream().limit(MAX_GUESTS_DISPLAY).collect(Collectors.toList())
            );
            return loadedGuests;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
