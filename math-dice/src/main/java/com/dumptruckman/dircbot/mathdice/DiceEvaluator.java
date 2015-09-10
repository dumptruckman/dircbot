package com.dumptruckman.dircbot.mathdice;

import com.dumptruckman.dircbot.DircBot;
import com.fathzer.soft.javaluator.BracketPair;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Function;
import com.fathzer.soft.javaluator.Operator;
import com.fathzer.soft.javaluator.Operator.Associativity;
import com.fathzer.soft.javaluator.Parameters;

import java.util.Arrays;
import java.util.Iterator;

public class DiceEvaluator extends DoubleEvaluator {

    private static final Operator LESS_THAN = new Operator("<", 2, Associativity.LEFT, 0);
    private static final Operator LESS_THAN_EQUAL = new Operator("<=", 2, Associativity.LEFT, 0);
    private static final Operator GREATER_THAN = new Operator(">", 2, Associativity.LEFT, 0);
    private static final Operator GREATER_THAN_EQUAL = new Operator(">=", 2, Associativity.LEFT, 0);
    private static final Operator EQUAL = new Operator("=", 2, Associativity.LEFT, 0);
    private static final Operator NOT_EQUAL = new Operator("!=", 2, Associativity.LEFT, 0);
    private static final Operator DICE = new Operator("d", 2, Associativity.LEFT, 6);
    private static final Operator DICE_SINGLE = new Operator("d", 1, Associativity.LEFT, 5);

    private static final Operator[] OPERATORS = new Operator[]{DICE, DICE_SINGLE, LESS_THAN, LESS_THAN_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL, EQUAL, NOT_EQUAL, NEGATE, MINUS, PLUS, MULTIPLY, DIVIDE, EXPONENT, MODULO};
    private static final Function[] FUNCTIONS = new Function[]{MIN, MAX, SUM, AVERAGE, ROUND, CEIL, FLOOR, ABS, RANDOM};

    private static Parameters getParameters() {
        Parameters parameters = new Parameters();
        parameters.addOperators(Arrays.asList(OPERATORS));
        parameters.addFunctions(Arrays.asList(FUNCTIONS));
        parameters.addFunctionBracket(BracketPair.PARENTHESES);
        parameters.addExpressionBracket(BracketPair.PARENTHESES);
        return parameters;
    }

    private final MathDicePlugin plugin;

    DiceEvaluator(MathDicePlugin plugin) {
        super(getParameters());
        this.plugin = plugin;
    }

    @Override
    protected Double evaluate(Operator operator, Iterator<Double> operands, Object evaluationContext) {
        if (operator == DICE || operator == DICE_SINGLE) {
            if (evaluationContext == null || !(evaluationContext instanceof DiceRolls)) {
                System.out.println("Invalid Evaluation Context = " + evaluationContext);
                evaluationContext = new DiceRolls();
            }
            DiceRolls rolls = (DiceRolls) evaluationContext;

            int number = 1;
            if (operator == DICE) {
                number = operands.next().intValue();
            }
            int sides = operands.next().intValue();

            rolls.addDice(number);
            if (rolls.getTotalNumber() > 100) {
                throw new IllegalArgumentException("You may not roll more than 100 dice!");
            }

            StringBuilder rollString = new StringBuilder("").append(number).append("d").append(sides).append("=");
            double total = 0;
            for (int i = 0; i < number; i++) {
                int roll = plugin.getDiceCache().getRoll(sides);
                total += roll;
                if (i != 0) {
                    rollString.append(",");
                }
                rollString.append(roll);
            }
            rolls.addRoll(rollString.toString());
            return total;
        } else if (operator == GREATER_THAN) {
            return operands.next() > operands.next() ? 1D : 0D;
        } else if (operator == GREATER_THAN_EQUAL) {
            return operands.next() >= operands.next() ? 1D : 0D;
        } else if (operator == LESS_THAN) {
            return operands.next() < operands.next() ? 1D : 0D;
        } else if (operator == LESS_THAN_EQUAL) {
            return operands.next() <= operands.next() ? 1D : 0D;
        } else if (operator == EQUAL) {
            return operands.next().doubleValue() == operands.next().doubleValue() ? 1D : 0D;
        } else if (operator == NOT_EQUAL) {
            return operands.next().doubleValue() != operands.next().doubleValue() ? 1D : 0D;
        } else {
            return super.evaluate(operator, operands, evaluationContext);
        }
    }
}
