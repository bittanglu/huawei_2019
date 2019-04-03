package com.huawei;
import java.util.*;

public class Car{
	int id;
	int from;
	int to;
	int speed;
	int planTime;
	Position position;
	int state;
	int min_spend = Integer.MAX_VALUE;
	ArrayList<Integer> rote = new ArrayList<Integer>();
	Car(){
	}
 	Car(int id,int from,int to,int speed,int planTime){
		this.id = id;
		this.from = from;
		this.to = to;
		this.speed = speed;
		this.planTime = planTime;
	}
	public int getId(){
		return id;
	}
	public int getFrom(){
		return from;
	}
	public int getTo(){
		return to;
	}
	public int getSpeed(){
		return speed;
	}
	public int getPlanTime(){
		return planTime;
	}
	public void setPlanTime(int time){
		planTime = time;
	}
	public void setPosition(Position position){
		this.position = position;
	}
	public Position getPosition(){
		return position;
	}
	public void setState(int state){
		this.state = state;
	}
	public int getState(){
		return state;
	}
	public void setRote(String[] path,int[][] map){
		rote.clear();
		for(int i = 0;i < path.length-1;i++){
			rote.add(map[Integer.parseInt(path[i])][Integer.parseInt(path[i+1])]);
		}
	}
	public void setMinSpend(int time){
		this.min_spend = time;
	}
	public int getMinSpend(){
		return min_spend;
	}
	public ArrayList<Integer> getRote(){
		return rote;
	}
	public String getAnswer(){
		String st = "(" + id + "," + planTime;
		int len = rote.size();
		for(int i = 0;i < len;i++){
			st += "," + rote.get(i);
		}
		st += ")" + "\r\n";
		return st;
	}
}