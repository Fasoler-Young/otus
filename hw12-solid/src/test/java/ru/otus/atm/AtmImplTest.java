package ru.otus.atm;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AtmImplTest {

    private Atm atm;

    @BeforeEach
    void setUp() {
        atm = new AtmImpl(Set.of(Denomination._1000, Denomination._100, Denomination._10));
    }

    @Test
    void putMoney() {
        List<Denomination> denominations = getTestDenominations();
        List<Denomination> returns = atm.putMoney(denominations);
        assertArrayEquals(
                new Denomination[] {Denomination._5000, Denomination._500},
                returns.toArray(),
                "Возвращенные купюры не соответствуют");
    }

    private static List<Denomination> getTestDenominations() {
        return List.of(
                Denomination._100,
                Denomination._1000,
                Denomination._100,
                Denomination._100,
                Denomination._100,
                Denomination._100,
                Denomination._100,
                Denomination._100,
                Denomination._100,
                Denomination._100,
                Denomination._100,
                Denomination._10,
                Denomination._500,
                Denomination._5000);
    }

    @Test
    void getMoney_1000() {
        List<Denomination> denominations = getTestDenominations();
        atm.putMoney(denominations);
        List<Denomination> result = atm.getMoney(1000);
        assertArrayEquals(
                new Denomination[] {Denomination._1000}, result.toArray(), "Возвращенные купюры не соответствуют 1000");
    }

    @Test
    void getMoney_300() {
        List<Denomination> denominations = getTestDenominations();
        atm.putMoney(denominations);
        List<Denomination> result = atm.getMoney(300);
        assertArrayEquals(
                new Denomination[] {Denomination._100, Denomination._100, Denomination._100},
                result.toArray(),
                "Возвращенные купюры не соответствуют 300");
    }

    @Test
    void getMoney_1110() {
        List<Denomination> denominations = getTestDenominations();
        atm.putMoney(denominations);
        List<Denomination> result = atm.getMoney(1110);
        assertArrayEquals(
                new Denomination[] {Denomination._1000, Denomination._100, Denomination._10},
                result.toArray(),
                "Возвращенные купюры не соответствуют 1110");
    }

    @Test
    void getBalance() {
        List<Denomination> denominations = getTestDenominations();
        atm.putMoney(denominations);
        Integer result = atm.getBalance();
        assertEquals(2010, result, "Баланс не соответствует внесенным купюрам");
    }
}
