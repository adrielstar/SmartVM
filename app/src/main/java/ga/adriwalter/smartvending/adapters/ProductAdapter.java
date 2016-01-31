package ga.adriwalter.smartvending.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import ga.adriwalter.smartvending.R;
import ga.adriwalter.smartvending.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context mContext;
    private HashMap<String, Product> mProducts;

    public ProductAdapter(Context context, HashMap<String, Product> products) {
        mContext = context;
        mProducts = products;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_poster, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String productPoster = mProducts.get(String.valueOf(position)).getImage();
        String productTitle = mProducts.get(String.valueOf(position)).getTitle();

        Uri imagePath = Uri.parse(
                "android.resource://ga.adriwalter.smartvending/drawable/"
                        + productPoster);

        Picasso.with(mContext)
                .load(imagePath)
                .resize(300, 300)
                .into(viewHolder.mProductPoster);

//        viewHolder.mProductPoster.setImageURI(imagePath);
        viewHolder.mProductPosterLabel.setText(productTitle);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.product_poster) ImageView mProductPoster;
        @Bind(R.id.product_poster_label) TextView mProductPosterLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}