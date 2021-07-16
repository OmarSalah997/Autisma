package com.example.autisma;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapView;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapImage;
import static com.example.autisma.LOGIN.IP;

public class Helpful_institiutions extends AppCompatActivity {
    Communication com;
    //second parameter default value.
    String url = IP + "institution"; // route
    //Json body data
    JSONObject institutions;
    private MapView mMapView;
    private final LocationManager locationMangaer = null;
    private MapElementLayer mPinLayer;

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
        pin p1= new pin(29.97576699206561, 31.05278581428084, getString(R.string.Dreamschool));
        pin p2= new pin(30.1823345071871, 31.509303309850587, getString(R.string.Happyworldschool));
        pin p3= new pin(29.96673392691783, 31.31324828555084, getString(R.string.ElYousreschool));
        pin p4= new pin(30.087166914079003, 31.366137474175723, getString(R.string.Zaiedmedicalcenter));
        pin p5= new pin(30.051213261635226, 31.333088989520586, getString(R.string.Goldenmedicalcenter));
        pin p6= new pin(29.982979680365673, 31.31432811835838, getString(R.string.Dr_Hannan));
        pin p7= new pin(30.010486153250756, 31.28853283184964, getString(R.string.Alwaysfriends));
        pin p8= new pin(29.96339426415316, 31.285388903014816, getString(R.string.EgyptianAssociation));
        pin p9= new pin(30.02230411383431, 31.22682667417729, getString(R.string.EbnyFoundation));
        pin p10= new pin(30.045366898310558, 31.235668684704752, getString(R.string.KayanAssociation));
        pin p11= new pin(29.956104084264325, 31.287423103015033, getString(R.string.TheEgyptianCanadian));
        pin p12= new pin(30.086119683023796, 31.28916909930772, getString(R.string.SwedishCenter));
        pin p13= new pin(29.972374697256146, 31.274278831850623, getString(R.string.MaadiCenter));
        ArrayList<pin>arr=new ArrayList<>();
        arr.add(p1);arr.add(p2);arr.add(p3);arr.add(p4);arr.add(p5);arr.add(p6);arr.add(p7);arr.add(p8);arr.add(p9);arr.add(p10);arr.add(p11);arr.add(p12);arr.add(p13);
        AddLocationsPins(arr,mPinLayer);
        mMapView.getLayers().add(mPinLayer);
        loadlocale();
        setContentView(R.layout.helpful_institutions);
        ((FrameLayout) findViewById(R.id.map_view)).addView(mMapView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
        {
            locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Location location = locationMangaer.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                mMapView.setScene(
                        MapScene.createFromLocationAndZoomLevel(new Geopoint(latitude,longitude), 20),
                        MapAnimationKind.DEFAULT);}
                else {GpuOff("Gps Status!!", "Your GPS is: OFF"); }
            }else{
            alertbox(getString(R.string.accessDenied),getString(R.string.accessLocation),this);
        }
*/

        final ListView list = findViewById(R.id.inst_list);
        final ArrayList<InstData> arrayList = new ArrayList<InstData>();

        /*
        com=new Communication(Helpful_institiutions.this);
        JSONObject jsonBody = new JSONObject();

        com.REQUEST_NO_AUTHORIZE(Request.Method.GET, url, jsonBody,new Communication.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject response) throws JSONException {

institutions=response.getJSONObject("result");
                Log.e("success",institutions.toString());
                Log.e("success", String.valueOf(institutions.length()));

                for (int i = 0; i <institutions.length(); i++) {
                    JSONObject c = institutions.getJSONObject(String.valueOf(i));

                    Log.e("success",c.toString());
                    Log.e("success",c.getString("name"));
                    arrayList.add(new InstData(c.getString("name"),c.getString("address"),"dreamidealschool",c.getString("phone"),c.getString("address"),c.getString("facebook"),c.getString("website")));
                }
                InstAdapter customAdapter = new InstAdapter(Helpful_institiutions.this, arrayList);

                list.setAdapter(customAdapter);}

            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
*/

