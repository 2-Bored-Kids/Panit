import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ColorTasks {

    public static void colorMode(){
        pickColor();
    }

    public static void pickColor(){

        JFrame frame = new JFrame("Color Picker");
        frame.setSize(650, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.requestFocus();

        JColorChooser colorChooser = new JColorChooser(Utils.getColor(Main.getPen()));
        colorChooser.setBounds(0,0, 650, 400);
        frame.add(colorChooser);

        JButton button = new JButton("Change color");
        button.setBounds(0, 400, 650, 50);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.pickColor(colorChooser.getColor());
                UI.a_colors.clearSelection();
                frame.dispose();
            }
        });
        frame.add(button);

        frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                super.windowLostFocus(e);
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }
}

