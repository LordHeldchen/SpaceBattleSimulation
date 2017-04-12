package battle;

import logging.battleLogger.BattleLogger;
import ships.blueprints.MutableBaseStat;

public abstract class CombatActor implements Comparable<CombatActor> {
	private final MutableBaseStat      timeCost;

    private int            currentInitiative;

    protected SingleBattle currentBattle;
    protected BattleLogger logger;

    protected CombatActor(int startInitiative, MutableBaseStat timeCost) {
        this.setCurrentInitiative(startInitiative);
        this.timeCost = timeCost;
    }

    protected final int loseInitiative() {
        int currentIni = getCurrentInitiative();
        setCurrentInitiative(currentIni - timeCost.getCalculatedValue());
        return currentIni;
    }

    public final void setCurrentBattle(SingleBattle singleBattle) {
        this.currentBattle = singleBattle;
    }

    public final void addBattleLoger(BattleLogger battleLogger) {
        this.logger = battleLogger;
    }

    abstract CombatTarget takeAction();

    @Override
    public int compareTo(CombatActor other) {
        return other.getCurrentInitiative() - this.getCurrentInitiative();
    }

	public int getCurrentInitiative() {
		return currentInitiative;
	}

	public void setCurrentInitiative(int currentInitiative) {
		this.currentInitiative = currentInitiative;
	}
}
