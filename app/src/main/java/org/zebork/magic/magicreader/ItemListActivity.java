package org.zebork.magic.magicreader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.ActivityManager;
import android.widget.Toast;

import org.zebork.magic.magicreader.dummy.APPInfo;
import org.zebork.magic.magicreader.dummy.DummyContent;
import org.zebork.magic.magicreader.dummy.InfoGetter;


import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * 主界面
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        String avail = "可用内存: " + String.valueOf(info.availMem >> 20) + "MB";
        String total = "内存总量: " + String.valueOf(info.totalMem >> 20) + "MB";

        DummyContent.ITEM_MAP.get("3").details = avail + "\n" + total;

        StringBuilder installedApp = new StringBuilder();
        //installedApp.append("APP\tSize\tUpdateDate\n");
        List<APPInfo> appInfos = InfoGetter.getInstallApp(this);
        int isSysApp = 0;
        for(APPInfo app : appInfos) {
            installedApp.append("APP\t" + app.getAppLabel() + "\n"
                    + "大小\t" + app.getAppSize() + "\n"
                    + "系统应用\t" + app.getFlag() + "\n"
                    + "安装位置\t" + app.isInSDCard() + "\n"
                    + "更新时间\t" + app.getUpdateDate() + "\n"
                    + "packet\t" + app.getPkgName() + "\n\n"
            );
            if (app.getFlag() == "True")
                isSysApp += 1;
        }
        DummyContent.ITEM_MAP.get("4").setDynamic("共" + appInfos.size() + "个应用, 其中" + isSysApp
                + "个系统应用，" + (appInfos.size() - isSysApp) + "个第三方应用。\n\n"
                + installedApp.toString());

        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            DummyContent.ITEM_MAP.get("1").setDynamic(InfoGetter.getMacAddress(this)
                    + InfoGetter.getInfo(this) + InfoGetter.getNetworkInfo(this));
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_NETWORK_STATE}, 2);
        } else {
            DummyContent.ITEM_MAP.get("1").setDynamic(InfoGetter.getMacAddress(this)
                    + InfoGetter.getInfo(this) + InfoGetter.getNetworkInfo(this));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DummyContent.ITEM_MAP.get("1").setDynamic(InfoGetter.getMacAddress(this)
                            + InfoGetter.getInfo(this) + InfoGetter.getNetworkInfo(this));
                } else {
                    Toast.makeText(this, "权限拒绝！", Toast.LENGTH_SHORT).show();
                    DummyContent.ITEM_MAP.get("1").setDynamic(InfoGetter.getMacAddress(this));
                }
                break;
            case 2:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DummyContent.ITEM_MAP.get("1").setDynamic(InfoGetter.getMacAddress(this)
                            + InfoGetter.getInfo(this) + InfoGetter.getNetworkInfo(this));
                } else {
                    Toast.makeText(this, "权限拒绝！", Toast.LENGTH_SHORT).show();
                    DummyContent.ITEM_MAP.get("1").setDynamic(InfoGetter.getMacAddress(this));
                }
                break;
            default:
        }
    }



    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();

                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);

                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
