package Save_World.company;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Window extends JFrame {

    private Window() {
        this.setTitle("Save World");

        if (true) {
            this.setUndecorated(true);
            this.setExtendedState(this.MAXIMIZED_BOTH);
        } else {
            this.setSize(1024, 768);
            this.setLocationRelativeTo(null);
            this.setResizable(false);
        }

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(new Framework());
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
    }
}
