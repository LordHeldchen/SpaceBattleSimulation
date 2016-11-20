package ships.basicShips;

import java.util.List;
import java.util.Set;

import battle.ShipInstance;
import ships.blueprints.ArmorLayout;
import ships.blueprints.Blueprint;
import ships.blueprints.Component;
import ships.blueprints.PropulsionLayout;
import ships.blueprints.ShieldLayout;
import ships.blueprints.WeaponBlueprint;

public class Blueprint_StandardBattleship extends Blueprint {
    private static Set<WeaponBlueprint>  availableWeaponLayouts;
    private final int                    maxWeaponLayouts     = 3;
    private static Set<ArmorLayout>      availableArmorLayouts;
    private final int                    maxArmorLayouts      = 1;
    private static Set<ShieldLayout>     availableShieldLayouts;
    private final int                    maxShieldLayouts     = 1;
    private static Set<PropulsionLayout> availablePropulsionLayouts;
    private final int                    maxPropulsionLayouts = 1;
    private static Set<Component>        availableElectronicLayouts;
    private final int                    maxElectronicLayouts = 1;

    static {
        //TODO Actually the Component Pattern might be appropriate here. Description of components probably via String-identified attributes?
        availableWeaponLayouts.add(new WeaponBlueprint("'Mammoth' Heavy Rail Gun Batteries", 60, 900, 9, 40));
        availableArmorLayouts.add(new ArmorLayout("Basic Battleship Armor", 2400, 10));
        availableShieldLayouts.add(new ShieldLayout("Standard Battleship Barrier Shield", 0, 500, 100, 9));
        availablePropulsionLayouts.add(new PropulsionLayout("Basic Battleship Turbine", 30, 35, 40, 5));
    }

    public Blueprint_StandardBattleship(
                    String name,
                    String description,
                    List<WeaponBlueprint> weaponLayouts,
                    List<ArmorLayout> armorLayouts,
                    List<ShieldLayout> shieldLayouts,
                    List<PropulsionLayout> propulsionLayouts,
                    List<Component> electronicsLayouts) {
        super(name, description, weaponLayouts, armorLayouts, shieldLayouts, propulsionLayouts, electronicsLayouts);
    }

    @Override
    final public Set<WeaponBlueprint> getAvailableWeaponLayouts() {
        return availableWeaponLayouts;
    }

    @Override
    final public Set<ArmorLayout> getAvailableArmorLayouts() {
        return availableArmorLayouts;
    }

    @Override
    public Set<ShieldLayout> getAvailableShieldLayouts() {
        return availableShieldLayouts;
    }

    @Override
    public Set<PropulsionLayout> getAvailablePropulsionLayouts() {
        return availablePropulsionLayouts;
    }

    @Override
    public Set<Component> getAvailableElectronicLayouts() {
        return availableElectronicLayouts;
    }

    @Override
    public ShipInstance instantiateShip(int idOfOwningEmpire) {
        // TODO Auto-generated method stub
        return null;
    }
}
