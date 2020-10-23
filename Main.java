import java.util.List;
import java.util.ArrayList;

public class Main {

  public List<Event> generateEvents() {
    List<Event> events = new ArrayList<Event>();
    int count = (int) Math.floor(Math.random() * 100);
    for (int i = 0; i < count; i++) {
      events.add(new Event("click_logo", System.currentTimeMillis()));
    }
    return events;
  }

  public static void main(String[] args) {
    Main main = new Main();

    // Imagine we get a list of events from somewhere.
    List<Event> events = main.generateEvents();

    String target = "click_logo";
    long count = events.stream()
      .filter(e -> {
        boolean isMatch = e.getName().equals(target);
        if (isMatch) {
          System.out.println(e);
        }
        return isMatch;
      })
      .count();

    System.out.println(String.format("There are %d matching events.", count));
  }
}
