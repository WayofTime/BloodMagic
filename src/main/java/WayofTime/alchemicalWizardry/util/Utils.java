package WayofTime.alchemicalWizardry.util;

public class Utils {

    public static boolean isInteger(String integer) {
        try {
            Integer.parseInt(integer);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
