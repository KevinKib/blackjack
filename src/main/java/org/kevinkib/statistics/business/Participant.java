package org.kevinkib.statistics.business;

import java.util.List;

public record Participant(Role role, Hand hand, List<ParticipantTurn> turns) {
}
