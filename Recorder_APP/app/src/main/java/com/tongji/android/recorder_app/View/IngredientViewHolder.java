package com.tongji.android.recorder_app.View;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import com.tongji.android.recorder_app.Model.Ingredient;
import com.tongji.android.recorder_app.R;

public class IngredientViewHolder extends ChildViewHolder {

    private TextView mIngredientTextView;
    private TextView mContactsName;

    public IngredientViewHolder(View itemView) {
        super(itemView);
        mIngredientTextView = (TextView) itemView.findViewById(R.id.ingredient_textview);
        mContactsName = (TextView) itemView.findViewById(R.id.contactsName);
    }

    public void bind(Ingredient ingredient) {
        mIngredientTextView.setText(ingredient.getPhone());
        mContactsName.setText(ingredient.getName());
    }
}
