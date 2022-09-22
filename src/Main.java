import java.awt.*;

public class Main {
    private static GraphicsHelper gfx;

    public static void main(String[] args) {
        gfx = GraphicsHelper.getInstance();
        gfx.flush();

        mainReference();
    }

    public static void drawSnowflake(int depth, Vec2 center, double l) {

    }

    public static void example() {
        // this is an example routine to demonstrate the graphics library
        for (int i = 10; i > 0; i--) {
            // add a circle with decreasing size to the draw queue
            gfx.drawCircle(gfx.center(), i * 20, new Color(i * 20, i * 20, i * 20));
            // generate a point offset from the center at a decreasing angle
            Vec2 offsetPoint = GraphicsUtil.offsetByAngle(gfx.center(), i * Math.PI / 5.0, i * 20);
            // add a line from the center to that point
            gfx.drawLine(gfx.center(), offsetPoint, 10, new Color(255 - i * 20, 255 - i * 20, 255 - i * 20));
            // refresh (re-draw)
            gfx.refresh();
            // delay for 1 second
            gfx.delay(1);
        }
    }

    public static void mainReference() {
        for (int i = 0; i < 7; i++) {
            gfx.clear();
            gfx.drawText(new Vec2(50, 50), "bonjour", 30, Color.MAGENTA);
            gfx.drawText(new Vec2(50, 90), "fancy text", new Font("Serif", GFXText.BOLD_ITALIC, 30), Color.DARK_GRAY);
            drawSnowflakeReference(i, gfx.center(), 200);
            gfx.refresh();
            gfx.delay(1);
        }
    }

    public static void drawSnowflakeReference(int depth, Vec2 center, double l) {
        if (depth == 0) return;

        for (int i = 0; i < 6; i++) {
            Vec2 pt = GraphicsUtil.offsetByAngle(center, Math.PI / 3 * i, l);
            gfx.drawLine(center.x(), center.y(), pt.x(), pt.y(), new BasicStroke(4, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER), Color.BLUE);
            drawSnowflakeReference(depth-1, pt, l/3);
        }
    }
}
