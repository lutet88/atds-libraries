import java.awt.*;
import javax.swing.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * A 2D drawing canvas class, extending JComponent.
 * Encapsulates a Queue of GFXDrawable objects, which are rendered on refresh().
 * @see GFXDrawable
 * @see JComponent
 */
class DrawCanvas extends JComponent {
    private Queue<GFXDrawable> drawQueue = new LinkedList<>();

    /**
     * Overrides JComponent.paintComponent.
     * Called towards the end of the paint() and repaint() routines.
     * Iterates through the draw object queue and paints each object in order.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        for (GFXDrawable gd : drawQueue) {
            gd.draw(g2d);
        }
    }

    /**
     * Flushes the draw queue, removing all GFXDrawable objects from it.
     */
    public void flush() {
        drawQueue = new LinkedList<>();
    }

    /**
     * Adds a GFXDrawable object to the draw queue.
     * @param dr Object to add to the draw queue.
     */
    public void add(GFXDrawable dr) {
        drawQueue.add(dr);
    }

    /**
     * Refreshes and re-renders, calling repaint().
     * In order to draw new objects, refresh() must be called after add().
     */
    public void refresh() {
        repaint();
    }

    /**
     * Gets the current draw queue.
     * @return a Queue with all objects that will be drawn next time refresh() is called.
     */
    public Queue<GFXDrawable> getDrawQueue() {
        return drawQueue;
    }
}

/**
 * The main GraphicsHelper, a singleton class representing one drawable window.
 * This class contains all the drawing methods for interacting with the DrawCanvas.
 * By default, the window opened is 720x720.
 *
 * <br><br>Example:
 * <pre>{@code
 * // get the instance of this class, or create it if it doesn't exist
 * GraphicsHelper gfx = GraphicsHelper.getInstance();
 * // draw a circle on the center point with radius 100
 * gfx.drawCircle(gfx.center(), 100, Color.BLUE);
 * // refresh the canvas, redrawing all objects to be drawn
 * gfx.refresh();
 * }</pre>
 * @see DrawCanvas
 */
public class GraphicsHelper {
    private static GraphicsHelper singleton;
    private final JFrame window;
    private final DrawCanvas dc;

    /**
     * Creates a GraphicsHelper. Called once by getInstance(), as GraphicsHelper is a singleton.
     * @param w width of the window
     * @param h height of the window
     */
    private GraphicsHelper(int w, int h) {
        window = new JFrame();
        window.setSize(w, h);
        dc = new DrawCanvas();
        window.getContentPane().add(dc);
        window.setVisible(true);
    }

    /**
     * Returns a Vec2, representing the center of the drawing area.
     * @return an integer Vec2 containing the center's coordinates.
     */
    public Vec2 center() {
        return new Vec2(window.getSize().getWidth() / 2, window.getSize().getHeight() / 2);
    }

    /**
     * Returns the current instance of this GraphicsHelper.
     * As GraphicsHelper is a singleton, there is only one instance on each run.
     * getInstance() should be called instead of any constructors to create a GraphicsHelper object.
     * @return the current instance of GraphicsHelper.
     */
    public static GraphicsHelper getInstance() {
        if (singleton == null) singleton = new GraphicsHelper(720, 720);
        return singleton;
    }

    /**
     * Sets the window size of this GraphicsHelper.
     * @param w width of the window
     * @param h height of the window
     */
    public static void setWindowSize(int w, int h) {
        getInstance().window.setSize(w, h);
    }

    /**
     * Clears the draw queue, removing all objects from the queue.
     * This method does not refresh. See flush()
     */
    public void clear() {
        dc.flush();
    }

    /**
     * Flushes the draw queue, removing all objects drawn on screen.
     */
    public void flush() {
        dc.flush();
        dc.refresh();
    }

    /**
     * Refreshes and re-draws all objects in the draw queue.
     * In order to draw new objects, refresh() must be called after any drawing methods.
     */
    public void refresh() {
        dc.refresh();
    }

    /**
     * Draws a line from (x1, y1) to (x2, y2) with a fixed thickness and color.
     * @param x1 x1 in pixels
     * @param y1 y1 in pixels
     * @param x2 x2 in pixels
     * @param y2 y2 in pixels
     * @param thickness width of the line stroke in pixels.
     * @param c color of the line.
     */
    public void drawLine(double x1, double y1, double x2, double y2, double thickness, Color c) {
        dc.add(new GFXLine(new Vec2(x1, y1), new Vec2(x2, y2), getDefaultStroke(thickness), c));
    }

