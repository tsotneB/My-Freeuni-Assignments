package Models;

import static Models.Constants.*;

public class CourierStatus implements Status {

    private String status;

    public CourierStatus() {
        this.status = FREE;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public boolean setStatus(String status) {
        if (status.equals(FREE) || status.equals(OCCUPIED)) {
            this.status = status;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof CourierStatus)) return false;
        else return ((CourierStatus) obj).getStatus().equals(status);
    }
}
