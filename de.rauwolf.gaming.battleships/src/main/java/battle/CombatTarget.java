package main.java.battle;

import java.util.List;

public interface CombatTarget {
    void receiveAttack(Shot list);
    boolean isDestroyed();
	boolean reactBeforeAttacker(ShipInstance attacker);

    void startCurrentBattle();
	void endCurrentBattle();

	List<CombatActor> getCombatActors();
}