    /**
     * Draws a line from p1 to p2 with a fixed thickness and color.
     * @param p1 p1, a Vec2 in pixels
     * @param p2 p2, a Vec2 in pixels
     * @param thickness width of the line stroke in pixels.
     * @param c color of the line.
     */
    public void drawLine(Vec2 p1, Vec2 p2, double thickness, Color c) {
        dc.add(new GFXLine(p1, p2, getDefaultStroke(thickness), c));
    }

    public void drawLine(double x1, double y1, double x2, double y2, Stroke stroke, Color c) {
        dc.add(new GFXLine(new Vec2(x1, y1), new Vec2(x2, y2), stroke, c));
    }

    public void drawLine(Vec2 p1, Vec2 p2, Stroke stroke, Color c) {
        dc.add(new GFXLine(p1, p2, stroke, c));
    }

    /**
     * Draws a circle with radius r at (x, y) with a fixed color.
     * @param x x in pixels
     * @param y y in pixels
     * @param r r in pixels
     * @param c color of the point.
     */
    public void drawCircle(double x, double y, double r, Color c) {
        dc.add(new GFXCircle(new Vec2(x - r/2, y - r/2), r, getDefaultStroke(0), c));
    }

    /**
     * Draws a circle with radius r at pt with a fixed color.
     * @param pt pt, a Vec2 in pixels
     * @param r r in pixels
     * @param c color of the point.
     */
    public void drawCircle(Vec2 pt, double r, Color c) {
        dc.add(new GFXCircle(pt.add(new Vec2(-r/2, -r/2)), r, getDefaultStroke(0), c));
    }

    /**
     * Draws the outline of a circle with radius r at (x, y) with a fixed color and brush thickness.
     * @param x x in pixels
     * @param y y in pixels
     * @param r r in pixels
     * @param thickness width of the outline stroke in pixels.
     * @param c color of the outline stroke.
     */
    public void drawCircleOutline(double x, double y, double r, double thickness, Color c) {
        dc.add(new GFXCircleOutline(new Vec2(x - r/2, y - r/2), r, getDefaultStroke(thickness), c));
    }

    /**
     * Draws the outline of a circle with radius r at a center point with a fixed color and brush thickness.
     * @param center center, a Vec2 in pixels
     * @param r r in pixels
     * @param thickness width of the outline stroke in pixels.
     * @param c color of the outline stroke.
     */
    public void drawCircleOutline(Vec2 center, double r, double thickness, Color c) {
        dc.add(new GFXCircleOutline(center.add(new Vec2(-r/2, -r/2)), r, getDefaultStroke(thickness), c));
    }

    public void drawCircleOutline(double x, double y, double r, Stroke stroke, Color c) {
        dc.add(new GFXCircleOutline(new Vec2(x - r/2, y - r/2), r, stroke, c));
    }

    public void drawCircleOutline(Vec2 center, double r, Stroke stroke, Color c) {
        dc.add(new GFXCircleOutline(center.add(new Vec2(-r/2, -r/2)), r, stroke, c));
    }

    public void drawShape(Shape shape, Stroke stroke, Color strokeColor, Color fillColor) {
        dc.add(new GFXShape(shape, stroke, strokeColor, fillColor));
    }

    public void drawText(Vec2 pos, String text, String fontName, int fontSize, int fontStyle, Color clr) {
        dc.add(new GFXText(pos, text, new Font(fontName, fontStyle, fontSize), clr));
    }

    public void drawText(Vec2 pos, String text, Font font, Color clr) {
        dc.add(new GFXText(pos, text, font, clr));
    }

    public void drawText(Vec2 pos, String text, int fontSize, Color clr) {
        dc.add(new GFXText(pos, text, new Font("SansSerif", GFXText.PLAIN, fontSize), clr));
    }

    /**
     * Prints the entire contents of the drawing queue.
     */
    public void printObjects() {
        System.out.println(dc.getDrawQueue().toString());
    }

