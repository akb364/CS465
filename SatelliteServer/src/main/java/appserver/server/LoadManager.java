package appserver.server;

import java.util.ArrayList;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class LoadManager {

    static ArrayList satellites = null;
    static int lastSatelliteIndex = -1;
    int numberSatellites;

    public LoadManager() {
        satellites = new ArrayList<String>();
        numberSatellites = 0;
    }

    public void satelliteAdded(String satelliteName) {
        // add satellite
        numberSatellites++;
        satellites.add(satelliteName);
        System.out.println("[LoadManager.satelliteAdded]" + satelliteName + " added");
    }


    public String nextSatellite() throws Exception {
     
        synchronized (satellites) {
            // implement policy that returns the satellite name according to a round robin methodology
            lastSatelliteIndex++;
            
            if(lastSatelliteIndex >= numberSatellites - 1)
            {
                lastSatelliteIndex = 0;
            }
            
            return (String)satellites.get(lastSatelliteIndex);
        }
    }
}
