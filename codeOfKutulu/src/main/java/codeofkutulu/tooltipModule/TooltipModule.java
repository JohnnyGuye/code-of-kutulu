package codeofkutulu.tooltipModule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.Module;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.inject.Inject;

public class TooltipModule implements Module {

  GameManager<AbstractPlayer> gameManager;
  @Inject
  GraphicEntityModule entityModule;
  Map<Integer, Map<String, Object>> registrations;
  Map<Integer, Map<String, Object>> newRegistrations;
  Map<Integer, String[]> extra, newExtra;
  Set<Integer> removals;

  @Inject
  TooltipModule(GameManager<AbstractPlayer> gameManager) {
    this.gameManager = gameManager;
    gameManager.registerModule(this);
    registrations = new HashMap<>();
    newRegistrations = new HashMap<>();
    extra = new HashMap<>();
    newExtra = new HashMap<>();
    removals = new HashSet<>();
  }

  @Override
  public void onGameInit() {
    // gameManager.setViewGlobalData("tooltips", null);
    sendFrameData();
  }

  @Override
  public void onAfterGameTurn() {
    sendFrameData();
  }

  @Override
  public void onAfterOnEnd() {
    sendFrameData();
  }

  private void sendFrameData() {
    Object[] data = { registrations, newExtra, removals };
    gameManager.setViewData("tooltips", data);

    removals.stream().forEach(entity -> {
      registrations.remove(entity);
      extra.remove(entity);
    });

    removals.clear();
    newRegistrations.clear();
    newExtra.clear();
  }

  public void registerEntity(int id, Map<String, Object> params) {
    if (!params.equals(registrations.get(id))) {
      newRegistrations.put(id, params);
      registrations.put(id, params);
    }
  }

  public Map<String, Object> getParams(int id) {
    Map<String, Object> params = registrations.get(id);
    if (params == null) {
      params = new HashMap<>();
      registrations.put(id, params);
    }
    return params;
  }

  public void removeEntity(int id) {
    removals.add(id);
    registrations.remove(id);
  }

  public void updateExtraTooltipText(Entity<?> entity, String... lines) {
    int id = entity.getId();
    if (!Arrays.equals(lines, extra.get(id))) {
      newExtra.put(id, lines);
      extra.put(id, lines);
    }
  }

  public void removeUnitToolTip(Entity<?> entity) {
    entity.setVisible(false); // erases from the view the unit sprite too
  }
}
