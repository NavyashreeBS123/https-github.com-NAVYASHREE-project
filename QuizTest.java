import java.awt.event.*;
import javax.swing.*;

public class QuizTest extends JFrame implements ActionListener {

    JLabel label;
    JRadioButton radioButtons[] = new JRadioButton[4];
    JButton btnNext, btnResult;
    ButtonGroup bg;

    int count = 0, current = 0;

    // Questions array
    String questions[] = {
        "Android official language?",
        "Backend language?",
        "Database related?",
        "OOP language?",
        "Frontend language?",
        "Not a programming language?",
        "Used for web?",
        "Server-side language?",
        "Scripting language?",
        "Compiled language?"
    };

    // Correct answers index
    int correct[] = {1, 0, 3, 2, 3, 1, 3, 0, 3, 2};

    QuizTest(String s) {
        super(s);

        label = new JLabel();
        add(label);

        bg = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            radioButtons[i] = new JRadioButton();
            add(radioButtons[i]);
            bg.add(radioButtons[i]);
        }

        btnNext = new JButton("Next");
        btnResult = new JButton("Result");

        btnNext.addActionListener(this);
        btnResult.addActionListener(this);

        add(btnNext);
        add(btnResult);

        btnResult.setVisible(false);

        setData();

        label.setBounds(30, 40, 450, 20);

        for (int i = 0; i < 4; i++) {
            radioButtons[i].setBounds(50, 80 + i * 30, 200, 20);
        }

        btnNext.setBounds(100, 240, 100, 30);
        btnResult.setBounds(270, 240, 100, 30);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocation(250, 100);
        setSize(600, 350);
        setVisible(true);
    }

    // Display question
    void setData() {
        label.setText("<html>Q" + (current + 1) + ": " + questions[current] + "</html>");
        // same options
        radioButtons[0].setText("Java");
        radioButtons[1].setText("Kotlin");
        radioButtons[2].setText("C++");
        radioButtons[3].setText("JavaScript");

        bg.clearSelection();
    }

    // Boolean method
    boolean checkAns() {
        int selected = -1;

        for (int i = 0; i < 4; i++) {
            if (radioButtons[i].isSelected()) {
                selected = i;
            }
        }

        return selected == correct[current];
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnNext) {

            // check if option selected
            boolean selected = false;
            for (int i = 0; i < 4; i++) {
                if (radioButtons[i].isSelected()) {
                    selected = true;
                }
            }

            if (!selected) {
                JOptionPane.showMessageDialog(this, "Please select an option!");
                return;
            }

            // check answer
            if (checkAns()) {
                count++;
            }

            current++;

            if (current < questions.length) {
                setData();
            } else {
                btnNext.setVisible(false);
                btnResult.setVisible(true);
            }
        }

        if (e.getSource() == btnResult) {
            JOptionPane.showMessageDialog(this, "Your Score: " + count + "/10");
        }
    }

    public static void main(String[] args) {
        new QuizTest("Quiz App (Clean Version)");
    }
}