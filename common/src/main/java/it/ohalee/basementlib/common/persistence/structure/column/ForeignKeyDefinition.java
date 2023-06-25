package it.ohalee.basementlib.common.persistence.structure.column;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ForeignKeyDefinition {
    private final String name;
    private final String outerDb;
    private final String outerTable;
    private final String outerColumn;
    private final String constraint;
}
