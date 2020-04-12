import java.awt.*;
import javax.swing.*;
import javax.swing.text.html.parser.Parser;


public class WeatherGUI extends JFrame {

    private JButton button = new JButton("Get");
    private JTextField input = new JTextField("", 5);
    private JLabel label = new JLabel("Input your city: ");

    public WeatherGUI() {
        super("Simple Weather App");
        this.setBounds(100, 100, 250, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 2, 2, 2));
        container.add(label);
        container.add(input);

        button.addActionListener(e -> {
                    JOptionPane.showMessageDialog(null, weatherParser.doParser(input), "", JOptionPane.PLAIN_MESSAGE);
                });
        container.add(button);
    }
}
