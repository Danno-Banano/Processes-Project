package processes_project.lootandrun;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.mortbay.thread.ThreadPool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by trevor on 4/12/16.
 */ public class Manager { private MainMap map; public HashMap<Integer, MapObject> mapObjects; private int numMonsters; private int numCaches; private int maxNumMonsters; private int maxNumCaches; private double loadRadius; private double tapRadius; private long refreshTimer; private DatabaseHelper dbHelper; private String[] dbCols; BlockingQueue<Runnable> workQueue; ThreadPoolExecutor threadPoolExecutor;

    public Manager(Context context, MainMap map, final int maxNumMonsters, int maxNumCaches, double loadRadius, double tapRadius, int refreshTimer) {
        this.map = map;
        mapObjects = new HashMap<>();
        numMonsters = 0;
        numCaches = 0;
        this.maxNumMonsters = maxNumMonsters;
        this.maxNumCaches = maxNumCaches;
        this.loadRadius = loadRadius;
        this.tapRadius = tapRadius;
        this.refreshTimer = refreshTimer;
        this.dbHelper = new DatabaseHelper(context);
        dbCols = new String[]{"ID", "TYPE", "NAME", "HEALTH", "MAX_HEALTH", "INVENTORY", "LATITUDE",
                "LONGITUDE", "POWER"};

        int numCores = Runtime.getRuntime().availableProcessors();
        //workQueue = new LinkedBlockingDeque<>();
        //threadPoolExecutor = new ThreadPoolExecutor(numCores, numCores, 1, TimeUnit.SECONDS, workQueue);
    }

    private void fetchMonstersFromDatabase() {
        String tableName = "lnr_db";
        String whereClause = "TYPE like ?";
        String[] whereArgs = new String[]{"monster"};

        Cursor cursor = dbHelper.getReadableDatabase().query(tableName, dbCols, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ArrayList<Item> monsterInventory = new ArrayList<>();
            monsterInventory.add(new LootClass().randomLooter());
            placeNewMonster(
                    new Character(cursor.getInt(3), cursor.getString(2), null, cursor.getInt(8), monsterInventory)
                    , cursor.getDouble(6), cursor.getDouble(7));
            dbHelper.getWritableDatabase().delete(tableName, "ID=?", new String[]{"" + cursor.getInt(0)});
            cursor.moveToNext();
        }
    }

    private void fetchCachesFromDatabase() {
        String tableName = "lnr_db";
        String whereClause = "TYPE like ?";
        String[] whereArgs = new String[]{"cache"};

        Cursor cursor = dbHelper.getReadableDatabase().query(tableName, dbCols, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            placeNewCache(cursor.getDouble(6), cursor.getDouble(7));
            dbHelper.getWritableDatabase().delete(tableName, "ID=?", new String[]{"" + cursor.getInt(0)});
            cursor.moveToNext();
        }
    }

    public void storeMapObjectsInDatabase() {
        Iterator it;

        it = mapObjects.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry entry = (HashMap.Entry) it.next();
            ((MapObject) entry.getValue()).storeInDatabase();
            it.remove();
        }
    }

    public void startManager() {
        fetchMonstersFromDatabase();
        fetchCachesFromDatabase();

        /*
        // Draw a blue circle around the main player
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    map.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            map.getGoogleMap().addCircle(new CircleOptions()
                                .center(new LatLng(MainMap.getMainPlayer().getCharLocation().getLatitude(), MainMap.getMainPlayer().getCharLocation().getLongitude()))
                                .radius(tapRadius)
                                .strokeColor(Color.BLUE)
                                .fillColor(Color.argb(100, 50, 50, 50)));
                            }
                    });
                }
            }
        }.start();
        */

        Thread t = new Thread() {
            public void run() {
                while (true) {
                    try {
                        sleep(refreshTimer);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    map.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (numMonsters < maxNumMonsters) {
                                placeNewMonster();
                            }
                            if (numCaches < maxNumCaches) {
                                placeNewCache();
                            }
                        }
                    });
                }
            }
        };

        t.start();
    }

    private <E> Marker placeObjectOnMap(E object, String iconFileName) {
        if (MainMap.getMainPlayer().getCharLocation() == null) {
            return null;
        }

        LatLng objLatLng =
                new LatLng(MainMap.getMainPlayer().getCharLocation().getLatitude() + randomValWithinLoadRadius()
                        , MainMap.getMainPlayer().getCharLocation().getLongitude() + randomValWithinLoadRadius());

        Marker objMarker = map.getGoogleMap().addMarker(new MarkerOptions()
                .position(objLatLng)
                .title(object.toString())
                .icon(BitmapDescriptorFactory.fromAsset(iconFileName)));

        return objMarker;
    }

    private <E> Marker placeObjectOnMap(E object, LatLng objLatLng, String iconFileName) {
        Marker objMarker = map.getGoogleMap().addMarker(new MarkerOptions()
                .position(objLatLng)
                .title(object.toString())
                .icon(BitmapDescriptorFactory.fromAsset(iconFileName)));

        return objMarker;
    }

    public void placeNewMonster() {
        Character newMonster = new Character(20, "Zombie", null, 10, new ArrayList<Item>());
        Marker newMonsterLocation = placeObjectOnMap(newMonster, "halfbodycrawling.png");
        MonsterMapObject newMapObj = new MonsterMapObject(newMonster, newMonsterLocation);
        mapObjects.put(newMonsterLocation.hashCode(), newMapObj);
        if (newMonsterLocation != null && numMonsters < maxNumMonsters) {
            numMonsters++;
            newMapObj.randomWalk();
        }
        else {
            newMonsterLocation.remove();
            mapObjects.remove(newMonsterLocation);
        }
    }

    public void placeNewMonster(Character newMonster, double lat, double lng) {
        Marker newMonsterLocation = placeObjectOnMap(newMonster, new LatLng(lat, lng), "halfbodycrawling.png");
        MonsterMapObject newMapObj = new MonsterMapObject(newMonster, newMonsterLocation);
        mapObjects.put(newMonsterLocation.hashCode(), newMapObj);
        if (newMonsterLocation != null && numMonsters < maxNumMonsters) {
            numMonsters++;
            newMapObj.randomWalk();
        }
        else {
            newMonsterLocation.remove();
            mapObjects.remove(newMonsterLocation);
        }
    }

    public void placeNewCache() {
        LootClass newCache = new LootClass();
        Marker newCacheLocation = placeObjectOnMap(newCache, "lockedchest.png");
        CacheMapObject newMapObj = new CacheMapObject(newCache, newCacheLocation);
        mapObjects.put(newCacheLocation.hashCode(), newMapObj);

        // If the item cannot be placed, get rid of it
        if (!(newCacheLocation != null && numCaches < maxNumCaches)) {
            newCacheLocation.remove();
            mapObjects.remove(newCacheLocation.hashCode());
        }

        numCaches++;
    }

    public void placeNewCache(double lat, double lng) {
        LootClass newCache = new LootClass();
        Marker newCacheLocation = placeObjectOnMap(newCache, new LatLng(lat, lng), "lockedchest.png");
        CacheMapObject newMapObj = new CacheMapObject(newCache, newCacheLocation);
        mapObjects.put(newCacheLocation.hashCode(), newMapObj);

        // If the item cannot be placed, get rid of it
        if (!(newCacheLocation != null && numCaches < maxNumCaches)) {
            newCacheLocation.remove();
            mapObjects.remove(newCacheLocation.hashCode());
        }

        numCaches++;
    }

    public double randomValWithinLoadRadius() {
        return (new Random().nextDouble()) * loadRadius * (new Random().nextBoolean() ? 1 : -1);
    }

    public class MapObject <E> {
        public E mapObj;
        public Marker objMarker;

        public MapObject() {

        }

        public MapObject(E mapObj, Marker objMarker) {
            this.mapObj = mapObj;
            this.objMarker = objMarker;
        }

        public boolean handleMarkerClick() {
            return false;
        }

        public void storeInDatabase() {

        }

        public double distanceToPlayer() {
            double x1 = MainMap.getMainPlayer().getCharLocation().getLatitude();
            double y1 = MainMap.getMainPlayer().getCharLocation().getLongitude();
            double x2 = objMarker.getPosition().latitude;
            double y2 = objMarker.getPosition().longitude;
            return Math.sqrt(((x2-x1) * (x2-x1)) + ((y2-y1) * (y2-y1)));
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
                                    LatLng newPos = new LatLng(
                                            objMarker.getPosition().latitude + (0.000005 * (new Random().nextBoolean() ? 1 : -1))
                                            , objMarker.getPosition().longitude + (0.000005 * (new Random().nextBoolean() ? 1 : -1)));

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

        @Override
        public boolean handleMarkerClick() {
            fightMainPlayer();
            return true;
        }

        @Override
        public void storeInDatabase() {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("TYPE", "monster");
            values.put("NAME", mapObj.getCharName());
            values.put("HEALTH", mapObj.getHealth());
            values.put("LATITUDE", objMarker.getPosition().latitude);
            values.put("LONGITUDE", objMarker.getPosition().longitude);
            values.put("POWER", mapObj.getAttackDamage());

            db.insert("lnr_db", null, values);
        }

        public void fightMainPlayer() {
            if (distanceToPlayer() > tapRadius) {
                Context context = map.getApplicationContext();
                CharSequence text = "Object is out of range";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();
                return;
            }



            map.fightMonster(mapObj);
            objMarker.remove();
            mapObjects.remove(objMarker.hashCode());
            numMonsters--;
        }
    }

    private class CacheMapObject extends MapObject<LootClass> {
        public CacheMapObject(LootClass mapObj, Marker objMarker) {
            super(mapObj, objMarker);
        }

        @Override
        public boolean handleMarkerClick() {
            collect();
            return true;
        }

        @Override
        public void storeInDatabase() {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("TYPE", "cache");
            values.put("LATITUDE", objMarker.getPosition().latitude);
            values.put("LONGITUDE", objMarker.getPosition().longitude);

            db.insert("lnr_db", null, values);
        }

        public void collect() {
            if (distanceToPlayer() > tapRadius) {
                Context context = map.getApplicationContext();
                CharSequence text = "Object is out of range";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();
                return;
            }

            Item collectedItem = mapObj.randomLooter();
            MainMap.getMainPlayer().addItemToInventory(collectedItem);

            Context context = map.getApplicationContext();
            CharSequence text = "Collected a " + collectedItem.getName();
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, text, duration).show();

            objMarker.remove();
            mapObjects.remove(objMarker.hashCode());
            numCaches--;
        }
    }
}
