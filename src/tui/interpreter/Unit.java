package tui.interpreter;

import java.util.Objects;
import java.util.Optional;

public class Unit {
    UnitValue unit;

    public Unit(String unit) {
        switch (unit) {
            case "s":
                this.unit = UnitValue.SECOND;
                break;
            case "min":
                this.unit = UnitValue.MINUTE;
                break;
            case "h":
                this.unit = UnitValue.HOUR;
        }
    }

    public static Unit smallest(Unit u1, Unit u2) {
        if (u1.unit.compareTo(u2.unit) <= 0) {
            return u1;
        } else {
            return u2;
        }
    }

    public static Optional<Unit> calc_unit(Optional<Unit> u1, Optional<Unit> u2) {
        if (u1.isEmpty() && u2.isEmpty()) {
            return Optional.empty();
        } else if (u1.isPresent() && u2.isEmpty()) {
            return u1;
        } else return u1.map(s -> smallest(s, u2.get())).or(() -> u2);
    }

    public static int factor(Optional<Unit> u1, Optional<Unit> u2) {
        if (u1.isEmpty() && u2.isEmpty() || u1.isPresent() && u2.isEmpty() || u1.isEmpty() && u2.isPresent()) {
            return 0;
        } else {
            Unit unit1 = u1.get();
            Unit unit2 = u2.get();
            int xd = Math.max(0, unit1.unit.ordinal() - unit2.unit.ordinal());
            return (int) Math.pow(60, xd);
        }
    }

    @Override
    public String toString() {
        return switch (unit) {
            case SECOND -> "s";
            case MINUTE -> "min";
            case HOUR -> "h";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit1 = (Unit) o;
        return unit == unit1.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit);
    }
}
