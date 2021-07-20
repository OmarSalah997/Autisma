package com.example.autisma;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapImage;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.autisma.LOGIN.IP;

public class Doctors extends AppCompatActivity {
    private MapView mMapView;
    private final LocationManager locationMangaer = null;
    private MapElementLayer mPinLayer;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        mMapView = new MapView(this, MapRenderMode.RASTER);        //  MapRenderMode.RASTER for 2D map
        mMapView.onCreate(savedInstanceState);
        mMapView.setCredentialsKey(BuildConfig.CREDENTIALS_KEY);
        mPinLayer = new MapElementLayer();
        Helpful_institiutions.pin p1= new Helpful_institiutions.pin(30.12183384226699, 31.368656918354688, getString(R.string.Dr_Mona));
        Helpful_institiutions.pin p2= new Helpful_institiutions.pin(30.09660215940048, 31.332898316503414, getString(R.string.Dr_Manal));
        Helpful_institiutions.pin p3= new Helpful_institiutions.pin(29.96518362865358, 31.276615931850795, getString(R.string.Dr_maha));
        Helpful_institiutions.pin p4= new Helpful_institiutions.pin(30.06357704537131, 31.212207540302487, getString(R.string.Dr_Souad));
        Helpful_institiutions.pin p5= new Helpful_institiutions.pin(30.087603346677867, 31.33889943184763, getString(R.string.Goldenmedicalcenter));
        Helpful_institiutions.pin p6= new Helpful_institiutions.pin(29.982979680365673, 31.31432811835838, getString(R.string.Dr_Hannan));
        Helpful_institiutions.pin p7= new Helpful_institiutions.pin(30.010486153250756, 31.28853283184964, getString(R.string.Alwaysfriends));
        Helpful_institiutions.pin p8= new Helpful_institiutions.pin(30.600371917673836, 31.49069809311127, getString(R.string.EgyptianAssociation));
        Helpful_institiutions.pin p9= new Helpful_institiutions.pin(30.719990056254403, 31.25604193527057, getString(R.string.EbnyFoundation));
        Helpful_institiutions.pin p10= new Helpful_institiutions.pin(30.057472481775324, 31.356798800048754, getString(R.string.KayanAssociation));
        Helpful_institiutions.pin p11= new Helpful_institiutions.pin(30.059417062024554, 31.49183199678413, getString(R.string.TheEgyptianCanadian));
        ArrayList<Helpful_institiutions.pin>arr=new ArrayList<>();
        arr.add(p1);arr.add(p2);arr.add(p3);arr.add(p4);arr.add(p5);arr.add(p6);arr.add(p7);arr.add(p8);arr.add(p9);arr.add(p10);arr.add(p11);
        AddLocationsPins(arr,mPinLayer);
        mMapView.getLayers().add(mPinLayer);
        loadlocale();
        setContentView(R.layout.helpful_institutions);
        ((FrameLayout) findViewById(R.id.map_view)).addView(mMapView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final ListView list = findViewById(R.id.inst_list);
        final ArrayList<InstData> arrayList = new ArrayList<InstData>();
        arrayList.add(new InstData(getString(R.string.Dr_Mona), "41 أحمد عبد النبي، الهايكستب، قسم النزهة", "doctor", "01099221443", "أستاذ الطب النفسي كلية الطب جامعة عين شمس – إستشاري وحدة الأطفال والمراهقين مستشفى د. عادل صادق"));
        arrayList.add(new InstData(getString(R.string.Dr_Manal), "39 ش الامام على - الدور 4-ميدان الاسماعيلية - مصر الجديدة", "doctor", "01008811351", "استشارى الطب النفسى"));
        arrayList.add(new InstData(getString(R.string.Dr_maha), "5 ش 216،, معادي السرايات الغربية، حي المعادي", "doctor", "0225210734", " إستشارى الطب النفسى للأطفال والمراهقين"));
        arrayList.add(new InstData(getString(R.string.Dr_Souad), " 197 أ ش 26 يوليو ميدان سفنكس، الجيزة، العجوزة", "doctor", "02233022027", "أستاذ طب نفسي أطفال"));
        arrayList.add(new InstData("د. هالة حماد", "95 ب ش الميرغنى برج الشمس أمام ماكدونالدز المطار مصر الجديدة،", "doctor", "02222914419", "استشاري الطب النفسي \" المركز الاستشاري البريطاني للاطفال والمراهقين"));
        arrayList.add(new InstData("د. داليا مصطفى", "9 شارع المختار-الروضة-المنيل", "doctor", "01021760100", " أستاذ أمراض التخاطب بكلية الطب - جامعة القاهرة"));
        arrayList.add(new InstData("د. طارق السحراوي", "41 أحمد عبد النبي، الهايكستب، قسم النزهة", "doctor", "0226205757", "مدرس الطب النفسى كلية الطب جامعة عين شمس – إستشارى وحدة الأطفال مستشفى د. عادل صادق"));
        arrayList.add(new InstData(" د. رانيا قاسم", " 13 محمد عوض من مكرم عبيد-مدينة نصر", "doctor", "01140601044", "استشاري و مدرس الطب النفسي بكلية الطب جامعة عين شمس"));
        arrayList.add(new InstData("د. أحمد رمزي", "الزقازيق : شارع الغشام", "doctor", "01009323164", "أخصائي الطب النفسي "));
        arrayList.add(new InstData("د. نهى حجاج", "ميت غمر-شارع الجيش-برج النيل", "doctor", "011238709", "رئيس قسم أمراض المخ و الأعصاب و الطب النفسي للاطفال و البالغين بمستشفي ميت غمر"));
        arrayList.add(new InstData("د. نهال ياسر", "مستشفى العائلة (مدينة نصر)ش محمد المقريف أمام النادي الأهلي", "doctor", "01020115115", "أخصائي النفسى للاطفال"));
        arrayList.add(new InstData("  د. هشام عبد الرحمن", "الرحاب : المركز الطبي الاول", "doctor", "01099280120", "استشاري الطب النفسي للاطفال"));
        InstAdapter customAdapter = new InstAdapter(this, arrayList);
        list.setAdapter(customAdapter);
    }
    private void setLocale(String s) {
        Locale locale= new Locale(s);
        Locale.setDefault(locale);
        Configuration config=new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor= getSharedPreferences("settings_lang",MODE_PRIVATE).edit();
        editor.putString("my lang",s);
        editor.apply();
    }
    private void loadlocale() {
        SharedPreferences prefs= getSharedPreferences("settings_lang", Activity.MODE_PRIVATE);
        String language=prefs.getString("my lang","");
        if(language.equals(""))
            language= Resources.getSystem().getConfiguration().locale.getLanguage();
        setLocale(language);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver,
                LocationManager.GPS_PROVIDER);
        return gpsStatus;
    }
    protected void alertbox(String title, String mymessage, final Activity A) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mymessage)
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent myIntent = getIntent();
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    protected void GpuOff(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mymessage)
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    private void AddLocationsPins(ArrayList<Helpful_institiutions.pin> arr, MapElementLayer mPinLayer){
        Drawable d = getResources().getDrawable(R.drawable.ic_baseline_pin_drop_24);
        Bitmap pinBitmap = drawableToBitmap(d); // your pin graphic
        MapImage im=new MapImage(pinBitmap);
        for (int i=0;i<arr.size();i++)
        {
            MapIcon pushpin = new MapIcon();
            Geopoint G;     // your pin lat-long coordinates
            G = new Geopoint(arr.get(i).lat,arr.get(i).longt);
            String title = arr.get(i).title;// title to be shown next to the pin
            pushpin.setLocation(G);
            pushpin.setTitle(title);
            pushpin.setImage(im);
            mPinLayer.getElements().add(pushpin);
        }
    }

}