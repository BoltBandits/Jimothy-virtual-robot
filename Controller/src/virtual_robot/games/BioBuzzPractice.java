package virtual_robot.games;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import virtual_robot.controller.Filters;
import virtual_robot.controller.Game;
import virtual_robot.controller.VirtualField;

/**
 * BioBuzz practice-field variant of the Decode game.
 *
 * Replicates the official 2026-2027 Decode goals and ramps and ADDS practice
 * "tipping buckets" for driver training. The buckets are finite-mass dynamic
 * bodies that get pushed around when the robot hits them.
 *
 * To USE this field: open {@code virtual_robot.config.Config} and change
 * {@code public static final Game GAME = new Decode();}
 * to                 {@code public static final Game GAME = new BioBuzzPractice();}
 *
 * To TUNE bucket layout: edit the BUCKET_POSITIONS array below. Positions are
 * in METERS from field center. (0, 0) is the field center; +y is "up" on
 * the on-screen view; the field extends +/- {@code HALF_FIELD_WIDTH_METERS}.
 */
public final class BioBuzzPractice extends Game {

    // Rough footprint of a practice bucket, in meters (0.20 m ~ 8 in)
    private static final double BUCKET_SIZE = 0.20;

    // {x, y} center positions, in meters, in the field frame.
    private static final double[][] BUCKET_POSITIONS = {
            { -0.60,  0.30 },  // Blue-side practice bucket (left-front)
            {  0.60,  0.30 },  // Red-side practice bucket (right-front)
            {  0.00, -0.60 },  // Center-back bucket
    };

    private static final double BUCKET_DENSITY = 15.0;

    @Override
    public void initialize() {
        super.initialize();

        // ----- Decode field: goals + ramps (static) -----
        Polygon blueGoalPoly = new Polygon(new Vector2(0, 0), new Vector2(0, -0.547),
                new Vector2(0.217, -0.547), new Vector2(0.604, 0));
        Rectangle blueRampRect = new Rectangle(0.217, 1.282);
        Polygon redGoalPoly = new Polygon(new Vector2(0, 0), new Vector2(-0.624, 0),
                new Vector2(-0.217, -0.547), new Vector2(0, -0.547));
        Rectangle redRampRect = new Rectangle(0.217, 1.282);

        blueGoalPoly.translate(-VirtualField.HALF_FIELD_WIDTH_METERS,
                VirtualField.HALF_FIELD_WIDTH_METERS);
        blueRampRect.translate(-VirtualField.HALF_FIELD_WIDTH_METERS
                + blueRampRect.getWidth() / 2.0, blueRampRect.getHeight() / 2.0);
        redGoalPoly.translate(VirtualField.HALF_FIELD_WIDTH_METERS,
                VirtualField.HALF_FIELD_WIDTH_METERS);
        redRampRect.translate(VirtualField.HALF_FIELD_WIDTH_METERS
                - redRampRect.getWidth() / 2.0, redRampRect.getHeight() / 2.0);

        Convex[] staticConvexes = { blueGoalPoly, blueRampRect, redGoalPoly, redRampRect };
        for (Convex convex : staticConvexes) {
            Body body = new Body();
            BodyFixture fixture = body.addFixture(convex);
            fixture.setFilter(Filters.WALL_FILTER);
            body.setMass(MassType.INFINITE);
            world.addBody(body);
        }

        // ----- Practice buckets (dynamic) -----
        for (double[] pos : BUCKET_POSITIONS) {
            Body bucket = new Body();
            BodyFixture fixture = bucket.addFixture(
                    new Rectangle(BUCKET_SIZE, BUCKET_SIZE),
                    BUCKET_DENSITY,
                    0.4,
                    0.1);
            fixture.setFilter(Filters.WALL_FILTER);
            bucket.setMass(MassType.NORMAL);
            bucket.translate(pos[0], pos[1]);
            bucket.setLinearDamping(2.5);
            bucket.setAngularDamping(3.0);
            world.addBody(bucket);
        }
    }

    @Override public void resetGameElements() { }
    @Override public boolean hasHumanPlayer() { return false; }
    @Override public boolean isHumanPlayerAuto() { return false; }
    @Override public void setHumanPlayerAuto(boolean selected) { }
    @Override public void updateHumanPlayerState(double millis) { }
    @Override public void requestHumanPlayerAction() { }
    @Override public boolean isHumanPlayerActionRequested() { return false; }
    @Override public void stopGameElements() { }
}
