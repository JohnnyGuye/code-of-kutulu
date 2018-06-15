package codeofkutulu.models;

public enum MinionState {
  SPAWNING(0), 
  SERVING_MY_MASTER(1), 
  PREPARING_RUSH(2), 
  RUSH(3), 
  STUNNED(4),
  RECALLING_SUCCESS(10), 
  RECALLING_FAILURE(11);


  private int id;

  MinionState(int id) {
    this.id = id;
  }

  int getId() {
    return id;
  }
}
