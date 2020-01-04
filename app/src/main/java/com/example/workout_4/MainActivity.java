package com.example.workout_4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workout_4.Adapter.RecordListAdapter;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BarGraphSeries<DataPoint> series;
    SQL_DataBase sql_dataBase;

    String choseTOOL = "";
    String choseDate ="";
    String query_date_text="";

    int size = 0;
    int get_age = 0;
    int get_gender = 0;
    double get_weight = 0;
    double get_tall = 0;
    double get_activity = 0;
    double bmr = 0;
    double tdee = 0;
    double bmi = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //TAB
        final TabHost tabhost = (TabHost)findViewById(R.id.tabhost);

        //TAB1's component
        final Button chest = (Button)findViewById(R.id.button);
        final Button hand = (Button)findViewById(R.id.button4);
        final Button leg = (Button)findViewById(R.id.button2);
        final Button back = (Button)findViewById(R.id.button3);

        //TAB2's component
        final Button btn_database = (Button)findViewById(R.id.btn_watchSQL);
        final Button btn_query = (Button)findViewById(R.id.btn_query);
        final EditText query_date = (EditText)findViewById(R.id.query_date);
        final GraphView graph = (GraphView)findViewById(R.id.graph);
        final Spinner spin_spName = (Spinner)findViewById(R.id.spin_spName);

        //TAB3's component
        final EditText tall = (EditText)findViewById(R.id.tall);
        final EditText weight = (EditText)findViewById(R.id.weight);
        final EditText age = (EditText)findViewById(R.id.age);
        final Spinner gender = (Spinner)findViewById(R.id.gender);
        final Spinner often = (Spinner)findViewById(R.id.often);
        final Button bth_porfile = (Button)findViewById(R.id.btn_profile);



        tabhost.setup();
        TabHost.TabSpec TS = null;
        TAB_Setting(tabhost,TS);

        TAB1_Setting(chest,back,leg,hand);
        TAB2_Setting(btn_database,btn_query,query_date,graph,spin_spName);
        TAB3_Setting(tall,weight,age,gender,often,bth_porfile);


    }



    private void TAB_Setting(TabHost tabhost , TabHost.TabSpec TS){

        TS = tabhost.newTabSpec("");
        TS.setContent(R.id.tab1);
        TS.setIndicator(getString(R.string.tab1_name));
        tabhost.addTab(TS);

        TS = tabhost.newTabSpec("");
        TS.setContent(R.id.tab2);
        TS.setIndicator(getString(R.string.tab2_name));
        tabhost.addTab(TS);

        TS = tabhost.newTabSpec("");
        TS.setContent(R.id.tab3);
        TS.setIndicator(getString(R.string.tab3_name));
        tabhost.addTab(TS);

    }

    private void TAB1_Setting(Button chest, Button back, Button leg, Button hand) {
        chest.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this  , TrainingPage.class);
                Bundle bundle = new Bundle();
                bundle.putInt("choose",1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this  , TrainingPage.class);
                Bundle bundle = new Bundle();
                bundle.putInt("choose",2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        leg.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this  , TrainingPage.class);
                Bundle bundle = new Bundle();
                bundle.putInt("choose",3);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        hand.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this  , TrainingPage.class);
                Bundle bundle = new Bundle();
                bundle.putInt("choose",4);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void TAB2_Setting(Button btn_database, Button btn_query, final EditText query_date, final GraphView graph, final Spinner spin_spName) {
        //Graph 設定
        TAB2_SettingGraph(graph);

        //資料庫設定
        sql_dataBase = TAB2_SettingDataBase();

        
        //查詢並且畫圖
        series = new BarGraphSeries<DataPoint>(new DataPoint[0]);
        btn_query.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                query_date_text = query_date.getText().toString();
                if(query_date_text.length()>0){
                    series.resetData(getDataPoint());

                    graph.addSeries(series);

                    //Graph點設定
                    //series.setThickness(8);
                    //series.setDrawBackground(true);
                    //series.setDrawDataPoints(true);
                    series.setAnimated(true);
                    series.setSpacing(60);
                    series.setDrawValuesOnTop(true);
                    series.setValuesOnTopColor(Color.BLACK);


                    //series.setDataPointsRadius(15);
                }

            }
        });



        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPointInterface) {

                String sqltmpdate="";
                double x = dataPointInterface.getX();
                int xV = Math.round((float) x);

                if(xV<10)
                    sqltmpdate = choseDate+"0"+xV+"日";
                else
                    sqltmpdate = choseDate+xV+"日";

                String record = "";
                Cursor findRecord = sql_dataBase.getSportRecord(sqltmpdate,choseTOOL);
                while (findRecord.moveToNext()){
                    record += findRecord.getString(0)+"\n\n"+ getString(R.string.totalKG) +findRecord.getString(1)+"KG\n\n";
                }

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.series_record,null);
                final Button dialog_seriescheck = (Button) mView.findViewById(R.id.dialog_seriesok);
                final TextView dialog_seriesrecord = (TextView) mView.findViewById(R.id.dialog_seriesrecord);
                final TextView dialog_seriesDate = (TextView)mView.findViewById(R.id.dialog_seriesDate);
                dialog_seriesrecord.setText(record);
                dialog_seriesrecord.setMovementMethod(ScrollingMovementMethod.getInstance());
                dialog_seriesDate.setText(sqltmpdate);


                mBuilder.setView(mView);
                final AlertDialog record_dialog = mBuilder.create();
                record_dialog.show();

                dialog_seriescheck.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        record_dialog.cancel();
                    }
                });

            }
        });


        final List tmp_name = new ArrayList();
        final String[] sp = {getString(R.string.chest), getString(R.string.back), getString(R.string.leg), getString(R.string.hand)};
        Spinner spinner = (Spinner)findViewById(R.id.spin_sp);
        ArrayAdapter<String> spList = new ArrayAdapter<>(MainActivity.this, R.layout.myspinner, sp);

        spinner.setAdapter(spList);
        spList.setDropDownViewResource(R.layout.myspinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tmp_name.clear();

                switch (position){
                    case 0:{
                        Cursor findName = sql_dataBase.getSportName(getString(R.string.chestTraining));

                        while (findName.moveToNext()){
                            tmp_name.add(findName.getString(0));
                        }
                        size = tmp_name.size();

                        String [] spName = new String[size];
                        for(int i = 0 ;i<size;i++){
                            spName[i] = tmp_name.get(i).toString();
                        }
                        ArrayAdapter<String> spNameList = new ArrayAdapter<>(MainActivity.this,
                                R.layout.myspinner, spName);
                        spNameList.setDropDownViewResource(R.layout.myspinner);
                        spin_spName.setAdapter(spNameList);

                        break;
                    }
                    case 1:{
                        Cursor findName = sql_dataBase.getSportName(getString(R.string.backTraining));

                        while (findName.moveToNext()){
                            tmp_name.add(findName.getString(0));
                        }
                        size = tmp_name.size();

                        String [] spName = new String[size];
                        for(int i = 0 ;i<size;i++){
                            spName[i] = tmp_name.get(i).toString();
                        }
                        ArrayAdapter<String> spNameList = new ArrayAdapter<>(MainActivity.this,
                                R.layout.myspinner, spName);
                        spNameList.setDropDownViewResource(R.layout.myspinner);
                        spin_spName.setAdapter(spNameList);
                        break;
                    }
                    case 2:{
                        Cursor findName = sql_dataBase.getSportName(getString(R.string.legTraining));

                        while (findName.moveToNext()){
                            tmp_name.add(findName.getString(0));
                        }
                        size = tmp_name.size();

                        String [] spName = new String[size];
                        for(int i = 0 ;i<size;i++){
                            spName[i] = tmp_name.get(i).toString();
                        }
                        ArrayAdapter<String> spNameList = new ArrayAdapter<>(MainActivity.this,
                                R.layout.myspinner, spName);
                        spNameList.setDropDownViewResource(R.layout.myspinner);
                        spin_spName.setAdapter(spNameList);
                        break;
                    }
                    case 3:{
                        Cursor findName = sql_dataBase.getSportName(getString(R.string.handTraining));

                        while (findName.moveToNext()){
                            tmp_name.add(findName.getString(0));
                        }
                        size = tmp_name.size();

                        String [] spName = new String[size];
                        for(int i = 0 ;i<size;i++){
                            spName[i] = tmp_name.get(i).toString();
                        }
                        ArrayAdapter<String> spNameList = new ArrayAdapter<>(MainActivity.this,
                                R.layout.myspinner, spName);
                        spNameList.setDropDownViewResource(R.layout.myspinner);
                        spin_spName.setAdapter(spNameList);
                        break;
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        spin_spName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choseTOOL = String.valueOf(parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        btn_database.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.sql_dialog,null);
                final Button dialog_check = (Button) mView.findViewById(R.id.dialog_SQLok);
                final Button dialog_delete = (Button) mView.findViewById(R.id.dialog_SQLdelete);
                final ListView dialog_list = (ListView) mView.findViewById(R.id.dialog_SQLlist);

                Cursor res1 = sql_dataBase.getTraingDate();
                final List tmp_date = new ArrayList();

                while (res1.moveToNext()){
                    tmp_date.add(res1.getString(0));
                }

                final String[] date = new String[tmp_date.size()];
                for(int i = 0 ;i<tmp_date.size();i++){
                    date[i]=tmp_date.get(i).toString();
                }

                RecordListAdapter adapter = new RecordListAdapter(MainActivity.this,date);
                dialog_list.setAdapter(adapter);

                mBuilder.setView(mView);
                final AlertDialog SQL_dialog = mBuilder.create();
                SQL_dialog.show();

                dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AlertDialog.Builder theDayBuilder = new AlertDialog.Builder(MainActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.theday_record,null);
                        final Button theDay_ok = (Button) mView.findViewById(R.id.dialog_theDayok);
                        final Button theDay_delete = (Button) mView.findViewById(R.id.dialog_theDaydelete);
                        final TextView theDay_list = (TextView) mView.findViewById(R.id.dialog_theDaytext);
                        final TextView theDay = (TextView) mView.findViewById(R.id.dialog_theDay);
                        theDay_list.setMovementMethod(ScrollingMovementMethod.getInstance());

                        theDay.setText(date[position]);

                        Cursor res = sql_dataBase.getDateRecord(date[position]);

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()){
                            buffer.append("\t\t\t\t資料編號 : "+res.getString(0)+"\n");
                            buffer.append("動作 : "+res.getString(1)+"\n");
                            buffer.append("總公斤 : "+res.getString(2)+"KG"+"\n");
                            buffer.append(res.getString(3)+"\n"+"\n"+"\n");
                        }
                        theDay_list.setText(buffer.toString());

                        theDayBuilder.setView(mView);
                        final AlertDialog theDay_dialog = theDayBuilder.create();
                        theDay_dialog.show();

                        theDay_delete.setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                                View mView = getLayoutInflater().inflate(R.layout.delete_dialog,null);
                                final Button deleteOK = (Button) mView.findViewById(R.id.deleteOK);
                                final EditText deleteID = (EditText) mView.findViewById(R.id.deleteID);

                                mBuilder.setView(mView);
                                final AlertDialog deleteSQL = mBuilder.create();
                                deleteSQL.show();

                                deleteOK.setOnClickListener(new Button.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Integer deleteRows = sql_dataBase.deleteData(deleteID.getText().toString());
                                        if(deleteRows > 0)
                                            Toast.makeText(MainActivity.this,getString(R.string.dataDeleteSuccess),Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(MainActivity.this,getString(R.string.dataDeleteFail),Toast.LENGTH_SHORT).show();
                                        deleteSQL.cancel();
                                        theDay_dialog.cancel();
                                    }
                                });


                            }
                        });

                        theDay_ok.setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                theDay_dialog.cancel();
                            }
                        });

                    }
                });


                dialog_delete.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.delete_day,null);
                        final Button deleteOK = (Button) mView.findViewById(R.id.deleteDayOK);
                        final EditText deleteDate = (EditText) mView.findViewById(R.id.deleteDay);
                        final AlertDialog deleteSQL = mBuilder.create();

                        deleteSQL.show();
                        mBuilder.setView(mView);


                        deleteOK.setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String thedate = "";
                                if(deleteDate.getText().toString().length()>0)
                                {
                                    String tmpdate[] = deleteDate.getText().toString().split("/");
                                    if(Integer.parseInt(tmpdate[1])<10)
                                        tmpdate[1] = "0"+tmpdate[1];

                                    if(Integer.parseInt(tmpdate[2])<10)
                                        tmpdate[2] = "0"+tmpdate[2];

                                    thedate = tmpdate[0]+"年"+tmpdate[1]+"月"+tmpdate[2]+"日";
                                }

                                Integer deleteRows = sql_dataBase.deleteSomeData(thedate);
                                if(deleteRows > 0)
                                    Toast.makeText(MainActivity.this,getString(R.string.dataDeleteSuccess),Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(MainActivity.this,getString(R.string.dataDeleteFail),Toast.LENGTH_SHORT).show();
                                deleteSQL.cancel();
                                SQL_dialog.cancel();
                            }
                        });

                    }
                });

                dialog_check.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQL_dialog.cancel();
                    }
                });

            }
        });

    }

    private void TAB2_SettingGraph(final GraphView graph){
        //graph格式
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX)
                    return super.formatLabel((int)value, isValueX)+"號";
                else{
                    return super.formatLabel((int)value, isValueX)+"KG";
                }

            }
        });
        //Grapgh設定
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(31);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(3000);

        //縮放
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
    }

    private SQL_DataBase TAB2_SettingDataBase(){
        return new SQL_DataBase(this);
    }

    private void TAB3_Setting(final EditText tall, final EditText weight, final EditText age, Spinner gender, Spinner often, Button bth_porfile) {
        final String[] steGender = {"男","女"};
        ArrayAdapter<String> genderList = new ArrayAdapter<>(MainActivity.this, R.layout.myspinner, steGender);
        genderList.setDropDownViewResource(R.layout.myspinner);

        gender.setAdapter(genderList);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    get_gender = 5;
                else
                    get_gender = -161;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] strOften = {"無運動習慣","一周一至三天","一周三至五天","一周六至七天","一天訓練兩次"};
        ArrayAdapter<String> oftenList = new ArrayAdapter<>(MainActivity.this, R.layout.myspinner, strOften);
        oftenList.setDropDownViewResource(R.layout.myspinner);

        often.setAdapter(oftenList);

        often.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    get_activity = 1.2;
                else if(position==1)
                    get_activity = 1.375;
                else if(position==2)
                    get_activity = 1.55;
                else if(position==3)
                    get_activity = 1.725;
                else
                    get_activity = 1.9;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bth_porfile.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_age = Integer.parseInt(age.getText().toString());
                get_tall = Double.valueOf(tall.getText().toString());
                get_weight = Double.valueOf(weight.getText().toString());

                String bmi_status="";

                //基礎代謝率
                bmr = TAB3_CalculateBMR(get_weight, get_tall, get_age, get_gender);

                //TDEE
                tdee = TAB3_CalculateTDEE(bmr ,get_activity);

                //BMI
                bmi = TAB3_CalculateBMI(get_weight ,get_tall );

                if(bmi < 18.5)
                    bmi_status = "(過輕)";
                else if(bmi<24)
                    bmi_status = "(正常)";
                else
                    bmi_status = "(過重)";
                AlertDialog.Builder profileBuilder = new AlertDialog.Builder(MainActivity.this);
                View profileView = getLayoutInflater().inflate(R.layout.profile_dialog,null);
                final TextView show_bmi = (TextView)profileView.findViewById(R.id.bmi);
                final TextView show_bmr = (TextView)profileView.findViewById(R.id.bmr);
                final TextView show_tdee = (TextView)profileView.findViewById(R.id.tdee);
                final TextView show_up = (TextView)profileView.findViewById(R.id.suggest_up);
                final TextView show_down = (TextView)profileView.findViewById(R.id.suggest_down);
                final TextView show_main = (TextView)profileView.findViewById(R.id.suggest_maintain);
                final Button btn_ok = (Button)profileView.findViewById(R.id.profile_ok);

                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(1);    //小數後兩位
                show_bmi.setText(nf.format(bmi)+bmi_status);
                show_bmr.setText(nf.format(bmr)+"大卡");
                show_tdee.setText(nf.format(tdee)+"大卡");
                show_up.setText(nf.format((tdee*1.075))+"大卡");
                show_down.setText(nf.format((tdee*0.85))+"大卡");
                show_main.setText(nf.format(tdee)+"大卡");

                profileBuilder.setView(profileView);
                final AlertDialog profile_dialog = profileBuilder.create();
                profile_dialog.show();

                btn_ok.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profile_dialog.cancel();
                    }
                });




            }
        });
    }

    private double TAB3_CalculateBMI(double get_weight, double get_tall) {
        return get_weight/(get_tall/100)/(get_tall/100);
    }

    private double TAB3_CalculateTDEE(double bmr, double get_activity) {
        return bmr*get_activity;
    }

    private double TAB3_CalculateBMR(double get_weight,double get_tall,int get_age, int get_gender){

        bmr = (10*get_weight)+(6.25*get_tall)-(5*get_age)+get_gender;

        return bmr;
    }

    private DataPoint[] getDataPoint() {
        String tmp[]= query_date_text.split("/");
        if(Integer.parseInt(tmp[1]) <10)
            tmp[1] = "0"+tmp[1];
        choseDate = tmp[0]+"年"+tmp[1]+"月";
        Cursor cursor = sql_dataBase.getDatePoint(choseDate,choseTOOL);
        DataPoint[] dp = new DataPoint[cursor.getCount()];

        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToNext();
            String date = cursor.getString(0);
            date = date.substring(8);
            String tmpdate []= date.split("日");
            int xValue = Integer.parseInt(tmpdate[0]);
            int yValue = Integer.parseInt(cursor.getString(1));
            dp[i]=new DataPoint(xValue,yValue);
        }
        return dp;
    }
}
