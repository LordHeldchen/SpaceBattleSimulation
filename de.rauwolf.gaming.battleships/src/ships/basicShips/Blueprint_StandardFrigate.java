package ships.basicShips;

import java.util.List;
import java.util.Set;

import ships.blueprints.Blueprint;
import ships.layouts.ArmorLayout;
import ships.layouts.ElectronicsLayout;
import ships.layouts.PropulsionLayout;
import ships.layouts.ShieldLayout;
import ships.layouts.WeaponLayout;

public class Blueprint_StandardFrigate extends Blueprint {
    private static Set<WeaponLayout>      availableWeaponLayouts;
    private final int                     maxWeaponLayouts     = 1;
    private static Set<ArmorLayout>       availableArmorLayouts;
    private final int                     maxArmorLayouts      = 1;
    private static Set<ShieldLayout>      availableShieldLayouts;
    private final int                     maxShieldLayouts     = 1;
    private static Set<PropulsionLayout>  availablePropulsionLayouts;
    private final int                     maxPropulsionLayouts = 1;
    private static Set<ElectronicsLayout> availableElectronicLayouts;
    private final int                     maxElectronicLayouts = 2;

    static {
        availableWeaponLayouts.add(new WeaponLayout("'Punisher' Combat Missiles", 85, 140, 3, 20));
        availableWeaponLayouts.add(new WeaponLayout("'Perforator' Assault Laser Batteries", 90, 210, 4, 30));
        availableArmorLayouts.add(new ArmorLayout("Basic Frigate Armor", 320, 4));
        availableArmorLayouts.add(new ArmorLayout("Assault Frigate Armor", 200, 2));
        availablePropulsionLayouts.add(new PropulsionLayout("Basic Frigate Turbine", 80, 20, 90, 10));
        availablePropulsionLayouts.add(new PropulsionLayout("Stealth Drives", 70, 30, 70, 12));
    }

    public Blueprint_StandardFrigate(
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