        String phonenumber = "01201119781";
        String Description = "مدرسة اليسر للحالات الخاصة";
        String Location = "31 Maadi Street ,Cairo";
        arrayList.add(new InstData(getString(R.string.Dreamschool), "شارع الواحات 6 اكتوبر", "dreamidealschool", "01280111961",
                "مدرسة دريم للتربية النموذجية ( لذوى الاحتياجات الخاصة وصعوبات التعلم )",
                "https://www.facebook.com/DreamIdealEducationSchoolEgypt", "http://dreamidealschool.academy"));
        arrayList.add(new InstData(getString(R.string.Happyworldschool), " جمعية أحمد عرابي – خط 2 شمال – قطعة 5047", "happyworld", "01223558636/01003913173",
                "مدرسة هابي وورلد ( مؤسسة ألاء لذوي الاحتياجات الخاصة )", "https://www.facebook.com/pg/%D9%85%D8%AF%D8%B1%D8%B3%D8%A9-%D9%87%D8%A7%D8%A8%D9%89-%D9%88%D8%B1%D9%84%D8%AF-%D9%84%D8%B0%D9%88%D9%89-%D8%A7%D9%84%D8%A7%D8%AD%D8%AA%D9%8A%D8%A7%D8%AC%D8%A7%D8%AA-%D8%A7%D9%84%D8%AE%D8%A7%D8%B5%D8%A9-188872947840947"));
        arrayList.add(new InstData(getString(R.string.ElYousreschool), "طريق كارفور الرئيسى - بجوار مركز شباب زهراء المعادى", "elyosr", "27340026",
                "مدرسة اليسر للحالات الخاصة ( التدخل المبكرــ التأخر الدراسي ــ الشلل الدماغي ــ التوحد ــ صعوبات التعلم )",
                "https://www.facebook.com/alyosserschoolspneed/", "http://alyosserspneed.com/"));
        arrayList.add(new InstData(getString(R.string.Zaiedmedicalcenter), "طريق الاوتوستراد باتجاه مدينة نصر بجوار مجمع النسور وعامر جروب", "zayedcenter", "01002198575",
                " مركز زايد الطبي للتأهيل بالقوات الجوية المصرية الصَرح الأحدث لرعاية وتأهيل ذوى القدرات الخاصّة",
                "https://www.facebook.com/zayedcentre/", "http://zayedcenter.com"));
        arrayList.add(new InstData(getString(R.string.Goldenmedicalcenter), "شارع الحسن ابن علي متفرع من شارع الطيران الحي السابع مدينة نصر", "zahabycenter", "01102626909",
                "المركز الذهبي التخصصي المركز الاول المتكامل لاطفال الروضه والمدارس والدمج والتوحد في تعديل السلوك بالمنهج الأمريكي",
                "https://www.facebook.com/%D8%A7%D9%84%D9%85%D8%B1%D9%83%D8%B2-%D8%A7%D9%84%D8%B0%D9%87%D8%A8%D9%8A-%D8%A7%D9%84%D8%AA%D8%AE%D8%B5%D8%B5%D9%8A-337886163323469/"));

