package org.kevinkib;

import java.sql.Date;

public record GameEntity(Long id, Date creationDate, Date endDate, Long playerId, String playerName, String state){
}
