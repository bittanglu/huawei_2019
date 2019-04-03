package com.huawei;
import java.util.*;
import java.io.*;

public class Test{
	static final int INF = 10000;
	static int road_num = 0;
	static int car_num = 0;
	static int[][] init_graph;
    static int[][] graph;
	static int[][] map;
	static int[][] max_w;
	static LoadData data = new LoadData();
   
	public static String Dijsktra(int[][]graph,int start,int end){
        int length = graph.length;
		int[][] weight = new int[length][length];
        int[] shortPath = new int[length];
        shortPath[0] = 0;
        String path[] = new String[length];
		start = data.dict_cross.get(start);
		end = data.dict_cross.get(end);
		//deepcopy
		for(int i = 1;i < length;i++)
			for(int j = 1;j < length;j++)
				weight[i][j] = graph[i][j];
        for(int i=0;i<length;i++){
            path[i] = start+","+i;
        }
        int visited[] = new int[length];
        visited[0] = 1;
        for(int count = 1;count<length;count++){
            int k=-1;
            int dmin = INF; 
            for(int i=0;i<length;i++){
                if(visited[i]==0 && weight[start][i] < dmin){
                    dmin = weight[start][i];
                    k=i;
                }
            }
			if(k == -1) continue;
            shortPath[k] = dmin;
            visited[k] = 1;
            for(int i=0;i<length;i++){
                if(visited[i]==0 && weight[start][k]+weight[k][i] < weight[start][i] && weight[k][i] < max_w[k][i]){
                    weight[start][i] = weight[start][k]+weight[k][i];
                    path[i] = path[k]+","+i;
                }
            }
        }
		String st = path[end] + "-"+ weight[start][end];
        return st;
    }
	public static void initGraph(Vector<Road> roadVec){
		int len = roadVec.size();
		int i,j;
		for(i = 0;i < len;i++){
			Road temp = roadVec.get(i);
			int x = temp.getFrom();
			int y = temp.getTo();
			x = data.dict_cross.get(x);
			y = data.dict_cross.get(y);
			int length = temp.getLength() ;
			int id = temp.getId();
			if (temp.getIsDuplex() == 1){
				graph[x][y] = length;
				graph[y][x] = length;
				init_graph[x][y] = length;
				init_graph[y][x] = length;
				
				max_w[x][y] = length * 13 / 4 + length * temp.getChannel() * 3;
				max_w[y][x] = length * 13 / 4 + length * temp.getChannel() * 3;
				//max_w[x][y] = length + 1;
				//max_w[y][x] = length + 1;
				map[x][y] = id;
				map[y][x] = id;
			}else{
				graph[x][y] = length;
				init_graph[x][y] = length;
				max_w[x][y] = length * 13 /4 + length * temp.getChannel() * 3;
				//max_w[x][y] = length + 1;
				map[x][y] = id;
			}
		}
	}
	public static void IncreaseWeigth(String[] path){
		int len = path.length;
		for(int i = 0;i < len - 1;i++){
			int x = Integer.parseInt(path[i]);
			int y = Integer.parseInt(path[i+1]);
			graph[x][y] = graph[x][y] + 1;
		}
	}
	public static void DecreaseWeigth(ArrayList<String>  path_list){
		int list_len = path_list.size();
		for(int j = 0;j<list_len;j++){
			String[] path = path_list.get(j).split(",");
			int len = path.length;
			for(int i = 0;i < len - 1;i++){
				int x = Integer.parseInt(path[i]);
				int y = Integer.parseInt(path[i+1]);
				if(graph[x][y] < INF && graph[x][y] > init_graph[x][y])
				graph[x][y] = graph[x][y] - 1;
			}
		}
			
	}
	public static int getLen(String[] path){
		int path_len = 0;
		int len = path.length;
		for(int i = 0;i < len - 1;i++){
			int x = Integer.parseInt(path[i]);
			int y = Integer.parseInt(path[i+1]);
			path_len += init_graph[x][y];
		}
		return path_len;
	}
	public static boolean isConflict(String[] path){
		int len = path.length;
		for(int i = 0;i < len - 1;i++){
			int x = Integer.parseInt(path[i]);
			int y = Integer.parseInt(path[i+1]);
			if(graph[x][y] > max_w[x][y]) {
				//System.out.println("Conflict!");
				return true;
			}
		}
		return false;
	}
	public static void cal(String carPath,String roadPath,String crossPath,String answerPath) {
		try{
			data.loadRoadInfo(roadPath);
			data.loadCarInfo(carPath);
			data.loadCrossInfo(crossPath);
		}catch(Exception e){
			e.printStackTrace(); 
		}
		int len = data.crossVec.size();
		road_num = data.roadVec.size();
		car_num = data.carVec.size();
		graph = new int[len+1][len+1];
		map = new int[len+1][len+1];
		max_w = new int[len+1][len+1];
		init_graph = new int[len+1][len+1];
		int i,j;
		for(i= 1;i <= len;i++)
			for (j = 1;j<=len;j++){
				if(i==j) graph[i][j] = 0;
				else graph[i][j] = INF;
			}
		initGraph(data.roadVec);
		//initMin();
		int carNum = data.carVec.size();
		System.out.println("Cars Number:" + carNum);
        int start,end;
		ArrayList<String> answer = new ArrayList<String>();
		int time = 1;
		ArrayList<Integer> remain_cars_index = new ArrayList<Integer>();
		HashMap<Integer,ArrayList<String>> dict = new HashMap<Integer,ArrayList<String>>();
		int break_num = 0;
		int Conflict_Factor = 3;
		for(i = 0;i < carNum;i++){
			Car temp = data.carVec.get(i);
			start = temp.getFrom();
			end = temp.getTo();
			String st = Dijsktra(graph,start,end);
			String ans[] = st.split("-");
			String path[] = ans[0].split(",");
			int spend_time = getLen(path)/temp.getSpeed();
			if(isConflict(path)){
				break_num++;
				remain_cars_index.add(i);
				if(break_num > 2500){
					for(int k = i+1;k < carNum;k++)
						remain_cars_index.add(k);
					break;
				}
			}else{
				IncreaseWeigth(path);
				//data.carVec.get(i).setPlanTime(temp.getPlanTime() + time);
				data.carVec.get(i).setRote(path,map);
				int Arrival_time = getLen(path)/temp.getSpeed() + temp.getPlanTime();
				if(dict.get(Arrival_time) == null){
					ArrayList<String> st_list = new ArrayList<String>();
					st_list.add(ans[0]);
					dict.put(Arrival_time,st_list);
				}else{
					ArrayList<String> st_list = dict.get(Arrival_time);
					st_list.add(ans[0]);
					dict.put(Arrival_time,st_list);
				}
				answer.add(data.carVec.get(i).getAnswer());
			}
		}
		System.out.println("Cars not remain:" + answer.size());
		int dt = 1;
		while(!remain_cars_index.isEmpty()){
			ArrayList<String> st_decrease = new ArrayList<String>();
			dt = 0;
			while(st_decrease.size() < 50){
				dt++;
				if(dict.get(time + dt) != null)
					st_decrease.addAll(dict.get(time + dt));
				if(remain_cars_index.size() < 100)
					break;
			}
			/**
			for(int k = time; k < time + dt;k++){
				if(dict.get(k) != null) {
					if (st_decrease.length() > 0) st_decrease = st_decrease + "," + dict.get(k);
					else st_decrease = dict.get(k);
				}
			}
			***/
			time += dt;
			break_num = 0;
			if(st_decrease.size() > 0){
				DecreaseWeigth(st_decrease);
				ArrayList<Integer> list = new ArrayList<Integer>();
				for(i = 0;i < remain_cars_index.size();i++){
					int index = remain_cars_index.get(i);
					Car temp = data.carVec.get(index);
					start = temp.getFrom();
					end = temp.getTo();
					String st = Dijsktra(graph,start,end);
					String ans[] = st.split("-");
					String path[] = ans[0].split(",");
					int spend_time = getLen(path)/temp.getSpeed();
					if(isConflict(path)){
						break_num++;
						list.add(index);
						if(break_num > 1000){
							for(int k = i+1;k < remain_cars_index.size();k++)
								list.add(remain_cars_index.get(k));
							break_num = 0;
							break;
						}
					}else{
						IncreaseWeigth(path);
						int start_time = Math.max(temp.getPlanTime(),time);
						data.carVec.get(index).setPlanTime(start_time);
						data.carVec.get(index).setRote(path,map);
						int Arrival_time = spend_time + start_time;
						if(dict.get(Arrival_time) == null){
							ArrayList<String> st_list = new ArrayList<String>();
							st_list.add(ans[0]);
							dict.put(Arrival_time,st_list);
						}else{
							ArrayList<String> st_list = dict.get(Arrival_time);
							st_list.add(ans[0]);
							dict.put(Arrival_time,st_list);
						}
						String st_ans = data.carVec.get(index).getAnswer();
						answer.add(st_ans);
					}
				}
				System.out.println("Time:" + time);
				System.out.println("Cars remain:" + list.size());
				remain_cars_index = list;
			}else{
				continue;
			}
		}
		try{
			FileWriter writer = new FileWriter(answerPath,false);
			for(i = 0;i < answer.size();i++){
				 writer.write(answer.get(i));
			}
			writer.flush();
	        writer.close();
		}catch (Exception e){
            e.printStackTrace();  
        }
	}
	public static void initMin(){
		int carNum = data.carVec.size();
		for(int i = 0;i < carNum;i++){
			Car temp = data.carVec.get(i);
			int start = temp.getFrom();
			int end = temp.getTo();
			String st = Dijsktra(graph,start,end);
			String ans[] = st.split("-");
			String path[] = ans[0].split(",");
			//data.carVec.get(i).setPlanTime(temp.getPlanTime() + time);
			int spend_time = getLen(path)/temp.getSpeed();
			data.carVec.get(i).setMinSpend(spend_time);
		}
	}
}