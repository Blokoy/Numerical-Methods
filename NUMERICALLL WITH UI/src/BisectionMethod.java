import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

import java.util.ArrayList;
import java.util.List;

public class BisectionMethod {

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

    public static class BisectionRslt {
        public List<IteRslt> iterations;
        public double root;
        public double initialLower;
        public double initialUpper;
        public String function;

        public BisectionRslt(List<IteRslt> iterations, double root, double initialLower, double initialUpper, String function) {
            this.iterations = iterations;
            this.root = root;
            this.initialLower = initialLower;
            this.initialUpper = initialUpper;
            this.function = function;
        }
    }

    public static BisectionRslt solve(String functionStr, double lower, double upper, double tolerance, int decimals) {
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

        double x0 = lower;
        double x1 = upper;
        double x2 = 0, x2Prev;
        double ea ;

        for (int iteration = 1; ; iteration++) {
            x2Prev = x2;
            x2 = round((x0 + x1) / 2, decimals);
            double fx2 = eval(expr, x2);

            if (iteration == 1) {
                ea = Double.MAX_VALUE;
            } else {
                ea = Math.abs((x2 - x2Prev) / x2);
                ea = round(ea, decimals);
            }

            results.add(new IteRslt(iteration, x0, x1, x2, fx2, iteration == 1 ? 0 : ea));

            double f_x0 = eval(expr, x0);
            if (f_x0 * fx2 < 0) {
                x1 = x2;
            } else {
                x0 = x2;
            }

            if (ea <= tolerance) {
                break;
            }
        }

        return new BisectionRslt(results, x2, lower, upper, functionStr);
    }

    private static double eval(Expression expr, double x) {
        return expr.setVariable("x", x).evaluate();
    }

    private static double round(double value, int decimals) {
        double factor = Math.pow(10, decimals);
        return Math.round(value * factor) / factor;
    }
}
