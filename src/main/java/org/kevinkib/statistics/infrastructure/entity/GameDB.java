package org.kevinkib.statistics.infrastructure.entity;

import java.sql.Date;

public record GameDB(
        Long id,
        Date creationDate,
        String state
) {
}
