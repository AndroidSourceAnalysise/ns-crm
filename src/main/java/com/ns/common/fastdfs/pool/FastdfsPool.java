package com.ns.common.fastdfs.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.common.MyException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @project: fastdfs-client-pool
 * @Title: FastdfsPool
 * @Package: me.windpace.fdfs.pool
 * @author: liuwei
 * @email: waylink@163.com	
 * @date: 2016年1月18日
 * @description: //TODO
 * @version:
 */
public class FastdfsPool extends Pool<FastdfsClient> {

	public FastdfsPool(GenericObjectPoolConfig poolConfig, String confPath ,Integer objMaxActive) throws FileNotFoundException, IOException, MyException {
		super(poolConfig, new FastdfsPooledObjectFactory(confPath, objMaxActive));
		
	}

	public FastdfsPool(GenericObjectPoolConfig poolConfig,Integer objMaxActive) throws IOException, MyException {
		super(poolConfig, new FastdfsPooledObjectFactory(objMaxActive));

	}

	public void returnBrokenResource(final FastdfsClient resource) {
		if (resource != null) {
			returnBrokenResourceObject(resource);
		}
	}

	public void returnResource(final FastdfsClient resource) {
		if (resource != null) {
			returnResourceObject(resource);
		}
	}

}
