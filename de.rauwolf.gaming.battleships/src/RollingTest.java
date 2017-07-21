import java.util.Arrays;
import java.util.Vector;

public class RollingTest {

    static Vector<int[]> fiveOfAKinds      = new Vector<int[]>();
    static Vector<int[]> fourOfAKinds      = new Vector<int[]>();
    static Vector<int[]> threeOfAKinds     = new Vector<int[]>();
    static Vector<int[]> twoOfAKinds       = new Vector<int[]>();
    static Vector<int[]> twoPairs          = new Vector<int[]>();
    static Vector<int[]> fullHouses        = new Vector<int[]>();
    static Vector<int[]> sixHighStraights  = new Vector<int[]>();
    static Vector<int[]> fiveHighStraights = new Vector<int[]>();

    static void is5OfAKind(int[] dice) {
        boolean isFiveOfAKind = true;
        for (int i = 1; i < dice.length; i++) {
            isFiveOfAKind &= (dice[0] == dice[i]);
        }
        if (isFiveOfAKind) {
            int[] toBeAdded = Arrays.copyOf(dice, 5);
            fiveOfAKinds.add(toBeAdded);
        }
    }

    static void is4OfAKind(int[] dice) {
        int[] numCount = { 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < dice.length; i++) {
            numCount[dice[i] - 1]++;
        }
        boolean is4OfAKind = false;
        for (int i = 0; i < numCount.length; i++) {
            if (numCount[i] == 4) {
                is4OfAKind = true;
            }
        }
        if (is4OfAKind) {
            int[] toBeAdded = Arrays.copyOf(dice, 5);
            fourOfAKinds.add(toBeAdded);
        }
    }

    static void is3OfAKind(int[] dice) {
        int[] numCount = { 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < dice.length; i++) {
            numCount[dice[i] - 1]++;
        }
        boolean is3OfAKind = false;
        boolean mightBeFullHouse = false;
        for (int i = 0; i < numCount.length; i++) {
            if (numCount[i] == 3) {
                is3OfAKind = true;
            }
            if (numCount[i] == 2) {
                mightBeFullHouse = true;
            }
        }
        if (is3OfAKind && !mightBeFullHouse) {
            int[] toBeAdded = Arrays.copyOf(dice, 5);
            threeOfAKinds.add(toBeAdded);
        }
    }

    static void is2OfAKind(int[] dice) {
        int[] numCount = { 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < dice.length; i++) {
            numCount[dice[i] - 1]++;
        }
        boolean is2OfAKind = false;
        boolean mightBeFullHouse = false;
        for (int i = 0; i < numCount.length; i++) {
            if (numCount[i] == 2) {
                is2OfAKind = !is2OfAKind;
            }
            if (numCount[i] == 3) {
                mightBeFullHouse = true;
            }
        }
        if (is2OfAKind && !mightBeFullHouse) {
            int[] toBeAdded = Arrays.copyOf(dice, 5);
            twoOfAKinds.add(toBeAdded);
        }
    }

    static void isTwoPairs(int[] dice) {
        int[] numCount = { 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < dice.length; i++) {
            numCount[dice[i] - 1]++;
        }
        int isTwoPairs = 0;
        for (int i = 0; i < numCount.length; i++) {
            if (numCount[i] == 2) {
                isTwoPairs++;
            }
        }
        if (isTwoPairs == 2) {
            int[] toBeAdded = Arrays.copyOf(dice, 5);
            twoPairs.add(toBeAdded);
        }
    }

    static void isFullHouse(int[] dice) {
        int[] numCount = { 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < dice.length; i++) {
            numCount[dice[i] - 1]++;
        }
        boolean has2OfAKind = false;
        boolean has3OfAKind = false;
        for (int i = 0; i < numCount.length; i++) {
            if (numCount[i] == 2) {
                has2OfAKind = true;
            }
            if (numCount[i] == 3) {
                has3OfAKind = true;
            }
        }
        if (has2OfAKind && has3OfAKind) {
            int[] toBeAdded = Arrays.copyOf(dice, 5);
            fullHouses.add(toBeAdded);
        }
    }

    static void is5HighStraight(int[] dice) {
        int[] numCount = { 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < dice.length; i++) {
            numCount[dice[i] - 1]++;
        }
        boolean is5HighStraight = true;
        for (int i = 0; i < numCount.length - 1; i++) {
            is5HighStraight &= (numCount[i] == 1);
        }
        if (is5HighStraight) {
            int[] toBeAdded = Arrays.copyOf(dice, 5);
            fiveHighStraights.add(toBeAdded);
        }
    }

    static void is6HighStraight(int[] dice) {
        int[] numCount = { 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < dice.length; i++) {
            numCount[dice[i] - 1]++;
        }
        boolean is6HighStraight = true;
        for (int i = 1; i < numCount.length; i++) {
            is6HighStraight &= (numCount[i] == 1);
        }
        if (is6HighStraight) {
            int[] toBeAdded = Arrays.copyOf(dice, 5);
            sixHighStraights.add(toBeAdded);
        }
    }

    public static void main(String[] args) {
        int[] dice = new int[5];
        int permutations = 0;

        for (dice[0] = 1; dice[0] <= 6; dice[0]++) {
            for (dice[1] = 1; dice[1] <= 6; dice[1]++) {
                for (dice[2] = 1; dice[2] <= 6; dice[2]++) {
                    for (dice[3] = 1; dice[3] <= 6; dice[3]++) {
                        for (dice[4] = 1; dice[4] <= 6; dice[4]++) {
                            is6HighStraight(dice);
                            is5HighStraight(dice);
                            isFullHouse(dice);
                            isTwoPairs(dice);
                            is2OfAKind(dice);
                            is3OfAKind(dice);
                            is4OfAKind(dice);
                            is5OfAKind(dice);
                            permutations++;
                        }
                    }
                }
            }
        }

        System.out.println("===== fiveOfAKinds: " + fiveOfAKinds.size());
        printVector(fiveOfAKinds);
        System.out.println("===== sixHighStraights: " + sixHighStraights.size());
        printVector(sixHighStraights);
        System.out.println("===== fiveHighStraights: " + fiveHighStraights.size());
        printVector(fiveHighStraights);
        System.out.println("===== fourOfAKinds: " + fourOfAKinds.size());
        printVector(fourOfAKinds);
        System.out.println("===== fullHouses: " + fullHouses.size());
        printVector(fullHouses);
        System.out.println("===== threeOfAKinds: " + threeOfAKinds.size());
        printVector(threeOfAKinds);
        System.out.println("===== twoPairs: " + twoPairs.size());
        printVector(twoPairs);
        System.out.println("===== twoOfAKinds: " + twoOfAKinds.size());
        printVector(twoOfAKinds);
        System.out.println("===== Nothing: " + (permutations
                                                - fiveOfAKinds.size()
                                                - fourOfAKinds.size()
                                                - threeOfAKinds.size()
                                                - twoOfAKinds.size()
                                                - twoPairs.size()
                                                - fullHouses.size()
                                                - sixHighStraights.size()
                                                - fiveHighStraights.size()));
        
        System.out.println("\n===== Overall permutations: " + permutations);
    }

    private static void printVector(Vector<int[]> toPrint) {
        for (int[] element : toPrint) {
            System.out.print("[");
            int count = 0;
            for (int dice : element) {
                System.out.print(dice);
                if(++count < element.length) {
                    System.out.print("; ");
                }
            }
            System.out.println("]");
        }
    }
}
