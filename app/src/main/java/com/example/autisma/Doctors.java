package com.example.autisma;


import android.location.Location;
import android.os.Bundle;

import android.util.Log;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyError;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.autisma.LOGIN.IP;

public class Doctors extends AppCompatActivity {
    Communication com;
    //second parameter default value.
    String url = IP+"doctors"; // route
    //Json body data
    JSONObject institutions;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpful_institutions);
        final ListView list = findViewById(R.id.inst_list);
        final ArrayList<InstData> arrayList = new ArrayList<InstData>();
        /*
        com=new Communication(Doctors.this);
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
                    arrayList.add(new InstData(c.getString("name"),c.getString("address"),"doctor",c.getString("phone"),c.getString("specialization")));
                }
                InstAdapter customAdapter = new InstAdapter(Doctors.this, arrayList);

                list.setAdapter(customAdapter);}
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });*/

        String phonenumber = "01201119781";
        String Description = "مدرسة اليسر للحالات الخاصة ( التدخل المبكرــ التأخر الدراسي ــ الشلل الدماغي ــ التوحد ــ صعوبات التعلم )";
        String Location = "31 Maadi Street ,Cairo";
        //   Location loc = new Location("dummyprovider");

        arrayList.add(new InstData("د. منى الشيخ", "41 أحمد عبد النبي، الهايكستب، قسم النزهة", "doctor", "01099221443", "أستاذ الطب النفسي كلية الطب جامعة عين شمس – إستشاري وحدة الأطفال والمراهقين مستشفى د. عادل صادق"));
        //  arrayList.add(new InstData("Dr. Taha Aloush",  "", "doctor",phonenumber,Description));
        arrayList.add(new InstData("د. منال عمر", "39 ش الامام على - الدور 4-ميدان الاسماعيلية - مصر الجديدة", "doctor", "01008811351", "استشارى الطب النفسى"));
        arrayList.add(new InstData("د. مها عماد الدين", "5 ش 216،, معادي السرايات الغربية، حي المعادي", "doctor", "0225210734", " إستشارى الطب النفسى للأطفال والمراهقين"));
        arrayList.add(new InstData("د. سعاد موسى", " 197 أ ش 26 يوليو ميدان سفنكس، الجيزة، العجوزة", "doctor", "02233022027", "أستاذ طب نفسي أطفال"));
        //   arrayList.add(new InstData("Dr. Adel Sadek"," 41 أحمد عبد النبي، الهايكستب، قسم النزهة", "doctor","0226205757",Description));
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

}