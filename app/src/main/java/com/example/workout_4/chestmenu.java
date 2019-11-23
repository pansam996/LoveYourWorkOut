package com.example.workout_4;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class chestmenu extends AppCompatActivity {

    //TextView te = null;
    boolean sta = false;
    boolean chose_ornot = false;
    boolean doing_ornot = false;
    boolean breaking = false;
    boolean record = false;
    boolean record_ornot = false;
    ListView menu = null;
    int chose_position = 0;
    int btn_count = 0;
    int set_count = 0;
    int btn_water_count = 0 ;
    boolean allFINISH = true;
    long elapsedMillis;
    int sp_total_work=0;
    String chose_part ="";

    String all_activity = ""; //記錄今日所有活動
    MyHelper myHelper;

    ArrayList<UserModel> allArea_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chestmenu);

        final Button back = (Button)findViewById(R.id.button5);
        final Button plus = (Button)findViewById(R.id.button8);
        final Button delete = (Button)findViewById(R.id.button6);
        final Button start = (Button)findViewById(R.id.button7);
        final TextView show_stear = (TextView)findViewById(R.id.textView5);
        final Button btn_record = (Button)findViewById(R.id.btnRecord);
        final Button btn_water = (Button)findViewById(R.id.btnWater);
        final Button btn_finish = (Button)findViewById(R.id.btnFinish);
        final TextView sp_name = (TextView)findViewById(R.id.picName);
        final TextView sp_record = (TextView)findViewById(R.id.allSetRecord);
        final TextView between_time = (TextView)findViewById(R.id.betweenTime);
        final Chronometer timer = (Chronometer)findViewById(R.id.timer);

        //開啟資料庫
        myHelper = new MyHelper(this);

        btn_record.setBackgroundResource(R.drawable.records);
        btn_water.setBackgroundResource(R.drawable.water);
        btn_finish.setText("START");
        sp_record.setMovementMethod(ScrollingMovementMethod.getInstance());

        show_stear.setVisibility(View.INVISIBLE);
        btn_water.setVisibility(View.INVISIBLE);
        btn_record.setVisibility(View.INVISIBLE);
        btn_finish.setVisibility(View.INVISIBLE);
        sp_name.setVisibility(View.INVISIBLE);
        sp_record.setVisibility(View.INVISIBLE);
        between_time.setVisibility(View.INVISIBLE);
        timer.setVisibility(View.INVISIBLE);

        //te = (TextView)findViewById(R.id.textView4);
        menu = (ListView)findViewById(R.id.list);

        back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i =0 ; i< allArea_list.size();i++) {
                    if(!(allArea_list.get(i).getFinish().equals("FINISH"))){
                       allFINISH = false;
                    }
                }

                if(!doing_ornot && allFINISH){
                    //完成所有訓練 秀出表單 然後離開
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(chestmenu.this);
                    View mView = getLayoutInflater().inflate(R.layout.all_activity,null);
                    final Button dialog_check = (Button) mView.findViewById(R.id.dialog_ok);
                    final TextView dialog_record = (TextView) mView.findViewById(R.id.dialog_record);
                    dialog_record.setMovementMethod(ScrollingMovementMethod.getInstance());
                    dialog_record.setText(all_activity);

                    mBuilder.setView(mView);
                    final AlertDialog record_dialog = mBuilder.create();
                    record_dialog.show();

                    dialog_check.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });

                }
                if(!doing_ornot && !allFINISH){
                    //還沒完成全部訓練 確定離開?
                    AlertDialog.Builder builder = new AlertDialog.Builder(chestmenu.this);
                    builder.setMessage("還沒完成全部訓練 是否離開?");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(chestmenu.this);
                            View mView = getLayoutInflater().inflate(R.layout.all_activity,null);
                            final Button dialog_check = (Button) mView.findViewById(R.id.dialog_ok);
                            final TextView dialog_record = (TextView) mView.findViewById(R.id.dialog_record);
                            dialog_record.setText(all_activity);

                            mBuilder.setView(mView);
                            final AlertDialog record_dialog = mBuilder.create();
                            record_dialog.show();

                            dialog_check.setOnClickListener(new Button.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                        }
                    });

                    builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog leave_dialog = builder.create();
                    leave_dialog.show();
                }

            }
        });

        plus.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = chestmenu.this.getIntent().getExtras();
                int chose = bundle.getInt("chose");

                switch (chose){
                    case 1:
                        chose_part="胸部訓練";
                        break;
                    case 2:
                        chose_part="背部訓練";
                        break;
                    case 3:
                        chose_part="腿部訓練";
                        break;
                    case 4:
                        chose_part="手部訓練";
                        break;

                }

                Bundle bundle1 = new Bundle();
                Intent intent = new Intent();
                bundle1.putInt("chosed",chose);
                intent.putExtras(bundle1);
                intent.setClass(chestmenu.this  , add_chest.class);
                startActivityForResult(intent,1);

            }
        });


        delete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<UserModel> add_list = new ArrayList<>();
                final ChestMenuAdapter adapter = new ChestMenuAdapter(chestmenu.this,add_list);
                menu.setAdapter(adapter);
            }
        });

        start.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allArea_list.size()>0){
                    plus.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.INVISIBLE);
                    start.setVisibility(View.INVISIBLE);
                    show_stear.setVisibility(View.VISIBLE);
                    sta= true;
                }
            }
        });

        btn_record.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(record){
                //跳出對話框 可以輸入重量與組數
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(chestmenu.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_record,null);
                final EditText mMaxWeight = (EditText) mView.findViewById(R.id.editText);
                final EditText mMaxNum = (EditText) mView.findViewById(R.id.editText2);
                final Button check = (Button) mView.findViewById(R.id.btncheck);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                check.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if(!mMaxWeight.getText().toString().isEmpty() && !mMaxNum.getText().toString().isEmpty()){
                                Toast.makeText(chestmenu.this,"記錄成功",Toast.LENGTH_SHORT).show();
                                set_count++;
                                sp_record.setText(sp_record.getText()+"\n"+set_count+" : "+mMaxWeight.getText().toString()+"KG"+"/"+mMaxNum.getText().toString()+"下");
                                int weight = 0;
                                int num = 0;
                                if(mMaxWeight.getText().toString().indexOf(".") !=-1){
                                    float a= Float.valueOf(mMaxWeight.getText().toString());
                                    weight = Math.round(a);
                                }
                                else
                                    weight=Integer.parseInt(mMaxWeight.getText().toString());

                                num = Integer.parseInt(mMaxNum.getText().toString());
                                sp_total_work += weight*num;
                                record = false;
                                record_ornot = true;
                                dialog.cancel();
                            }
                            else{
                                Toast.makeText(chestmenu.this,"請輸入完整資訊",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
             if(breaking && !record){
                 Toast.makeText(chestmenu.this,"請先點選喝水按鈕",Toast.LENGTH_SHORT).show();
             }
             if(!breaking){
                 Toast.makeText(chestmenu.this,"請先點選START",Toast.LENGTH_SHORT).show();
             }


            }
        });

        btn_water.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(breaking){
                    record = true;
                    btn_water.setBackgroundResource(R.drawable.workout);
                    btn_water_count++;


                    if(btn_water_count==1) {
                        Toast.makeText(chestmenu.this,"點選記錄圖示記錄此組活動",Toast.LENGTH_LONG).show();
                        if(elapsedMillis!=0){
                            timer.setBase(timer.getBase() + SystemClock.elapsedRealtime() - elapsedMillis);
                        }
                        else {
                            timer.setBase(SystemClock.elapsedRealtime());
                        }
                        timer.start();
                    }

                    if(!record_ornot && btn_water_count>1){
                        Toast.makeText(chestmenu.this,"上一組訓練尚未完成記錄",Toast.LENGTH_SHORT).show();
                    }
                    if(record_ornot && btn_water_count>1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(chestmenu.this);
                        builder.setMessage("休息結束，開始下一組?");

                        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                btn_water.setBackgroundResource(R.drawable.water);

                                //暫停然後記錄
                                elapsedMillis = SystemClock.elapsedRealtime();
                                sp_record.setText(sp_record.getText()+"\n"+"休息 "+timer.getText().toString());

                                timer.stop();
                                timer.setBase(SystemClock.elapsedRealtime());
                                elapsedMillis = 0;

                                record_ornot = false;
                                record = false;
                                btn_water_count = 0;
                                dialog.cancel();
                            }
                        });

                        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog leave_dialog = builder.create();
                        leave_dialog.show();
                    }



                }
                else {
                    Toast.makeText(chestmenu.this,"請先點選START",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_finish.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_finish.setText("FINISH");
                breaking = true;
                btn_count++;
                if(btn_count==1)
                    Toast.makeText(chestmenu.this,"每組結束後點選喝水按鈕並且記錄",Toast.LENGTH_LONG).show();
                if(sp_record.getText().toString().equals("記錄表") && btn_count>1){
                    //秀出對話方塊 尚未紀錄是否要離開?
                    AlertDialog.Builder builder = new AlertDialog.Builder(chestmenu.this);
                    builder.setMessage("尚未記錄，是否要離開?");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            //介面整理
                            btn_water.setBackgroundResource(R.drawable.water);
                            sp_name.setVisibility(View.INVISIBLE);
                            sp_record.setVisibility(View.INVISIBLE);
                            btn_finish.setVisibility(View.INVISIBLE);
                            btn_record.setVisibility(View.INVISIBLE);
                            btn_water.setVisibility(View.INVISIBLE);
                            between_time.setVisibility(View.INVISIBLE);
                            timer.setVisibility(View.INVISIBLE);
                            sp_record.setText("記錄表");
                            //提示動作
                            show_stear.setVisibility(View.VISIBLE);
                            //開關調整
                            btn_finish.setText("START");
                            doing_ornot = false;
                            record = false;
                            breaking = false;
                            set_count = 0;
                            btn_count=0;
                            sp_total_work = 0;
                            //歸零
                            if(elapsedMillis!=0){
                                timer.setBase(timer.getBase() + SystemClock.elapsedRealtime() - elapsedMillis);
                            }
                            else {
                                timer.setBase(SystemClock.elapsedRealtime());
                            }
                            timer.stop();
                            timer.setBase(SystemClock.elapsedRealtime());
                            elapsedMillis = 0;
                        }
                    });

                    builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog leave_dialog = builder.create();
                    leave_dialog.show();

                }

                if(btn_count >1 && !(sp_record.getText().toString().equals("記錄表"))){
                    //秀出對話方塊  訓練完成是否要離開?
                    AlertDialog.Builder builder = new AlertDialog.Builder(chestmenu.this);
                    builder.setMessage("訓練完成，是否要離開?");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //當天日期
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
                            Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
                            String str = formatter.format(curDate);

                            all_activity = all_activity+allArea_list.get(chose_position).getUserName()+"\n"+sp_record.getText()+"\n"+"總公斤為 : "+sp_total_work+"KG"+"\n"+"\n"+"\n";
                            //顯示finish 與記錄資料
                            allArea_list.get(chose_position).setFinish("FINISH");
                            allArea_list.get(chose_position).setSpRecord(sp_record.getText().toString()+"\n"+"總公斤為 : "+sp_total_work+"KG");
                            allArea_list.get(chose_position).setTotalWork(sp_total_work);

                            //資料庫新增資料
                            boolean isInserted = false;
                            if(!(sp_record.getText().toString().equals("記錄表")))
                                isInserted = myHelper.insert_Data(str,chose_part,allArea_list.get(chose_position).getUserName(),sp_total_work,sp_record.getText().toString());
                           if(isInserted)
                               Toast.makeText(chestmenu.this,"更新資料庫成功",Toast.LENGTH_SHORT).show();
                           else
                               Toast.makeText(chestmenu.this,"更新資料庫失敗",Toast.LENGTH_SHORT).show();

                            final ChestMenuAdapter adapter = new ChestMenuAdapter(chestmenu.this,allArea_list);
                            menu.setAdapter(adapter);
                            //介面整理
                            btn_water.setBackgroundResource(R.drawable.water);
                            sp_name.setVisibility(View.INVISIBLE);
                            sp_record.setVisibility(View.INVISIBLE);
                            btn_finish.setVisibility(View.INVISIBLE);
                            btn_record.setVisibility(View.INVISIBLE);
                            btn_water.setVisibility(View.INVISIBLE);
                            between_time.setVisibility(View.INVISIBLE);
                            timer.setVisibility(View.INVISIBLE);
                            sp_record.setText("記錄表");
                            //提示動作
                            show_stear.setVisibility(View.VISIBLE);
                            //開關調整
                            btn_finish.setText("START");
                            doing_ornot = false;
                            record = false;
                            breaking = false;
                            btn_count=0;
                            btn_water_count = 0;
                            sp_total_work=0;
                            //歸零
                            if(elapsedMillis!=0){
                                timer.setBase(timer.getBase() + SystemClock.elapsedRealtime() - elapsedMillis);
                            }
                            else {
                                timer.setBase(SystemClock.elapsedRealtime());
                            }
                            timer.stop();
                            timer.setBase(SystemClock.elapsedRealtime());
                            elapsedMillis = 0;
                        }
                    });

                    builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog leave_dialog = builder.create();
                    leave_dialog.show();
                }


            }
        });

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(sta && !doing_ornot && !(allArea_list.get(position).getFinish().equals("FINISH"))){
                    show_stear.setVisibility(View.INVISIBLE);

                    btn_water.setVisibility(View.VISIBLE);
                    btn_record.setVisibility(View.VISIBLE);
                    btn_finish.setVisibility(View.VISIBLE);
                    sp_name.setVisibility(View.VISIBLE);
                    sp_record.setVisibility(View.VISIBLE);
                    between_time.setVisibility(View.VISIBLE);
                    timer.setVisibility(View.VISIBLE);

                    sp_name.setText(allArea_list.get(position).getUserName());

                    chose_ornot = true;
                    chose_position = position;
                    set_count = 0;

                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 1: {
                Bundle bundle = data.getExtras();
                ArrayList<UserModel> list = (ArrayList<UserModel>) bundle.getSerializable("List");
                ArrayList<UserModel> add_list = new ArrayList<>();

                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isSelected()) {
                            add_list.add(list.get(i));
                        }
                    }
                }

                if(add_list.size()>0){
                    for(int i =0 ; i <add_list.size();i++) {
                        allArea_list.add(add_list.get(i));
                    }
                    final ChestMenuAdapter adapter = new ChestMenuAdapter(this,allArea_list);
                    menu.setAdapter(adapter);

                }

                break;
            }
        }
    }
}
