package com.codingame.view;

import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.Module;
import com.google.inject.Inject;

public class EndScreenModule implements Module {

    private GameManager<AbstractPlayer> gameManager;
    private int[] scores;
    private int[] sanities;

    @Inject
    EndScreenModule(GameManager<AbstractPlayer> gameManager) {
        this.gameManager = gameManager;
        gameManager.registerModule(this);
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }
    
    public void setSanities(int[] sanities) {
        this.sanities = sanities;
    }

    @Override
    public final void onGameInit() {
    }

    @Override
    public final void onAfterGameTurn() {
    }

    @Override
    public final void onAfterOnEnd() {
        Object[] data = { scores, sanities };
        gameManager.setViewData("endScreen", data);
    }

}