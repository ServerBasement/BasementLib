package it.ohalee.basementlib.common.persistence.hikari.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PropertyPair {
    private final HikariProperty property;
    private final String value;
}