    public static Stroke getDefaultStroke(double thickness) {
        return new BasicStroke((float) thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    /**
     * Delays the main thread and display thread from doing anything for s seconds.
     * @param s number of seconds to delay. Processed with millisecond precision.
     */
    public void delay(double s) {
        try {
            TimeUnit.MILLISECONDS.sleep((long) (s * 1000));
        } catch (InterruptedException ignored) {
            // do nothing
        }
    }

    /**
     * toString() override.
     * @return String representation of the GraphicsHelper
     */
    @Override
    public String toString() {
        return "GraphicsHelper@"+hashCode()+": "+dc.getDrawQueue().toString();
    }
}

/**
 * An interface implemented by all objects that need to be drawn by a java.awt.Graphics2D on refresh.
 * @see Graphics2D
 */
interface GFXDrawable {
    public void draw(Graphics2D g2d);
}

/**
 * A record representing a 2-dimensional vector.
 * @param x x-value of the vector
 * @param y y-value of the vector
 */
record Vec2 (double x, double y) {
    /**
     * toString() method for Vec2.
     * @return the String representation of this Vec2, to 3 decimal points.
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###0.###");
        return "(" + df.format(x) + ", " + df.format(y) + ")";
    }

    /**
     * Add another Vec2 to this Vec2.
     * @param other other Vec2
     * @return other added to this vector.
     */
    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x(), y + other.y());
    }

    /**
     * Get the negation of this Vec2.
     * @return a new Vec2(-x, -y)
     */
    public Vec2 neg() {
        return new Vec2(-x, -y);
    }

    /**
     * Get the normalized (unit vector) form of this Vec2.
     * Direction is preserved.
     * @return the normalized form of this Vec2.
     */
    public Vec2 norm() {
        return new Vec2(x / mag(), y / mag());
    }

    /**
     * Multiply this Vec2 by a scalar.
     * @param scalar a scalar.
     * @return this vector multiplied by a scalar.
     */
    public Vec2 multiply(double scalar) {
        return new Vec2(x * scalar, y * scalar);
    }

    /**
     * Gets the magnitude of this Vec2.
     * @return the magnitude of this Vec2.
     */
    public double mag() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Gets the angle of this Vector relative to the x-axis.
     * The angle returned is in the range (-pi, pi].
     * Uses atan2(y, x).
     * @return The angle of this vector.
     */
    public double angle() {
        return Math.atan2(y, x);
    }

    /**
     * Returns the dot product of this Vec2 and other.
     * @param other The other Vec2.
     * @return The dot product of this Vec2 and other.
     */
    public double dot(Vec2 other) {
        return x * other.x() + y * other.y();
    }
}

/**
 * A GFXDrawable line.
 * @param p1 Coordinates of the starting point
 * @param p2 Coordinates of the ending point
 * @param stroke Brush stroke
 * @param clr Brush color
 * @see GFXDrawable
 */
record GFXLine (Vec2 p1, Vec2 p2, Stroke stroke, Color clr) implements GFXDrawable {
    public void draw(Graphics2D g2d) {
        g2d.setStroke(stroke);
        g2d.setColor(clr);
        g2d.draw(new Line2D.Double(p1.x(), p1.y(), p2.x(), p2.y()));
    }
}

/**
 * A GFXDrawable filled circle.
 * @param center Coordinates of the center of the circle
 * @param r Radius in pixels
 * @param stroke Outer brush stroke
 * @param clr Fill color
 * @see GFXDrawable
 */
record GFXCircle (Vec2 center, double r, Stroke stroke, Color clr) implements GFXDrawable {

    public void draw(Graphics2D g2d) {
        g2d.setStroke(stroke);
        g2d.setColor(clr);
        Shape circle = new Ellipse2D.Double(center.x(), center.y(), r, r);
        g2d.fill(circle);
    }
}

/**
 * A GFXDrawable circle outline.
 * @param center Coordinates of the center of the circle
 * @param r Radius in pixels
 * @param stroke Brush stroke
 * @param clr Brush color
 */
record GFXCircleOutline (Vec2 center, double r, Stroke stroke, Color clr) implements GFXDrawable {
    public void draw(Graphics2D g2d) {
        g2d.setStroke(stroke);
        g2d.setColor(clr);
        Shape circle = new Ellipse2D.Double(center.x(), center.y(), r, r);
        g2d.draw(circle);
    }
}

record GFXShape(Shape shape, Stroke stroke, Color strokeColor, Color fillColor) implements GFXDrawable{
    public void draw(Graphics2D g2d) {
        g2d.setStroke(stroke);
        g2d.setColor(strokeColor);
        g2d.draw(shape);
        g2d.setColor(fillColor);
        g2d.fill(shape);
    }
}

record GFXText(Vec2 pos, String text, Font font, Color clr) implements GFXDrawable{
    public static final int PLAIN = Font.PLAIN;
    public static final int ITALIC = Font.ITALIC;
    public static final int BOLD = Font.BOLD;
    public static final int BOLD_ITALIC = Font.BOLD | Font.ITALIC;

    public void draw(Graphics2D g2d) {
        g2d.setFont(font);
        g2d.setColor(clr);
        g2d.drawString(text, (int) pos.x(), (int) pos.y());
    }
}
