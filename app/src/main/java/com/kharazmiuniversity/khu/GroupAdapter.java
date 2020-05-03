package com.kharazmiuniversity.khu;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.kharazmiuniversity.khu.data.ChannelMessageController;
import com.kharazmiuniversity.khu.data.GroupMessageController;
import com.kharazmiuniversity.khu.data.KhuAPI;
import com.kharazmiuniversity.khu.models.Channel;
import com.kharazmiuniversity.khu.models.ChannelMessage;
import com.kharazmiuniversity.khu.models.Group;
import com.kharazmiuniversity.khu.models.GroupMessage;
import com.kharazmiuniversity.khu.models.RequestChannelMessage;
import com.kharazmiuniversity.khu.models.RequestGroupMessage;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>
{
    List<Group> groupitems;
    List<Channel> channelitems;
    private Context mContext;

    public static String objectId;





    public GroupAdapter(List<Group> groupitems, List<Channel> channelitems ,Context context)
    {
        this.groupitems = groupitems;
        this.channelitems = channelitems;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i)
    {



        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.template_objects, viewGroup , false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {

        if ( position < groupitems.size())
        {
            holder.name.setText(groupitems.get(position).getName());
            holder.id =  groupitems.get(position).getId();


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    objectId = holder.id;

                    Intent groupChatIntent = new Intent(mContext, GroupChatActivity.class);

                    mContext.startActivity(groupChatIntent);

                }
            });


        }
        else {
            holder.name.setText(channelitems.get(position - groupitems.size()).getName());
            holder.id =  channelitems.get(position - groupitems.size()).getId();


            if (MyPreferenceManager.getInstance(mContext).getProffessor())
            {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                        objectId = holder.id;



                        Intent channelAdminIntent = new Intent(mContext, ChannelAdminActivity.class);

                        mContext.startActivity(channelAdminIntent);

                    }
                });
            }
            else {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                        objectId = holder.id;


                        Intent channelIntent = new Intent(mContext, ChannelActivity.class);

                        mContext.startActivity(channelIntent);

                    }
                });
            }




        }





    }

    @Override
    public int getItemCount()
    {
        return groupitems.size() + channelitems.size();
    }





    class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView name;
        public String id;

        public ViewHolder(@NonNull final View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.name);

        }



    }
}
