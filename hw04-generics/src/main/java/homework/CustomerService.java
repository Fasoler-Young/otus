package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    private final NavigableMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        // Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return cloneEntry(map.firstEntry()); // это "заглушка, чтобы скомилировать"
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return cloneEntry(map.higherEntry(customer));
    }

    private static Map.Entry<Customer, String> cloneEntry(Map.Entry<Customer, String> entry) {
        if (entry == null) {
            return null;
        }

        return new Map.Entry<>() {
            @Override
            public Customer getKey() {
                Customer immutable = entry.getKey();
                return new Customer(immutable.getId(), immutable.getName(), immutable.getScores());
            }

            @Override
            public String getValue() {
                return entry.getValue();
            }

            @Override
            public String setValue(String value) {
                return entry.setValue(value);
            }
        };
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
