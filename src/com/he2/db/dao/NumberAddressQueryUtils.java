package com.he2.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryUtils {

	private static String path = "data/data/com.he2.mobilesafe/files/address.db";

	public static String queryNumber(String number) {
		String address = number;
		// �����ݿ�
		SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READWRITE);
		/*
		 * ������ֻ������������ʽ ^
		 * ƥ�������ַ����Ŀ�ʼλ�á����������RegExp�����Multiline���ԣ�^Ҳƥ�䡰\n����\r��֮���λ�á� [xyz]
		 * �ַ����ϡ�ƥ��������������һ���ַ������磬��[abc]������ƥ�䡰plain���еġ�a���� \d �����ַ�ƥ�䡣��Ч�� [0-9]��
		 * {n} n�ǷǸ�����������ƥ�� n �Ρ����磬��o{2}���롰Bob���еġ�o����ƥ�䣬���롰food���е�������o��ƥ�䡣 $
		 * ƥ�������ַ�����β��λ�á���������� RegExp ����� Multiline ���ԣ�$ �����롰\n����\r��֮ǰ��λ��ƥ�䡣
		 */
		if (number.matches("^1[34568]\\d{9}$")) {
			// ���ƥ�䣬��Ϊ�ֻ�����
			// �������ݿ��ѯ,rawQueryΪ��ѯ�����漰�޸�
			Cursor cursor = database
					.rawQuery(
							"select location from data2 where id = (select outkey from data1 where id = ?)",
							new String[] { number.substring(0, 7) });
			while (cursor.moveToNext()) {
				String location = cursor.getString(0);
				address = location;
			}
			cursor.close();
		} else {
			// �������ֻ�����
			switch (number.length()) {
			case 3:
				// 110
				address = "�˾�����";
				break;
			case 4:
				// 5554
				address = "ģ����";
				break;
			case 5:
				// 10086
				address = "�ͷ��绰";
				break;
			case 7:
				//
				address = "���غ���";
				break;

			case 8:
				address = "���غ���";
				break;

			default:
				// ��;�绰
				if (number.length() > 10 && number.startsWith("0")) {
					// 010-59790386
					Cursor cursor = database.rawQuery(
							"select location from data2 where area = ?",
							new String[] { number.substring(1, 3) });

					while (cursor.moveToNext()) {
						String location = cursor.getString(0);
						address = location.substring(0, location.length() - 2);
					}
					// 0855-59790386
					cursor = database.rawQuery(
							"select location from data2 where area = ?",
							new String[] { number.substring(1, 4) });
					while (cursor.moveToNext()) {
						String location = cursor.getString(0);
						address = location.substring(0, location.length() - 2);
					}
					cursor.close();
				}
				break;
			}
		}
		return address;
	}

	public NumberAddressQueryUtils() {
		// TODO Auto-generated constructor stub
	}

}
