package net.fabricmc.bomb.explosion;

import java.util.ArrayList;
import java.util.TreeMap;

public class SphereMapGenerator {

    private TreeMap<Float, ArrayList<Mass>> sphereMap;
    private Mass currentMass;
    private float size;

    public SphereMapGenerator(){
        this.sphereMap = new TreeMap<Float, ArrayList<Mass>>();
        this.addSphereMap(0,0,0);
    }

    public void sizeUp(){
        // 近いマスを取得＆削除
        this.size = sphereMap.firstKey();
        this.currentMass = pollFirstEntry(this.getSize());

        int x = this.currentMass.x;
        int y = this.currentMass.y;
        int z = this.currentMass.z;

        // 新しいマス
        if (y == 0 && z == 0){
            this.addSphereMap(x+1,0,0);
        }
        if (y == 0 && x > z){
            this.addSphereMap(x,0,z+1);
        }
        if (z > y){
            this.addSphereMap(x,y+1,z);
        }
    }

    // 最近値マスを48個出力する
    public ArrayList<Mass> getMassList(){
        ArrayList<Mass> al = new ArrayList<>();
        int ix = this.currentMass.x;
        int iy = this.currentMass.y;
        int iz = this.currentMass.z;
        this.AuxiliaryF(al ,ix, iy, iz);
        this.AuxiliaryF(al ,ix, iz, iy);
        this.AuxiliaryF(al ,iy, ix, iz);
        this.AuxiliaryF(al ,iy, iz, ix);
        this.AuxiliaryF(al ,iz, iy, ix);
        this.AuxiliaryF(al ,iz, ix, iy);
        return al;
    }
    // 補助
    private void AuxiliaryF(ArrayList<Mass> al ,int x,int y,int z){
        al.add(new Mass(x, y, z));
        al.add(new Mass(-x, y, z));
        al.add(new Mass(x, -y, z));
        al.add(new Mass(x, y, -z));
        al.add(new Mass(-x, -y, z));
        al.add(new Mass(x, -y, -z));
        al.add(new Mass(-x, y, -z));
        al.add(new Mass(-x, -y, -z));
    }


    public float getSize() {
        return size;
    }

    // 追加
    private void addSphereMap(int x,int y,int z){
        float key = (float)Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
        if (!sphereMap.containsKey(key)) {
            sphereMap.put(key, new ArrayList<>());
        }
        sphereMap.get(key).add(new Mass(x, y, z));
    }
    // 取得&削除
    private Mass pollFirstEntry(float key){
        int in = sphereMap.get(key).size() - 1;
        Mass mx = sphereMap.get(key).get(in);
        sphereMap.get(key).remove(in);
        if(sphereMap.get(key).isEmpty()){
            sphereMap.remove(key);
        }
        return mx;
    }

    public static class Mass{
        public int x, y, z;
        public Mass(int x,int y,int z){
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
