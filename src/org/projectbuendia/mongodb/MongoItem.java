package org.projectbuendia.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoException;
import org.projectbuendia.sqlite.SQLITEConnection;

/**
 * @author Pim de Witte
 */
public interface MongoItem {
	boolean canExecute();
	boolean execute(DB db) throws MongoException;
}
