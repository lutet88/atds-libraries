// GraphicsUtil.java VERSION 0.1 (September 20, 2022)
/**
 * A helper class containing utility methods for 2D graphics.
 * More methods will be added here if needed.
 */
public class GraphicsUtil {
    /**
     * Returns the point offset r pixels from a point (x, y) at an angle theta
     * @param x x in pixels
     * @param y y in pixels
     * @param theta theta in radians
     * @param r r in pixels
     * @return a Vec2 containing the offset point.
     */
    public static Vec2 offsetByAngle(double x, double y, double theta, double r) {
        return new Vec2(x + r * Math.cos(theta), y + r * Math.sin(theta));
    }

    /**
     * Returns the point offset r pixels from a starting point pt at an angle theta
     * @param pt pt, a Vec2 in pixels
     * @param theta theta in radians
     * @param r r in pixels
     * @return a Vec2 containing the offset point.
     */
    public static Vec2 offsetByAngle(Vec2 pt, double theta, double r) {
        return new Vec2(pt.x() + r * Math.cos(theta), pt.y() + r * Math.sin(theta));
    }
}
