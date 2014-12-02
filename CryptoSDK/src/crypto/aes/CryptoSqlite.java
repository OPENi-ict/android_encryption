package crypto.aes;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Device's database management
 * @author CGI
 *
 */
public class CryptoSqlite extends SQLiteOpenHelper{
	private static final int VERSION_BASEDATOS = 1;
	
	public CryptoSqlite(Context context, String name,
			CursorFactory factory, int version) {
		super(context,name, factory, VERSION_BASEDATOS);
		// TODO Auto-generated constructor stub
	}
	
    /**
     *  Database creation
     */
	@Override
	public void onCreate(SQLiteDatabase db) {		
		
		db.execSQL("CREATE TABLE IF NOT EXISTS  User(idUserCloud INT PRIMARY KEY, object_key TEXT)");
	}

	/**
     * Update of the database
     */
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
	
		 db.execSQL("DROP TABLE IF EXISTS User");
		 	     
	     db.execSQL("CREATE TABLE  User(idUserCloud INT PRIMARY KEY, object_key TEXT)");
		
	}
	
	/**
	 * Gets the user's key
	 * @param idUserCloud user identifier in the database
	 * @param db
	 * @return  String secrectkey
	 */
	public String findKey(int idUserCloud,SQLiteDatabase db) {
	
		Cursor c = db.rawQuery("SELECT * FROM User where idUserCloud ='"+idUserCloud+"'",null);
		c.moveToFirst();
		String key = null;
		if(c.getCount()>0){
			key=c.getString(c.getColumnIndex("object_key"));
		}
		return key;
		
	}
	/**
	 * Storing key into the device's database 
	 * @param secrectkey
	 * @param idUserCloud
	 * @param db
	 */
	public void insertInd(String secrectkey,int idUserCloud,SQLiteDatabase db) {
		
		Cursor c = db.rawQuery("SELECT * FROM User where idUserCloud='"+idUserCloud+"'",null);
		c.moveToFirst();
		String key = null;
		if(c.getCount()>0){
			Log.d("deb","entrar");
			key=c.getString(c.getColumnIndex("idUserCloud"));
			db.execSQL("UPDATE User SET object_key='"+secrectkey+"' WHERE idUserCloud='"+key+"'");
			
		}else{
		
		db.execSQL("insert into User (idUserCloud,object_key )"
				+ "values('"+idUserCloud+"','"+secrectkey+"')");
		}
	}
	/**
	 * Inserts user identifier into the local database
	 * @param idUserCloud
	 * @param db
	 */
	public void registrerUser(int idUserCloud,SQLiteDatabase db){
	
		db.execSQL("insert into User (idUserCloud )"
				+ "values('"+idUserCloud+"')");
	}
	
	/**
	 * 
	 * @param password
	 * @param db
	 * @return true if "password" exists in the database
	 */
    public boolean findPassword(String password ,SQLiteDatabase db){
    	boolean existePass=true;
    	Cursor c = db.rawQuery("SELECT * FROM User where clave='"+password+"'",null);
    	c.moveToFirst();
    	if(c.getCount()==0)
        existePass=false; 	
    	
    	return  existePass;
    	
    }
    /***
     * 
     * @param DerivedKey
     * @param db
     * @return secretKey as String
     */
    public String findDerivedKey(String DerivedKey,SQLiteDatabase db) {
    	
		Cursor c = db.rawQuery("SELECT * FROM User where object_key ='"+DerivedKey+"'",null);
		c.moveToFirst();
		String key = null;
		if(c.getCount()>0){
			key=c.getString(c.getColumnIndex("object_key"));
		}
		
/*		Cursor c1 = db.rawQuery("SELECT * FROM User ",null);
		c1.moveToFirst();
		String key2=c1.getString(c.getColumnIndex("object_key"));*/
		return key;
		
	}
   
}
