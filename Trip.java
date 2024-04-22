import java.util.ArrayList;

public class Trip
{
  private String tripID;
  private String serviceID;
  private String route;
  private String depTime;
  private String headsign;
  private ArrayList<Integer> days;
  private ArrayList<String> times;
  private ArrayList<Stop> stops;

  public Trip(String id, String s, String r, String t, String h)
  {
    tripID = id;
    serviceID = s;
    route = r;
    depTime = t;
    headsign = h;
    times = new ArrayList<String>();
    stops = new ArrayList<Stop>();
    days = new ArrayList<Integer>();
  }

  public void setTripID(String t)
  {
    tripID = t;
  }
  public void setRoute(String r)
  {
    route = r;
  }
  public void setTime(String t) // start time
  {
    depTime = t;
  }

  public String getTripID()
  {
    return tripID;
  }
  public String getServiceID()
  {
    return serviceID;
  }
  public String getRoute()
  {
    return route;
  }
  public String getTime() // start time
  {
    return depTime;
  }
  public ArrayList<Stop> getStops()
  {
    return stops;
  }
  public ArrayList<Integer> getDays()
  {
    return days;
  }
  public ArrayList<String> getTimes() // time of all stops
  {
    return times;
  }

  public void addStop(Stop s)
  {
    stops.add(s);
  }
  public void addTime(String t) // time of all stops
  {
    times.add(t);
  }
  public void addDay(int d)
  {
    days.add(d);
  }
}