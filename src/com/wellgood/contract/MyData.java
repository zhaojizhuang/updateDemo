package com.wellgood.contract;

import com.wellgood.application.APP;

/**ȫ�ֱ�����������**/
public class MyData {
	/**�洢�û���������**/
	public static void saveName(String name){
		APP.app.setData("usr_name",name );
	}
	/**ȡ��������û���**/
	public static String getName(){
		String name =APP.app.getData("usr_name");
		return name;
	}
	/**�洢�û���������**/
	public static void savePassword(String password){
		APP.app.setData("usr_password",password );
	}
	/**ȡ��������û���**/
	public static String getPassword(){
		String name =APP.app.getData("usr_password");
		return name;
	}
	
	
	
	/**�洢��ס�˺�״̬������**/
	public static void saveRememberFlag(Boolean isRemember){
		String temp=Boolean.toString(isRemember);
		APP.app.setData("isRemember",temp );
	}
	/**ȡ�ü�ס�˺�״̬ **/
	public static Boolean getRememberFlag(){
		String temp=APP.app.getData("isRemember");
		Boolean isRemeber=Boolean.valueOf(temp).booleanValue();
		return isRemeber;
	}
}
