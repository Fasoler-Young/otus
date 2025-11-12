package ru.otus.atm;

import java.util.List;

public interface Atm {

    List<Denomination> putMoney(List<Denomination> denomination);

    List<Denomination> getMoney(Integer sum);

    Integer getBalance();
}
