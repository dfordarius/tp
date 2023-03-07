package seedu.address.model.client;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

class GoalTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Goal(null));
    }

    @Test
    public void constructor_invalidGender_throwsIllegalArgumentException() {
        String invalidGoal = "";
        assertThrows(IllegalArgumentException.class, () -> new Goal(invalidGoal));
    }

    @Test
    public void isValidGoal() {
        // null gender
        assertThrows(NullPointerException.class, () -> Goal.isValidGoal(null));

        // invalid genders
        assertFalse(Goal.isValidGoal("")); // empty string
        assertFalse(Goal.isValidGoal("  ")); // empty whitespace

        // valid genders
        assertTrue(Goal.isValidGoal("gain")); // one word
        assertTrue(Goal.isValidGoal("lose weight")); // two words
        assertTrue(Goal.isValidGoal("i want to be strong like my friend jacob")); // a sentence
    }
    @Test
    public void test_equalsSymmetric() {
        Goal goalA = new Goal("lose weight");
        Goal goalB = new Goal("lose weight");
        assertTrue(goalA.equals(goalB) && goalB.equals(goalA));
        assertTrue(goalA.hashCode() == goalB.hashCode());
    }
}
