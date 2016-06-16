package com.tongji.android.recorder_app.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tongji.android.recorder_app.Application.MyApplication;
import com.tongji.android.recorder_app.Fragment.RankingListFragment;
import com.tongji.android.recorder_app.Model.Friend;
import com.tongji.android.recorder_app.Model.FriendList;
import com.tongji.android.recorder_app.Model.Ingredient;
import com.tongji.android.recorder_app.Model.Recipe;
import com.tongji.android.recorder_app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class RecipeAdapter extends ExpandableRecyclerAdapter<RecipeViewHolder, IngredientViewHolder> {

    private LayoutInflater mInflator;
    private Context context;

    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    public RecipeAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        this.context=context;
        mInflator = LayoutInflater.from(context);
    }

    @Override
    public RecipeViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View recipeView = mInflator.inflate(R.layout.recipe_view, parentViewGroup, false);
        return new RecipeViewHolder(recipeView);
    }

    @Override
    public IngredientViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View ingredientView = mInflator.inflate(R.layout.ingredient_view, childViewGroup, false);


        return new IngredientViewHolder(ingredientView);
    }

    @Override
    public void onBindParentViewHolder(RecipeViewHolder recipeViewHolder, int position, ParentListItem parentListItem) {
        Recipe recipe = (Recipe) parentListItem;
        recipeViewHolder.bind(recipe);
    }

    @Override
    public void onBindChildViewHolder(IngredientViewHolder ingredientViewHolder, final int position, Object childListItem) {
        final Ingredient ingredient = (Ingredient) childListItem;
        ingredientViewHolder.bind(ingredient);

        ingredientViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog myDialog = ProgressDialog.show(context, "正在查询..", "查询中,请稍后..", true, true);
                final String friendphoneNum = ingredient.getPhone();
               // Toast.makeText(context,ingredient.getPhone()+"",Toast.LENGTH_SHORT).show();
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.add("phoneNumber",ingredient.getPhone());
                client.post("http://lshunran.com:3000/recorder/search", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        myDialog.dismiss();
                        String re = new String(responseBody);
                        try{
                            JSONObject object = new JSONObject(re);
                            int errCode =object.getInt("errCode");
                            int isExist = object.getInt("isExist");
                            if(errCode == 0 && isExist == 1 ){
                                builder = new AlertDialog.Builder(context);
                                alert = builder.setIcon(R.drawable.success)
                                        .setTitle("Adding Friends")
                                        .setMessage("Are you sure you want to add "+friendphoneNum)
                                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                AsyncHttpClient client = new AsyncHttpClient();
                                                RequestParams params = new RequestParams();
                                                MyApplication myapp = (MyApplication)context.getApplicationContext();
                                                params.add("phoneNumber",myapp.getPhoneNumber());
                                                params.add("addPhoneNumber",friendphoneNum);
                                                client.post("http://lshunran.com:3000/recorder/add", params, new AsyncHttpResponseHandler() {
                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                        String re = new String(responseBody);
                                                        try{
                                                            JSONObject object = new JSONObject(re);
                                                            int errCode =object.getInt("errCode");
                                                            if(errCode == 0 ){
                                                                int score = object.getInt("score");
                                                                String username = object.getString("username");
                                                                Friend mynewfriend = new Friend(friendphoneNum,username,score);
                                                                FriendList.addItem(mynewfriend);
                                                                NoSQLEntity<Friend> entity=new NoSQLEntity<Friend>("friend",friendphoneNum+"");
                                                                entity.setData(mynewfriend);
                                                                NoSQL.with(context).using(Friend.class).save(entity);
//
                                                                builder = new AlertDialog.Builder(context);
                                                                alert = builder.setIcon(R.drawable.success)
                                                                        .setTitle("Congratulations：")
                                                                        .setMessage("You have added the "+friendphoneNum+" as your friend!Now you can check his/her habit list")
                                                                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {

                                                                            }
                                                                        })
                                                                        .setPositiveButton("Check Now", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                               // adapter.notifyDataSetChanged();
                                                                                Intent intent = new Intent(RankingListFragment.RELOAD_RANKING);

                                                                                context.sendBroadcast(intent);

                                                                            }
                                                                        }).create();
                                                                alert.show();
                                                            }else if(errCode== 1){
                                                                builder = new AlertDialog.Builder(context);
                                                                alert = builder.setIcon(R.drawable.error)
                                                                        .setTitle("Sorry：")
                                                                        .setMessage("You have added the "+friendphoneNum+" as friend")

                                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {

                                                                            }
                                                                        }).create();
                                                                alert.show();
                                                            }
                                                        }catch (JSONException e){
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                                    }
                                                });
                                            }
                                        }).create();
                                alert.show();
                            }else if(errCode==0 && isExist == 0){
                                builder = new AlertDialog.Builder(context);
                                alert = builder.setIcon(R.drawable.error)
                                        .setTitle("Sorry：")
                                        .setMessage("The phone number "+friendphoneNum+" haven't register")

                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).create();
                                alert.show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });
    }
}
