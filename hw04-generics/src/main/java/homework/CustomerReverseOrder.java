package homework;

import java.util.Stack;

public class CustomerReverseOrder {

    Stack<Customer> queue = new Stack<>();

    public void add(Customer customer) {
        queue.add(customer);
    }

    public Customer take() {
        return queue.pop();
    }
}
