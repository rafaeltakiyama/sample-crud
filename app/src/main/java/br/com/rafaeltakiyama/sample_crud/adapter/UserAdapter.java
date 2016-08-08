package br.com.rafaeltakiyama.sample_crud.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.rafaeltakiyama.sample_crud.R;
import br.com.rafaeltakiyama.sample_crud.delegate.Delegate;
import br.com.rafaeltakiyama.sample_crud.model.User;


/**
 * Created by rafaelakiyama on 20/07/16.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private static List<User> users;
    private static Delegate delegate;

    public UserAdapter(List<User> testeList, Delegate delegate) {
        this.users = testeList;
        this.delegate = delegate;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_card_view_user, viewGroup, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder userViewHolder, int position) {
        User user = users.get(position);
        userViewHolder.tvLetter.setText(user.getFirstLetterFromUserName());
        userViewHolder.tvUserName.setText(user.getCompleteName());
        userViewHolder.tvUserId.setText(String.valueOf(user.getId()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvUserId;
        protected TextView tvUserName;
        protected TextView tvLetter;

        public UserViewHolder(View v) {
            super(v);
            tvLetter = (TextView) v.findViewById(R.id.txt_letter);
            tvUserId = (TextView)  v.findViewById(R.id.txt_user_id);
            tvUserName = (TextView)  v.findViewById(R.id.txt_user_name);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int layoutPosition = getLayoutPosition();
                    delegate.onClickRecycleView(users.get(layoutPosition));
                }
            });
        }
    }
}
