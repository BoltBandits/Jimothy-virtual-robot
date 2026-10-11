package virtual_robot.games;

import com.qualcomm.robotcore.hardware.CRServoImpl;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import virtual_robot.controller.Game;
import virtual_robot.controller.VirtualBot;
import virtual_robot.controller.VirtualField;
import virtual_robot.robots.classes.JimothyBot;

/**
 * The BioBuzz lab game.
 *
 * Counter-based (no physics balls): the robot's pose and mechanism state are
 * polled each tick; being in the right zone with the right mechanism running
 * updates {@link BioBuzzScoreboard}. OpModes watch the scoreboard and get
 * immediate telemetry feedback for correct code.
 *
 * Zones:
 *   - 4 FLOWERS, one at each wall midpoint (BIOBUZZ layout)
 *   - 1 HIVE at field center
 *   - 2 GARDENS in opposite corners
 *
 * Interaction rules:
 *   - Robot in flower pickup radius AND intake power > 0.3 for PICKUP_DWELL_MS
 *       → +1 pollenCarried (capped at MAX_CARRY)
 *   - Robot in hive radius AND shooter velocity > SHOOTER_READY AND windmill in
 *       feed direction for LAUNCH_DWELL_MS
 *       → -1 carried, +1 hive
 *   - Robot in alliance garden AND intake power < -0.3 (eject) for GARDEN_DWELL_MS
 *       → -1 carried, +1 garden
 */
public final class BioBuzzGame extends Game {

    // ----- geometry (field-center meters) ---------------------------------
    private static final double H = VirtualField.HALF_FIELD_WIDTH_METERS;
    // Flowers sit ~0.3 m inside each wall midpoint; tune to match biobuzz648.bmp.
    private static final Zone[] FLOWERS = new Zone[] {
            new Zone("Flower-N",  0.0,  H - 0.30, 0.25),
            new Zone("Flower-S",  0.0, -(H - 0.30), 0.25),
            new Zone("Flower-E",  H - 0.30,  0.0, 0.25),
            new Zone("Flower-W", -(H - 0.30), 0.0, 0.25),
    };
    private static final Zone HIVE = new Zone("Hive", 0.0, 0.0, 0.50);
    private static final Zone[] GARDENS = new Zone[] {
            new Zone("Garden-Blue", -(H - 0.30),  H - 0.30, 0.30),
            new Zone("Garden-Red",   H - 0.30, -(H - 0.30), 0.30),
    };

    // ----- interaction thresholds -----------------------------------------
    private static final double INTAKE_ON   =  0.30;
    private static final double INTAKE_EJECT = -0.30;
    private static final double SHOOTER_READY_TICKS = 150.0;
    private static final double WINDMILL_FEED      = -0.5;
    private static final double PICKUP_DWELL_MS = 300.0;
    private static final double LAUNCH_DWELL_MS = 500.0;
    private static final double GARDEN_DWELL_MS = 400.0;

    // ----- per-tick dwell timers (reset when robot leaves the zone) ------
    private final double[] flowerDwell = new double[FLOWERS.length];
    private double hiveDwell = 0;
    private final double[] gardenDwell = new double[GARDENS.length];

    // ----- HUD -----------------------------------------------------------
    private Text hudScore;
    private Text hudCarried;
    private Text hudHive;
    private Text hudGarden;
    private Text hudEvent;

    // ====================================================================
    @Override
    public void initialize() {
        super.initialize();
        buildVisuals();
        BioBuzzScoreboard.reset();
    }

    @Override
    public void resetGameElements() { BioBuzzScoreboard.reset(); }

    @Override public boolean hasHumanPlayer() { return false; }
    @Override public boolean isHumanPlayerAuto() { return false; }
    @Override public void setHumanPlayerAuto(boolean selected) { }
    @Override public void requestHumanPlayerAction() { }
    @Override public boolean isHumanPlayerActionRequested() { return false; }
    @Override public void stopGameElements() { }

