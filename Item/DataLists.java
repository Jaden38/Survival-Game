package Item;

import java.util.Arrays;
import java.util.List;

public class DataLists {
    // List of possible resource names
    public static final List<String> resourceNames = Arrays.asList("Gold", "Iron", "Wood", "Food", "Water");

    // List of possible resource quantities
    public static final List<Integer> resourceQuantities = Arrays.asList(10, 20, 30, 40, 50);

    // List of possible danger names
    public static final List<String> dangerNames = Arrays.asList("Dragon", "Goblin", "Giant", "Hellhorse", "Dreadnought", "Gnoll", "Dwarf", "Pheonix");

    // List of possible monster quantities
    public static final List<Integer> monsterQuantities = Arrays.asList(1, 2, 3, 4, 5);

    // List of possible danger descriptions
    public static final List<String> dangerDescriptions = Arrays.asList(
            "Breathes fire", "Steals treasures", "Spreads plague", "Casts dark spells", "Rises from the dead");

    // List of possible danger damages
    public static final List<Integer> dangerDamages = Arrays.asList(10, 20, 30, 40, 50, 60);

    public static final List<Integer> dangerXp = Arrays.asList(5,10,15,20,25,30);
}
