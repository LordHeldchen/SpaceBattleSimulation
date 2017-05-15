package battle.CombatActions;

import battle.ShipInstance;
import battle.Shot;

public interface CombatAction {
    public boolean execute(ShipInstance ship, Shot shot);
}
