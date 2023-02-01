package Models;

import static Models.Constants.*;

public class OrderStatus implements Status {

    private String status;

    public OrderStatus() {
        status = ONWAY;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public boolean setStatus(String status) {
        if (status.equals(DELIVERED) || status.equals(ONWAY) || status.equals(NOTRECEIVE)) {
            this.status = status;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof OrderStatus)) return false;
        return ((OrderStatus) obj).getStatus().equals(status);
    }
}
