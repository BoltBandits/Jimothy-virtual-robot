package virtual_robot.games;

/**
 * Shared counter state for the BioBuzz lab game.
 *
 * The {@link BioBuzzGame} updates these counters each physics tick based on
 * where the robot is and what its mechanisms are doing. Student OpModes read
 * them to confirm their logic worked:
 *
 * <pre>
 * telemetry.addData("Pollen carried", BioBuzzScoreboard.getPollenCarried());
 * telemetry.addData("Score",         BioBuzzScoreboard.getAllianceScore());
 * </pre>
 *
 * Nothing in here imports JavaFX, so it is safe to touch from any thread.
 * All fields are {@code volatile} because the physics thread writes them and
 * the OpMode thread reads them.
 */
public final class BioBuzzScoreboard {

    /** Max pollen the robot can carry at one time (BIOBUZZ rulebook limit = 5). */
    public static final int MAX_CARRY = 5;

    // Pollen the robot is physically holding right now (not yet scored).
    private static volatile int pollenCarried = 0;

    // Totals placed/scored this match.
    private static volatile int pollenInHive = 0;
    private static volatile int pollenInGarden = 0;
    private static volatile int pollenInFlowers = 0;

    private static volatile int allianceScore = 0;

    // Last-event description for the HUD + telemetry.
    private static volatile String lastEvent = "";

    private BioBuzzScoreboard() { /* static only */ }

    // ------------------------------------------------------------------ reads

    public static int    getPollenCarried()   { return pollenCarried; }
    public static int    getPollenInHive()    { return pollenInHive; }
    public static int    getPollenInGarden()  { return pollenInGarden; }
    public static int    getPollenInFlowers() { return pollenInFlowers; }
    public static int    getAllianceScore()   { return allianceScore; }
    public static String getLastEvent()       { return lastEvent; }

    // -------------------------------------------------------------- writes

    static synchronized boolean pickupOnePollen(String fromFlower) {
        if (pollenCarried >= MAX_CARRY) return false;
        pollenCarried++;
        lastEvent = "+1 pollen from " + fromFlower + " (carrying " + pollenCarried + ")";
        return true;
    }

    static synchronized boolean launchOnePollenIntoHive() {
        if (pollenCarried <= 0) return false;
        pollenCarried--;
        pollenInHive++;
        allianceScore += 2;  // simple scoring: 2 pts per hive deposit
        lastEvent = "+2 hive deposit (score " + allianceScore + ")";
        return true;
    }

    static synchronized boolean placeOnePollenInGarden() {
        if (pollenCarried <= 0) return false;
        pollenCarried--;
        pollenInGarden++;
        allianceScore += 1;  // 1 pt per pollen in garden
        lastEvent = "+1 garden place (score " + allianceScore + ")";
        return true;
    }

    static synchronized boolean placeOnePollenInFlower(String flowerName) {
        if (pollenCarried <= 0) return false;
        pollenCarried--;
        pollenInFlowers++;
        allianceScore += 2;  // 2 pts per pollen in owned flower
        lastEvent = "+2 flower place at " + flowerName + " (score " + allianceScore + ")";
        return true;
    }

    /** Called by BioBuzzGame.resetGameElements() at the start of each OpMode. */
    public static synchronized void reset() {
        pollenCarried   = 0;
        pollenInHive    = 0;
        pollenInGarden  = 0;
        pollenInFlowers = 0;
        allianceScore   = 0;
        lastEvent       = "";
    }
}
