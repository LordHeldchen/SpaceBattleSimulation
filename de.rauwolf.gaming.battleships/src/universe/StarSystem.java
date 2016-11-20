package universe;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import battle.ShipInstance;
import ships.Fleet;
import ships.blueprints.ArmorLayout;
import ships.blueprints.Blueprint;
import ships.blueprints.PropulsionLayout;
import ships.blueprints.WeaponBlueprint;

public class StarSystem {
    private static final int participatingEmpireA = 1;
    private static final int participatingEmpireB = 2;

    private static Blueprint getStandardFighterBlueprint() {
        WeaponBlueprint weaponLayout = new WeaponBlueprint("'Vulcan' Close Combat Laser Guns", 100, 50, 3, 5);
        ArmorLayout armorLayout = new ArmorLayout("Basic Fighter Armor", 75, 2);
        PropulsionLayout propulsionLayout = new PropulsionLayout("Basic Fighter Turbine", 100, 15, 100, 0);

        LinkedList<WeaponBlueprint> weaponList = new LinkedList<WeaponBlueprint>();
        weaponList.add(weaponLayout);
        LinkedList<ArmorLayout> armorList = new LinkedList<ArmorLayout>();
        armorList.add(armorLayout);
        LinkedList<PropulsionLayout> propulsionList = new LinkedList<PropulsionLayout>();
        propulsionList.add(propulsionLayout);

        Blueprint fighterBlueprint = new Blueprint("Standard Fighter", "SomeDescription",
                        weaponList, armorList, null, propulsionList, null);

        return fighterBlueprint;
    }

    public static Set<Fleet> getAllFleetsFromStarSystem() {
        Blueprint fighterBlueprint = getStandardFighterBlueprint();

        Set<Fleet> listOfFleets = new HashSet<Fleet>();

        Fleet fleetA = new Fleet();

        for (int i = 0; i < 5; i++) {
            fleetA.add(new ShipInstance(participatingEmpireA, fighterBlueprint));
        }

        Fleet fleetB = new Fleet();
        for (int i = 0; i < 10; i++) {
            fleetB.add(new ShipInstance(participatingEmpireB, fighterBlueprint));
        }

        listOfFleets.add(fleetA);
        listOfFleets.add(fleetB);
        return listOfFleets;
    }
}
