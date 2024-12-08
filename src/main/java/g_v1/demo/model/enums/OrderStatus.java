package g_v1.demo.model.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    STATUS_UNPAID("unpaid"),
    STATUS_POCESSING("prepared"),
    STATUS_SHIPPED("shipped"),
    STATUS_IN_TRANSIT("in_transit"),
    STATUS_CANCELLED("canceled"),
    STATUS_DELIVERED("delivered");

    private String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public static OrderStatus fromDescription(String description) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.description.equals(description)) {
                return orderStatus;
            }
        }
        return null;
    }
}
