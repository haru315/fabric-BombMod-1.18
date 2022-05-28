package net.fabricmc.bomb.explosion;

import java.util.ArrayList;
import java.util.TreeMap;

public class CircleMapGenerator {

    private TreeMap<Float, ArrayList<Mass>> circleMap;
    private Mass currentMass;
    private float size;

    public CircleMapGenerator(){
        this.circleMap = new TreeMap<Float, ArrayList<Mass>>();
        this.addCircleMap(0,0);
    }

    public void sizeUp(){
        // 近いマスを取得＆削除
        this.size = circleMap.firstKey();
        this.currentMass = pollFirstEntry(this.getSize());

        int x = this.currentMass.x;
        int y = this.currentMass.y;

        // 新しいマス
        int ix = 0;
        int iy = 0;
        if (y == 0){
            ix = x+1;
            this.addCircleMap(ix,0);
        }
        if (x > y){
            ix = x;
            iy = y+1;
            this.addCircleMap(ix,iy);
        }
    }

    public ArrayList<Mass> getMass(){
        ArrayList<Mass> al = new ArrayList<>();
        int ix = this.currentMass.x;
        int iy = this.currentMass.y;
        al.add(new Mass(ix,iy));
        al.add(new Mass(-ix,iy));
        al.add(new Mass(ix,-iy));
        al.add(new Mass(-ix,-iy));
        al.add(new Mass(iy,ix));
        al.add(new Mass(-iy,ix));
        al.add(new Mass(iy,-ix));
        al.add(new Mass(-iy,-ix));
        return al;
    }

    public float getSize() {
        return size;
    }

    // 追加
    private void addCircleMap(int x,int y){
        float key = (float)Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
        if (!circleMap.containsKey(key)) {
            circleMap.put(key, new ArrayList<Mass>());
        }
        circleMap.get(key).add(new Mass(x, y));
    }
    // 取得&削除
    private Mass pollFirstEntry(float key){
        int in = circleMap.get(key).size() - 1;
        Mass mx = circleMap.get(key).get(in);
        circleMap.get(key).remove(in);
        if(circleMap.get(key).isEmpty()){
            circleMap.remove(key);
        }
        return mx;
    }

    public static class Mass{
        public int x, y;
        public Mass(int x,int y){
            this.x = x;
            this.y = y;
        }
    }
}
