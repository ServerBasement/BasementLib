package it.ohalee.basementlib.common.persistence.connector.hikari.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PropertyPair {
    private final HikariProperty property;
    private final String value;
}
