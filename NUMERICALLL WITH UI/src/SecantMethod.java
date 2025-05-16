import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

import java.util.ArrayList;
import java.util.List;

public class SecantMethod {

    public static class IteRslt {
        public int iteration;
        public double x0, x1, x2, fx2, ea;

        public IteRslt(int iteration, double x0, double x1, double x2, double fx2, double ea) {
            this.iteration = iteration;
            this.x0 = x0;
            this.x1 = x1;
            this.x2 = x2;
            this.fx2 = fx2;
            this.ea = ea;
        }
    }

    public static class SecantRslt {
        public List<IteRslt> iterations;
        public double root;
        public double initialX0;
        public double initialX1;
        public String function;

        public SecantRslt(List<IteRslt> iterations, double root, double initialX0, double initialX1, String function) {
            this.iterations = iterations;
            this.root = root;
            this.initialX0 = initialX0;
            this.initialX1 = initialX1;
            this.function = function;
        }
    }

    public static SecantRslt solve(String functionStr, double x0, double x1, double tolerance, int decimals) {
        List<IteRslt> results = new ArrayList<>();

        Expression expr = new ExpressionBuilder(functionStr)
                .variables("x")
                .functions(
                        new Function("sin", 1) {
                            @Override
                            public double apply(double... args) {
                                return Math.sin(args[0]);
                            }
                        },
                        new Function("cos", 1) {
                            @Override
                            public double apply(double... args) {
                                return Math.cos(args[0]);
                            }
                        },
                        new Function("tan", 1) {
                            @Override
                            public double apply(double... args) {
                                return Math.tan(args[0]);
                            }
                        }
                )
                .build();

        double x2 = 0, ea = Double.MAX_VALUE;

        for (int iteration = 1; ; iteration++) {
            double fx0 = eval(expr, x0);
            double fx1 = eval(expr, x1);

            if (fx1 - fx0 == 0) {
                throw new ArithmeticException("Division by zero in Secant method. f(x1) - f(x0) = 0");
            }

            x2 = round(x1 - fx1 * (x1 - x0) / (fx1 - fx0), decimals);
            double fx2 = eval(expr, x2);

            if (iteration == 1) {
                ea = Double.MAX_VALUE;
            } else {
                ea = Math.abs((x2 - x1) / x2);
                ea = round(ea, decimals);
            }

            results.add(new IteRslt(iteration, x0, x1, x2, fx2, iteration == 1 ? 0 : ea));

            if (ea <= tolerance) {
                break;
            }

            x0 = x1;
            x1 = x2;
        }

        return new SecantRslt(results, x2, results.get(0).x0, results.get(0).x1, functionStr);
    }

    private static double eval(Expression expr, double x) {
        return expr.setVariable("x", x).evaluate();
    }

    private static double round(double value, int decimals) {
        double factor = Math.pow(10, decimals);
        return Math.round(value * factor) / factor;
    }


}
