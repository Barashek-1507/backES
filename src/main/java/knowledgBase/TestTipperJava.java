package knowledgBase;

import java.util.HashMap;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfGravity;
import net.sourceforge.jFuzzyLogic.membership.*;
import net.sourceforge.jFuzzyLogic.plot.JDialogFis;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Rule;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.RuleExpression;
import net.sourceforge.jFuzzyLogic.rule.RuleTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import net.sourceforge.jFuzzyLogic.ruleAccumulationMethod.RuleAccumulationMethodMax;
import net.sourceforge.jFuzzyLogic.ruleActivationMethod.RuleActivationMethodMin;
import net.sourceforge.jFuzzyLogic.ruleConnectionMethod.RuleConnectionMethodAndMin;
import net.sourceforge.jFuzzyLogic.ruleConnectionMethod.RuleConnectionMethodOrMax;

public class TestTipperJava {

    static void animateFis(FIS fis) throws Exception {
        if (JFuzzyChart.UseMockClass) {
            Gpr.debug("Using mock class");
            return; // Nothing done
        }

        // Create a plot
        JDialogFis jdf = new JDialogFis(fis, 800, 600);

        // Set different values for 'upperArterialPressure' and 'service'. Evaluate the system and show variables
        //		for( double service = 0.0, upperArterialPressure = 1; service <= 10; service += 0.1 ) {
        for (double service = 0.0, upperArterialPressure = 1; service <= 10; service += 0.1) {
            upperArterialPressure = service;
            // Evaluate system using these parameters
            fis.getVariable("").setValue(service);
            fis.getVariable("upperArterialPressure").setValue(upperArterialPressure);
            fis.evaluate();

            // Print result & update plot
            System.out.println(String.format("Service: %2.2f\tupperArterialPressure:%2.2f\t=> tip: %2.2f %%", service, upperArterialPressure, fis.getVariable("tip").getValue()));
            jdf.repaint();

            // Small delay
            Thread.sleep(100);
        }

    }

