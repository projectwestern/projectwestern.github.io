import java.util.ArrayList;

public class Route
{
  private String shortName;
  private String longName;
  private ArrayList<Trip> trips;

  // but doesn't a route have stops? that will be taken from each of the trips.

  public Route(String s, String l)
  {
    shortName = s;
    longName = l;
    trips = new ArrayList<Trip>();
  }

  public void setShortName(String s)
  {
    shortName = s;
  }
  public void setLongName(String l)
  {
    longName = l;
  }

  public String getShortName()
  {
    return shortName;
  }
  public String getLongName()
  {
    return longName;
  }
  public ArrayList<Trip> getTrips()
  {
    return trips;
  }

  public void addTrip(Trip t)
  {
    trips.add(t);
  }
}