import javax.swing.*;
import java.awt.*;


public class Main extends JFrame {

    private JTextField functionInput;
    private JTextArea outputArea;

    public Main() {
        setTitle("Numerical Methods GUI");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Left side
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, 600));

        // function input
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel functionLabel = new JLabel("Enter Function f(x):");
        inputPanel.add(functionLabel, BorderLayout.NORTH);

        functionInput = new JTextField();
        functionInput.setPreferredSize(new Dimension(200, 30)); // Resize input bar
        inputPanel.add(functionInput, BorderLayout.CENTER);

        // Method buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] methods = {"Bisection", "Secant", "Doolittle", "Gauss-Seidel", "Simpson's 1/3"};
        for (String method : methods) {
            JButton button = new JButton(method);
            button.addActionListener(e -> onClicked(method));
            buttonsPanel.add(button);
        }

        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(buttonsPanel, BorderLayout.CENTER);

        // Output
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13)); // slightly larger font
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Final layout
        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
    }



    private void onClicked(String method) {
        String function = functionInput.getText();
        outputArea.setText("");

        if (function.isEmpty()) {
            outputArea.setText("Please enter a function.");
            return;
        }

        try {
            switch (method) {
                case "Bisection":
                    double lower = Double.parseDouble(JOptionPane.showInputDialog("Enter lower bound (x0):"));
                    double upper = Double.parseDouble(JOptionPane.showInputDialog("Enter upper bound (x1):"));
                    double tolB = Double.parseDouble(JOptionPane.showInputDialog("Enter tolerance (e.g. 0.0001):"));
                    int decB = Integer.parseInt(JOptionPane.showInputDialog("Enter decimal precision (e.g. 4):"));
                    var resultB = BisectionMethod.solve(function, lower, upper, tolB, decB);
                    StringBuilder sbB = new StringBuilder();
                    sbB.append("Bisection Method\nFunction: f(x) = ").append(resultB.function)
                            .append("\nInitial Bounds: x0 = ").append(resultB.initialLower)
                            .append(", x1 = ").append(resultB.initialUpper).append("\n\n");
                    sbB.append(String.format("%-10s %-12s %-12s %-12s %-14s %-10s%n",
                            "Iter", "x0", "x1", "x2", "f(x2)", "Ea"));
                    for (var r : resultB.iterations) {
                        sbB.append(String.format("%-10d %-12." + decB + "f %-12." + decB + "f %-12." + decB + "f %-14." + decB + "f %-10." + decB + "f%n",
                                r.iteration, r.x0, r.x1, r.x2, r.fx2, r.ea));
                    }
                    sbB.append("\nFinal Estimated Root: ").append(String.format("%." + decB + "f", resultB.root));
                    outputArea.setText(sbB.toString());
                    break;

                case "Secant":
                    double x0 = Double.parseDouble(JOptionPane.showInputDialog("Enter initial guess x0:"));
                    double x1 = Double.parseDouble(JOptionPane.showInputDialog("Enter initial guess x1:"));
                    double tolS = Double.parseDouble(JOptionPane.showInputDialog("Enter tolerance (e.g. 0.0001):"));
                    int decS = Integer.parseInt(JOptionPane.showInputDialog("Enter decimal precision (e.g. 4):"));
                    var resultS = SecantMethod.solve(function, x0, x1, tolS, decS);
                    StringBuilder sbS = new StringBuilder();
                    sbS.append("Secant Method\nFunction: f(x) = ").append(resultS.function)
                            .append("\nInitial Guesses: x0 = ").append(resultS.initialX0)
                            .append(", x1 = ").append(resultS.initialX1).append("\n\n");
                    sbS.append(String.format("%-10s %-12s %-12s %-12s %-14s %-10s%n",
                            "Iter", "x0", "x1", "x2", "f(x2)", "Ea"));
                    for (var r : resultS.iterations) {
                        sbS.append(String.format("%-10d %-12." + decS + "f %-12." + decS + "f %-12." + decS + "f %-14." + decS + "f %-10." + decS + "f%n",
                                r.iteration, r.x0, r.x1, r.x2, r.fx2, r.ea));
                    }
                    sbS.append("\nFinal Root Estimate: ").append(String.format("%." + decS + "f", resultS.root));
                    outputArea.setText(sbS.toString());
                    break;

                case "Doolittle":
                case "Gauss-Seidel":
                case "Simpson's 1/3":
                    outputArea.setText("(Result Placeholder)");
                    break;
            }
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
