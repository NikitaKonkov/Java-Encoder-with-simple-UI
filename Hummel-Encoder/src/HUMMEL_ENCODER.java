import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class HUMMEL_ENCODER {
    private String EncoTextCopy;
    private String PBkeyCopy;
    private JTextField pbLenField;
    private JButton setPbLenButton;
    private JTextField textField;
    private JTextField pswField;
    private JButton encryptButton;
    private JButton decryptButton;
    private JTextArea resultArea;

    public HUMMEL_ENCODER() {
        JFrame frame = new JFrame("HUMMEL ENCODER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,   520);

        JPanel panel = new JPanel();
        frame.setLocationRelativeTo(null);
        panel.setBackground(Color.ORANGE); // for example, to set the background color to red

        frame.add(panel);
        placeComponents(panel);


        setPbLenButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                short pbLen = Short.parseShort(pbLenField.getText());
                SHUFFLE_CYPHER.setPbLen(pbLen);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }}});

        encryptButton.addActionListener(e -> {
            String text = textField.getText();
            String psw = pswField.getText();
            try {
                List<String> result = SHUFFLE_CYPHER.Hummelnest_Crypter(text, psw);
                EncoTextCopy = result.get(0);
                PBkeyCopy = result.get(1);
                resultArea.setText("[ ENCODED TEXT ]\n" + result.get(0) + "\n\n[ PUBLIC KEY ]\n" + result.get(1));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                String psw = pswField.getText();
                try {
                    String result = SHUFFLE_CYPHER.Hummelnest_Dectrypter(text, psw);
                    resultArea.setText("[ DECODED TEXT ]\n" +result);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);




        JLabel titleLabel = new JLabel("HUMMEL ENCODER");
        titleLabel.setBounds(320,  0,  200,  20);
        titleLabel.setFont(new Font("Impact", Font.PLAIN,  20));
        panel.add(titleLabel);


        JLabel infoLabel = new JLabel("PB[1-15]  PV[16-1024]");
        infoLabel.setBounds(520,  28,  200,  15);
        infoLabel.setFont(new Font("MS Gothic", Font.PLAIN,  15));
        panel.add(infoLabel);


        pbLenField = new JTextField(20);
        pbLenField.setBounds(520,  0,  165,  25);
        panel.add(pbLenField);

        setPbLenButton = new JButton("Keylen");
        setPbLenButton.setBounds(695,  0,  80,  25);
        panel.add(setPbLenButton);

        JLabel userLabel = new JLabel("Text");
        userLabel.setBounds(10,  55,  40,  25);
        panel.add(userLabel);

        textField = new JTextField(6);
        textField.setFont(new Font("Arial", Font.PLAIN,  20));
        textField.setBounds(40,  55,  735,  25);
        panel.add(textField);

        JLabel pswLabel = new JLabel("Password");
        pswLabel.setBounds(10,  0,  80,  25);
        panel.add(pswLabel);

        pswField = new JTextField(20);
        pswField.setBounds(80,  0,  165,  25);
        panel.add(pswField);

        encryptButton = new JButton("Encrypt");
        encryptButton.setBounds(300,  22,  80,  25);
        panel.add(encryptButton);

        decryptButton = new JButton("Decrypt");
        decryptButton.setBounds(400,  22,  80,  25);
        panel.add(decryptButton);

        resultArea = new JTextArea();
        resultArea.setFont(new Font("Arial", Font.PLAIN,  30));
        resultArea.setLineWrap(true); // Enable line wrapping
        resultArea.setWrapStyleWord(true); // Break lines at word boundaries

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBounds(10,  80,  765,  370); // Adjust the bounds as needed
        panel.add(scrollPane);

        JButton copyText = new JButton("Copy Text");
        copyText.setBounds(10,  450,  100,  25); // Adjust the bounds as needed
        panel.add(copyText);

        copyText.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(EncoTextCopy);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        JButton copyButton = new JButton("Copy Key");
        copyButton.setBounds(120,  450,  100,  25); // Adjust the bounds as needed
        panel.add(copyButton);

        copyButton.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(PBkeyCopy);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        JLabel autorLabel = new JLabel("by { $ymbitron }");
        autorLabel.setBounds(680,  455,  200,  20);
        autorLabel.setFont(new Font("Serif", Font.PLAIN,  14));
        panel.add(autorLabel);
        // by { $ymbitron }
    }

    public static void main(String[] args) throws IOException {
        SHUFFLE_CYPHER.setPRIMES("HARD_CODED_SIGNATURE"); // Matrix Master Key should be changed by private use.
        new HUMMEL_ENCODER();
    }
}
