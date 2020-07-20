package com.reobotetechnology.reobotegame.ui.home.ui.blog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BlogViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BlogViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("AQUI VAI FICAR OS VIDEOS E CONTEÚDOS BÍBLICOS");
    }

    public LiveData<String> getText() {
        return mText;
    }
}