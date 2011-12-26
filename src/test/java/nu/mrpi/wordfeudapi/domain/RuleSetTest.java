package nu.mrpi.wordfeudapi.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Pierre Ingmansson
 */
public class RuleSetTest {
    @Test
    public void fromStringEnglish() throws Exception {
        RuleSet ruleSet = RuleSet.fromString("english");
        assertEquals(RuleSet.English, ruleSet);
    }
}
