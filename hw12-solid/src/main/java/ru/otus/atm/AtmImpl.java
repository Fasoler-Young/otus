package ru.otus.atm;

import java.util.*;

public class AtmImpl implements Atm {

    Map<Denomination, Integer> cells = new EnumMap<>(Denomination.class);

    public AtmImpl(Set<Denomination> denominations) {
        for (Denomination denomination : denominations) {
            cells.put(denomination, 0);
        }
    }

    @Override
    public List<Denomination> putMoney(List<Denomination> denominations) {
        List<Denomination> unsupportedDenominations = new ArrayList<>();
        for (Denomination denomination : denominations) {
            Integer count = cells.get(denomination);
            if (count == null) {
                unsupportedDenominations.add(denomination);
            } else {
                cells.put(denomination, count + 1);
            }
        }
        unsupportedDenominations.sort(Comparator.comparing(Denomination::value).reversed());
        return unsupportedDenominations;
    }

    @Override
    public List<Denomination> getMoney(Integer sum) {
        List<Denomination> denominations = new ArrayList<>();
        int curSum = 0;
        for (Map.Entry<Denomination, Integer> entry : cells.entrySet()) {
            Denomination denomination = entry.getKey();
            Integer count = entry.getValue();
            for (int i = 0; i < count; i++) {
                if (curSum + denomination.value() <= sum) {
                    curSum += denomination.value();
                    denominations.add(denomination);
                }
            }
        }
        if (curSum == sum) {
            for (Denomination denomination : denominations) {
                cells.put(denomination, cells.get(denomination) - 1);
            }
            return denominations;
        } else {
            throw new IllegalStateException("Данная сумма не может быть выдана");
        }
    }

    @Override
    public Integer getBalance() {
        return cells.entrySet().stream()
                .map(map -> map.getKey().value() * map.getValue())
                .reduce(0, Integer::sum);
    }
}