    /**
     * Main
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Begin ");

        FIS fis = new FIS();

        // FUNCTION_BLOCK tipper
        FunctionBlock functionBlock = new FunctionBlock(fis);
        fis.addFunctionBlock("pneumonia", functionBlock);

        //		VAR_INPUT
        //		   service : REAL;
        //		   upperArterialPressure : REAL
        //		END_VAR

        Variable respiratoryRate = new Variable("respiratoryRate");
        Variable upperArterialPressure = new Variable("upperArterialPressure");
        Variable lowerArterialPressure = new Variable("lowerArterialPressure");
        Variable age = new Variable("age");
        functionBlock.setVariable(respiratoryRate.getName(), respiratoryRate);
        functionBlock.setVariable(upperArterialPressure.getName(), upperArterialPressure);
        functionBlock.setVariable(lowerArterialPressure.getName(), lowerArterialPressure);
        functionBlock.setVariable(age.getName(), age);

        //		VAR_OUTPUT
        //		   tip : REAL;
        //		END_VAR

        Variable pneumonia = new Variable("pneumonia");
        functionBlock.setVariable(pneumonia.getName(), pneumonia);


        Value lowRateX[] = { new Value(0), new Value(15) };
        Value lowRateY[] = { new Value(1), new Value(0) };
        MembershipFunction lowRate = new MembershipFunctionPieceWiseLinear(lowRateX, lowRateY);

        MembershipFunction normalRate = new MembershipFunctionTrapetzoidal(new Value(14), new Value(16), new Value(20), new Value(26));

        Value highRateX[] = { new Value(22), new Value(40), new Value(50) };
        Value highRateY[] = { new Value(0), new Value(1), new Value(1) };
        MembershipFunction highRate = new MembershipFunctionPieceWiseLinear(highRateX, highRateY);

        LinguisticTerm ltLowRate = new LinguisticTerm("lowRate", lowRate);
        LinguisticTerm ltNormalRate = new LinguisticTerm("middleRate", normalRate);
        LinguisticTerm ltHighRate = new LinguisticTerm("highRate", highRate);

        respiratoryRate.add(ltLowRate);
        respiratoryRate.add(ltNormalRate);
        respiratoryRate.add(ltHighRate);

        //		FUZZIFY upperArterialPressure
        //		   TERM rancid := (0, 1) (1, 1) (3,0) ;
        //		   TERM delicious := (7,0) (9,1) (10,1);
        //		END_FUZZIFY

        Value lowPressureLX[] = { new Value(0), new Value(60) };
        Value lowPressureLY[] = { new Value(1), new Value(0) };
        MembershipFunction lowPressureL = new MembershipFunctionPieceWiseLinear(lowPressureLX, lowPressureLY);

        MembershipFunction middlePressureL = new MembershipFunctionTriangular(new Value(55), new Value(80), new Value(105));

        MembershipFunction highPressureL = new MembershipFunctionTrapetzoidal(new Value(100), new Value(110), new Value(150), new Value(200));

        LinguisticTerm ltLowPressureL = new LinguisticTerm("lowPressureL", lowPressureL);
        LinguisticTerm ltMiddlePressureL = new LinguisticTerm("middlePressureL", middlePressureL);
        LinguisticTerm ltHighPressureL = new LinguisticTerm("highPressureL", highPressureL);

        lowerArterialPressure.add(ltLowPressureL);
        lowerArterialPressure.add(ltMiddlePressureL);
        lowerArterialPressure.add(ltHighPressureL);


        Value lowPressureUX[] = { new Value(0), new Value(95) };
        Value lowPressureUY[] = { new Value(1), new Value(0) };
        MembershipFunction lowPressureU = new MembershipFunctionPieceWiseLinear(lowPressureUX, lowPressureUY);

        MembershipFunction middlePressureU = new MembershipFunctionTriangular(new Value(85), new Value(120), new Value(175));

        MembershipFunction highPressureU = new MembershipFunctionTrapetzoidal(new Value(170), new Value(183), new Value(195), new Value(200));

        LinguisticTerm ltLowPressureU = new LinguisticTerm("lowPressureU", lowPressureU);
        LinguisticTerm ltMiddlePressureU = new LinguisticTerm("middlePressureU", middlePressureU);
        LinguisticTerm ltHighPressureU = new LinguisticTerm("highPressureU", highPressureU);

        upperArterialPressure.add(ltLowPressureU);
        upperArterialPressure.add(ltMiddlePressureU);
        upperArterialPressure.add(ltHighPressureU);


        Value juniorX[] = { new Value(0), new Value(40) };
        Value juniorY[] = { new Value(1), new Value(0) };
        MembershipFunction junior = new MembershipFunctionPieceWiseLinear(juniorX, juniorY);

        MembershipFunction middle = new MembershipFunctionTrapetzoidal(new Value(30), new Value(45), new Value(55), new Value(70));

        Value almostDeadX[] = { new Value(60), new Value(124) };
        Value almostDeadY[] = { new Value(0), new Value(1) };
        MembershipFunction almostDead = new MembershipFunctionPieceWiseLinear(almostDeadX, almostDeadY);

        LinguisticTerm ltJunior = new LinguisticTerm("junior", junior);
        LinguisticTerm ltMiddle = new LinguisticTerm("middle", middle);
        LinguisticTerm ltAlmostDead = new LinguisticTerm("almostDead", almostDead);

        age.add(ltJunior);
        age.add(ltMiddle);
        age.add(ltAlmostDead);
        
        MembershipFunction first = new MembershipFunctionTriangular(new Value(0), new Value(5), new Value(10));
        MembershipFunction second = new MembershipFunctionTriangular(new Value(10), new Value(15), new Value(20));
        MembershipFunction third = new MembershipFunctionTriangular(new Value(20), new Value(25), new Value(30));

        LinguisticTerm ltFirst = new LinguisticTerm("first", first);
        LinguisticTerm ltSecond = new LinguisticTerm("second", second);
        LinguisticTerm ltThird = new LinguisticTerm("third", third);

        pneumonia.add(ltFirst);
        pneumonia.add(ltSecond);
        pneumonia.add(ltThird);

        pneumonia.setDefuzzifier(new DefuzzifierCenterOfGravity(pneumonia));

        //		RULEBLOCK No1
        //		   ACCU : MAX;
        //		   AND : MIN;
        //		   ACT : MIN;
        RuleBlock ruleBlock = new RuleBlock(functionBlock);
        ruleBlock.setName("No1");
        ruleBlock.setRuleAccumulationMethod(new RuleAccumulationMethodMax());
        ruleBlock.setRuleActivationMethod(new RuleActivationMethodMin());

        //		   RULE 1 : IF service IS poor OR upperArterialPressure is rancid THEN tip IS cheap;
        Rule rule1 = new Rule("Rule1", ruleBlock);
        RuleTerm term1 = new RuleTerm(respiratoryRate, "lowRate", false);
        RuleTerm term2 = new RuleTerm(upperArterialPressure, "middlePressureU", false);
        RuleTerm term3 = new RuleTerm(lowerArterialPressure, "middlePressureL", false);
        RuleTerm term4 = new RuleTerm(age, "junior", false);

        RuleExpression firstRule = new RuleExpression(term1, term2, RuleConnectionMethodAndMin.get());
        RuleExpression secondRule = new RuleExpression(firstRule, term3, RuleConnectionMethodAndMin.get());
        RuleExpression tretiyaRule = new RuleExpression(secondRule, term4, RuleConnectionMethodAndMin.get());
        rule1.setAntecedents(tretiyaRule);

        rule1.addConsequent(pneumonia, "first", false);
        ruleBlock.add(rule1);

        Rule rule2 = new Rule("Rule2", ruleBlock);
        RuleTerm term11 = new RuleTerm(respiratoryRate, "lowRate", false);
        RuleTerm term21 = new RuleTerm(upperArterialPressure, "lowPressureU", false);
        RuleTerm term31 = new RuleTerm(lowerArterialPressure, "lowPressureL", false);
        RuleTerm term41 = new RuleTerm(age, "almostDead", false);

        RuleExpression firstRule1 = new RuleExpression(term11, term21, RuleConnectionMethodAndMin.get());
        RuleExpression secondRule1 = new RuleExpression(firstRule1, term31, RuleConnectionMethodAndMin.get());
        RuleExpression tretiyaRule1 = new RuleExpression(secondRule1, term41, RuleConnectionMethodAndMin.get());
        rule2.setAntecedents(tretiyaRule1);

        rule2.addConsequent(pneumonia, "second", false);
        ruleBlock.add(rule2);

        Rule rule3 = new Rule("Rule3", ruleBlock);
        RuleTerm term12 = new RuleTerm(respiratoryRate, "highRate", false);
        RuleTerm term22 = new RuleTerm(upperArterialPressure, "lowPressureU", false);
        RuleTerm term32 = new RuleTerm(lowerArterialPressure, "lowPressureL", false);
        RuleTerm term42 = new RuleTerm(age, "almostDead", false);

        RuleExpression firstRule2 = new RuleExpression(term12, term22, RuleConnectionMethodAndMin.get());
        RuleExpression secondRule2 = new RuleExpression(firstRule2, term32, RuleConnectionMethodAndMin.get());
        RuleExpression tretiyaRule2 = new RuleExpression(secondRule2, term42, RuleConnectionMethodAndMin.get());
        rule3.setAntecedents(tretiyaRule2);

        rule3.addConsequent(pneumonia, "third", false);
        ruleBlock.add(rule3);


        HashMap<String, RuleBlock> ruleBlocksMap = new HashMap<String, RuleBlock>();
        ruleBlocksMap.put(ruleBlock.getName(), ruleBlock);
        functionBlock.setRuleBlocks(ruleBlocksMap);

        System.out.println(fis);
        animateFis(fis);

        System.out.println("End TestTipperJava");
    }

    public TestTipperJava() {
    }
}
