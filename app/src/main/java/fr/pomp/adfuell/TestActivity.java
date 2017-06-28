package fr.pomp.adfuell;


import android.content.Intent;
import android.os.Bundle;

import java.io.File;

import fr.pomp.adfuell.utils.edena.EDButterKniff;
import fr.pomp.adfuell.utils.edena.EDEventBus;
import fr.pomp.adfuell.utils.edena.EDGson;
import fr.pomp.adfuell.utils.edena.retrofit.EDDownloadService;
import fr.pomp.adfuell.utils.edena.retrofit.test.TestModel;

public class TestActivity extends BaseActivity {
    EDButterKniff edButterKniff;
    EDEventBus edEventBus;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        getSupportFragmentManager().beginTransaction().add(R.id.frame, new TestFragment()).commit();
        edButterKniff = EDButterKniff.getInstance();
        edButterKniff.activity(this);
        edEventBus = EDEventBus.getInstance(this);
        app = (App) getApplication();
    }

/*
    @OnClick(R.id.button)
    public void change(Button btn) {
        TestBddRequest testBdd = new TestBddRequest(this);
        long id = testBdd.insert();
        btn.setText("clicked " + id);
        //testBdd.select();
        //edEventBus.post(new EDEventBus(1,"Test"));

        retrofitTest();
    }
*/
    void retrofiDownload(){
        File pdf = new File( getExternalFilesDir(null) + File.separator + "pdf2.pdf");
        if(pdf.exists()) pdf.delete();
        Intent msgIntent = new Intent(this, EDDownloadService.class);
        msgIntent.putExtra(EDDownloadService.NOTIF_ACTIVATE,true);
        msgIntent.putExtra(EDDownloadService.NOTIF_ICO,R.drawable.ic_warning_black_36dp);
        msgIntent.putExtra(EDDownloadService.URL_ACTIVATE,"https://inforef.be/swi/download/apprendre_python3_5.pdf");
        msgIntent.putExtra(EDDownloadService.PATH_SAVE,pdf.getAbsolutePath());
        startService(msgIntent);
    }

    void retrofitTest(){
        TestModel testModel = new TestModel();
    }



    public void testJson(){
        EDGson gson = EDGson.getInstance();
        class Test{
            public Test(String a, String b){
                this.a =a;
                this.b =b;
            }
            public String a;
            public String b;
        }
        Test a = new Test("edena","edena2");
        Test b = new Test("b1","b2");
        String str = gson.serialize(a);
        String stre = gson.getGson().toJson(b);
        System.out.println(str);
        System.out.println(stre);

    }

}
