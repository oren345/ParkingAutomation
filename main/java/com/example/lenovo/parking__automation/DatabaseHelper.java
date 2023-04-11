package com.example.lenovo.parking__automation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// SQLiteOpenHelper sinifindan faydalanan Veritaban, ve Tablo Olusturmada Kullanacagimiz
// yardimci sinif.

    public class DatabaseHelper extends SQLiteOpenHelper {
    /*
        ID  Name  Surname  Marks
        1   Mark  Taylor   78
        2   Tom   Smith    89
        3   John  Mal      97
        4   Max   Nickson  90

        Yukardaki gibi 4 sutundan olusan bir tablo yapilacak.

     */

        // Veritabani ismi, tablo ismi ve sutun isimlerini belirledigimiz degiskenler.

        public static final String DATABASE_NAME = "Otopark.db";
        public static final String TABLE_NAME = "otopark_table";
        public static final String Col_1 = "ID";
        public static final String Col_2 = "PLAKA";
        public static final String Col_3 = "LOGİNHOUR";
        public static final String Col_4 = "EXİTHOUR";

        // DatabaseHelper yapilandiricisi (constructor)
        // MainActivity'de DatabaseHelper nesnesi olustururken bu metodu kullandik.
        public DatabaseHelper(Context context) {
            //super metodu SQLiteOpenHelper'dan odunc aldigimiz veritabanı oluusturmak icin
            //kullanilan bir metod.
            //parametreler sırasiyla context, veritabani ismi, null ve versiyon numarasi alacak.
            super(context, DATABASE_NAME, null, 1);
        }

        // Veritabani ilk yaratildiginda cagirilan metod.
        @Override
        public void onCreate(SQLiteDatabase db) {
            // execSQL metodu, parantez icinde yer alan sql ifadesini calistirir.
            // TABLE_NAME adindaki tabloyu yaratan sql ifadesi
            // AUTOINCREMENT ozelligi olan id sutunundaki degerler her yeni bilgi girisinde
            // otomatik olarak 1'den baslayarak birer birer artar.
            db.execSQL("create table "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, PLAKA STRİNG" +
                    ", LOGİNHOUR INTEGER, EXİTHOUR INTEGER)");
        }

        // Veritabaninda guncelleme yapilacaginda cagirilmasi gereken metod.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
        }

        // Tabloya bilgi girmek icin hazirlanan metod.
        public boolean insertData(String plaka, String loginhour, String exithour){
            // getWritableDatabase metodu okuma/yazma islemleri icin veritabanini acar
            SQLiteDatabase db = this.getWritableDatabase();

            // ContentValues sınıfı nesnesine tablo'daki sutun sirasina uyacak sekilde
            // yukarıdaki uc parametreyi yerlestiriyoruz.
            ContentValues contentValues = new ContentValues();
            contentValues.put(Col_2, plaka);
            contentValues.put(Col_3, loginhour);
            contentValues.put(Col_4, exithour);

            // contentValues'a yerlestirme islemi bitince artik SQLiteDatabase sinifina ait
            // insert metoduyla veritabanina veri giriyoruz.
            // long result degiskenine yapilan atamanin nedeni insert metodunun long turunde
            // donus yapmasidir. Eger -1 dondururse hata olmustur, dolayısıyla false donduruyoruz.
            long result = db.insert(TABLE_NAME,null,contentValues);
            if (result == -1)
                return false;
            else
                return true;
        }

        // Veritabanindaki TABLE_NAME adli tablodaki verileri cekmek icin kullandigimiz metod.
        public Cursor getAllData(){
            SQLiteDatabase db = this.getWritableDatabase();
            // Cursor sinifi bir tablodaki bilgileri satir satir tutabilen bir veri seti olusturmaya
            // yarar.
            // rawQuery metoduyla parantez icindeki sql ifadeyi calistirilir.
            Cursor result = db.rawQuery("select * from "+TABLE_NAME,null);
            // result adindaki Cursor nesnesine tablomuzdaki verileri yerlestirdik.
            return result;
        }

        // Tablodaki id, name, surname ve marks degerlerini guncellemek icin kullandigimiz metod.
        public boolean updateData(String id, String plaka, int loginhour, int exithour){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Col_1, id);
            contentValues.put(Col_2, plaka);
            contentValues.put(Col_3, loginhour);
            contentValues.put(Col_4, exithour);
            // update metodu parametre olarak sirasiyla, tablo ismi, contentValues, sql'de
            // where ifadesine karsilik olarak gelecek bir string, where ifadesindeki ? isaretine
            // karsilik olarak gelecek String arrayi alır.
            // asagidaki ornekte ? kismina, yukardaki updateData parametresindeki id
            // degiskenini koyduk.
            db.update(TABLE_NAME, contentValues, "ID = ?",new String[]{id});
            return true;
        }

        // Tablodan veri silmek icin kullandigimiz metod. Parametre olarak id alip o satiri siliyoruz.
        // Yukaridaki metoddan farkli olarak contentValues'a ihtiyacimiz yok.
        public Integer deleteData(String id){
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_NAME,"ID = ?", new String[]{id});
        }
    }


