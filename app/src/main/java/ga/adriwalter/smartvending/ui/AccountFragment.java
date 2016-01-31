package ga.adriwalter.smartvending.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ga.adriwalter.smartvending.R;
import ga.adriwalter.smartvending.model.Account;

public class AccountFragment extends Fragment {

    @Bind(R.id.amountValue) EditText mAmountValue;
    @Bind(R.id.currentAmount) TextView mCurrentBalance;
    @Bind(R.id.btnAddMoney) Button mAddMoneyButton;
    private Account mAccount;

    public AccountFragment() {
        // Required constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, rootView);

        // Specify which class to query
        ParseQuery<Account> query = ParseQuery.getQuery("BankAccount");

        query.whereEqualTo("id", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<Account>() {
            @Override
            public void done(List<Account> accountList, ParseException e) {
                if (e == null) {
                    if (accountList.size() == 0) {
                        // Do nothing!
                    } else {
                        double amount = accountList.get(0).getBalance();
                        mCurrentBalance.setText(String.valueOf(amount));
                    }
                }
            }
        });

        mAddMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoneyToAccount();
            }
        });

        return rootView;
    }

    private void addMoneyToAccount() {
        final double amount = Double.parseDouble(mAmountValue.getText().toString());

        ParseQuery<Account> query = ParseQuery.getQuery("BankAccount");

        query.whereEqualTo("id", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<Account>() {
            @Override
            public void done(List<Account> accountList, ParseException e) {
                if (e == null) {
                    if (accountList.size() == 0) {
                        mAccount = new Account(ParseUser.getCurrentUser().getObjectId(), amount);
                        mAccount.setOwner(ParseUser.getCurrentUser());
                        mAccount.saveInBackground();
                        mCurrentBalance.setText(String.valueOf(amount));
                        mAmountValue.setText("");
                    } else {
                        mAccount = accountList.get(0);
                        double newBalance = mAccount.getBalance() + amount;
                        mAccount.put("balance", newBalance);
                        mAccount.saveInBackground();
                        mCurrentBalance.setText(String.valueOf(newBalance));
                        mAmountValue.setText("");
                    }
                }
            }
        });
    }
}
