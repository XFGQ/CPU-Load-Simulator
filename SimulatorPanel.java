package project;

import javax.swing.*;
import java.awt.*;
import java.util.Deque;

public class SimulatorPanel extends JPanel {
    private final SimulationEngine engine;

    public SimulatorPanel(SimulationEngine engine) {
        this.engine = engine;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(900, 400));
        setDoubleBuffered(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var cores = engine.getCores();

        g.setColor(Color.BLACK);
        g.drawString("Policy: " + engine.getPolicy() +
                "  | quantum=" + engine.getQuantum() +
                "  | balanceEvery=" + engine.getBalanceEvery() +
                "  | tick=" + engine.getNowTick(), 20, 15);

        int y = 30;
        int gapY = 70;

        for (var c : cores) {
            g.setColor(Color.BLACK);
            g.drawString("Core " + c.getId(), 20, y + 15);

            int x = 100;

            g.setColor(new Color(210, 235, 255));
            g.fillRect(x, y, 90, 40);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(x, y, 90, 40);
            g.setColor(Color.BLACK);
            g.drawString("RUN", x + 30, y + 25);

         
            x += 110;

            
            Deque<task> q = c.getScheduler().queueRef();

            if (q != null) {
                int boxW = 60, boxH = 40, gap = 8, drawn = 0;
                for (task t : q) {
                    if (drawn > 10) break;
                    g.setColor(new Color(230, 230, 230));
                    g.fillRect(x, y, boxW, boxH);
                    g.setColor(Color.GRAY);
                    g.drawRect(x, y, boxW, boxH);
                    g.setColor(Color.BLACK);
                    g.drawString(t.getId(), x + 8, y + 15);
                    g.drawString("rem:" + t.getRemainingTime(), x + 3, y + 30);
                    x += boxW + gap;
                    drawn++;
                }
            }

            y += gapY;
        }
    }
}
