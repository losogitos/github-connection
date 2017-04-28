package com.example.lukasz.githubconnection.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lukasz.githubconnection.R;
import com.example.lukasz.githubconnection.connection.Connection;
import com.example.lukasz.githubconnection.model.Repo;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Łukasz Ciupa on 28.04.17.
 */

public class ReposActivity extends AppCompatActivity {

    private static final String TAG = ReposActivity.class.getName();
    public static final String USER = "user";
    private String user;
    private ReposAdapter listAdapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestForData();
            }
        });
        Intent intent = getIntent();
        user = intent.getStringExtra(USER);
        listAdapter = new ReposAdapter(new ArrayList<Repo>(0));
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.repos_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout.setRefreshing(true);
        requestForData();
    }

    private void requestForData() {
        Connection.gitHubService().listRepos(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repo>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "Completed");
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error: " + e);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(List<Repo> repos) {
                Log.d(TAG, "Repos");
                listAdapter.replaceData(repos);
            }
        });
    }

    private static class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ViewHolder> {

        private List<Repo> repos;

        public ReposAdapter(List<Repo> repos) {
            setList(repos);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View repoView = inflater.inflate(R.layout.item_repo, parent, false);

            return new ViewHolder(repoView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Repo repo = repos.get(position);

            viewHolder.title.setText(repo.getName() != null ? repo.getName() : "");
            viewHolder.description.setText(repo.getDescription() != null ? repo.getDescription() : "");
        }

        private void setList(List<Repo> repos) {
            this.repos = repos;
        }

        public void replaceData(List<Repo> repos) {
            setList(repos);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return repos.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView title;

            public TextView description;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.repo_title);
                description = (TextView) itemView.findViewById(R.id.repo_detail);
            }
        }
    }
}

