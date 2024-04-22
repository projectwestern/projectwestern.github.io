import java.util.ArrayList;

public class Stop
{
  private String stopID;
  private String stopName;
  private double stopLat;
  private double stopLon;
  private ArrayList<String> routes;
  private ArrayList<String> schedule;

  public Stop(String id, String n, double la, double lo)
  {
    stopID = id;
    stopName = n;
    stopLat = la;
    stopLon = lo;
    routes = new ArrayList<String>();
    schedule = new ArrayList<String>();
  }

  public void setStopID(String s)
  {
    stopID = s;
  }
  public void setName(String n)
  {
    stopName = n;
  }
  public void setLat(double l)
  {
    stopLat = l;
  }
  public void setLon(double l)
  {
    stopLon = l;
  }

  public String getStopID()
  {
    return stopID;
  }
  public String getName()
  {
    return stopName;
  }
  public double getLat()
  {
    return stopLat;
  }
  public double getLon()
  {
    return stopLon;
  }
  public ArrayList<String> getRoutes()
  {
    return routes;
  }
  public ArrayList<String> getSchedule()
  {
    return schedule;
  }

  public void addRoute(String r)
  {
    routes.add(r);
  }
  public void addSchedule(String t)
  {
    schedule.add(t);
  }
}