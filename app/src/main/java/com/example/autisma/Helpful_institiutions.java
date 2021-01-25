package com.example.autisma;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.autisma.LOGIN.IP;

public class Helpful_institiutions extends AppCompatActivity {
    Communication com;
  //second parameter default value.
    String url = IP+"institution"; // route
   //Json body data
   JSONObject institutions;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com=new Communication(Helpful_institiutions.this);
        setContentView(R.layout.helpful_institutions);
        final ArrayList<InstData> arrayList = new ArrayList<InstData>();
        final ListView list = findViewById(R.id.inst_list);
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

        });


     //   Location loc = new Location("dummyprovider");
/*
        String phonenumber="01201119781";
        String Description="مدرسة اليسر للحالات الخاصة ( التدخل المبكرــ التأخر الدراسي ــ الشلل الدماغي ــ التوحد ــ صعوبات التعلم )";
        String Location="31 Maadi Street ,Cairo";
        arrayList.add(new InstData("مدرسة دريم للتربية النوذجية", "شارع الواحات 6 اكتوبر", "dreamidealschool","01280111961",
                "مدرسة دريم للتربية النموذجية ( لذوى الاحتياجات الخاصة وصعوبات التعلم )",
                "https://www.facebook.com/DreamIdealEducationSchoolEgypt","http://dreamidealschool.academy"));
        arrayList.add(new InstData("مدرسة هابي وورلد"," جمعية أحمد عرابي – خط 2 شمال – قطعة 5047", "happyworld","01223558636/01003913173",
                "مدرسة هابي وورلد ( مؤسسة ألاء لذوي الاحتياجات الخاصة )","https://www.facebook.com/pg/%D9%85%D8%AF%D8%B1%D8%B3%D8%A9-%D9%87%D8%A7%D8%A8%D9%89-%D9%88%D8%B1%D9%84%D8%AF-%D9%84%D8%B0%D9%88%D9%89-%D8%A7%D9%84%D8%A7%D8%AD%D8%AA%D9%8A%D8%A7%D8%AC%D8%A7%D8%AA-%D8%A7%D9%84%D8%AE%D8%A7%D8%B5%D8%A9-188872947840947"));
        arrayList.add(new InstData("مدرسة اليسر للحالات الخاصة", "طريق كارفور الرئيسى - بجوار مركز شباب زهراء المعادى", "elyosr","27340026",
                "مدرسة اليسر للحالات الخاصة ( التدخل المبكرــ التأخر الدراسي ــ الشلل الدماغي ــ التوحد ــ صعوبات التعلم )",
                "https://www.facebook.com/alyosserschoolspneed/","http://alyosserspneed.com/"));
        arrayList.add(new InstData("مركز زايد الطبى","طريق الاوتوستراد باتجاه مدينة نصر بجوار مجمع النسور وعامر جروب", "zayedcenter","01002198575",
                " مركز زايد الطبي للتأهيل بالقوات الجوية المصرية الصَرح الأحدث لرعاية وتأهيل ذوى القدرات الخاصّة",
                "https://www.facebook.com/zayedcentre/","http://zayedcenter.com"));
        arrayList.add(new InstData("المركز الذهبي", "شارع الحسن ابن علي متفرع من شارع الطيران الحي السابع مدينة نصر", "zahabycenter","01102626909",
                "المركز الذهبي التخصصي المركز الاول المتكامل لاطفال الروضه والمدارس والدمج والتوحد في تعديل السلوك بالمنهج الأمريكي",
                "https://www.facebook.com/%D8%A7%D9%84%D9%85%D8%B1%D9%83%D8%B2-%D8%A7%D9%84%D8%B0%D9%87%D8%A8%D9%8A-%D8%A7%D9%84%D8%AA%D8%AE%D8%B5%D8%B5%D9%8A-337886163323469/"));

        arrayList.add(new InstData("مركز دكتورة /حنان محمود", "القاهرة - المعادي المعراج - خلف كارفور المعادي", "hanan","01142241162",
                "لمركز متخصص في تدريب وعلاج حالات التوحد خاصة حالات التوحد الشديدة تحت أشراف دكتورة / حنان محمود طبيبة امراض التخاطب",
                "https://www.facebook.com/autism.therapy1/","https://kalameecenter.com"));
        arrayList.add(new InstData("دايما صحاب(د مى الرخاوى)", "11 شارع 22 -المقطم", "friendsforever","01279998344",
                "هو أول مركز للعلاج الجماعي للأطفال والمراهقين في مصر والشرق الأوسط وأول مركز تخصصي للقياسات النفسية للأطفال (إرشاد تربوي - علاج نفسي وتأهيل سلوكي)",
                "https://www.facebook.com/daymanashab","http://daymanashab.com"));
        arrayList.add(new InstData("الجمعية المصرية للتوحد", "8 شارع زهراء المعادى ", "autisticsociety","01061400805",
                "الجمعية هي منظمة غير ربحية يمكن للآباء اللجوء إليها في حال كانت لديهم شكوك بأن طفلهم متوحد أو إذا تم تشخيص طفلهم بالفعل بالتوحد",
                "https://www.facebook.com/pg/autismeg","http://www.egyptautism.com"));
        arrayList.add(new InstData("مؤسسة ابنى ", "4 شارع النجمة -ميدان هليوبوليس - مصر الجديدة - القاهرة", "ebny","226447317",
                "مؤسسة تعمل فى مجال التوحد ورعاية الفئات الخاصة المؤسسة حصلت على المركز الأول على مستوى الجمهورية سنه2010",
                "https://www.facebook.com/%D9%85%D8%A4%D8%B3%D8%B3%D8%A9-%D8%A7%D8%A8%D9%86%D9%89-%D9%84%D9%84%D9%81%D8%A6%D8%A7%D8%AA-%D8%A7%D9%84%D8%AE%D8%A7%D8%B5%D8%A9-%D9%88-%D8%A7%D9%84%D8%AA%D9%88%D8%AD%D8%AF-132922420102668/"));
        arrayList.add(new InstData("مركز كيان", "12 من مصر والسودان ، حدائق القبه، محافظة القاهرة", "kayan","23302135",
                "جمعية كيان مؤسسة خيرية متخصصة في تقديم الرعاية وخدمات التأهيل للأطفال ذوي لإعاقة من كافة الشرائح الاجتماعية المصرية وبخاصة غير القادرين منهم ، وتهتم بتدريب أولياء الأمور والمتخصصين في مجال الإعاقة",
                "https://www.facebook.com/kayanegypt/","http://www.kayanegypt.com/"));

        arrayList.add(new InstData("المركز المصرى الكندى", "فرع النزهه :14 ش جمال الدين على بجوار ملاهى السندباد - فرع المعادى: 5/3 شارع اللاسلكى بجوار شركة غاز مصر", "canadian","01115000412",
                "اول مركز للنيوروفيدباك والبيوفيدباك بمصر والشرق الاوسط وبأحدث الاجهزة",
                "https://www.facebook.com/NeurofeedbackEgypt/","https://eccad.net/"));
        arrayList.add(new InstData("المركز السويدى لعلاج التوحد ", "8077 شارع الاشجار-من شارع 9 -المقطم", "swedish","01000702750",
                "أول مركز سويدي لعلاج حالات التدخل المبكر (التوحد) والدمج التعليمى لذوى الأحتياجات الخاصة ",
                "https://www.facebook.com/ssc4specialneeds/","https://www.sscforspecialneeds.com/"));
        arrayList.add(new InstData("مركز المعادى للخدمات التعليمية (LRC)", "مبنى 9 ,شارع 278 -المعادى الجديدة", "lrc","01222332809",
                "مركز المعادى للخدمات التعليمية يقدم خدمات تشخيصية واستشارية مناسبة للأطفال من جميع الأعمار الذين يعانون من مجموعة واسعة من صعوبات التعلم واضطرابات النمو و / أو مشاكل السلوك",
                "https://www.facebook.com/LRCegypt/","http://lrcegypt.org/"));*/
    /*
        try{
        InstAdapter customAdapter = new InstAdapter(this, arrayList);

        list.setAdapter(customAdapter);}
        catch(Exception e){
            e.printStackTrace();
        }*/
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
    }}




