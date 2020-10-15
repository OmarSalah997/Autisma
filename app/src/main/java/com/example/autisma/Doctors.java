package com.example.autisma;


import android.location.Location;
import android.os.Bundle;

import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Doctors extends AppCompatActivity {

        /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpful_institutions);

        final ListView list = findViewById(R.id.inst_list);
        String phonenumber="01201119781";
        String Description="مدرسة اليسر للحالات الخاصة ( التدخل المبكرــ التأخر الدراسي ــ الشلل الدماغي ــ التوحد ــ صعوبات التعلم )";
        String Location="31 Maadi Street ,Cairo";
     //   Location loc = new Location("dummyprovider");
        ArrayList<InstData> arrayList = new ArrayList<InstData>();
        arrayList.add(new InstData("Dr. Mona el Sheikh", "41 أحمد عبد النبي، الهايكستب، قسم النزهة", "doctor","01099221443","أستاذ الطب النفسي كلية الطب جامعة عين شمس – إستشاري وحدة الأطفال والمراهقين مستشفى د. عادل صادق"));
      //  arrayList.add(new InstData("Dr. Taha Aloush",  "", "doctor",phonenumber,Description));
        arrayList.add(new InstData("Dr. Manal Omar",  "39 ش الامام على - الدور 4-ميدان الاسماعيلية - مصر الجديدة", "doctor","01008811351","استشارى الطب النفسى"));
        arrayList.add(new InstData("Dr. Maha Emad ElDin",  "5 ش 216،, معادي السرايات الغربية، حي المعادي", "doctor","0225210734"," إستشارى الطب النفسى للأطفال والمراهقين"));
        arrayList.add(new InstData("Dr. Souad Moussa"," 197 أ ش 26 يوليو ميدان سفنكس، الجيزة، العجوزة", "doctor","02233022027","أستاذ طب نفسي أطفال"));
     //   arrayList.add(new InstData("Dr. Adel Sadek"," 41 أحمد عبد النبي، الهايكستب، قسم النزهة", "doctor","0226205757",Description));
        arrayList.add(new InstData("Dr. Hala Hammad", "95 ب ش الميرغنى برج الشمس أمام ماكدونالدز المطار مصر الجديدة،", "doctor","02222914419","استشاري الطب النفسي \" المركز الاستشاري البريطاني للاطفال والمراهقين"));
        arrayList.add(new InstData("Dr. Dalia Mostafa", "9 شارع المختار-الروضة-المنيل", "doctor","01021760100"," أستاذ أمراض التخاطب بكلية الطب - جامعة القاهرة"));
        arrayList.add(new InstData("Dr. Tarek el Sahrawy", "41 أحمد عبد النبي، الهايكستب، قسم النزهة", "doctor","0226205757","مدرس الطب النفسى كلية الطب جامعة عين شمس – إستشارى وحدة الأطفال مستشفى د. عادل صادق"));
        arrayList.add(new InstData("Dr. Ranya Qasem"," 13 محمد عوض من مكرم عبيد-مدينة نصر", "doctor","01140601044","استشاري و مدرس الطب النفسي بكلية الطب جامعة عين شمس"));
        arrayList.add(new InstData("Dr. Ahmed Ramzy",  "الزقازيق : شارع الغشام", "doctor","01009323164","أخصائي الطب النفسي "));
        arrayList.add(new InstData("Dr. Noha Haggag",  "ميت غمر-شارع الجيش-برج النيل", "doctor","011238709","رئيس قسم أمراض المخ و الأعصاب و الطب النفسي للاطفال و البالغين بمستشفي ميت غمر"));
        arrayList.add(new InstData("Dr. Nehal Yasser", "مستشفى العائلة (مدينة نصر)ش محمد المقريف أمام النادي الأهلي", "doctor","01020115115","أخصائي النفسى للاطفال"));
        arrayList.add(new InstData("Dr. Hesham Abdelrahman", "الرحاب : المركز الطبي الاول", "doctor","01099280120","استشاري الطب النفسي للاطفال"));
        InstAdapter customAdapter = new InstAdapter(this, arrayList);
        list.setAdapter(customAdapter);
    }



}
