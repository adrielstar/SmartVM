package ga.adriwalter.smartvending.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import ga.adriwalter.smartvending.model.Product;
import ga.adriwalter.smartvending.utils.BluethoothConnection;

public class DetailActivity extends AppCompatActivity {

    private BluethoothConnection mBluetoothConnection;
    private Account mAccount;
    private Product mProduct;

    @Bind(R.id.detail_product_poster) ImageView mDetailProductPoster;
    @Bind(R.id.product_price_label) TextView mProductPriceLabel;
    @Bind(R.id.product_description) TextView mProductDescriptionLabel;
    @Bind(R.id.product_buy_button) Button mBtnBuyProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        mProduct = Product.productHashMap.get(id);

        String productTitle = mProduct.getTitle();
        String productPoster = mProduct.getImage();
        double productPrice = mProduct.getPrice();
        String productDescription = mProduct.getDescription();

        mBluetoothConnection = new BluethoothConnection(this, id);

        mBtnBuyProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                removeMoneyFromAccount(view);
            }
        });

        CollapsingToolbarLayout appBarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(productTitle);
        }

        Uri imagePath = Uri.parse(
                "android.resource://com.freedom_mobile.smartvending/drawable/"
                        + productPoster);

        mDetailProductPoster.setImageURI(imagePath);
        mProductPriceLabel.setText(String.format("Price: %s", String.valueOf(productPrice)));
        mProductDescriptionLabel.setText(productDescription);
    }

    private void removeMoneyFromAccount(final View view) {
        ParseQuery<Account> query = ParseQuery.getQuery("BankAccount");

        query.whereEqualTo("id", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<Account>() {
            @Override
            public void done(List<Account> accountList, ParseException e) {
                if (e == null) {
                    if (accountList.size() == 0) {
                        // Snackbar
                        Snackbar snackbar = Snackbar
                                .make(view, "Your balance is to low", Snackbar.LENGTH_LONG)
                                .setAction("Add Money", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        Intent intent = new Intent(DetailActivity.this,AccountFragment.class);
//                                        startActivity(intent);
                                    }
                                });

                        // Changing message text color
                        snackbar.setActionTextColor(Color.RED);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    } else if (accountList.get(0).getBalance() == 0) {
                        // Snackbar
                        Snackbar snackbar = Snackbar
                                .make(view, "You dont have money on your account", Snackbar.LENGTH_LONG)
                                .setAction("Add Money", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });

                        // Changing message text color
                        snackbar.setActionTextColor(Color.RED);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    } else if (accountList.get(0).getBalance() < mProduct.getPrice()) {
                        // Snackbar
                        Snackbar snackbar = Snackbar
                                .make(view, "You don't have enough money!", Snackbar.LENGTH_LONG)
                                .setAction("Add Money", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });

                        // Changing message text color
                        snackbar.setActionTextColor(Color.RED);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    } else {
                        mAccount = accountList.get(0);
                        double newBalance = mAccount.getBalance() - mProduct.getPrice();
                        mAccount.put("balance", newBalance);
                        mAccount.saveInBackground();
                        mBluetoothConnection.blueTooth();
//                        mCurrentBalance.setText(String.valueOf(newBalance));
                    }
                }
            }
        });
    }
}