        arrayList.add(new InstData(getString(R.string.Dr_Hannan), "القاهرة - المعادي المعراج - خلف كارفور المعادي", "hanan", "01142241162",
                "لمركز متخصص في تدريب وعلاج حالات التوحد خاصة حالات التوحد الشديدة تحت أشراف دكتورة / حنان محمود طبيبة امراض التخاطب",
                "https://www.facebook.com/autism.therapy1/", "https://kalameecenter.com"));
        arrayList.add(new InstData(getString(R.string.Alwaysfriends), "11 شارع 22 -المقطم", "friendsforever", "01279998344",
                "هو أول مركز للعلاج الجماعي للأطفال والمراهقين في مصر والشرق الأوسط وأول مركز تخصصي للقياسات النفسية للأطفال (إرشاد تربوي - علاج نفسي وتأهيل سلوكي)",
                "https://www.facebook.com/daymanashab", "http://daymanashab.com"));
        arrayList.add(new InstData(getString(R.string.EgyptianAssociation), "8 شارع زهراء المعادى ", "autisticsociety", "01061400805",
                "الجمعية هي منظمة غير ربحية يمكن للآباء اللجوء إليها في حال كانت لديهم شكوك بأن طفلهم متوحد أو إذا تم تشخيص طفلهم بالفعل بالتوحد",
                "https://www.facebook.com/pg/autismeg", "http://www.egyptautism.com"));
        arrayList.add(new InstData(getString(R.string.EbnyFoundation), "4 شارع النجمة -ميدان هليوبوليس - مصر الجديدة - القاهرة", "ebny", "226447317",
                "مؤسسة تعمل فى مجال التوحد ورعاية الفئات الخاصة المؤسسة حصلت على المركز الأول على مستوى الجمهورية سنه2010",
                "https://www.facebook.com/%D9%85%D8%A4%D8%B3%D8%B3%D8%A9-%D8%A7%D8%A8%D9%86%D9%89-%D9%84%D9%84%D9%81%D8%A6%D8%A7%D8%AA-%D8%A7%D9%84%D8%AE%D8%A7%D8%B5%D8%A9-%D9%88-%D8%A7%D9%84%D8%AA%D9%88%D8%AD%D8%AF-132922420102668/"));
        arrayList.add(new InstData(getString(R.string.KayanAssociation), "12 من مصر والسودان ، حدائق القبه، محافظة القاهرة", "kayan", "23302135",
                "جمعية كيان مؤسسة خيرية متخصصة في تقديم الرعاية وخدمات التأهيل للأطفال ذوي لإعاقة من كافة الشرائح الاجتماعية المصرية وبخاصة غير القادرين منهم ، وتهتم بتدريب أولياء الأمور والمتخصصين في مجال الإعاقة",
                "https://www.facebook.com/kayanegypt/", "http://www.kayanegypt.com/"));

        arrayList.add(new InstData(getString(R.string.TheEgyptianCanadian), "فرع النزهه :14 ش جمال الدين على بجوار ملاهى السندباد - فرع المعادى: 5/3 شارع اللاسلكى بجوار شركة غاز مصر", "canadian", "01115000412",
                "اول مركز للنيوروفيدباك والبيوفيدباك بمصر والشرق الاوسط وبأحدث الاجهزة",
                "https://www.facebook.com/NeurofeedbackEgypt/", "https://eccad.net/"));
        arrayList.add(new InstData(getString(R.string.SwedishCenter), "8077 شارع الاشجار-من شارع 9 -المقطم", "swedish", "01000702750",
                "أول مركز سويدي لعلاج حالات التدخل المبكر (التوحد) والدمج التعليمى لذوى الأحتياجات الخاصة ",
                "https://www.facebook.com/ssc4specialneeds/", "https://www.sscforspecialneeds.com/"));
        arrayList.add(new InstData(getString(R.string.MaadiCenter), "مبنى 9 ,شارع 278 -المعادى الجديدة", "lrc", "01222332809",
                "مركز المعادى للخدمات التعليمية يقدم خدمات تشخيصية واستشارية مناسبة للأطفال من جميع الأعمار الذين يعانون من مجموعة واسعة من صعوبات التعلم واضطرابات النمو و / أو مشاكل السلوك",
                "https://www.facebook.com/LRCegypt/", "http://lrcegypt.org/"));
        InstAdapter customAdapter = new InstAdapter(this, arrayList);
        list.setAdapter(customAdapter);
    }

      /*
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
     Toast.makeText(getApplicationContext(),"lk",Toast.LENGTH_SHORT).show();
              TextView tittle =view.findViewById(R.id.single_row);
                ImageView imag = view.findViewById(R.id.icon);

                Intent intent = new Intent(Helpful_institiutions.this,Profile.class);
                 intent.putExtra("inst_name", tittle.getText().toString());
                intent.putExtra("inst_img",  imag.getDrawable().toString());
                startActivity(intent);
            }
        }); */
    private void setLocale(String s) {
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("settings_lang", MODE_PRIVATE).edit();
        editor.putString("my lang", s);
        editor.apply();
    }

    private void loadlocale() {
        SharedPreferences prefs = getSharedPreferences("settings_lang", Activity.MODE_PRIVATE);
        String language = prefs.getString("my lang", "");
        if (language.equals(""))
            language = Resources.getSystem().getConfiguration().locale.getLanguage();
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
    private void AddLocationsPins(ArrayList<pin> arr,MapElementLayer mPinLayer){
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
    public static class pin
    { public double lat,longt;
        public String title;
        public pin(double lat, double longt, String title) {
            this.lat = lat;
            this.longt = longt;
            this.title = title;
        }
    }
    }




