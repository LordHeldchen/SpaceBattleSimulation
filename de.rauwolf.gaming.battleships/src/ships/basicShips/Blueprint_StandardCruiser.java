package ships.basicShips;

import java.util.List;
import java.util.Set;

import ships.blueprints.Blueprint;
import ships.layouts.ArmorLayout;
import ships.layouts.ElectronicsLayout;
import ships.layouts.PropulsionLayout;
import ships.layouts.ShieldLayout;
import ships.layouts.WeaponLayout;

public class Blueprint_StandardCruiser extends Blueprint {
    private static Set<WeaponLayout>      availableWeaponLayouts;
    private final int                     maxWeaponLayouts     = 2;
    private static Set<ArmorLayout>       availableArmorLayouts;
    private final int                     maxArmorLayouts      = 1;
    private static Set<ShieldLayout>      availableShieldLayouts;
    private final int                     maxShieldLayouts     = 1;
    private static Set<PropulsionLayout>  availablePropulsionLayouts;
    private final int                     maxPropulsionLayouts = 1;
    private static Set<ElectronicsLayout> availableElectronicLayouts;
    private final int                     maxElectronicLayouts = 2;

    static {
        //TODO Actually the Component Pattern might be appropriate here. Description of components probably via String-identified attributes?
        availableWeaponLayouts.add(new WeaponLayout("'Pulsar' Heavy Blaster Cannons", 60, 340, 8, 25));
        availableWeaponLayouts.add(new WeaponLayout("'Magnus' Rail Gun Batteries", 65, 420, 5, 30));
        availableArmorLayouts.add(new ArmorLayout("Basic Cruiser Armor", 900, 6));
        availableArmorLayouts.add(new ArmorLayout("Battlecruiser Armor", 1200, 7));
        availableShieldLayouts.add(new ShieldLayout("Standard Cruiser Barrier Shield", 0, 250, 50, 6));
        availableShieldLayouts.add(new ShieldLayout("Battlecruiser Barrier Shield", 0, 300, 60, 7));
        availablePropulsionLayouts.add(new PropulsionLayout("Basic Cruiser Turbine", 50, 25, 55, 7));
        availablePropulsionLayouts.add(new PropulsionLayout("Basic Battlecruiser Turbine", 40, 25, 50, 6));
    }

    public Blueprint_StandardCruiser(
                    String name,
                    String description,
                    List<WeaponLayout> weaponLayouts,
                    List<ArmorLayout> armorLayouts,
                    List<ShieldLayout> shieldLayouts,
                    List<PropulsionLayout> propulsionLayouts,
                    List<ElectronicsLayout> electronicsLayouts) {
        super(name, description, weaponLayouts, armorLayouts, shieldLayouts, propulsionLayouts, electronicsLayouts);
    }

    @Override
    final public Set<WeaponLayout> getAvailableWeaponLayouts() {
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
    public Set<ElectronicsLayout> getAvailableElectronicLayouts() {
        return availableElectronicLayouts;
    }
}
