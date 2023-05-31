package tui.parser;

import java.util.Objects;
import java.util.Optional;

public class Value extends NodeOrLeaf  {
    float value;
    Optional<Unit> unit;
    int exponent;

    public Value(float value, Optional<Unit> unit, int exponent) {
        this.value = value;
        this.unit = unit;
        this.exponent = exponent;
    }

    public Value(float value) {
        this(value, Optional.empty(), 0);
    }

    public Value(float value, String unit) {
        this(value, Optional.of(new Unit(unit)), 1);
    }

    public Value add(Value rhs) {
        int left_factor = Unit.factor(unit, rhs.unit);
        float new_value;
        if (left_factor == 0) {
            new_value = value + rhs.value;
        } else if (left_factor == 1) {
            int right_factor = Unit.factor(rhs.unit, unit);
            new_value = value + rhs.value * right_factor;
        } else {
            new_value = value * left_factor + rhs.value;
        }

        return new Value(new_value, Unit.calc_unit(unit, rhs.unit), exponent);
    }

    public Value sub(Value rhs) {
        int left_factor = Unit.factor(unit, rhs.unit);
        float new_value;
        if (left_factor == 0) {
            new_value = value - rhs.value;
        } else if (left_factor == 1) {
            int right_factor = Unit.factor(rhs.unit, unit);
            new_value = value - rhs.value * right_factor;
        } else {
            new_value = value * left_factor - rhs.value;
        }

        return new Value(new_value, Unit.calc_unit(unit, rhs.unit), exponent);
    }

    public Value mul(Value rhs) {
        int left_factor = Unit.factor(unit, rhs.unit);
        float new_value;
        if (left_factor == 0) {
            new_value = value * rhs.value;
        } else if (left_factor == 1) {
            int right_factor = Unit.factor(rhs.unit, unit);
            new_value = value * rhs.value * right_factor;
        } else {
            new_value = value * left_factor * rhs.value;
        }

        return new Value(new_value, Unit.calc_unit(unit, rhs.unit), exponent + rhs.exponent);
    }

    public Value div(Value rhs) throws ArithmeticException {
        if(rhs.value == 0) {
            throw new ArithmeticException("Cannot divide by 0!");
        }
        int left_factor = Unit.factor(unit, rhs.unit);
        float new_value;
        if (left_factor == 0) {
            new_value = value / rhs.value;
        } else if (left_factor == 1) {
            int right_factor = Unit.factor(rhs.unit, unit);
            new_value = value / rhs.value * right_factor;
        } else {
            new_value = value * left_factor / rhs.value;
        }
        int new_exponent = exponent - rhs.exponent;
        Optional<Unit> new_unit;
        if (new_exponent == 0) {
            new_unit = Optional.empty();
        } else {
            new_unit = Unit.calc_unit(unit, rhs.unit);
        }
        return new Value(new_value, new_unit, new_exponent);
    }

    public Value pow(Value rhs) {
        int left_factor = Unit.factor(unit, rhs.unit);
        float new_left = value;
        float new_right = rhs.value;
        if (left_factor == 1) {
            int right_factor = Unit.factor(rhs.unit, unit);
            new_right = rhs.value * right_factor;
        } else if (left_factor > 1) {
            new_left = value * left_factor;
        }
        return new Value((float) Math.pow(new_left, new_right), Unit.calc_unit(unit, rhs.unit), exponent);
    }

    @Override
    public String toString() {
        String unit_str = unit.map(s -> " " + s).orElse("");
        String pow_str = unit.isPresent() && exponent > 1 ? "^" + exponent : "";
        return value + unit_str + pow_str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value value1 = (Value) o;
        boolean v =value == value1.value;
        boolean e = exponent == value1.exponent;
        boolean u = unit.equals(value1.unit);
        return v && e && u;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit, exponent);
    }
}
