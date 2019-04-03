package com.huawei;
public class Road{
	int id;
	int length;
	int speed;
	int channel;
	int form;
	int to;
	int isDuplex;
	
	public void Road(int id,int length,int speed,int channel,int form,int to,int isDuplex){
		this.id = id;
		this.length = length;
		this.speed = speed;
		this.channel = channel;
		this.form = form;
		this.to = to;
		this.isDuplex = isDuplex;
	}
	public int getId(){
		return id;
	}
	public int getLength(){
		return length;
	}
	public int getSpeed(){
		return speed;
	}
	public int getChannel(){
		return channel;
	}
	public int getFrom(){
		return form;
	}
	public int getTo(){
		return to;
	}
	public int getIsDuplex(){
		return isDuplex;
	}
}