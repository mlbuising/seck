package com.pcwerk.seck.search;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseImpl implements DatabaseDao {
	private static Configuration conf = null;

	static {
		conf = HBaseConfiguration.create();
	}

	public static void creatTable(String tableName, String[] familys)
			throws Exception {

		HBaseAdmin admin = new HBaseAdmin(conf);

		if (admin.tableExists(tableName))
			System.out.println("table already exists!");
		else {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);

			for (int i = 0; i < familys.length; i++)
				tableDesc.addFamily(new HColumnDescriptor(familys[i]));

			admin.createTable(tableDesc);

			System.out.println("create table " + tableName + " ok.");
		}
	}

	public void put(String tableName, String rowKey, String family,
			String qualifier, String value) throws Exception {
		try {
			HTable table = new HTable(conf, tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
					Bytes.toBytes(value));
			table.put(put);
			System.out.println("insert recored " + rowKey + " to table " + tableName
					+ " ok.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void put(String url, String body, List<String> classifications) {
		// TODO Auto-generated method stub

	}

	public void update(String url, String body) {
		// TODO Auto-generated method stub

	}

	public void update(String url, List<String> classifications) {
		// TODO Auto-generated method stub

	}

	public void update(String url, String body, List<String> classifications) {
		// TODO Auto-generated method stub

	}

	public void delete(String url) {
		// TODO Auto-generated method stub

	}

	public String getBody(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getClassications(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public void put(String url, String body) {
		// TODO Auto-generated method stub
		
	}

}
