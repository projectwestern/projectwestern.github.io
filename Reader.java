import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;

public class Reader
{
  public static void main(String [] args)
  {
    ArrayList<Stop> stops = new ArrayList<Stop>();
    ArrayList<Trip> trips = new ArrayList<Trip>();
    ArrayList<Route> routes = new ArrayList<Route>();

    // get files
    ArrayList<String> mondayService = new ArrayList<String>();
    
    try
    {
      Scanner s = new Scanner(new File("gtfs/calendar.txt"));
      int z = 0; // skip first
      while (s.hasNextLine())
      {
        if (z == 0)
        {
          s.nextLine();
          z++;
        }
        else
        {
          String data = s.nextLine();
          String serviceID = data.substring(0, data.indexOf(","));
          data = data.substring(data.indexOf(",") + 1);
          String monday = data.substring(0, data.indexOf(",")); // 0 or 1
          if (monday.equals("1"))
          {
            mondayService.add(serviceID);
          }
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error - no calendar.txt.");
    }

    try
    {
      Scanner s = new Scanner(new File("gtfs/routes.txt"));
      int z = 0; // skip first
      while (s.hasNextLine())
      {
        if (z == 0)
        {
          s.nextLine();
          z++;
        }
        else
        {
          String data = s.nextLine();
          String routeName = data.substring(0, data.indexOf(","));
          data = data.substring(data.indexOf(",") + 1);
          data = data.substring(data.indexOf(",") + 1);
          String routeFull = data.substring(0, data.indexOf(","));
          routes.add(new Route(routeName, routeFull));
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error - no routes.txt.");
    }
    
    try
    {
      Scanner s = new Scanner(new File("gtfs/trips.txt"));
      int tripCount = 0;
      int z = 0; // skip first
      while (s.hasNextLine())
      {
        if (z == 0)
        {
          s.nextLine();
          z++;
        }
        else
        {
          String data = s.nextLine();
          String routeIDtrip = data.substring(0, data.indexOf(","));
          data = data.substring(data.indexOf(",") + 1);
          String serviceIDtrip = data.substring(0, data.indexOf(","));
          data = data.substring(data.indexOf(",") + 1);
          String tripIDtrip = data.substring(0, data.indexOf(","));
          data = data.substring(data.indexOf(",") + 1);
          data = data.substring(data.indexOf(",") + 1);
          data = data.substring(data.indexOf(",") + 1);
          data = data.substring(data.indexOf(",") + 1);
          String tripheadsign = data.substring(0, data.indexOf(","));

          for (int i = 0; i < mondayService.size(); i++)
          {
            if (serviceIDtrip.equals(mondayService.get(i)))
            {
              // moves to add trip if active Monday service
              trips.add(new Trip(tripIDtrip, serviceIDtrip, routeIDtrip, tripheadsign, "99:99")); // time loads after
              tripCount++;
              if (tripCount % 100 == 0) // update every 100
              {
                System.out.println("Trips Added: " + tripCount);
              }
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error - no trips.txt.");
    }

    try
    {
      Scanner s = new Scanner(new File("gtfs/stops.txt"));
      int z = 0; // skip first
      int stopCount = 0;
      while (s.hasNextLine())
      {
        if (z == 0)
        {
          s.nextLine();
          z++;
        }
        else
        {
          String data = s.nextLine();
          String stopID = data.substring(0, data.indexOf(","));
          data = data.substring(data.indexOf(",") + 1); // skip code
          data = data.substring(data.indexOf(",") + 1);
          String stopName = data.substring(0, data.indexOf(","));
          data = data.substring(data.indexOf(",") + 1);
          double stopLat = Double.parseDouble(data.substring(0, data.indexOf(",")));
          data = data.substring(data.indexOf(",") + 1);
          double stopLon = Double.parseDouble(data.substring(0, data.indexOf(",")));
          stops.add(new Stop(stopID, stopName, stopLat, stopLon));

          stopCount++;
          if (stopCount % 100 == 0)
          {
            System.out.println("Stops Added: " + stopCount);
          }
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error - no stops.txt.");
    }

    try
    {
      Scanner s = new Scanner(new File("gtfs/stop_times.txt"));
      int z = 0; // skip first
      int timeCount = 0;
      while (s.hasNextLine())
      {
        if (z == 0)
        {
          s.nextLine();
          z++;
        }
        else
        {
          String data = s.nextLine();
          String tripIDtimes = data.substring(0, data.indexOf(","));
          data = data.substring(data.indexOf(",") + 1);
          String departuretimes = data.substring(0, 5);
          data = data.substring(data.indexOf(",") + 1);
          String stopIDtimes = data.substring(0, data.indexOf(","));
          data = data.substring(data.indexOf(",") + 1);
          String stoporder = data.substring(0, data.indexOf(","));

          for (int i = 0; i < trips.size(); i++)
          {
            if (trips.get(i).getTripID().equals(tripIDtimes))
            {
              for (int j = 0; j < stops.size(); j++)
              {
                if (stops.get(j).getStopID().equals(stopIDtimes))
                {
                  // assign stop to trip
                  trips.get(i).addStop(stops.get(j));
                  
                  // assign time to trip if stop order is 1
                  if (stoporder.equals("1"))
                  {
                    trips.get(i).setTime(departuretimes);
                  }
                  
                  // add time to trip schedule
                  trips.get(i).addTime(departuretimes);

                  // assign route to stop
                  if (!stops.get(j).getRoutes().contains(trips.get(i).getRoute()))
                  {
                    stops.get(j).addRoute(trips.get(i).getRoute());
                  }

                  // assign time to stop
                  // STILL HAS TO BE SPLIT BY DAY
                  // WEEKEND SCHEDULES DON'T WORK
                  stops.get(j).addSchedule(departuretimes);

                  timeCount++;
                  if (timeCount % 100 == 0) // update every 100, estimate of 3 million
                  {
                    System.out.println("Stop Times Added: " + timeCount);
                  }
                }
              }
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error - no stop_times.txt.");
    }

    System.out.println("Stop Times Complete.");

    // export to new file system

    // Monday schedule
    try
    {
      File scheduleFile = new File("gtfs/new-schedule.txt");
      FileWriter scheduleWriter = new FileWriter(scheduleFile);

      scheduleWriter.write("trip_id,trip_route,stop_id,stop_name,stop_time\n");

      for (int i = 0; i < trips.size(); i++)
      {
        for (int k = 0; k < trips.get(i).getTimes().size(); k++) // loop through every stop on each trip in order
        {
          if (trips.get(i).getTimes().get(k).substring(0, 3).equals("24:"))
          {
            trips.get(i).getTimes().set(k, "00:" + trips.get(i).getTimes().get(k).substring(3));
          }
          if (trips.get(i).getTimes().get(k).substring(0, 3).equals("25:"))
          {
            trips.get(i).getTimes().set(k, "01:" + trips.get(i).getTimes().get(k).substring(3));
          }
          scheduleWriter.append(trips.get(i).getTripID() + "," + trips.get(i).getRoute() + "," + trips.get(i).getStops().get(k).getStopID() + "," + trips.get(i).getStops().get(k).getName() + "," + trips.get(i).getTimes().get(k) + "\n");
        }
      }
      scheduleWriter.close();
      System.out.println("Schedule File Written.");
    }
    catch (Exception e)
    {
      System.out.println("Error - can't write schedule.");
    }

    // stop - demographics coming later
    try
    {
      File stopFile = new File("gtfs/new-stops.txt");
      FileWriter stopWriter = new FileWriter(stopFile);

      stopWriter.write("stop_id,stop_name,stop_lat,stop_lon,routes\n");

      for (int i = 0; i < stops.size(); i++)
      {
        for (int j = 0; j < stops.get(i).getSchedule().size(); j++)
        {
          if (stops.get(i).getSchedule().get(j).substring(0, 3).equals("24:"))
          {
            stops.get(i).getSchedule().set(j, "00:" + stops.get(i).getSchedule().get(j).substring(3));
          }
          if (stops.get(i).getSchedule().get(j).substring(0, 3).equals("25:"))
          {
            stops.get(i).getSchedule().set(j, "01:" + stops.get(i).getSchedule().get(j).substring(3));
          }
        }

        Collections.sort(stops.get(i).getSchedule());
        
        stopWriter.append(stops.get(i).getStopID() + "," + stops.get(i).getName() + "," + stops.get(i).getLat() + "," + stops.get(i).getLon() + "," + stops.get(i).getRoutes() + stops.get(i).getSchedule() + "\n");
      }

      stopWriter.close();
      System.out.println("Stop File Written.");
    }
    catch (Exception e)
    {
      System.out.println("Error - can't write stops.");
    }
  }
}