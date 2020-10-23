class Event {

  private String name;

  private long timestamp;

  public Event(String name, long timestamp) {
    this.name = name;
    this.timestamp = timestamp;
  }

  public String getName() {
    return this.name;
  }

  public long getTimestamp() {
    return this.timestamp;
  }

  @Override
  public String toString() {
    return String.format("%d: Event %s", this.timestamp, this.name);
  }
}
