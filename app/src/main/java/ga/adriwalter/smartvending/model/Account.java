package ga.adriwalter.smartvending.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("BankAccount")
public class Account extends ParseObject {

    private String mId;
    private double mBalance;

    public Account() {
        super();
        // Empty constructor.
    }

    public Account(String id, double balance) {
        super();
        mId = id;
        mBalance = balance;

        setId(id);
        setBalance(balance);
    }

    public String getId() {
        return getString("id");
    }

    public void setId(String id) {
        put("id", id);
    }

    public double getBalance() {
        return getDouble("balance");
    }

    public void setBalance(double balance) {
        put("balance", balance);
    }

    public ParseUser getUser()  {
        return getParseUser("owner");
    }

    public void setOwner(ParseUser user) {
        put("owner", user);
    }
}
