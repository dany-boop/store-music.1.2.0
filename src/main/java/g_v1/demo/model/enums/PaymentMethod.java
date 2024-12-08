package g_v1.demo.model.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {

    PAYMENT_ETHERIUM("Etherium"),
    PAYMENT_DEBIT("Debit"),
    PAYMENT_COD("COD");

    private String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public static PaymentMethod fromDescription(String description) {
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.description.equals(description)) {
                return paymentMethod;
            }
        }
        return null;
    }
}
