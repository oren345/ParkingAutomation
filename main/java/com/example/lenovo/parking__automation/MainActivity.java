package com.example.lenovo.parking__automation;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.parking__automation.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb; // DatabaseHelper nesnemiz

    // EditText ve Button degiskenlerimiz
    EditText editPlaka, editLoginhour, editExithour, editTextID;
    Button btnAddData, btnViweAll, textucret;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);// Context olarak this girerek myDb nesnemizi doldurduk.

        // Arayuzde olusturdugumuz editText ve Buttonlarin referanslarini aldik.
        editPlaka = (EditText)findViewById(R.id.plaka);
        editLoginhour = (EditText)findViewById(R.id.loginhour);
        editExithour = (EditText)findViewById(R.id.exithour);
        btnAddData = (Button)findViewById(R.id.button);
        btnViweAll = (Button)findViewById(R.id.button2);
        editTextID = (EditText)findViewById(R.id.id);


        // Asagida olusturulan metodlar onCreate'de bir kere cagirilmaz ise calismazlar.
        AddData();
        viewAll();
    }


    // Arayuzdeki editText'lere girilen degerlerin Add Data buttonuna tıklanmasiyle
    // veritabanina eklenmesini saglayan metod.
    public void AddData(){
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Hatirlanacagi uzere DatabaseHelper sinifinda yarattigimiz
                        // insertData metodu boolean donduruyor ve 3 tane parametre aliyordu:
                        // name, surname, marks.
                        boolean isInserted = myDb.insertData(editPlaka.getText().toString(),
                                editLoginhour.getText().toString(),
                                editExithour.getText().toString());
                        // Eger basarili bir ekleme yapilirsa isInserted degiskeni true, aksi
                        // takdirde false degerini alir.

                        // True - false durumuna gore bildirim olusturduk.
                        if(isInserted == true) {
                            Toast.makeText(MainActivity.this, "Data Inserted",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Data not Inserted",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
    }

    // Veritabani tablosundaki degerleri kullanıcıya gostermek icin hazirlanan View All
    // button'una basilinca calisacak metod.
    public void viewAll(){
        btnViweAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // res adindaki Cursor tipi degiskene DatabaseHelper sinifinda
                        // olusturdugumuz getAllData metodu ile veriyi yerlestirdik.
                        Cursor res = myDb.getAllData();

                        // getCount() metodu kac tane satır varsa onu gosterir. 0 ise hata
                        // mesaji veriyoruz.
                        if(res.getCount()==0){
                            // asagida olusturdugumuz showMessage metodunu cagiriyoruz.
                            // Baslik "Error", mesaj icerigi ise "Nothing Found"
                            showMessage("Error","Nothing found");
                            return;
                        }

                        // StringBuffer sinifi String sinifina benzer fakat thread-safe ve mutability
                        // gibi farkli ozellikler tasimasi sebebiyle bazi durumlarda tercih edilir.
                        StringBuffer buffer = new StringBuffer();

                        // Cursor sinifina ait moveToNext() metodu res Cursor'undaki satirlari tek
                        // tek dolasmaya yarar. Son satira gelene kadar true dondurur.
                        while(res.moveToNext()){
                            // StringBuffer sinifina ait append metodu buffer'a asagida yazilanlari
                            // ekler.
                            // Cursor sinifina ait getString(int i) metodu, i degerine gore
                            // hangi sutundan deger getirecegini belirleyip bu degeri donduur.
                            buffer.append("Id :"+res.getString(0)+"\n");
                            buffer.append("plaka :"+res.getString(1)+"\n");
                            buffer.append("loginHour :"+res.getString(2)+"\n");
                            buffer.append("exitHour :"+res.getString(3)+"\n\n");
                        }

                        // buffer'daki bilgiyi dondurur.
                        showMessage("Data", buffer.toString());
                    }
                }
        );
    }


    // AlertDialog ile mesaj goruntuleme metodu. Parametre olarak baslik ve mesaj istiyoruz.
    public void showMessage(String title, String message){
        // AlertDialog.Builder sinifi ile kullaniciya Toast'dan daha farkli bir tipde mesaj
        // gonderebiliyoruz.
        // Asagidaki sekilde nesneyi olusturduk.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Nesnenin ozelliklerini belirledik.
        builder.setCancelable(true);// bu ifade true ise kullanici gonderilen mesaji kapatabilir
        builder.setTitle(title); // mesajin basligi
        builder.setMessage(message); // mesajin icerigi
        builder.show(); // mesaji gosterir
    }
}

