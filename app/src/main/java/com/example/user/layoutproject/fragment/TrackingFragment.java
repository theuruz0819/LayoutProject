package com.example.user.layoutproject.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.layoutproject.R;
import com.example.user.layoutproject.RealmController;
import com.example.user.layoutproject.Task.ReflashTargetTask;
import com.example.user.layoutproject.model.TrackTarget;

import java.util.List;

import io.realm.Realm;

import static com.example.user.layoutproject.RealmController.with;

public class TrackingFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private MyItemRecyclerViewAdapter.OnListFragmentInteractionListener mListener;

    private List<TrackTarget> targetList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Paint p = new Paint();
    private Realm realm;
    private RealmController controller;

    public TrackingFragment() {
    }

    public static TrackingFragment newInstance(int columnCount) {
        TrackingFragment fragment = new TrackingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        realm = RealmController.with(this).getRealm();
        controller = with(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tracking_item_list, container, false);
        targetList = with(this).getTargetList();
        // Set the adapter
        Context context = view.getContext();

        mListener = new MyItemRecyclerViewAdapter.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(TrackTarget item) {

            }
        };
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        final MyItemRecyclerViewAdapter adapter = new MyItemRecyclerViewAdapter(targetList, mListener, context);
        recyclerView.setAdapter(adapter);
        initSwipe(adapter,recyclerView);

        final List<String> tracingUrls = RealmController.with(this).getAllTracingUrl();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("tag3", "pull to reflasgh");
                ReflashTargetTask task = new ReflashTargetTask();
                task.setTargetUrl(tracingUrls);
                task.setReflashFinishListener(new ReflashTargetTask.ReflashFinishListener() {
                    @Override
                    public void reflash(List<TrackTarget> targets) {
                        realm.beginTransaction();
                        realm.where(TrackTarget.class).findAll().deleteAllFromRealm();
                        try{
                            for (TrackTarget target:targets) {
                                realm.copyToRealm(target);
                            }
                            realm.commitTransaction();
                        }catch (Exception e){
                            realm.cancelTransaction();
                            Snackbar snackbar = Snackbar
                                    .make(getView(), e.getMessage(), Snackbar.LENGTH_LONG);
                        }
                        targetList = controller.getTargetList();
                        adapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
                task.execute();
            }
        });

        return view;
    }
    private void showRemoveDialog(String targetTitle, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener){
        new AlertDialog.Builder(getActivity())
                .setTitle("Remove Tracking Target")
                .setMessage("Remove " + targetTitle + " ?")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", cancelListener)
                .show();
    }

    private void initSwipe(final MyItemRecyclerViewAdapter adapter, RecyclerView recyclerView){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT){
                    DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            with(getActivity()).RemoveTarget(targetList.get(position).getTargetUrl());
                            adapter.removeItem(position);
                        }
                    };
                    DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyDataSetChanged();
                        }
                    };
                    showRemoveDialog(targetList.get(position).getTitle(), okListener, cancelListener);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
