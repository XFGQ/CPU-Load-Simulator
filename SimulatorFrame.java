package project;

import javax.swing.*;
import java.awt.*;

public class SimulatorFrame extends JFrame {
    private final SimulationEngine engine;
    private final SimulatorPanel panel;
    private Timer timer;
    private boolean running = false;

    public SimulatorFrame(SimulationEngine engine) {
        super("CPU Load Balancer – Simulation");
        this.engine = engine;
        this.panel = new SimulatorPanel(engine);

        JButton startBtn = new JButton("Start");
        JButton pauseBtn = new JButton("Pause");
        JButton stepBtn  = new JButton("Step");
        JSlider speed    = new JSlider(10, 1000, 300); // ms/tick

        startBtn.addActionListener(e -> running = true);
        pauseBtn.addActionListener(e -> running = false);
        stepBtn.addActionListener(e -> doStep());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(startBtn);
        controls.add(pauseBtn);
        controls.add(stepBtn);
        controls.add(new JLabel("Speed (ms/tick):"));
        controls.add(speed);

        timer = new Timer(speed.getValue(), e -> {
            timer.setDelay(speed.getValue());
            if (running && !engine.allDone()) {
                doStep();
            }
        });

        setLayout(new BorderLayout());
        add(controls, BorderLayout.NORTH);
        add(new JScrollPane(panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 480);
        setLocationRelativeTo(null);
        timer.start();
    }

    private void doStep() {
        engine.step();
        panel.repaint();

        if (engine.allDone()) {
            running = false;
            JOptionPane.showMessageDialog(this, engine.report(),
                    "Simulation Report", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
