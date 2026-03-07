package org.kevinkib;

import java.sql.Date;

public record LegacyGameEntity(Long id, Date creationDate, Date endDate, Long playerId, String playerName, String state){
}
