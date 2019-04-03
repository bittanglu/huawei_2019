package com.huawei;

import java.util.*;
import java.io.*;

public class LoadData{

    Vector<Road> roadVec = new Vector<Road>();
    Vector<Car> carVec = new Vector<Car>();
    Vector<Cross> crossVec = new Vector<Cross>();
	HashMap<Integer,Integer> dict_cross = new HashMap<Integer,Integer>();
	int cross_num = 1;
    public void loadRoadInfo(String filename) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        reader.readLine();
		String line;
        while((line = reader.readLine())!=null){   
			line = line.replaceAll(" ", "");
            String item[] = line.substring(1,line.length()-1).split(",");
            Road roadInfo = new Road();   
            roadInfo.id = Integer.parseInt(item[0]); //id
            roadInfo.length = Integer.parseInt(item[1]); //length
            roadInfo.speed = Integer.parseInt(item[2]); //speed
            roadInfo.channel = Integer.parseInt(item[3]); //channel
            roadInfo.form = Integer.parseInt(item[4]); 
            roadInfo.to = Integer.parseInt(item[5]); 
            roadInfo.isDuplex = Integer.parseInt(item[6]);             
            roadVec.add(roadInfo);
		}
	}

    public void loadCarInfo(String filename) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        reader.readLine();
		String line;
        while((line=reader.readLine())!=null){
			line = line.replaceAll(" ", "");
            String item[] = line.substring(1,line.length()-1).split(",");
            Car carInfo = new Car();   
            carInfo.id = Integer.parseInt(item[0]); 
            carInfo.from = Integer.parseInt(item[1]); 
            carInfo.to = Integer.parseInt(item[2]); 
            carInfo.speed = Integer.parseInt(item[3]);
            carInfo.planTime = Integer.parseInt(item[4]);              
            carVec.add(carInfo);
		}
		Comparator<Car> ct = new MyComparator();
        Collections.sort(carVec,ct);
	}
	class MyComparator implements Comparator<Car> {
		public int compare(Car car1, Car car2) {
			if(car1.getPlanTime() > car2.getPlanTime())
				return 1;
			else if(car1.getPlanTime() < car2.getPlanTime())
				return -1;
			else 
				return 0;
		}
	}

    public void loadCrossInfo(String filename) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        reader.readLine();
		String line;
        while((line=reader.readLine())!=null){    
			line = line = line.replaceAll(" ", "");
            String item[] = line.substring(1,line.length()-1).split(",");
            int[] roadId = {Integer.parseInt(item[1]),Integer.parseInt(item[2]),Integer.parseInt(item[3]),Integer.parseInt(item[4])};
			int id = Integer.parseInt(item[0]);
            Cross crossInfo = new Cross(cross_num,id,roadId); 
			dict_cross.put(id,cross_num);
			cross_num++;            
            crossVec.add(crossInfo);
		}
	}

    public void clearAllVec(){
        crossVec.clear();
        carVec.clear();
        roadVec.clear();
		cross_num = 1;
    }
}