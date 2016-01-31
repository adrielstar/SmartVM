package ga.adriwalter.smartvending.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import ga.adriwalter.smartvending.R;
import ga.adriwalter.smartvending.adapters.ProductAdapter;
import ga.adriwalter.smartvending.model.Product;
import ga.adriwalter.smartvending.utils.RecyclerTouchListener;
import ga.adriwalter.smartvending.utils.SpacesItemDecoration;

public class MainFragment extends Fragment {


    public static final int PORTRAIT_MODE = 2;
    public static final int LANDSCAPE_MODE = 3;

    @Bind(R.id.productRecyclerView)
    RecyclerView mRecyclerView;

    public MainFragment() {
        // Required constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        float recyclerViewSpacing = getResources().getDimension(R.dimen.recyclerViewPadding);

        mRecyclerView.addItemDecoration(new SpacesItemDecoration((int) recyclerViewSpacing));
        mRecyclerView.setHasFixedSize(true);
        if (this.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), PORTRAIT_MODE));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), LANDSCAPE_MODE));
        }
        mRecyclerView.setAdapter(new ProductAdapter(getActivity(), Product.productHashMap));

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                        detailIntent.putExtra("id", String.valueOf(position));
                        startActivity(detailIntent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        return rootView;
    }
}
