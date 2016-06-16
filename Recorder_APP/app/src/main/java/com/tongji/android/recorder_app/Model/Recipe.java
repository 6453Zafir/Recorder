package com.tongji.android.recorder_app.Model;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

public class Recipe implements ParentListItem {

    private String mName;
    private List<Ingredient> mIngredients;

    public Recipe(String name, List<Ingredient> ingredients) {
        mName = name;
        mIngredients = ingredients;
    }

    public String getName() {
        return mName;
    }

    @Override
    public List<?> getChildItemList() {
        return mIngredients;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
