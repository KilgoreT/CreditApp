package k.kilg.creditapp.entities;

/**
 * Created by apomazkin on 13.04.2018.
 * k.kilg.creditapp.entities
 * 13.04.2018
 * 15:18
 */
public class Resume {
    private String mAllPaymentAmount;
    private String mAllOverpayment;

    public Resume() {
    }

    public String getAllPaymentAmount() {
        return mAllPaymentAmount;
    }

    public void setAllPaymentAmount(String mAllPaymentAmount) {
        this.mAllPaymentAmount = mAllPaymentAmount;
    }

    public String getAllOverpayment() {
        return mAllOverpayment;
    }

    public void setAllOverpayment(String mAllOverpayment) {
        this.mAllOverpayment = mAllOverpayment;
    }
}
