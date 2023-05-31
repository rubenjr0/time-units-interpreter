package tui.tests;

import org.junit.jupiter.api.Test;
import tui.parser.Unit;
import tui.parser.Value;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValueTest {

    @Test
    void add_without_units() {
        Value v1 = new Value(1);
        Value v2 = new Value(1);
        Value result = v1.add(v2);
        Value expected = new Value(2);
        assertEquals(expected, result);
    }

    @Test
    void add_with_same_units() {
        Value v1 = new Value(1, "h");
        Value v2 = new Value(1, "h");
        Value result = v1.add(v2);
        Value expected = new Value(2, "h");
        assertEquals(expected, result);
    }

    @Test
    void add_with_different_units() {
        Value v1 = new Value(1, "h");
        Value v2 = new Value(1, "s");
        Value result = v1.add(v2);
        Value expected = new Value(3601, "s");
        assertEquals(expected, result);
    }


    @Test
    void sub_without_units() {
        Value v1 = new Value(1);
        Value v2 = new Value(1);
        Value result = v1.sub(v2);
        Value expected = new Value(0);
        assertEquals(expected, result);
    }

    @Test
    void sub_with_same_units() {
        Value v1 = new Value(2, "h");
        Value v2 = new Value(1, "h");
        Value result = v1.sub(v2);
        Value expected = new Value(1, "h");
        assertEquals(expected, result);
    }

    @Test
    void sub_with_different_units() {
        Value v1 = new Value(1, "h");
        Value v2 = new Value(1, "s");
        Value result = v1.sub(v2);
        Value expected = new Value(3599, "s");
        assertEquals(expected, result);
    }

    @Test
    void mul_without_units() {
        Value v1 = new Value(2);
        Value v2 = new Value(2);
        Value result = v1.mul(v2);
        Value expected = new Value(4);
        assertEquals(expected, result);
    }

    @Test
    void mul_with_one_unit() {
        Value v1 = new Value(1, "h");
        Value v2 = new Value(2);
        Value result = v1.mul(v2);
        Value expected = new Value(2, "h");
        assertEquals(expected, result);
    }

    @Test
    void mul_with_same_units() {
        Value v1 = new Value(2, "h");
        Value v2 = new Value(2, "h");
        Value result = v1.mul(v2);
        Value expected = new Value(4, Optional.of(new Unit("h")), 2);
        assertEquals(expected, result);
    }

    @Test
    void mul_with_different_units() {
        Value v1 = new Value(1, "min");
        Value v2 = new Value(2, "s");
        Value result = v1.mul(v2);
        Value expected = new Value(120, Optional.of(new Unit("s")), 2);
        assertEquals(expected, result);
    }

    @Test
    void div_by_zero() {
        Value v1 = new Value(1);
        Value v2 = new Value(0);
        assertThrows(ArithmeticException.class, () -> v1.div(v2));
    }

    @Test
    void div_without_units() {
        Value v1 = new Value(4);
        Value v2 = new Value(2);
        Value result = v1.div(v2);
        Value expected = new Value(2);
        assertEquals(expected, result);
    }

    @Test
    void div_without_exponent_same_units() {
        Value v1 = new Value(2, "s");
        Value v2 = new Value(2, "s");
        Value result = v1.div(v2);
        Value expected = new Value(1);
        assertEquals(expected, result);
    }

    @Test
    void div_with_exponent_same_units() {
        Value v1 = new Value(2, Optional.of(new Unit("s")), 2);
        Value v2 = new Value(2, "s");
        Value result = v1.div(v2);
        Value expected = new Value(1, Optional.of(new Unit("s")), 1);
        assertEquals(expected, result);
    }

    @Test
    void div_without_exponent_different_units() {
        Value v1 = new Value(1, "min");
        Value v2 = new Value(2, "s");
        Value result = v1.div(v2);
        Value expected = new Value(30);
        assertEquals(expected, result);
    }

    @Test
    void div_with_exponent_different_units() {
        Value v1 = new Value(1,  Optional.of(new Unit("min")), 2);
        Value v2 = new Value(2, "s");
        Value result = v1.div(v2);
        Value expected = new Value(30, "s");
        assertEquals(expected, result);
    }



    @Test
    void pow_without_units() {
        Value v1 = new Value(2);
        Value v2 = new Value(3);
        Value result = v1.pow(v2);
        Value expected = new Value(8);
        assertEquals(expected, result);
    }

    @Test
    void pow_with_same_units() {
        Value v1 = new Value(2, "h");
        Value v2 = new Value(3, "h");
        Value result = v1.pow(v2);
        Value expected = new Value(8, "h");
        assertEquals(expected, result);
    }

    @Test
    void pow_with_different_units() {
        Value v1 = new Value(1, "min");
        Value v2 = new Value(2, "s");
        Value result = v1.pow(v2);
        Value expected = new Value(3600, "s");
        assertEquals(expected, result);
    }
}