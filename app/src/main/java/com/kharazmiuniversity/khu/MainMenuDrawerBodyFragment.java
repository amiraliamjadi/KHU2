package com.kharazmiuniversity.khu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.kharazmiuniversity.khu.data.KhuAPI;
import com.kharazmiuniversity.khu.data.ObjectsController;
import com.kharazmiuniversity.khu.models.Channel;
import com.kharazmiuniversity.khu.models.GetObject;
import com.kharazmiuniversity.khu.models.Group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainMenuDrawerBodyFragment extends Fragment
{


    private RecyclerView objects;
    private List<Group> groupList = new ArrayList<>();
    private List<Channel> channelList = new ArrayList<>();
    private GroupAdapter groupAdapter;

    private ProgressBar progressBar;


    // marboot be gereftan e groups ha
    KhuAPI.getObjectsCallback objectsCallback = new KhuAPI.getObjectsCallback() {
        @Override
        public void onResponse(List<Group> inputListGroup , List<Channel> inputListChannel)
        {

            if (progressBar.isShown())
            {
                progressBar.setVisibility(View.INVISIBLE);
            }

            groupList.clear();
            channelList.clear();

            Collections.sort(inputListGroup, new Comparator<Group>() {
                @Override
                public int compare(Group x, Group y) {
                    return x.getName().compareTo(y.getName());
                }
            });

            Collections.sort(inputListChannel, new Comparator<Channel>() {
                @Override
                public int compare(Channel x, Channel y) {
                    return x.getName().compareTo(y.getName());
                }
            });

            groupList.addAll(inputListGroup);
            channelList.addAll(inputListChannel);
            groupAdapter.notifyDataSetChanged();

        }

        @Override
        public void onFailure(String cause) {

        }
    };



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        findViews(view);
        initObjectList();



        progressBar.setVisibility(View.VISIBLE);

        GetObject getObject = new GetObject();
        String username = MyPreferenceManager.getInstance(getActivity()).getUsername();
        getObject.setUsername(username);

        ObjectsController objectsController = new ObjectsController(objectsCallback);
        objectsController.start(getObject);

    }



    private void findViews(View view)
    {
        objects = view.findViewById(R.id.objects);
        progressBar = view.findViewById(R.id.menu_progress_bar);

    }

    private void initObjectList()
    {
        groupAdapter = new GroupAdapter(groupList ,channelList, getActivity());
        objects.setLayoutManager(new LinearLayoutManager(getActivity()));
        objects.setAdapter(groupAdapter);


    }




}
