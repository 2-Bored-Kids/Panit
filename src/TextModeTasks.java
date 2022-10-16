import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class TextModeTasks {

    public static boolean texting = false;


    public static void textMode(int x, int y){
        if (texting){
            return;
        }
        texting = true;
        pickText(x, y);
    }




    public static void pickText(int x, int y){

        JFrame frame = new JFrame(UI.getMenuText("textTitle"));
        frame.setSize(260, 215);
        frame.setBackground(new Color(0, 186, 255));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.requestFocus();

        JLabel label = new JLabel(UI.getMenuText("textTitle") + ":");
        label.setBounds(20, 10, 200, 20);
        frame.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(20, 30, 200, 30);
        frame.add(textField);

        JSlider slider = new JSlider(1, 100, UI.r_line_width.wert() * 2);
        slider.setBorder(new LineBorder(Color.lightGray));
        slider.setBounds(20, 65, 200, 30);
        frame.add(slider);

        JComboBox<String> comboBox = new JComboBox<>(Consts.fontsNames);
        comboBox.setBounds(20, 100, 200, 30);
        frame.add(comboBox);

        JButton button = new JButton(UI.getMenuText("textButton"));
        button.setBounds(20, 135, 200, 30);
        button.addActionListener(e -> {
            PenTasks.printText(new Text(x, y, textField.getText(), slider.getValue(), Consts.fonts[comboBox.getSelectedIndex()]));
            frame.setVisible(false);
            frame.dispose();
        });
        frame.add(button);

        frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                super.windowLostFocus(e);
                frame.dispose();
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                TextModeTasks.texting = false;
            }
        });

        frame.setVisible(true);
    }
}
