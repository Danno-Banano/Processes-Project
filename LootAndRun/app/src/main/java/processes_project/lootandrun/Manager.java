package processes_project.lootandrun;

import android.location.Location;
import android.os.SystemClock;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by trevor on 4/12/16.
 */
public class Manager {
    private MainMap map;
    private HashMap<Integer, MapObject<Character>> monsters;
    private HashMap<Integer, MapObject<LootClass>> caches;
    private int maxNumMonsters;
    private int maxNumItems;
    private double loadRadius;
    private long refreshTimer;

    public Manager(MainMap map, final int maxNumMonsters, int maxNumItems, double loadRadius, int refreshTimer) {
        this.map = map;
        monsters = new HashMap<>();
        caches = new HashMap<>();
        this.maxNumMonsters = maxNumMonsters;
        this.maxNumItems = maxNumItems;
        this.loadRadius = loadRadius;
        this.refreshTimer = refreshTimer;
    }

    public void startManager() {
        Thread t = new Thread() {
            public void run() {
                while (true) {
                    try {
                        sleep(refreshTimer);
                        map.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (monsters.size() < maxNumMonsters) {
                                    placeNewMonster();
                                }
                                if (caches.size() < maxNumItems) {
                                    placeNewCache();
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();
    }

    private <E> Marker placeObjectOnMap(E object) {
        if (MainMap.getMainPlayer().getCharLocation() == null) {
            return null;
        }

        LatLng objLatLng =
                new LatLng(MainMap.getMainPlayer().getCharLocation().getLatitude() + randomValWithinLoadRadius()
                        , MainMap.getMainPlayer().getCharLocation().getLongitude() + randomValWithinLoadRadius());

        Marker objMarker = map.getGoogleMap().addMarker(new MarkerOptions()
                .position(objLatLng)
                .title(object.toString()));

        return objMarker;
    }

    public Character placeNewMonster() {
        Character newMonster = new Character(20, "Zombie", null, 10, new ArrayList<Item>());
        Marker newMonsterLocation = placeObjectOnMap(newMonster);
        MonsterMapObject newMapObj = new MonsterMapObject(newMonster, newMonsterLocation);
        monsters.put(newMonsterLocation.hashCode(), newMapObj);
        if (newMonsterLocation != null && monsters.size() < maxNumMonsters) {
            newMapObj.randomWalk();
        }
        else {
            newMonsterLocation.remove();
            monsters.remove(newMonsterLocation);
        }

        return null;
    }

    public Item placeNewCache() {
        LootClass newCache = new LootClass();
        Marker newCacheLocation = placeObjectOnMap(newCache);
        CacheMapObject newMapObj = new CacheMapObject(newCache, newCacheLocation);
        caches.put(newCache.hashCode(), newMapObj);

        // If the item cannot be placed, get rid of it
        if (!(newCacheLocation != null && caches.size() < maxNumItems)) {
            newCacheLocation.remove();
            caches.remove(newCacheLocation.hashCode());
        }

        return null;
    }

    public double randomValWithinLoadRadius() {
        return (new Random().nextDouble()) * loadRadius * (new Random().nextBoolean() ? 1 : -1);
    }

    private class MapObject <E> {
        public E mapObj;
        public Marker objMarker;

        public MapObject() {

        }

        public MapObject(E mapObj, Marker objMarker) {
            this.mapObj = mapObj;
            this.objMarker = objMarker;
        }
    }

    private class MonsterMapObject extends MapObject<Character> {
        public MonsterMapObject(Character mapObj, Marker objMarker) {
            super(mapObj, objMarker);
        }

        public void randomWalk() {
            Thread t = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            sleep(100);
                            map.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    /*
                                    LatLng charLatLng = new LatLng(MainMap.getMainPlayer().getCharLocation().getLatitude(), MainMap.getMainPlayer().getCharLocation().getLongitude());
                                    float[] results = new float[1];
                                    Location.distanceBetween(charLatLng.latitude, charLatLng.longitude, monsterMarker.getPosition().latitude, monsterMarker.getPosition().longitude, results);
                                    if (results[0] >= 0.0001) {
                                        //map.fightMonster();
                                    }

                                    long start = SystemClock.currentThreadTimeMillis();
                                    long elapsed = SystemClock.uptimeMillis() - start;
                                    double interp = new LinearInterpolator().getInterpolation((float) elapsed / 500);
                                    */
                                    LatLng newPos = new LatLng(
                                            objMarker.getPosition().latitude + (0.00005 * (new Random().nextBoolean() ? 1 : -1))
                                            , objMarker.getPosition().longitude + (0.00005 * (new Random().nextBoolean() ? 1 : -1)));

                                    //double lat = interp * newPos.latitude + (1-interp) * monsterMarker.getPosition().latitude;
                                    //double lng = interp * newPos.longitude + (1-interp) * monsterMarker.getPosition().longitude;

                                    //LatLng intermediatePos = new LatLng(lat, lng);
                                    objMarker.setPosition(newPos);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            t.start();
        }

        public void fightMainPlayer() {

        }
    }

    private class CacheMapObject extends MapObject<LootClass> {
        public CacheMapObject(LootClass mapObj, Marker objMarker) {
            super(mapObj, objMarker);
        }

        public void collect() {

        }
    }
}
