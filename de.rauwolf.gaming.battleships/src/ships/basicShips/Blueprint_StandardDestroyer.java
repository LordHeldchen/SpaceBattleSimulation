package ships.basicShips;

import java.util.List;
import java.util.Set;

import ships.blueprints.Blueprint;
import ships.layouts.ArmorLayout;
import ships.layouts.ElectronicsLayout;
import ships.layouts.PropulsionLayout;
import ships.layouts.ShieldLayout;
import ships.layouts.WeaponLayout;

public class Blueprint_StandardDestroyer extends Blueprint {
    private static Set<WeaponLayout>      availableWeaponLayouts;
    private final int                     maxWeaponLayouts     = 3;
    private static Set<ArmorLayout>       availableArmorLayouts;
    private final int                     maxArmorLayouts      = 1;
    private static Set<ShieldLayout>      availableShieldLayouts;
    private final int                     maxShieldLayouts     = 1;
    private static Set<PropulsionLayout>  availablePropulsionLayouts;
    private final int                     maxPropulsionLayouts = 1;
    private static Set<ElectronicsLayout> availableElectronicLayouts;
    private final int                     maxElectronicLayouts = 1;

    static {
        //TODO Actually the Component Pattern might be appropriate here. Description of components probably via String-identified attributes?
        availableWeaponLayouts.add(new WeaponLayout("'Sharp' Precision Lasers", 90, 80, 3, 15));
        availableArmorLayouts.add(new ArmorLayout("Basic Destroyer Armor", 450, 4));
        availableShieldLayouts.add(new ShieldLayout("Standard Destroyer Barrier Shield", 0, 100, 20, 4));
        availablePropulsionLayouts.add(new PropulsionLayout("Basic Destroyer Turbine", 70, 20, 70, 9));
    }

    public Blueprint_StandardDestroyer(
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