    @Override
    public void updateHumanPlayerState(double millis) {
        VirtualBot bot = controller.getBot();
        if (!(bot instanceof JimothyBot)) return;
        JimothyBot jim = (JimothyBot) bot;

        double botXm = bot.getX() / VirtualField.PIXELS_PER_METER;
        double botYm = bot.getY() / VirtualField.PIXELS_PER_METER;

        DcMotorEx   intake   = jim.getIntakeMotorRef();
        DcMotorEx   shooter  = jim.getShooterMotorRef();
        CRServoImpl windmill = jim.getWindmillServoRef();
        if (intake == null || shooter == null || windmill == null) return;

        double intakePower   = intake.getPower();
        double shooterTicks  = Math.abs(shooter.getVelocity());
        double windmillPower = windmill.getPower();

        // --- flower pickup --------------------------------------------------
        for (int i = 0; i < FLOWERS.length; i++) {
            Zone f = FLOWERS[i];
            if (f.contains(botXm, botYm) && intakePower > INTAKE_ON) {
                flowerDwell[i] += millis;
                if (flowerDwell[i] >= PICKUP_DWELL_MS) {
                    if (BioBuzzScoreboard.pickupOnePollen(f.name)) {
                        flowerDwell[i] = 0;
                    } else {
                        // carry full; freeze timer at threshold
                        flowerDwell[i] = PICKUP_DWELL_MS;
                    }
                }
            } else {
                flowerDwell[i] = 0;
            }
        }

        // --- hive launch ----------------------------------------------------
        boolean readyToLaunch = HIVE.contains(botXm, botYm)
                && shooterTicks >= SHOOTER_READY_TICKS
                && windmillPower <= WINDMILL_FEED;
        if (readyToLaunch) {
            hiveDwell += millis;
            if (hiveDwell >= LAUNCH_DWELL_MS) {
                if (BioBuzzScoreboard.launchOnePollenIntoHive()) hiveDwell = 0;
                else hiveDwell = LAUNCH_DWELL_MS;
            }
        } else {
            hiveDwell = 0;
        }

        // --- garden place --------------------------------------------------
        for (int i = 0; i < GARDENS.length; i++) {
            Zone g = GARDENS[i];
            if (g.contains(botXm, botYm) && intakePower < INTAKE_EJECT) {
                gardenDwell[i] += millis;
                if (gardenDwell[i] >= GARDEN_DWELL_MS) {
                    if (BioBuzzScoreboard.placeOnePollenInGarden()) gardenDwell[i] = 0;
                    else gardenDwell[i] = GARDEN_DWELL_MS;
                }
            } else {
                gardenDwell[i] = 0;
            }
        }
    }

    @Override
    public void updateDisplay() {
        super.updateDisplay();
        if (hudScore == null) return;
        hudScore.setText("Score: "    + BioBuzzScoreboard.getAllianceScore());
        hudCarried.setText("Carried: " + BioBuzzScoreboard.getPollenCarried()
                + " / " + BioBuzzScoreboard.MAX_CARRY);
        hudHive.setText("Hive: "   + BioBuzzScoreboard.getPollenInHive());
        hudGarden.setText("Garden: " + BioBuzzScoreboard.getPollenInGarden());
        hudEvent.setText(BioBuzzScoreboard.getLastEvent());
    }

    // ====================================================================
    private void buildVisuals() {
        Platform.runLater(() -> {
            if (fieldPane == null) return;

            // Translucent circles marking each pickup / scoring zone.
            double ppm = VirtualField.PIXELS_PER_METER;
            double half = VirtualField.HALF_FIELD_WIDTH;
            for (Zone f : FLOWERS) addZoneCircle(f, Color.rgb(255, 215, 0, 0.25), half, ppm);
            addZoneCircle(HIVE, Color.rgb(255, 120, 0, 0.20), half, ppm);
            for (Zone g : GARDENS) addZoneCircle(g, Color.rgb(60, 200, 60, 0.25), half, ppm);

            // HUD box (top-left corner of the field pane).
            VBox hud = new VBox(2);
            hud.setLayoutX(8);
            hud.setLayoutY(8);
            hud.setStyle("-fx-background-color: rgba(0,0,0,0.55); -fx-padding: 6; -fx-background-radius: 4;");
            hudScore   = styled("Score: 0");
            hudCarried = styled("Carried: 0 / " + BioBuzzScoreboard.MAX_CARRY);
            hudHive    = styled("Hive: 0");
            hudGarden  = styled("Garden: 0");
            hudEvent   = styled("");
            hud.getChildren().addAll(hudScore, hudCarried, hudHive, hudGarden, hudEvent);
            fieldPane.getChildren().add(hud);
        });
    }

    private void addZoneCircle(Zone z, Color fill, double halfPx, double ppm) {
        // Convert field-center meters to fieldPane pixels (origin top-left, y DOWN).
        double cxPx = halfPx + z.xMeters * ppm;
        double cyPx = halfPx - z.yMeters * ppm;
        Circle c = new Circle(cxPx, cyPx, z.radiusMeters * ppm, fill);
        c.setStroke(fill.darker());
        fieldPane.getChildren().add(c);
    }

    private static Text styled(String s) {
        Text t = new Text(s);
        t.setFill(Color.WHITE);
        t.setFont(Font.font("Monospaced", 12));
        return t;
    }

    // --------------------------------------------------------------------
    private static final class Zone {
        final String name;
        final double xMeters;
        final double yMeters;
        final double radiusMeters;
        Zone(String n, double x, double y, double r) {
            name = n; xMeters = x; yMeters = y; radiusMeters = r;
        }
        boolean contains(double xm, double ym) {
            double dx = xm - xMeters;
            double dy = ym - yMeters;
            return dx * dx + dy * dy <= radiusMeters * radiusMeters;
        }
    }
}
