package com.codingame.game;

import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

import codeofkutulu.models.PlayerUnit;

public class Player extends AbstractMultiplayerPlayer {
  private static final int MAX_MESSAGE_LENGTH = 26;

  private PlayerUnit playerUnit;
  private String displayMessage = "";

  @Override
  public int getExpectedOutputLines() {
    return 1;
  }

  public void kill(String msg) {
    this.deactivate(msg);
  }

  public PlayerUnit getPlayerUnit() {
    return playerUnit;
  }

  public PlayerUnit createUnit() {
    playerUnit = new PlayerUnit(this.getIndex());
    return playerUnit;
  }

  public void setDisplayMessage(String message) {
    if (message == null) {
      this.displayMessage = "";
      return;
    }

    if (message.length() > MAX_MESSAGE_LENGTH)
      message = message.substring(0, Math.max(0, MAX_MESSAGE_LENGTH - 6)) + " (...)";
    this.displayMessage = message;
  }

  public String getDisplayMessage() {
    return displayMessage;
  }

}