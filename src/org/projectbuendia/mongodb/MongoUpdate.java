package org.projectbuendia.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import org.projectbuendia.sqlite.Item;
import org.projectbuendia.sqlite.SQLITEConnection;

/**
 * @author Pim de Witte
 */
public abstract class MongoUpdate implements MongoItem {
	private BasicDBObject obj;
    private String collection;

	protected MongoUpdate(String collection, BasicDBObject obj) {
		this.obj = obj;
        this.collection = collection;
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public final boolean execute(DB connection) {
        connection.command(obj);
		return true;
	}
}
